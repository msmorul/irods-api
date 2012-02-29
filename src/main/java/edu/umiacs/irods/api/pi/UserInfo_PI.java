/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define UserInfo_PI "str userName[NAME_LEN]; str rodsZone[NAME_LEN]; 
 * str userType[NAME_LEN]; int sysUid; struct AuthInfo_PI; struct UserOtherInfo_PI;"
 * @author toaster
 */
public class UserInfo_PI implements IRodsPI {

    private String userName;
    private String rodsZone;
    private String userType;
    private int sysUid;
    private AuthInfo_PI authInfo_PI;
    private UserOtherInfo_PI userOtherInfo_PI;

    public UserInfo_PI(String userName, String rodsZone, String userType, int sysUid, AuthInfo_PI authInfo_PI, UserOtherInfo_PI userOtherInfo_PI)
    {
        this.userName = userName;
        this.rodsZone = rodsZone;
        this.userType = userType;
        this.sysUid = sysUid;
        this.authInfo_PI = authInfo_PI;
        this.userOtherInfo_PI = userOtherInfo_PI;
    }
    
    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getUserName()
    {
        return userName;
    }

    public String getRodsZone()
    {
        return rodsZone;
    }

    public String getUserType()
    {
        return userType;
    }

    public int getSysUid()
    {
        return sysUid;
    }

    public AuthInfo_PI getAuthInfo_PI()
    {
        return authInfo_PI;
    }

    public UserOtherInfo_PI getUserOtherInfo_PI()
    {
        return userOtherInfo_PI;
    }

}
