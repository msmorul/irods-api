/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * #define VaultPathPolicy_PI "int scheme; int addUserName; int trimDirCnt;"
 * @author toaster
 */
public class VaultPathPolicy_PI implements IRodsPI
{

    int scheme;
    int addUserName;
    int trimDirCnt;

    public VaultPathPolicy_PI(int scheme, int addUserName, int trimDirCnt)
    {
        this.scheme = scheme;
        this.addUserName = addUserName;
        this.trimDirCnt = trimDirCnt;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getAddUserName()
    {
        return addUserName;
    }

    public int getScheme()
    {
        return scheme;
    }

    public int getTrimDirCnt()
    {
        return trimDirCnt;
    }
}
