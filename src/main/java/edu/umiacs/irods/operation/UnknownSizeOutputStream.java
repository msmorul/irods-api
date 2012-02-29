/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.BitstreamCallback;
import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.DataObjCloseInp_PI;
import edu.umiacs.irods.api.pi.DataObjInp_PI;
import edu.umiacs.irods.api.pi.DataObjWriteInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.api.pi.KeyValPair_PI;
import edu.umiacs.irods.api.pi.OpenedDataObjInp_PI;
import edu.umiacs.irods.api.pi.OprTypeEnum;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author toaster
 */
public class UnknownSizeOutputStream extends OutputStream {

    private IRodsConnection connection;
    private int fd;
    private static final Logger LOG = Logger.getLogger(UnknownSizeOutputStream.class);

    public UnknownSizeOutputStream(IRodsConnection connection, String path,
            String resource) throws IOException {
        this.connection = connection;

        containsResource(connection, resource);
        open(path, resource, false,-1);

    }

    public UnknownSizeOutputStream(IRodsConnection connection, String path,
            String resource, boolean overwrite) throws IOException {
        this.connection = connection;

        containsResource(connection, resource);
        open(path, resource, overwrite,-1);

    }

    private void open(String fileName, String resource, boolean overwrite, int replicaNumber) throws IOException {
        IrodsApiRequest apiReq;
        DataObjInp_PI body;
        int status;
        String dataType = "generic";

        Map<String, String> destInfo = new HashMap<String, String>();
        destInfo.put("dataType", dataType);
        destInfo.put("destRescName", resource);
        if (overwrite)
            destInfo.put("forceFlag", "");
        if (replicaNumber > -1)
            destInfo.put("replNum", Integer.toString(replicaNumber));
        
        KeyValPair_PI keyValPair_PI = new KeyValPair_PI(destInfo);

        if (connection != null) {
            body = new DataObjInp_PI(fileName, 0, 2, 0, 0, 0,
                    OprTypeEnum.NO_OPR_TYPE,
                    keyValPair_PI);
            apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_CREATE_AN, body,
                    null);

            if ((status = apiReq.sendRequest(connection)) < 0) {
                connection = null;
                throw new IRodsRequestException("Error opening file, error: "
                        + ErrorEnum.valueOf(status));

            } else {
                fd = status;
            }
        }
    }

    @Override
    public void write(int b) throws IOException {
        throw new UnsupportedOperationException("Single byte ops not supported");
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        int status;
        IrodsApiRequest apiReq;
        if (connection == null) {
            throw new IOException("Inputstream closed");
        }

        BsCallback bsCallback = new BsCallback(len, off, b);

        if (connection.getVesion().is201Compat()) {

            DataObjWriteInp_PI body = new DataObjWriteInp_PI(fd, len);
            apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_WRITE201_AN,
                    body, bsCallback);
        } else {
            OpenedDataObjInp_PI body = new OpenedDataObjInp_PI(fd, len, 0, 0, 0, 0, new KeyValPair_PI((Map) null));
            apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_WRITE_AN,
                    body, bsCallback);
        }


        try {
            status = apiReq.sendRequest(connection);
        } catch (IOException ioe) {
            LOG.error("Exception sending read request, ", ioe);
            connection.closeConnection();
            connection = null;
            throw ioe;
        }

        if (status < 0) {
            close();
            throw new IRodsRequestException(ErrorEnum.valueOf(status));
        }

        if (bsCallback.ioe != null)
        {
            close();
            throw bsCallback.ioe;
        }
    }

    private class BsCallback implements BitstreamCallback {

        private int len;
        private int off;
        private byte[] buffer;
        private IOException ioe;

        public BsCallback(int len, int off, byte[] buffer) {
            this.len = len;
            this.off = off;
            this.buffer = buffer;
        }

        public long getTotalBytes() {
            return len;
        }

        public void writeBitstream(OutputStream os) {
            try {
                os.write(buffer, off, len);
            } catch (IOException ioe) {
                this.ioe = ioe;
                LOG.error("Error writing to network stream ", ioe);
            }
        }
    }

    @Override
    public void close() throws IOException {
        IrodsApiRequest apiReq;

        int status;

        if (connection == null) {
            return;
        }

        if (connection.getVesion().is201Compat()) {
            DataObjCloseInp_PI body = new DataObjCloseInp_PI(fd, 0);
            apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_CLOSE201_AN, body,
                    null);
        } else {
            OpenedDataObjInp_PI body = new OpenedDataObjInp_PI(fd, 0, 0, 0, 0, 0, new KeyValPair_PI((Map) null));
            apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_CLOSE_AN, body, null);
        }

        try {
            status = apiReq.sendRequest(connection);
        } catch (IOException ioe) {
            connection.closeConnection();
            connection = null;
            throw ioe;
        }

        if ((status) < 0) {
            connection.closeConnection();
            connection = null;
            throw new IRodsRequestException("Error closing file, error: "
                    + ErrorEnum.valueOf(status));
        } else {
            fd = 0;
        }
        connection = null;
    }

    private void containsResource(IRodsConnection c, String resource) throws IOException {
        try {
            QueryBuilder qb = new QueryBuilder(GenQueryEnum.COL_R_RESC_NAME);
            QueryResult qr = qb.execute(c);
            while (qr.next()) {

                if (resource.equals(qr.getValue(GenQueryEnum.COL_R_RESC_NAME))) {
                    LOG.trace("successfully found resource: " + resource);
                    return;
                }

            }
            LOG.trace("No resource found: " + resource);
            throw new IRodsRequestException("No Such resource " + resource);
        } catch (IOException ex) {

            if (ex instanceof IRodsRequestException) {
                throw ex;
            }
            LOG.info("IOException in containsResource: ", ex);
            c.closeConnection();
            throw new IRodsRequestException("Error communicating with irods", ex);
        }
    }
}
