/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 *#define AuthInfo_PI "str authScheme[NAME_LEN]; int authFlag; int flag; 
 * int ppid; str host[NAME_LEN]; str authStr[NAME_LEN];"
 * 
 * @author toaster
 */
public class AuthInfo_PI implements IRodsPI {

    private String authScheme;
    private int authFlag;
    private int ppid;
    private String host;
    private String authStr;

    public AuthInfo_PI(String authScheme, int authFlag, int ppid, String host, String authStr)
    {
        this.authScheme = authScheme;
        this.authFlag = authFlag;
        this.ppid = ppid;
        this.host = host;
        this.authStr = authStr;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAuthScheme()
    {
        return authScheme;
    }

    public int getAuthFlag()
    {
        return authFlag;
    }

    public int getPpid()
    {
        return ppid;
    }

    public String getHost()
    {
        return host;
    }

    public String getAuthStr()
    {
        return authStr;
    }

    public void setAuthStr(String authStr)
    {
        this.authStr = authStr;
    }
    
}
