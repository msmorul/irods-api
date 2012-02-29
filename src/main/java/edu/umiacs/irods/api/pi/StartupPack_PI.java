/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 *<StartupPack_PI>
 *<irodsProt>1</irodsProt>
 *<reconnFlag>0</reconnFlag>
 *<connectCnt>0</connectCnt>
 *<proxyUser>rods</proxyUser>
 *<proxyRcatZone>tempZone</proxyRcatZone>
 *<clientUser>rods</clientUser>
 *<clientRcatZone>tempZone</clientRcatZone>
 *<relVersion>rods0.9</relVersion>
 *<apiVersion>b</apiVersion>
 *<option></option>
 *</StartupPack_PI>
 * 
 * #define StartupPack_PI "int irodsProt; int reconnFlag; int connectCnt; str proxyUser[NAME_LEN]; 
 * str proxyRcatZone[NAME_LEN]; str clientUser[NAME_LEN]; str clientRcatZone[NAME_LEN]; 
 * str relVersion[NAME_LEN]; str apiVersion[NAME_LEN]; str option[NAME_LEN];"
 * 
 * @author toaster
 */
public class StartupPack_PI implements IRodsPI
{

    private ProtocolEnum irodsProt = ProtocolEnum.XML_PROT;
    private int reconnFlag = 0;
    private int connectCnt = 0;
    private String proxyUser;
    private String proxyRcatZone;
    private String clientUser;
    private String clientRcatZone;
    private String relVersion;
    private String apiVersion;
    private String option;
    private byte[] bytes;

    public StartupPack_PI()
    {
    }

    public StartupPack_PI(String user, String zone, VersionEnum version)
    {
        this.clientUser = user;
        this.proxyUser = user;
        this.proxyRcatZone = zone;
        this.clientRcatZone = zone;
        relVersion = version.getRelVersion();
        apiVersion = version.getApiVersion();
    }

    public ProtocolEnum getIrodsProt()
    {
        return irodsProt;
    }

    public byte[] getBytes()
    {

        if ( bytes == null )
        {
            bytes = toString().getBytes();
        }
        return bytes;
    }

//    public StartupPack_PI parseProtocol(ProtocolToken pt) throws ParseException
//    {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

//    public ApiNumberEnum getApiNumber()
//    {
//        return ApiNumberEnum.NO_API_NUMBER;
//    }

    public int getReconnFlag()
    {
        return reconnFlag;
    }

    public int getConnectCnt()
    {
        return connectCnt;
    }

    public String getProxyUser()
    {
        return proxyUser;
    }

    public String getProxyRcatZone()
    {
        return proxyRcatZone;
    }

    public String getClientUser()
    {
        return clientUser;
    }

    public String getClientRcatZone()
    {
        return clientRcatZone;
    }

    public String getRelVersion()
    {
        return relVersion;
    }

    public String getApiVersion()
    {
        return apiVersion;
    }

    public String getOption()
    {
        return option;
    }

    @Override
    public String toString()
    {
        return "<StartupPack_PI><irodsProt>" + irodsProt.getType() +
                "</irodsProt><reconnFlag>" + reconnFlag +
                "</reconnFlag><connectCnt>" + connectCnt +
                "</connectCnt><proxyUser>" + proxyUser +
                "</proxyUser><proxyRcatZone>" + proxyRcatZone +
                "</proxyRcatZone><clientUser>" + clientUser +
                "</clientUser><clientRcatZone>" + clientRcatZone +
                "</clientRcatZone><relVersion>" + relVersion +
                "</relVersion><apiVersion>" + apiVersion +
                "</apiVersion><option>" + option +
                "</option></StartupPack_PI>";
    }
}
