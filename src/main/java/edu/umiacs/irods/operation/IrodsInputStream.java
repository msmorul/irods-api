/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.DataObjCloseInp_PI;
import edu.umiacs.irods.api.pi.DataObjInp_PI;
import edu.umiacs.irods.api.pi.DataObjReadInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.KeyValPair_PI;
import edu.umiacs.irods.api.pi.OpenedDataObjInp_PI;
import edu.umiacs.irods.api.pi.OprTypeEnum;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * 
 * @author toaster
 */
public class IrodsInputStream extends InputStream {

    private static final Logger LOG = Logger.getLogger(IrodsInputStream.class);
    private IRodsConnection connection;
    int fd;

    public IrodsInputStream(String fileName, IRodsConnection connection) throws IOException {

        this.connection = connection;
        open(fileName);
    }

    private void open(String fileName) throws IOException {
        IrodsApiRequest apiReq;
        DataObjInp_PI body;
        int status;

        if (connection != null) {
            body = new DataObjInp_PI(fileName, 0, 0, 0, 0, 0,
                    OprTypeEnum.NO_OPR_TYPE,
                    new KeyValPair_PI((Map) null));
            apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_OPEN_AN, body,
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
    public int read(byte[] b) throws IOException {

        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int status;
        IrodsApiRequest apiReq;
        if (connection == null) {
            throw new IOException("Inputstream closed");
        }

        if (connection.getVesion().is201Compat()) {

            DataObjReadInp_PI body = new DataObjReadInp_PI(fd, len);
            apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_READ201_AN,
                    body, null);
        } else {
            OpenedDataObjInp_PI body = new OpenedDataObjInp_PI(fd, len, 0, 0, 0, 0, new KeyValPair_PI((Map) null));
            apiReq = new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_READ_AN,
                    body, null);
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
        try {
            if (apiReq.getInputStreamSize() == 0) {
                return -1;
            }
            int iStreamLength = (int) apiReq.getInputStreamSize();
            int totalRead = 0;
            int currOff = off;
            while (totalRead < iStreamLength) {

                int read = apiReq.getResultInputStream().read(b, currOff, iStreamLength);
                if (LOG.isTraceEnabled()) {
                    LOG.trace("partial read: " + read + " currOffset " + currOff + " len " + iStreamLength);
                }
                totalRead += read;
                currOff += read;
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("Returning read: " + iStreamLength + " request (off,len): " + off + "," + len);
            }
            return iStreamLength;
        } catch (IOException ioe) {
            throw ioe;
        }

    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Inputstream does not allow single char reads");
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
}
