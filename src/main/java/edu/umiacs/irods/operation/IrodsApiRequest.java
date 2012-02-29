/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.BitstreamCallback;
import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.ResultMessage;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.HeaderTypeEnum;
import edu.umiacs.irods.api.pi.IRodsPI;
import edu.umiacs.irods.api.pi.ParseException;
import edu.umiacs.irods.api.pi.ProtocolToken;
import edu.umiacs.irods.api.pi.RError_PI;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;

/**
 * Send a high-level irods api request message. The message consists of an 
 * api number, message body, and optionally a callback to load a bitstream.
 * 
 * api requests may be re-used, however they are not thread-safe
 * 
 * @author toaster
 */
public class IrodsApiRequest
{

    private ApiNumberEnum apiNumber;
    private BitstreamCallback bsCallback;
    private ProtocolToken resultToken;
    private IRodsPI body,  resultBody;
    private RError_PI resultError;
    private InputStream resultStream;
    private long resultByteCount;
    private static final Logger LOG = Logger.getLogger(IrodsApiRequest.class);

    public IrodsApiRequest(ApiNumberEnum apiNumber, IRodsPI body,
            BitstreamCallback bs)
    {
        this.apiNumber = apiNumber;
        if ( apiNumber.hasInputBitstream() && bs == null )
        {
            throw new NullPointerException(apiNumber +
                    " requires a bitstream callback");
        }
        else if ( !apiNumber.hasInputBitstream() && bs != null )
        {
            throw new NullPointerException(apiNumber +
                    " requires no bitstream callback");
        }
        this.bsCallback = bs;
        this.body = body;
    }

    public final ApiNumberEnum getApiNumber()
    {
        return apiNumber;
    }

    public final int sendRequest(IRodsConnection ic) throws IOException
    {
        resultBody = null;
        resultStream = null;
        resultByteCount = 0;
        resultError = null;

        ResultMessage rm = ic.sendMessage(HeaderTypeEnum.RODS_API_REQ,
                apiNumber, body, bsCallback);

        if ( rm.getHeader().getType() != HeaderTypeEnum.RODS_API_REPLY )
        {
            throw new IOException("Enexpected return message type: " + rm.getHeader().getType());
        }

        resultToken = rm.getBodyToken();
        if ( rm.getHeader().getBsLen() > 0 )
        {
            LOG.trace("Setting inputstream response");
            resultByteCount = rm.getHeader().getBsLen();
            resultStream = rm.getBitInputStream();
        }

        if ( rm.getHeader().getErrorLen() > 0 )
        {
            LOG.trace("Setting error message");
            resultError = rm.getErrorMsg();
        }

        return rm.getHeader().getIntInfo();
    }

    /**
     * Return the result input stream. This must be called before any other 
     * operation on the irods connection. Otherwise, the returned bitstream will
     * already be closed.
     * 
     * @return
     */
    public final InputStream getResultInputStream()
    {
        return resultStream;
    }

    public final long getInputStreamSize()
    {
        return resultByteCount;
    }

    public final RError_PI getErrorPI()
    {
        return resultError;
    }

    /**
     * Parse the result into the specified pack instruction. The supplied packing
     * instruction MUST contain a constructor that takes a single ProtocolTokan
     * as an argument
     * 
     * @param expectedClass type to return
     * @return result packing instruction
     * @throws edu.umiacs.irods.api.pi.ParseException if constructor can't parse
     */
    public final <T extends IRodsPI> T getResultPI(Class<T> expectedClass)
            throws ParseException
    {

        if ( !apiNumber.getOutPI().isAssignableFrom(expectedClass) )
        {
            throw new ClassCastException("Instruction " + apiNumber +
                    " expects class " + apiNumber.getOutPI() + " found: " +
                    expectedClass);
        }

        if ( resultToken == null )
        {
            return null;
        }

        if ( resultBody != null && resultBody.getClass() == expectedClass )
        {
            return expectedClass.cast(resultBody);
        }

        if ( resultToken != null )
        {
            try
            {
                T msgBody =
                        expectedClass.getConstructor(ProtocolToken.class).newInstance(resultToken);
                resultBody = msgBody;
                return msgBody;
            }
            catch ( InstantiationException ex )
            {
                throw new RuntimeException(ex);
            }
            catch ( IllegalAccessException ex )
            {
                throw new RuntimeException(ex);
            }
            catch ( IllegalArgumentException ex )
            {
                throw new RuntimeException(ex);
            }
            catch ( InvocationTargetException ex )
            {
                if ( ex.getCause() instanceof ParseException )
                {
                    throw (ParseException) ex.getCause();
                }
                throw new RuntimeException(ex);
            }
            catch ( NoSuchMethodException ex )
            {
                throw new RuntimeException("Illegal class " +
                        apiNumber.getOutPI().getName() +
                        " doesn't have a constructor that takes protocoltoken");
            }
            catch ( SecurityException ex )
            {
                throw new RuntimeException(ex);
            }
        }

        return null;
    }
}
