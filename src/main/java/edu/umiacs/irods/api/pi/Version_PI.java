/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 *#define Version_PI "int status; str relVersion[NAME_LEN]; str apiVersion[NAME_LEN]; int reconnPort; 
 * str reconnAddr[LONG_NAME_LEN]; int cookie;"
 * 
 * <Version_PI>
 * <status>0</status>
 * <relVersion>rods0.9</relVersion>
 * <apiVersion>b</apiVersion>
 * <reconnPort>0</reconnPort>
 * <reconnAddr></reconnAddr>
 * <cookie>0</cookie>
 * </Version_PI>
 * 
 * @author toaster
 */
public class Version_PI implements IRodsPI
{

    private int status;
    private String relVersion;
    private String apiVersion;
    private int reconnPort;
    private String reconnAddr;
    private int cookie;
    private byte[] bytes;

    public Version_PI(int status, String relVersion, String apiVersion, 
            int reconnPort, String reconnAddr, int cookie)
    {
        this.status = status;
        this.relVersion = relVersion;
        this.apiVersion = apiVersion;
        this.reconnPort = reconnPort;
        this.reconnAddr = reconnAddr;
        this.cookie = cookie;

    }

    public Version_PI(ProtocolToken pt) throws ParseException
    {
        try
        {
            for ( ProtocolToken p : ProtocolToken.parseTokens(pt.getValue()) )
            {
                if ( "status".equals(p.getName()) )
                {
                    status = Integer.parseInt(p.getValue());
                }
                else if ( "relVersion".equals(p.getName()) )
                {
                    relVersion = p.getValue();
                }
                else if ( "apiVersion".equals(p.getName()) )
                {
                    apiVersion = p.getValue();
                }
                else if ( "reconnPort".equals(p.getName()) )
                {
                    reconnPort = Integer.parseInt(p.getValue());
                }
                else if ( "reconnAddr".equals(p.getName()) )
                {
                    reconnAddr = p.getValue();
                }
                else if ( "cookie".equals(p.getName()) )
                {
                    cookie = Integer.parseInt(p.getValue());
                }
                else
                {
                    throw new ParseException("Enexpected token: " + p.getName());
                }

            }
        }
        catch ( NumberFormatException e )
        {
            throw new ParseException(e.getMessage());
        }

    }

    public byte[] getBytes()
    {
        if ( bytes == null )
        {
            bytes = toString().getBytes();
        }
        return bytes;
    }


    @Override
    public String toString()
    {
        return "<Version_PI><status>" + status + "</status><relVersion>" +
                relVersion + "</relVersion><apiVersion>" + apiVersion +
                "</apiVersion><reconnPort>" + reconnPort +
                "</reconnPort><reconnAddr>" + reconnAddr +
                "</reconnAddr><cookie>" + cookie + "</cookie></Version_PI>";

    }

    public int getStatus()
    {
        return status;
    }

    public String getRelVersion()
    {
        return relVersion;
    }

    public String getApiVersion()
    {
        return apiVersion;
    }

    public int getReconnPort()
    {
        return reconnPort;
    }

    public String getReconnAddr()
    {
        return reconnAddr;
    }

    public int getCookie()
    {
        return cookie;
    }
}
