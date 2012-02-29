/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define dataObjWriteInp_PI "int dataObjInx; int len;"

 * @author toaster
 */
public class DataObjWriteInp_PI implements IRodsPI {

    private int dataObjInx;
    private int len;
    private byte[] bytes;

    public DataObjWriteInp_PI(int dataObjInx, int len) {
        this.dataObjInx = dataObjInx;
        this.len = len;
    }

    
    
    public byte[] getBytes()
    {
        if (bytes == null)
            bytes = toString().getBytes();
        return bytes;
    }

    public int getLen() {
        return len;
    }

    public int getDataObjInx() {
        return dataObjInx;
    }

    @Override
    public String toString()
    {
        return "<DataObjWriteInp_PI><dataObjInx>" + dataObjInx + "</dataObjInx><len>" + len + "</len></DataObjWriteInp_PI>";
    }

    
}
