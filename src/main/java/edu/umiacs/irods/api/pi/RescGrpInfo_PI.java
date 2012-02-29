/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define RescGrpInfo_PI "str rescGroupName[NAME_LEN]; str *rescName; 
 * int *cacheNext; struct *RescGrpInfo_PI;"
 * 
 * @author toaster
 */
public class RescGrpInfo_PI implements IRodsPI {

    private String rescGroupName;
    private String rescName;
    private int cacheNext;
    private RescGrpInfo_PI rescGrpInfo_PI;

    public RescGrpInfo_PI(String rescGroupName, String rescName, int cacheNext, RescGrpInfo_PI rescGrpInfo_PI)
    {
        this.rescGroupName = rescGroupName;
        this.rescName = rescName;
        this.cacheNext = cacheNext;
        this.rescGrpInfo_PI = rescGrpInfo_PI;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCacheNext()
    {
        return cacheNext;
    }

    public String getRescGroupName()
    {
        return rescGroupName;
    }

    public RescGrpInfo_PI getRescGrpInfo_PI()
    {
        return rescGrpInfo_PI;
    }

    public String getRescName()
    {
        return rescName;
    }
    
    
}
