/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * #define dataObjReadInp_PI "int l1descInx; int len;"
 * @author toaster
 */
public class DataObjReadInp_PI implements IRodsPI
{
    private int l1descInx;
    private int len;
    
    private byte[] bytes;

    public DataObjReadInp_PI(int l1descInx, int len)
    {
        this.l1descInx = l1descInx;
        this.len = len;

    }
    
    @Override
    public String toString()
    {
        return "<dataObjReadInp_PI><l1descInx>" + l1descInx + "</l1descInx><len>" +
                len + "</len></dataObjReadInp_PI>";

    }

    public byte[] getBytes()
    {

        if (bytes == null)
            bytes = toString().getBytes();
        return bytes;
    }

    public int getLen()
    {
        return len;
    }

    public int getL1descInx()
    {
        return l1descInx;
    }

}
