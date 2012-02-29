/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;

/**
 * #define MsParamArray_PI "int paramLen; int oprType; struct *MsParam_PI[paramLen];"
 * @author toaster
 */
public class MsParamArray_PI implements IRodsPI {

    private int oprType;
    private List<MsParam_PI> msParan_PI;

    public MsParamArray_PI(int oprType, List<MsParam_PI> msParan_PI)
    {
        this.oprType = oprType;
        this.msParan_PI = msParan_PI;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<MsParam_PI> getMsParan_PI()
    {
        return new ArrayList<MsParam_PI>(msParan_PI);
    }

    public int getOprType()
    {
        return oprType;
    }
    
}
