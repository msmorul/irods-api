/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api;

import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.HeaderTypeEnum;
import edu.umiacs.irods.api.pi.IRodsPI;
import edu.umiacs.irods.api.pi.MsgHeaderRequest_PI;
import edu.umiacs.irods.api.pi.MsgHeaderResponse_PI;
import edu.umiacs.irods.api.pi.ProtocolToken;
import edu.umiacs.irods.api.pi.RError_PI;
import edu.umiacs.irods.api.pi.VersionEnum;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 * Class to handle the wire-level communication of talking to an IRODS server.
 * Other than ensuring message order is correct, all message creation and parsing
 * is handled through the current command handed to sendMessage. 
 * 
 *  If an exception occurs at this level, the connection is closed and no further
 *  operations are permitted. An exception 
 * 
 *  This class is not thread-safe. sent messages will not overlap, however no
 *  promise can be made regarding handling returned inputstreams and sending 
 *  messages. When a new message is sent any open input stream will be fully 
 *  ready and then closed, this WILL intefer with any clients that have not 
 * already handled their streams.
 * 
 * 
 * @author toaster
 */
public class IRodsConnection
{

    private static final Logger LOG = Logger.getLogger(IRodsConnection.class);
    private Socket connection;
    private RodsInputStream activeInput;
    private RodsOutputStream activeOutput;
    private boolean debug = LOG.isDebugEnabled();
    private VersionEnum vesion;
    private static final int MAX_HEADER_SIZE = 2048;

    IRodsConnection(Socket connection)
    {
        if ( connection == null )
        {
            throw new NullPointerException("connection is null");
        }

        this.connection = connection;
    }

    public static IRodsConnection openConnection(String host, int port)
            throws UnknownHostException, IOException
    {

        return new IRodsConnection(new Socket(host, port));
    }

    public void setVesion(VersionEnum vesion) {
        this.vesion = vesion;
    }

