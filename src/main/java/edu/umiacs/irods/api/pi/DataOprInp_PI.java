/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define DataOprInp_PI "int oprType; int numThreads; int srcL3descInx; int destL3descInx; 
 * int srcRescTypeInx; int destRescTypeInx; double offset; double dataSize; struct KeyValPair_PI;"
 * @author toaster
 */
public class DataOprInp_PI implements IRodsPI {

    private int oprType;
    private int numThreads;
    private int srcL3descInx;
    private int destL3descInx;
    private int srcRescTypeInx;
    private int destRescTypeInx;
    private long offset;
    private long dataSize;
    private KeyValPair_PI keyValPair_PI;

    public DataOprInp_PI(int oprType, int numThreads, int srcL3descInx, int destL3descInx, 
            int srcRescTypeInx, int destRescTypeInx, long offset, long dataSize, KeyValPair_PI keyValPair_PI)
    {
        this.oprType = oprType;
        this.numThreads = numThreads;
        this.srcL3descInx = srcL3descInx;
        this.destL3descInx = destL3descInx;
        this.srcRescTypeInx = srcRescTypeInx;
        this.destRescTypeInx = destRescTypeInx;
        this.offset = offset;
        this.dataSize = dataSize;
        this.keyValPair_PI = keyValPair_PI;
    }

    public int getOprType()
    {
        return oprType;
    }

    public int getNumThreads()
    {
        return numThreads;
    }

    public int getSrcL3descInx()
    {
        return srcL3descInx;
    }

    public int getDestL3descInx()
    {
        return destL3descInx;
    }

    public int getSrcRescTypeInx()
    {
        return srcRescTypeInx;
    }

    public int getDestRescTypeInx()
    {
        return destRescTypeInx;
    }

    public double getOffset()
    {
        return offset;
    }

    public double getDataSize()
    {
        return dataSize;
    }

    public KeyValPair_PI getKeyValPair_PI()
    {
        return keyValPair_PI;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
