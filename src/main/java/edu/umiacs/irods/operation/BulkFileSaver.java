/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.RodsUtil;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.DataObjInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.api.pi.KeyValPair_PI;
import edu.umiacs.irods.api.pi.ObjTypeEnum;
import edu.umiacs.irods.api.pi.OprTypeEnum;
import edu.umiacs.irods.api.pi.PortalOprOut_PI;
import edu.umiacs.irods.api.pi.RodsObjStat_PI;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author toaster
 */
public class BulkFileSaver {

    private static final Logger LOG = Logger.getLogger(BulkFileSaver.class);
    private ConnectOperation co;
    private int opCount;
    private String irodsPath;
    private IrodsOperations ops;
    private List<BulkTransferListener> listeners =
            new ArrayList<BulkTransferListener>();
    private boolean cancelled = false;
    private BulkFileHandler fileHandler;
    private int bufferSize = 2097152;
    private byte buffer[];
    private int MAXOPCOUNT = 250;
    private int receiveThreads = 0;

    public BulkFileSaver(ConnectOperation co, BulkFileHandler fileHandler,
            String irodsPath) {
        this.co = co;
        this.fileHandler = fileHandler;
        this.irodsPath = irodsPath;
        this.ops = new IrodsOperations(co);
    }

    public int getReceiveThreads() {
        return receiveThreads;
    }

    public void setReceiveThreads(int receiveThreads) {
        this.receiveThreads = receiveThreads;
    }

    public void addListener(BulkTransferListener bll) {
        listeners.add(bll);
    }

    public void removeListener(BulkTransferListener bll) {
        listeners.remove(bll);
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized void execute(boolean background) {

        Runnable r = new MyRunnable();

        if (!background) {
            r.run();
        } else {
            Thread t = new Thread(r);
            t.start();
        }
    }

    private void callHandleException(Throwable t) {
        for (BulkTransferListener bll : listeners) {
            bll.handleException(t);
        }
    }

    private void callStartTransfer() {
        for (BulkTransferListener bll : listeners) {
            bll.startTransfer();
        }
    }

    private void callStartFile(String path) {
        for (BulkTransferListener bll : listeners) {
            bll.startFile(path);
        }
    }

    private void callEndFile(String path) {
        for (BulkTransferListener bll : listeners) {
            bll.endFile(path);
        }
    }

    private void callBytesWritten(int written) {
        for (BulkTransferListener bll : listeners) {
            bll.bytesWritten(written);
        }
    }

    private void callEndTransfer() {
        for (BulkTransferListener bll : listeners) {
            bll.endTransfer();
        }
    }

    private void downloadDirectory(String irodsDir, String localpart) {
        if (cancelled) {
            return;
        }

        if (!fileHandler.mkdir(localpart)) {
            callHandleException(new IOException("Local directory doesn't exist or cannot be created "
                    + localpart));
            return;
        }

        // Download files in this directory
        for (String dir : listDirectories(irodsDir)) {
            if (cancelled) {
                return;
            }
            if (fileHandler.processItem(dir.substring(irodsPath.length()), true)) {
                downloadDirectory(dir, dir.substring(irodsPath.length()));
            }
        }

        // recurse into child directories
        for (String localFile : listFiles(irodsDir)) {
            if (cancelled) {
                return;
            }
            String relPath = irodsDir.substring(irodsPath.length()) + localFile;
            if (fileHandler.processItem(relPath, true)) {
                downloadFile(irodsDir + "/" + localFile);
            }
        }
    }

    private List<String> listDirectories(String parent) {

        QueryBuilder qb;
        QueryResult qr;
        List<String> directories = new ArrayList<String>();
        try {

            qb = new QueryBuilder(GenQueryEnum.COL_COLL_NAME);


            qb.eq(GenQueryEnum.COL_COLL_PARENT_NAME, parent);
            testConnection();
            qr = qb.execute(co.getConnection());


            while (qr.next()) {
                if (qr.getValue(GenQueryEnum.COL_COLL_NAME).equals(parent)) {
                    continue;
                }

                directories.add(qr.getValue(GenQueryEnum.COL_COLL_NAME));

            }
            return directories;

        } catch (IRodsRequestException ex) {
            if (ex.getErrorCode() == ErrorEnum.CAT_NO_ROWS_FOUND) {
                return Collections.emptyList();
            }
            callHandleException(ex);
            return Collections.emptyList();
        } catch (IOException ex) {
            callHandleException(ex);
            return Collections.emptyList();
        }

    }

    private List<String> listFiles(String parent) {

        List<String> files = new ArrayList<String>();
        QueryBuilder qb;
        QueryResult qr;
        try {
            qb =
                    new QueryBuilder(GenQueryEnum.COL_DATA_NAME);

            qb.eq(GenQueryEnum.COL_COLL_NAME, parent);
            testConnection();
            qr = qb.execute(co.getConnection());

            while (qr.next()) {
                files.add(qr.getValue(GenQueryEnum.COL_DATA_NAME));
            }

        } catch (IOException ex) {
            callHandleException(ex);
        }
        return files;
    }

    private void downloadFile(String irodsFile) {
        InputStream is;
        DataObjInp_PI inPi;
        IrodsApiRequest apiReq;
        RodsObjStat_PI stat;
        int status;

        callStartFile(irodsFile);

        if (cancelled) {
            return;
        }

        try {
            // get file status
            stat = ops.stat(irodsFile);
            fileHandler.openFile(irodsFile.substring(irodsPath.length()),
                    stat.getObjSize());
        } catch (IOException e) {
            callHandleException(e);
            return;
        }

        try {

            inPi = new DataObjInp_PI(irodsFile, 0, 0, 0, stat.getObjSize(), receiveThreads,
                    OprTypeEnum.GET_OPR, new KeyValPair_PI((Map) null));

            apiReq =
                    new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_GET_AN, inPi,
                    null);

            testConnection();
            status = apiReq.sendRequest(co.getConnection());

            if (status < 0) {
                throw new IOException("irods error: "
                        + ErrorEnum.valueOf(status));
            }

            // inline stream
            if ((is = apiReq.getResultInputStream()) != null) {
                LOG.trace("In-line file download");
                int bytesRead;
                long offset = 0;

                if (buffer == null) {
                    buffer = new byte[bufferSize];
                }
                while ((bytesRead = is.read(buffer)) >= 0) {
                    fileHandler.writeBytes(buffer, offset, bytesRead);
                    offset += bytesRead;
                    callBytesWritten(bytesRead);
                }

                is.close();
                fileHandler.closeFile();
            } // multi-threaded out of band
            else {
                LOG.trace("Threaded file download");
                PortalOprOut_PI outPI =
                        apiReq.getResultPI(PortalOprOut_PI.class);
                new MultiThreadReceiver(outPI).receive();
            }
        } catch (InterruptedException ire) {
            callHandleException(ire);
        } catch (IOException ioe) {
            fileHandler.ioError(ioe);
        } finally {
            callEndFile(irodsFile);
        }
    }

