/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define ReconnMsg_PI "int cookie; int reconnOpr;"
 * 
 * @author toaster
 */
public class ReconnMsg_PI implements IRodsPI {

    private int cookie;
    private int reconnOpr;

    public ReconnMsg_PI(int cookie, int reconnOpr)
    {
        this.cookie = cookie;
        this.reconnOpr = reconnOpr;
    }
    
    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getReconnOpr()
    {
        return reconnOpr;
    }

    public int getCookie()
    {
        return cookie;
    }

}
