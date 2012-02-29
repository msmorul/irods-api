/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.BitstreamCallback;
import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.DataObjInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.KeyValPair_PI;
import edu.umiacs.irods.api.pi.ObjTypeEnum;
import edu.umiacs.irods.api.pi.OprTypeEnum;
import edu.umiacs.irods.api.pi.RodsObjStat_PI;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Example file for ingesting bulk data into irods
 * 
 * @author toaster
 */
public class BulkFileLoader
{

    private static final Logger LOG = Logger.getLogger(BulkFileLoader.class);
    private ConnectOperation connection;
    private IrodsOperations op;
    private String destDir;
    private String resource;
    private List<File> sources = new ArrayList<File>();
    private boolean cancel = false;
    private boolean executed = false;
    private List<BulkTransferListener> listeners =
            new ArrayList<BulkTransferListener>();
    private long totalBytes;

    public BulkFileLoader(ConnectOperation connection, String destinationDirectory, String resource)
    {
        this.connection = connection;
        this.destDir = destinationDirectory;
        this.resource = resource;
        op = new IrodsOperations(connection);
    }

    /**
     * Count the number of bytes to be processed. 
     * @return
     */
    public synchronized long getTotalBytes()
    {
        if ( totalBytes > 0 )
        {
            return totalBytes;
        }
        else
        {
            for ( File f : sources )
            {
                totalBytes += countFile(f);
            }
            return totalBytes;
        }
    }

    /**
     * Could bytes in given file or directory. If its a directory, recurse on it
     * 
     * @param f file to count
     * @return total size of file or directory contents
     */
    private long countFile(File f)
    {

        if ( f.isFile() )
        {
            return f.length();
        }
        else
        {
            long localCount = 0;
            for ( File ff : f.listFiles() )
            {
                localCount += countFile(ff);
            }
            return localCount;
        }

    }

    public synchronized void addSource(File... files)
    {
        if ( executed )
        {
            throw new IllegalStateException("Cannot add files to running or finished loader, execute() already called");
        }

        totalBytes = -1;
        for ( File f : files )
        {
            sources.add(f);
        }
    }

    public void cancel()
    {
        this.cancel = true;
    }

    public boolean isCancelled()
    {
        return cancel;
    }

    public void addListener(BulkTransferListener bll)
    {
        listeners.add(bll);
    }

    public void removeListener(BulkTransferListener bll)
    {
        listeners.remove(bll);
    }

    /**
     * ingest this batch of files into irods.
     * @param background if true, create new thread to perform ingestion. Otherwise, return only when ingest has finished
     */
    public synchronized void execute(boolean background)
    {
        if ( executed )
        {
            throw new IllegalStateException("Cannot re-execute running or finished loader, execute() already called");
        }

        executed = true;
        Runnable r = new MyRunnable();

        if ( !background )
        {
            r.run();
        }
        else
        {
            Thread t = new Thread(r);
            t.start();
        }
    }

    private void callHandleException(Throwable t)
    {
        for ( BulkTransferListener bll : listeners )
        {
            bll.handleException(t);
        }
    }

    private void callStartTransfer()
    {
        for ( BulkTransferListener bll : listeners )
        {
            bll.startTransfer();
        }
    }

    private void callStartFile(String path)
    {
        for ( BulkTransferListener bll : listeners )
        {
            bll.startFile(path);
        }
    }

    private void callEndFile(String path)
    {
        for ( BulkTransferListener bll : listeners )
        {
            bll.endFile(path);
        }
    }

    private void callBytesWritten(int written)
    {
        for ( BulkTransferListener bll : listeners )
        {
            bll.bytesWritten(written);
        }
    }

    private void callEndTransfer()
    {
        for ( BulkTransferListener bll : listeners )
        {
            bll.endTransfer();
        }
    }

