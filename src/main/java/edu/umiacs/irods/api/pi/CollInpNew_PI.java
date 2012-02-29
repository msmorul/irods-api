/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define CollInpNew_PI "str collName[MAX_NAME_LEN]; int flags; int oprType; struct KeyValPair_PI;"
 * 
 * @author toaster
 */
public class CollInpNew_PI implements IRodsPI {

    private String collName;
    private int flags;
    private int oprType;
    private KeyValPair_PI keyValPair_PI;
    private byte[] bytes;

    public CollInpNew_PI(String collName, int flags, int oprType, KeyValPair_PI keyValPair_PI)
    {
        this.collName = collName;
        this.flags = flags;
        this.oprType = oprType;
        this.keyValPair_PI = keyValPair_PI;
    }
    
    public byte[] getBytes()
    {
        if (bytes == null)
            bytes = toString().getBytes();
        return bytes;
    }

    public KeyValPair_PI getKeyValPair_PI()
    {
        return keyValPair_PI;
    }

    public String getCollName()
    {
        return collName;
    }

    public int getFlags() {
        return flags;
    }

    public int getOprType() {
        return oprType;
    }
    

    @Override
    public String toString()
    {
        return "<CollInpNew_PI><collName>" + collName + "</collName><flags>"+flags+"</flags><oprType>"+oprType+"</oprType>" + keyValPair_PI + "</CollInpNew_PI>";
    }

    
}
