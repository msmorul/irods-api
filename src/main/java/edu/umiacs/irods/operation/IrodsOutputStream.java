/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.BitstreamCallback;
import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.ResultCallback;
import edu.umiacs.irods.api.ResultMessage;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.DataObjInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.api.pi.HeaderTypeEnum;
import edu.umiacs.irods.api.pi.KeyValPair_PI;
import edu.umiacs.irods.api.pi.OprTypeEnum;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * An input stream that uses the irods streaming put command. This is the most
 * efficient inputstream to load data into iRODS. Until you call close on this
 * stream, you will not be able to issue commands over the connection.
 *
 * If you need to write unknown sized data, or data intersperced w/ other irods
 * commands, use the unknownsizeoutputstream
 * 
 * @author toaster
 */
public class IrodsOutputStream extends OutputStream {

    private static final Logger LOG = Logger.getLogger(IrodsOutputStream.class);
    private OutputStream os;
    private long length;
    private IOException closeException;
    private IRodsConnection connection;
    private ResultMessage rm;

    public IrodsOutputStream(IRodsConnection connection, String file,
            String resource, long length) throws IOException {
        String dataType = "generic";
//        if (length < 1) {
//            length = -1;
//        } else {
            this.length = length;
//        }


        containsResource(connection, resource);

        Map<String, String> destInfo = new HashMap<String, String>();
        destInfo.put("dataType", dataType);
        destInfo.put("destRescName", resource);
        destInfo.put("dataIncluded", "");

        KeyValPair_PI keyValPair_PI = new KeyValPair_PI(destInfo);
        DataObjInp_PI body = new DataObjInp_PI(file, 448, 1, 0, this.length, 0,
                OprTypeEnum.PUT_OPR, keyValPair_PI);
        connection.nonblockingSendMessage(HeaderTypeEnum.RODS_API_REQ,
                ApiNumberEnum.DATA_OBJ_PUT_AN, body, new MyBitstreamCallback(),
                new MyResultCallback());
    }

    @Override
    public void write(int b) throws IOException {
        if (os == null) {
            throw new IOException("connection already closed");
        }
        try {
            os.write(b);
        } catch (IOException ioe) {
            connection.closeConnection();
            os = null;
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (os == null) {
            throw new IOException("connection already closed");
        }

        try {
            os.write(b);
        } catch (IOException ioe) {
            connection.closeConnection();
            os = null;
            throw ioe;
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (os == null) {
            throw new IOException("connection already closed");
        }

        try {
            os.write(b, off, len);
        } catch (IOException ioe) {
            connection.closeConnection();
            os = null;
            throw ioe;
        }
    }

    @Override
    public void close() throws IOException {
        if (os == null) {
            return;
        }

        try {
            LOG.trace("Calling close on outputstream");
            os.close();
        } catch (IOException ioe) {
            connection.closeConnection();
            os = null;
            throw ioe;
        }

        if (closeException != null) {
            throw closeException;
        }

        if (rm == null) {
            LOG.trace("result message is null");
            return;
        }

        if (rm.getHeader().getType() != HeaderTypeEnum.RODS_API_REPLY) {
            LOG.trace("Enexpected return message type: " + rm.getHeader().getType());
            throw new IOException("Enexpected return message type: " + rm.getHeader().getType());
        }

        if (rm != null && rm.getHeader().getIntInfo() < 0) {
            LOG.trace("bad return code from rm " + ErrorEnum.valueOf(rm.getHeader().getIntInfo()));
            throw new IRodsRequestException(ErrorEnum.valueOf(rm.getHeader().getIntInfo()));
        }
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

    class MyResultCallback implements ResultCallback {

        public void resultReceived(ResultMessage rm) {
            LOG.trace("Result receiveed" + rm.getHeader());
            IrodsOutputStream.this.rm = rm;
        }

        public void exceptionReceived(IOException ioe) {
            LOG.info("Error receiveed", ioe);
            IrodsOutputStream.this.closeException = ioe;
        }
    }

    class MyBitstreamCallback implements BitstreamCallback {

        public long getTotalBytes() {
            return length;
        }

        public void writeBitstream(OutputStream os) {
            IrodsOutputStream.this.os = os;
        }
    }
}
