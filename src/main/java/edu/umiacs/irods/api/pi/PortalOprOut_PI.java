/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.List;

/**
 * #define PortalOprOut_PI "int status; int l1descInx; int numThreads; 
 * str chksum[NAME_LEN]; struct PortList_PI;"
 * @author toaster
 */
public class PortalOprOut_PI implements IRodsPI
{

    private int status;
    private int l1descInx;
    private int numThreads;
    private String chksum;
    private PortList_PI portListPI;

    public PortalOprOut_PI(int status, int l1descInx, int numThreads, String chksum, 
            PortList_PI portListPI)
    {
        this.status = status;
        this.l1descInx = l1descInx;
        this.numThreads = numThreads;
        this.chksum = chksum;
        this.portListPI = portListPI;
    }

    public PortalOprOut_PI(ProtocolToken pt) throws ParseException
    {
        pt.checkName("PortalOprOut_PI");
        List<ProtocolToken> ptList = pt.getTokenListValue();
     
        ptList.get(0).checkName("status");
        status = ptList.get(0).getIntValue();
        
        ptList.get(1).checkName("l1descInx");
        l1descInx = ptList.get(1).getIntValue();
        
        ptList.get(2).checkName("numThreads");
        numThreads = ptList.get(2).getIntValue();
        
        ptList.get(3).checkName("chksum");
        chksum = ptList.get(3).getValue();
        
        portListPI = new PortList_PI(ptList.get(4));
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<PortalOprOut_PI><status>");
        sb.append(status);
        sb.append("</status><l1descInx>");
        sb.append(l1descInx);
        sb.append("</l1descInx><numThreads>");
        sb.append(numThreads);
        sb.append("</numThreads><chksum>");
        sb.append(chksum);
        sb.append(portListPI);
        sb.append("</PortalOprOut_PI>");
        return sb.toString();
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getStatus()
    {
        return status;
    }

    public int getL1descInx()
    {
        return l1descInx;
    }

    public int getNumThreads()
    {
        return numThreads;
    }

    public String getChksum()
    {
        return chksum;
    }

    public PortList_PI getPortListPI()
    {
        return portListPI;
    }
}