    private void recursePush(File localCWD, String parentPath) throws IOException
    {

        String irodsCWD = parentPath + "/" + localCWD.getName();
        RodsObjStat_PI stat;

        if ( cancel )
        {
            return;
        }

        try
        {
            // Create directory if it doesn't exist
            if ( (stat = op.stat(irodsCWD)) == null )
            {
                op.mkdir(irodsCWD);
            }
            else if ( stat.getObjType() != ObjTypeEnum.COLL_OBJ_T )
            {
                throw new IOException("File: " + irodsCWD +
                        " is not a directory");
            }
        }
        catch ( IRodsRequestException e )
        {
            throw new IOException("Error stating directory: " + irodsCWD + ' ' + e.getMessage());
        }

        // process each file
        for ( File f : localCWD.listFiles() )
        {
            if ( cancel )
            {
                return;
            }

            if ( f.isFile() )
            {
//                    LOG.debug("Pushing: " + f.getName());
                String irodsFilePath = irodsCWD + "/" + f.getName();
                pushFile(f, irodsFilePath, "generic");
            }
            else if ( f.isDirectory() )
            {
                // recurse on directories
                recursePush(f, irodsCWD);
            }
            else
            {
                LOG.error("Cannot push unknown file:" + f);
            }
        }
//        }
//        catch ( IRodsRequestException ex )
//        {
//            throw new ShellException("Error stating file: " + irodsCWD, ex);
//        }
//        catch ( IOException ex )
//        {
//            throw new ShellException("error getting connection", ex);
////                ctxShell.getConnectOperation().reconnect();
//        }
    }

    /**
     * Perform transfer of actual file into irods.  This will creat a bitstream
     * callback for each file, and call start/endFile listners.
     * 
     * @param f local file that will be uploaded
     * @param newPath full path in irods to new file
     * @param dataType datatype of file, usually 'generic'
     * @throws java.io.IOException
     */
    private void pushFile(File f, String newPath, String dataType) throws IOException
    {

        IRodsConnection ic = connection.getConnection();
        int status;
        // Optional data required for file ingestion
        // dataType - 
        Map<String, String> destInfo = new HashMap<String, String>();
        destInfo.put("dataType", dataType);
        destInfo.put("destRescName", resource);
        destInfo.put("dataIncluded", "");

        KeyValPair_PI keyValPair_PI = new KeyValPair_PI(destInfo);
        DataObjInp_PI body = new DataObjInp_PI(newPath, 448, 1, 0, f.length(), 0,
                OprTypeEnum.PUT_OPR, keyValPair_PI);

        IrodsApiRequest apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_PUT_AN,
                body, new MyCallback(f));

        callStartFile(newPath);
        try
        {
            if ( (status = apiReq.sendRequest(ic)) < 0 )
            {
                throw new IOException("Error pushing file, error: " +
                        ErrorEnum.valueOf(status));
            }
        }
        finally
        {
            callEndFile(newPath);
        }

    }

    /**
     * Inner class that performs actual transfer. Will call start and end transfer
     * on all listners. 
     */
    class MyRunnable implements Runnable
    {

        public void run()
        {

            try
            {
                callStartTransfer();
                for ( File f : sources )
                {
                    if ( cancel )
                    {
                        return;
                    }
                    try
                    {
                        if ( f.isDirectory() )
                        {
                            recursePush(f, destDir);
                        }
                        else
                        {
                            pushFile(f, destDir + "/" + f.getName(), "generic");
                        }
                    }
                    catch ( IOException ex )
                    {
                        callHandleException(ex);
//                        LOG.error("Transfer error")
                        cancel = true;
                    }

                }
            }
            finally
            {
                callEndTransfer();
            }
        }
    }

    /**
     * Callback class to feed data into the irods connection. Will call 
     * bytesWritten in listeners
     */
    class MyCallback implements BitstreamCallback
    {

        private File file;

        public MyCallback(File f)
        {
            this.file = f;

        }

        public long getTotalBytes()
        {
            return file.length();
        }

        public void writeBitstream(OutputStream os)
        {
            try
            {
                InputStream is = new FileInputStream(file);
                int bufferSize = 2048;
                byte buffer[];
                int bytesRead;

//                if ( bufferSize <= 0 )
//                {
//                    throw new IllegalArgumentException("Invalid buffer size: " +
//                            bufferSize);
//                }

                buffer = new byte[bufferSize];
                while ( (bytesRead = is.read(buffer)) >= 0 )
                {
                    os.write(buffer, 0, bytesRead);
                    callBytesWritten(bytesRead);
                }
                os.close();
            }
            catch ( IOException ex )
            {
                throw new RuntimeException("Error reading local file", ex);
            }
        }
    }
}
