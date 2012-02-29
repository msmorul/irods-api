/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.List;

/**
 * MiscSvrInfo_PI "int serverType; int serverBootTime; str relVersion[NAME_LEN]; 
 * str apiVersion[NAME_LEN]; str rodsZone[NAME_LEN];"
 * @author toaster
 */
public class MiscSvrInfo_PI implements IRodsPI
{

    private ServerTypeEnum serverType;
    private int serverBootTime;
    private String relVersion;
    private String apiVersion;
    private String rodsZone;
    private byte[] bytes;

    public MiscSvrInfo_PI(ServerTypeEnum serverType, int serverBootTime, String relVersion,
            String apiVersion, String rodsZone)
    {
        this.serverType = serverType;
        this.serverBootTime = serverBootTime;
        this.relVersion = relVersion;
        this.apiVersion = apiVersion;
        this.rodsZone = rodsZone;
    }
    
    public MiscSvrInfo_PI(ProtocolToken pt) throws ParseException
    {
        pt.checkName("MiscSvrInfo_PI");

        List<ProtocolToken> tokens = pt.getTokenListValue();
        
        tokens.get(0).checkName("serverType");
        serverType = ServerTypeEnum.valueOf(tokens.get(0).getIntValue());
        
        tokens.get(1).checkName("serverBootTime");
        serverBootTime = tokens.get(1).getIntValue();
        
        tokens.get(2).checkName("relVersion");
        relVersion = tokens.get(2).getValue();
        
        tokens.get(3).checkName("apiVersion");
        apiVersion = tokens.get(3).getValue();
        
        tokens.get(4).checkName("rodsZone");
        rodsZone = tokens.get(4).getValue();

    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("getBytes not implemented");
    }

    public ServerTypeEnum getServerType()
    {
        return serverType;
    }

    public int getServerBootTime()
    {
        return serverBootTime;
    }

    public String getRelVersion()
    {
        return relVersion;
    }

    public String getApiVersion()
    {
        return apiVersion;
    }

    public String getRodsZone()
    {
        return rodsZone;
    }
}
