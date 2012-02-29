/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.List;

/**
 * #define PortList_PI "int portNum; int cookie; int sock; int windowSize; 
 * str hostAddr[LONG_NAME_LEN];"
 * @author toaster
 */
public class PortList_PI implements IRodsPI
{

    private int portNum;
    private int cookie;
    private int sock;
    private int windowSize;
    private String hostAddr;

    public PortList_PI(int portNum, int cookie, int sock, int windowSize, String hostAddr)
    {
        this.portNum = portNum;
        this.cookie = cookie;
        this.sock = sock;
        this.windowSize = windowSize;
        this.hostAddr = hostAddr;
    }

    public PortList_PI(ProtocolToken pt) throws ParseException
    {
        pt.checkName("PortList_PI");
        
        List<ProtocolToken> ptList = pt.getTokenListValue();
        
        ptList.get(0).checkName("portNum");
        portNum = ptList.get(0).getIntValue();
        
        ptList.get(1).checkName("cookie");
        cookie = ptList.get(1).getIntValue();
        
        ptList.get(2).checkName("sock");
        sock = ptList.get(2).getIntValue();
        
        ptList.get(3).checkName("windowSize");
        windowSize = ptList.get(3).getIntValue();
                
        ptList.get(4).checkName("hostAddr");
        hostAddr = ptList.get(4).getValue();
        
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<PortList_PI><portNum>");
        sb.append(portNum);
        sb.append("</portNum><cookie>");
        sb.append(cookie);
        sb.append("</cookie><sock>");
        sb.append(sock);
        sb.append("</sock><windowSize>");
        sb.append(windowSize);
        sb.append("</windowSize><hostAddr>");
        sb.append(hostAddr);
        sb.append("</hostAddr></PortList_PI>");
        
        return sb.toString();
    }
    
    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPortNum()
    {
        return portNum;
    }

    public int getCookie()
    {
        return cookie;
    }

    public int getSock()
    {
        return sock;
    }

    public int getWindowSize()
    {
        return windowSize;
    }

    public String getHostAddr()
    {
        return hostAddr;
    }
}
