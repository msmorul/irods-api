/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import org.apache.log4j.Logger;

/**
 * Server-side response for message header. Big difference is the open ended
 * intInfo
 * 
 * <MsgHeader_PI>
 * <type>RODS_API_REQ</type>
 * <msgLen>264</msgLen>
 * <errorLen>0</errorLen>
 * <bsLen>0</bsLen>
 * <intInfo>633</intInfo>
 * </MsgHeader_PI>
 * 
 * #define MsgHeader_PI "str type[HEADER_TYPE_LEN]; int msgLen; int errorLen; 
 * int bsLen; int intInfo;"
 * 
 * @author toaster
 */
public class MsgHeaderResponse_PI implements IRodsPI
{

    private static final Logger LOG = Logger.getLogger(MsgHeaderResponse_PI.class);
    private int intInfo;
    private long bsLen;
    private int errorLen;
    private int msgLen;
    private HeaderTypeEnum type;
    private byte[] bytes;

    public MsgHeaderResponse_PI(HeaderTypeEnum type, int msgLen, int errorLen, int bsLen, int intInfo)
    {
        this.intInfo = intInfo;
        this.bsLen = bsLen;
        this.errorLen = errorLen;
        this.msgLen = msgLen;
        this.type = type;
    }

    public MsgHeaderResponse_PI(ProtocolToken pt) throws ParseException
    {

        try
        {
            for ( ProtocolToken p : ProtocolToken.parseTokens(pt.getValue()) )
            {
                if ( "bsLen".equals(p.getName()) )
                {
                    bsLen = Long.parseLong(p.getValue());
                }
                else if ( "msgLen".equals(p.getName()) )
                {
                    msgLen = Integer.parseInt(p.getValue());
                }
                else if ( "errorLen".equals(p.getName()) )
                {
                    errorLen = Integer.parseInt(p.getValue());
                }
                else if ( "intInfo".equals(p.getName()) )
                {
                    intInfo = Integer.parseInt(p.getValue());
                }
                else if ( "type".equals(p.getName()) )
                {
                    type = HeaderTypeEnum.valueOf(p.getValue());
                }
            }
        }
        catch ( NumberFormatException e )
        {
            throw new ParseException(e.getMessage());
        }
    }

    public int getIntInfo()
    {
        return intInfo;
    }

    public long getBsLen()
    {
        return bsLen;
    }

    public int getErrorLen()
    {
        return errorLen;
    }

    public int getMsgLen()
    {
        return msgLen;
    }

    public HeaderTypeEnum getType()
    {
        return type;
    }

    public byte[] getBytes()
    {
        if ( bytes == null )
        {
            bytes = toString().getBytes();
        }
        return bytes;
    }

//    public ApiNumberEnum getApiNumber()
//    {
//        return ApiNumberEnum.NO_API_NUMBER;
//    }

    @Override
    public String toString()
    {
        return "<MsgHeader_PI><type>" + type.toString() +
                "</type><msgLen>" + msgLen + "</msgLen><errorLen>" +
                errorLen + "</errorLen><bsLen>" + bsLen +
                "</bsLen><intInfo>" + intInfo +
                "</intInfo></MsgHeader_PI>";
    }
}
