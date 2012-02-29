/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.RodsUtil;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.DataObjInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.IntPi;
import edu.umiacs.irods.api.pi.KeyValPair_PI;
import edu.umiacs.irods.api.pi.OprTypeEnum;
import edu.umiacs.irods.api.pi.PortalOprOut_PI;
import edu.umiacs.irods.api.pi.RodsObjStat_PI;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 
 * @author toaster
 */
public class IrodsProxyInputStream extends InputStream {

    private static final Logger LOG = Logger.getLogger(IrodsProxyInputStream.class);
    private IRodsConnection connection;
    private int fd;
    private InputStream dataStream = null;
    private Socket proxyStream = null;

    public IrodsProxyInputStream(String fileName, IRodsConnection connection) throws IOException {

        this.connection = connection;
        open(fileName);
    }

    private void open(String fileName) throws IOException {
        IrodsApiRequest apiReq;
        DataObjInp_PI body;
        int status;
        RodsObjStat_PI stat;

        if (connection != null) {
            stat = stat(fileName);
            if (stat.getObjSize() <= 0)
                return;

            body = new DataObjInp_PI(fileName, 0, 0, 0, stat.getObjSize(), 1,
                    OprTypeEnum.GET_OPR, new KeyValPair_PI((Map) null));

            apiReq =
                    new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_GET_AN, body,
                    null);

//            status = apiReq.sendRequest(connection);

            // error Opening
            if ((status = apiReq.sendRequest(connection)) < 0) {
                connection = null;
                throw new IRodsRequestException("Error opening file, error: "
                        + ErrorEnum.valueOf(status));


            } else if ((dataStream = apiReq.getResultInputStream()) == null) {
                // external data stream
                PortalOprOut_PI outPI =
                        apiReq.getResultPI(PortalOprOut_PI.class);
                fd = outPI.getL1descInx();

//            private DataInputStream is;
                int cookie = outPI.getPortListPI().getCookie();
                int port = outPI.getPortListPI().getPortNum();
                String host = outPI.getPortListPI().getHostAddr();
                LOG.trace("Receved Proxy port for data " + host + ":" + port + " cookie: "+cookie );

                proxyStream = new Socket(host, port);
                OutputStream os = proxyStream.getOutputStream();
                DataInputStream is = new DataInputStream(proxyStream.getInputStream());
                this.dataStream = is;

                os.write(RodsUtil.renderInt(cookie));
                RodsUtil.readBytes(8, is);
                long offset = RodsUtil.parseLong(RodsUtil.readBytes(8, is));
                if (offset != 0) {
                    throw new IOException("Stream offset is not 0, offset: " + offset);
                }
                 
                long totalBytes =
                        RodsUtil.parseLong(RodsUtil.readBytes(8, is));
                LOG.trace("Opened Proxy port for data " + host + ":" + port );
            }
            else
            {
                LOG.trace("File embedded in control channel");
            }
        }
    }

    private RodsObjStat_PI stat(String path) throws IRodsRequestException {
        try {

            DataObjInp_PI inPi =
                    new DataObjInp_PI(path, 0, 0, 0, 0, 0,
                    OprTypeEnum.NO_OPR_TYPE, new KeyValPair_PI((Map) null));


            IrodsApiRequest apiReq =
                    new IrodsApiRequest(ApiNumberEnum.OBJ_STAT_AN, inPi, null);

            int status = apiReq.sendRequest(connection);

            if (status
                    == ErrorEnum.USER_FILE_DOES_NOT_EXIST.getInt()) {
                return null;
            }
            if (status < 0) {
                throw new IRodsRequestException(ErrorEnum.valueOf(status));
            }

            return apiReq.getResultPI(RodsObjStat_PI.class);
        } catch (IOException ex) {
            if (ex instanceof IRodsRequestException) {
                throw (IRodsRequestException) ex;
            }
            throw new IRodsRequestException(
                    "Communication error sending request", ex);
        }

    }

    @Override
    public int read(byte[] b) throws IOException {

        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (dataStream == null)
            return -1;
        return dataStream.read(b, off, len);
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Inputstream does not allow single char reads");
    }

    @Override
    public void close() throws IOException {

        if (fd > 0) {
            IntPi inPi = new IntPi(fd);
            IrodsApiRequest apiReq =
                    new IrodsApiRequest(ApiNumberEnum.OPR_COMPLETE_AN, inPi, null);

            int status = apiReq.sendRequest(connection);

            if (status < 0) {
                throw new IRodsRequestException(ErrorEnum.valueOf(status));
            }
            proxyStream.close();
        } else if (dataStream != null) {
            dataStream.close();
        }
    }
}
