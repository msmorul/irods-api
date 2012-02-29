/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 *#define DataObjInp_PI "str objPath[MAX_NAME_LEN]; int createMode; int openFlags; 
 * double offset; double dataSize; int numThreads; int oprType; 
 * struct *SpecColl_PI; struct KeyValPair_PI;"
 * @author toaster
 */
public class DataObjInp_PI implements IRodsPI
{

    private String objPath;
    private int createMode;
    private int openFlags;
    private long offset;
    private long dataSize;
    private int numThreads;
    private OprTypeEnum oprType;
    //TODO: What about SpecColl_PI
    private KeyValPair_PI keyValPair_PI;

    private byte[] bytes;
    
    public DataObjInp_PI(String objPath, int createMode, int openFlags,
            long offset, long dataSize, int numThreads, OprTypeEnum oprType, 
            KeyValPair_PI keyValPair_PI)
    {
        this.objPath = objPath;
        this.createMode = createMode;
        this.openFlags = openFlags;
        this.offset = offset;
        this.dataSize = dataSize;
        this.numThreads = numThreads;
        this.oprType = oprType;
        this.keyValPair_PI = keyValPair_PI;
    }

    
    
    @Override
    public String toString()
    {
        return "<DataObjInp_PI><objPath>" + objPath + "</objPath><createMode>" +
                createMode + "</createMode>" + "<openFlags>" + openFlags +
                "</openFlags><offset>" + offset + "</offset><dataSize>" +
                dataSize + "</dataSize><numThreads>" + numThreads +
                "</numThreads><oprType>" + oprType.getInt() + "</oprType>" +
                keyValPair_PI + "</DataObjInp_PI>";

    }

    public byte[] getBytes()
    {

        if (bytes == null)
            bytes = toString().getBytes();
        return bytes;
    }


    public String getObjPath()
    {
        return objPath;
    }

    public int getCreateMode()
    {
        return createMode;
    }

    public int getOpenFlags()
    {
        return openFlags;
    }

    public double getOffset()
    {
        return offset;
    }

    public int getNumThreads()
    {
        return numThreads;
    }

    public OprTypeEnum getOprType()
    {
        return oprType;
    }

    public KeyValPair_PI getKeyValPair_PI()
    {
        return keyValPair_PI;
    }
}