    public static enum DuplicateOption {

        OVERWRITE, IGNORE, RENAME
    }

    class MyRunnable implements Runnable {

        public void run() {

            callStartTransfer();
            try {
                // 1. stat file, if its a file, just download it, otherwise pull a directory
                RodsObjStat_PI stat = ops.stat(irodsPath);
                if (stat.getObjType() == ObjTypeEnum.DATA_OBJ_T) {
                    downloadFile(irodsPath);
                } else if (stat.getObjType() == ObjTypeEnum.COLL_OBJ_T) {
                    downloadDirectory(irodsPath, "/");
                }
                co.shutdown();
            } catch (IRodsRequestException ex) {
                callHandleException(ex);
            } finally {
                callEndTransfer();
            }
        }
    }

    private void testConnection() {
        if (opCount > MAXOPCOUNT) {
            opCount = 0;
            try {
                co.reconnect();
            } catch (IOException ex) {
                LOG.error("Error reconnecting", ex);
            }
        }
        opCount++;
    }

    class MultiThreadReceiver {

        private int cookie;
        private int threads;
        private int port;
        private String host;
        private IOException e = null;

        public MultiThreadReceiver(PortalOprOut_PI portal) {
            this.threads = portal.getNumThreads();
            this.cookie = portal.getPortListPI().getCookie();
            this.port = portal.getPortListPI().getPortNum();
            this.host = portal.getPortListPI().getHostAddr();
        }

        public void receive() throws IOException, InterruptedException {
            Thread[] t = new Thread[threads];
            MyRunnable[] r = new MyRunnable[threads];
            try {
                for (int i = 0; i < threads; i++) {
                    r[i] = new MyRunnable();
                    t[i] = new Thread(r[i]);
                    t[i].start();
                }
                for (int i = 0; i < threads; i++) {
                    t[i].join();
                }
            } finally {
                for (int i = 0; i < threads; i++) {
                    if (r[i] != null) {
                        r[i].close();
                    }

                }
                if (e != null) {
                    throw e;
                }
                fileHandler.closeFile();
            }
        }

        class MyRunnable implements Runnable {

            long offset;
            long totalBytes;
            long bytesRead;
            private Socket s;
            private OutputStream os;
            private DataInputStream is;

            public MyRunnable() throws IOException {
                try {
                    LOG.trace("Opening connection to: " + host + ":" + port);
                    s = new Socket(host, port);
                    os = s.getOutputStream();
                    is = new DataInputStream(s.getInputStream());
                } catch (IOException ioe) {
                    close();
                    throw ioe;
                }
            }

            public void close() {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ioe) {
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ioe) {
                    }
                }
                try {
                    if (s != null) {
                        s.close();
                    }
                } catch (IOException ioe) {
                }
            }

            public void run() {
                try {
                    os.write(RodsUtil.renderInt(cookie));
                    RodsUtil.readBytes(8, is);
                    offset = RodsUtil.parseLong(RodsUtil.readBytes(8, is));
                    totalBytes =
                            RodsUtil.parseLong(RodsUtil.readBytes(8, is));

                    byte[] buffer = new byte[4096];
                    int read = 0;
                    while ((read = is.read(buffer)) > 0) {
                        int toWrite = read;
                        if (bytesRead + read > totalBytes) {
                            toWrite = (int) (totalBytes - bytesRead);
                        }
                        fileHandler.writeBytes(buffer, offset + bytesRead,
                                toWrite);
                        bytesRead += toWrite;
                    }
                } catch (IOException ex) {
                    if (e != null) {
                        e = ex;
                    }

                } finally {
                    close();
                }

            }
        }
    }
}
