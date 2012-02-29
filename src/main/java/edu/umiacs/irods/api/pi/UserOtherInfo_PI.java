/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 *#define UserOtherInfo_PI "str userInfo[NAME_LEN]; str userComments[NAME_LEN]; 
 * str userCreate[TIME_LEN]; str userModify[TIME_LEN];"
 * 
 * @author toaster
 */
public class UserOtherInfo_PI implements IRodsPI {

    private String userInfo;
    private String userComments;
    private String userCreate; // date?
    private String userModify; // date?

    public UserOtherInfo_PI(String userInfo, String userComments, String userCreate, String userModify)
    {
        this.userInfo = userInfo;
        this.userComments = userComments;
        this.userCreate = userCreate;
        this.userModify = userModify;
    }
    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getUserInfo()
    {
        return userInfo;
    }

    public String getUserComments()
    {
        return userComments;
    }

    public String getUserCreate()
    {
        return userCreate;
    }

    public String getUserModify()
    {
        return userModify;
    }
    
}