    public VersionEnum getVesion() {
        return vesion;
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    public boolean isClosed()
    {
        return connection.isClosed();
    }

    /**
     * Return true if an response inputstream is still available, available means
     * the last call has returned an input stream that has not been closed yet.
     * If the last returned no input stream, then this returns true
     * 
     * @return true if response inputstream is open
     */
    public boolean isStreamIdle()
    {
        return (activeInput == null || activeInput.closed || activeOutput == null);
    }

    /**
     * Send a message over this connection with only a header that has a type and 
     * intInfo set. This is useful for authentication requests
     * 
     * @param msgType type of message
     * @param intInfo value to set for int info
     * @return server response
     * @throws java.io.IOException
     */
    public ResultMessage sendMessage(HeaderTypeEnum msgType,
            ApiNumberEnum intInfo)
            throws IOException
    {
        return sendMessage(msgType, intInfo, null, null, null);
    }

    /**
     * Send a message with a header and body. intInfo in the header is derived
     * from the body part
     * 
     * @param msgType header message type
     * @param bodyPart body to send, must not be null
     * @return server response
     * @throws java.io.IOException
     */
    public ResultMessage sendMessage(HeaderTypeEnum msgType,
            ApiNumberEnum intInfo, IRodsPI bodyPart)
            throws IOException
    {
        return sendMessage(msgType, intInfo,
                bodyPart, null, null);
    }

    /**
     * Send a message w/ header, body, and bitstream attachment.
     * 
     * @param msgType
     * @param bodyPart
     * @param bitstreamLength length of bitstream
     * @param bitstreamData callback to write data to stream. Must not write more than bitstreamLength bytes
     * @return server response
     * @throws java.io.IOException
     */
    public ResultMessage sendMessage(HeaderTypeEnum msgType,
            ApiNumberEnum intInfo, IRodsPI bodyPart,
            BitstreamCallback bitstreamData) throws IOException
    {
        return sendMessage(msgType, intInfo,
                bodyPart, null, bitstreamData);
    }

    /**
     * mostly the same as send message except that resultmessage is returned 
     * out of band. This allows the bitstream callback to hold the outputstream
     * open after it returne from writebits
     * 
     */
    public synchronized void nonblockingSendMessage(HeaderTypeEnum msgType,
            ApiNumberEnum intInfo, IRodsPI bodyPart,
            BitstreamCallback bitstreamData, ResultCallback callback)
    {
        // if we have no bits, then just do the blocking call
        // we may want this to be truly non-blocking in the future, but
        // good enough for now
        if ( bitstreamData == null )
        {
            try
            {
                sendMessage(msgType, intInfo, bodyPart, null, null);
            }
            catch ( IOException ioe )
            {
                callback.exceptionReceived(ioe);
            }
        }
        else
        {
            if ( isClosed() )
            {
                callback.exceptionReceived(new IOException("Connection closed"));
            }
            // if there is any active input, close it
            if ( activeInput != null && !activeInput.closed )
            {
                try
                {
                    activeInput.close();
                }
                catch ( IOException ioe )
                {
                    closeConnection();
                    callback.exceptionReceived(ioe);
                }
            }

            if ( activeOutput != null )
            {
                try
                {
                    activeOutput.close();
                }
                catch ( IOException ioe )
                {
                    closeConnection();
                    callback.exceptionReceived(ioe);
                }
            }

            try
            {
                long bsLen = 0;
                if ( bitstreamData != null )
                {
                    bsLen = bitstreamData.getTotalBytes();
                }

                MsgHeaderRequest_PI msgHeader =
                        createHeader(msgType, intInfo, bodyPart, null, bsLen);

                try
                {
//                                long start = System.currentTimeMillis();
                    transmitMessage(msgHeader, bodyPart, null, bitstreamData,
                            callback);
//                LOG.info("Transmit time: " + (System.currentTimeMillis() - start));

                }
                catch ( IOException io )
                {
                    if ( debug )
                    {
                        LOG.error("Error transmitting: ", io);
                    }
                    closeConnection();
                    callback.exceptionReceived(io);
                }

//            try
//            {
//                return handleResponse();
//            }
//            catch ( IOException io )
//            {
//                LOG.error("Error reading response: ", io);
//                closeConnection();
//                throw io;
//            }
            }
//            catch ( IOException e )
//            {
//                callback.exceptionReceived(e);
//            }
            catch ( Exception e )
            {
                LOG.error("Uncaught excepiton", e);
                closeConnection();
                callback.exceptionReceived(new IOException("Uncaught exception" + e.getMessage()));

            }
        }
    }

    /**
     * Sent a message and block on the response. This will close the connection
     * on any ioexception and re-throw the exception
     * 
     * @param msgType
     * @param intInfo
     * @param bodyPart
     * @param errorPart
     * @param bitstreamData
     * @return
     * @throws java.io.IOException
     */
    private synchronized ResultMessage sendMessage(HeaderTypeEnum msgType,
            ApiNumberEnum intInfo, IRodsPI bodyPart, RError_PI errorPart,
            BitstreamCallback bitstreamData) throws IOException
    {
        if ( isClosed() )
        {
            throw new IOException("Connection closed");
        }
        // if there is any active input, close it
        if ( activeInput != null && !activeInput.closed )
        {
            try
            {
                activeInput.close();
            }
            catch ( IOException ioe )
            {
                closeConnection();
                throw ioe;
            }
        }

        if ( activeOutput != null )
        {
            try
            {
                activeOutput.close();
            }
            catch ( IOException ioe )
            {
                closeConnection();
                throw ioe;
            }
        }

        try
        {
            long bsLen = 0;
            if ( bitstreamData != null )
            {
                bsLen = bitstreamData.getTotalBytes();
            }

            MsgHeaderRequest_PI msgHeader =
                    createHeader(msgType, intInfo, bodyPart, errorPart, bsLen);

            try
            {
//                                long start = System.currentTimeMillis();
                transmitMessage(msgHeader, bodyPart, errorPart, bitstreamData,
                        null);
//                LOG.info("Transmit time: " + (System.currentTimeMillis() - start));

            }
            catch ( IOException io )
            {
                if ( debug )
                {
                    LOG.error("Error transmitting: ", io);
                }
                closeConnection();
                throw io;
            }

            try
            {
                return handleResponse();
            }
            catch ( IOException io )
            {
                LOG.error("Error reading response: ", io);
                closeConnection();
                throw io;
            }
        }
        catch ( IOException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            LOG.error("Uncaught excepiton", e);
            closeConnection();
            throw new IOException("Uncaught exception" + e.getMessage());

        }

    }

    private MsgHeaderRequest_PI createHeader(HeaderTypeEnum msgType,
            ApiNumberEnum intInfo, IRodsPI bodyPart,
            RError_PI errorPart, long bitstreamLength)
    {

        int bodyLen = 0;
        int errorLen = 0;

        if ( bodyPart != null )
        {
            bodyLen = bodyPart.getBytes().length;
        }

        if ( errorPart != null )
        {
            errorLen = errorPart.getBytes().length;
        }

        return new MsgHeaderRequest_PI(msgType, bodyLen, errorLen,
                bitstreamLength, intInfo);
    }

    /**
     * Read the response from the irods from the wire. This just tosses ioexceptions
     * up the chain, no closing is done.
     * 
     * @return
     * @throws java.io.IOException if network read failed
     */
    private ResultMessage handleResponse() throws IOException
    {
        DataInputStream input;
        byte[] buffer;
        MsgHeaderResponse_PI responseHeaderPI;
        ProtocolToken pt = null;
        RError_PI errMsg = null;
        ProtocolToken bodyToken = null;

        input = new DataInputStream(connection.getInputStream());

        //1. Read response bytes
        int responseHeaderSize = RodsUtil.parseInt(RodsUtil.readBytes(4, input));
        if ( debug )
        {
            LOG.debug("Read response header size: " + responseHeaderSize);
        }
        if (responseHeaderSize < 0 || responseHeaderSize > MAX_HEADER_SIZE)
        {
            LOG.info("Unexpected header size " + responseHeaderSize + " is this even irods?");
            throw new IOException("Unexpected header size " + responseHeaderSize);
        }
        
        //2. Read response header
        buffer = RodsUtil.readBytes(responseHeaderSize, input);

        pt = ProtocolToken.parseToken(new String(buffer));
        responseHeaderPI = new MsgHeaderResponse_PI(pt);
        if ( debug )
        {
            LOG.debug("Read response header " + responseHeaderPI);
        }


        //5. parse response body
        if ( responseHeaderPI.getMsgLen() > 0 )
        {
            buffer = RodsUtil.readBytes(responseHeaderPI.getMsgLen(), input);

            // get token and verify/ set w/ command
            bodyToken = ProtocolToken.parseToken(new String(buffer));
            if ( debug )
            {
                LOG.debug("Read message token: " + bodyToken);
            }
        }

        //6. parse error message
        if ( responseHeaderPI.getErrorLen() > 0 )
        {
            buffer = RodsUtil.readBytes(responseHeaderPI.getErrorLen(), input);
            pt = ProtocolToken.parseToken(new String(buffer));
            errMsg = new RError_PI(pt);
            if ( debug )
            {
                LOG.debug("Read error token: " + errMsg);
            }

        }

        //7. wrap input stream
        if ( responseHeaderPI.getBsLen() > 0 )
        {
            activeInput =
                    new RodsInputStream(input, responseHeaderPI.getBsLen());
        }
        else
        {
            activeInput = null;
        }
        return new ResultMessage(responseHeaderPI, bodyToken, errMsg,
                activeInput);
    }

    /**
     * Sent message over the wire, this does not do any type of error recovery
     * for exceptione, but lets calling functions handle this.
     * 
     * @param msgHeaderPI
     * @param bodyPart
     * @param errorPart
     * @param bitstreamData
     * @throws java.io.IOException
     */
    private void transmitMessage(MsgHeaderRequest_PI msgHeaderPI,
            IRodsPI bodyPart,
            RError_PI errorPart, BitstreamCallback bitstreamData,
            ResultCallback callback) throws IOException
    {
        byte[] headerSize;
        byte[] header;
        OutputStream output;


        if ( isClosed() )
        {
            throw new IOException("Attempt to send command to closed connection");
        }

        header = msgHeaderPI.getBytes();
        headerSize = RodsUtil.renderInt(header.length);
        output = connection.getOutputStream();


        //1. Send Header Size
        if ( debug )
        {
            LOG.debug("Sending Header Size: " + header.length);
        }
        output.write(headerSize);

        //2. Send Header
        if ( debug )
        {
            LOG.debug("Sending Header: " + msgHeaderPI);
        }
        output.write(header);

        //3. Send Body
        if ( msgHeaderPI.getMsgLen() > 0 )
        {
            if ( debug )
            {
                LOG.debug("Sending Body: " + bodyPart);
            }
            output.write(bodyPart.getBytes());
        }

        // 4. Send Error
        if ( msgHeaderPI.getErrorLen() > 0 )
        {
            if ( debug )
            {
                LOG.debug("Sending Error: " + errorPart);
            }
            output.write(errorPart.getBytes());
        }


        if ( msgHeaderPI.getBsLen() > 0 )
        {
            //TODO: Wrap outputstream to limit number of bits sent
            if ( debug )
            {
                LOG.debug("Sending bitstream, size: " +
                        msgHeaderPI.getBsLen());
            }
//            RodsOutputStream ros = new RodsOutputStream(output);
            activeOutput = new RodsOutputStream(output, callback);
            bitstreamData.writeBitstream(activeOutput);
            activeOutput = null;
//            ros.close();
        }

        LOG.debug("Flushing");
        output.flush();

    }

    /**
     * Close current connection. Any future attempts to call send message will
     * result in an IOException. This will also attempt to send the appropriate
     * shutdown packet.
     */
    public synchronized void closeConnection()
    {
        LOG.debug("Underlying connection: " + isClosed());
        if ( isClosed() )
        {
            return;
        }
        // Read out the response stream and close it
        try
        {
            if ( activeInput != null && !activeInput.closed )
            {
                activeInput.close();
            }
        }
        catch ( IOException ex )
        {
            LOG.error("IOException reading out response stream: ", ex);
        }

        // attempt to send shutdown packet
        try
        {
            MsgHeaderRequest_PI header = createHeader(HeaderTypeEnum.RODS_DISCONNECT,
                    ApiNumberEnum.NO_API_NUMBER, null, null, 0);

            transmitMessage(header, null, null, null, null);
        }
        catch ( IOException ex )
        {
            LOG.trace("IOException sending shutdown packet: ", ex);
        }

        // close all streams, and finally the socket. Be as forceful as possible
        // when closing this down. 
        try
        {
            connection.shutdownOutput();

        }
        catch ( IOException ex )
        {
            LOG.error("IOException closing: ", ex);
        }

        try
        {
            connection.shutdownInput();
        }
        catch ( IOException ex )
        {
            LOG.error("IOException closing: ", ex);
        }

        try
        {
            connection.close();
        }
        catch ( IOException ex )
        {
            LOG.error("IOException closing: ", ex);
        }
    }

    class RodsOutputStream extends OutputStream
    {

        OutputStream os;
        private boolean closed = false;
        long bytesWritten = 0;
        ResultCallback callback;

        public RodsOutputStream(OutputStream os, ResultCallback callback)
        {
            this.os = os;
            this.callback = callback;
        }

        private void testClose() throws IOException
        {
            if ( isClosed() || closed )
            {
                throw new IOException("Connection closed");
            }
        }

        @Override
        public void write(int b) throws IOException
        {
            testClose();

            try
            {
                os.write(b);
                bytesWritten++;
            }
            catch ( IOException ioe )
            {
                closeConnection();
                throw ioe;
            }
        }

        @Override
        public void write(byte[] b) throws IOException
        {
            testClose();
            try
            {
                os.write(b);
                bytesWritten += b.length;
            }
            catch ( IOException ioe )
            {
                closeConnection();
                throw ioe;
            }
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException
        {
            testClose();
            try
            {
                os.write(b, off, len);
                bytesWritten += len;
            }
            catch ( IOException ioe )
            {
                closeConnection();
                throw ioe;
            }
        }

        @Override
        public void close() throws IOException
        {
            if ( isClosed() || closed )
            {
                return;
            }

            flush();
            closed = true;
            LOG.trace("Wrote bytes to stream: " + bytesWritten);
            if ( callback != null )
            {
                LOG.trace("Performing callback");
                try
                {
                    callback.resultReceived(handleResponse());
                }
                catch ( IOException ioe )
                {
                    callback.exceptionReceived(ioe);
                    closeConnection();
                }
                activeOutput = null;
            }
        }

        @Override
        public void flush() throws IOException
        {
            try
            {
                os.flush();
            }
            catch ( IOException ioe )
            {
                closeConnection();
                throw ioe;
            }
        }
    }

    class RodsInputStream extends InputStream
    {

        InputStream is;
        long maxRead;
        long bytesRead;
        boolean closed;

        RodsInputStream(InputStream wrappedStream, long maxRead)
        {
            is = wrappedStream;
            this.maxRead = maxRead;
        }

        @Override
        public int available() throws IOException
        {
            int available;

            if ( closed )
            {
                return 0;
            }

            try
            {
                available = is.available();
            }
            catch ( IOException ioe )
            {
                closed = true;
                closeConnection();
                throw ioe;
            }

            if ( (available + bytesRead) > maxRead )
            {
                return (int) (maxRead - bytesRead);
            }
            return available;
        }

        @Override
        public long skip(long n) throws IOException
        {
            if ( closed )
            {
                return -1;
            }
            try
            {
                if ( (bytesRead + n) > maxRead )
                {
                    return is.skip(maxRead - bytesRead);
                }
                return is.skip(n);
            }
            catch ( IOException ioe )
            {
                closed = true;
                closeConnection();
                throw ioe;
            }
        }

        @Override
        public synchronized void mark(int readlimit)
        {
        }

        @Override
        public boolean markSupported()
        {
            return false;
        }

        @Override
        public synchronized void reset() throws IOException
        {
            throw new IOException("Mark not supported");
        }

        @Override
        @SuppressWarnings("empty-statement")
        public void close() throws IOException
        {
            if ( closed )
            {
                return;
            }

            if ( bytesRead < maxRead )
            {
                byte[] block = new byte[1024];
                while ( read(block) != -1 )
                {
                    ;
                }
            }

            closed = true;
            activeInput = null;
        }

        @Override
        public int read() throws IOException
        {
            if ( closed )
            {
                throw new IOException("Stream closed");
            }
            if ( bytesRead >= maxRead )
            {
                return -1;
            }

            bytesRead++;
            try
            {
                return is.read();
            }
            catch ( IOException ioe )
            {
                closed = true;
                closeConnection();
                throw ioe;
            }
        }

        @Override
        public int read(byte[] b) throws IOException
        {
            return read(b, 0, b.length);
//            if ( closed )
//            {
//                throw new IOException("Stream closed");
//            }
//            try
//            {
//                if ( bytesRead >= maxRead )
//                {
//                    return -1;
//                }
//                else if ( (b.length + bytesRead) > maxRead )
//                {
//                    int canRead = (int) (maxRead - bytesRead);
//                    int read = is.read(b, 0, canRead);
//                    bytesRead += read;
//                    return read;
//                }
//                else
//                {
//                    int read = is.read(b);
//                    bytesRead += read;
//                    return read;
//                }
//            }
//            catch ( IOException ioe )
//            {
//                closed = true;
//                closeConnection();
//                throw ioe;
//            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException
        {
            if ( closed )
            {
                throw new IOException("Stream closed");
            }


            if ( bytesRead >= maxRead )
            {
                return -1;
            }
            else if ( (len + bytesRead) > maxRead )
            {
                int canRead = (int) (maxRead - bytesRead);
                int read;
                try
                {
                    read = is.read(b, off, canRead);
                }
                catch ( IOException ioe )
                {
                    closed = true;
                    closeConnection();
                    throw ioe;
                }
                bytesRead += read;
                return read;
            }
            else
            {
                int read = is.read(b, off, len);
                bytesRead += read;
                return read;
            }
        }
    }
}
