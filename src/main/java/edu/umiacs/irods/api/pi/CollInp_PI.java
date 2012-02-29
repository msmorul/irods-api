/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define CollInp_PI "str collName[MAX_NAME_LEN]; struct KeyValPair_PI;"
 * 
 * @author toaster
 */
public class CollInp_PI implements IRodsPI {

    private String collName;
    private KeyValPair_PI keyValPair_PI;
    private byte[] bytes;

    public CollInp_PI(String collName, KeyValPair_PI keyValPair_PI)
    {
        this.collName = collName;
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

    @Override
    public String toString()
    {
        return "<CollInp_PI><collName>" + collName + "</collName>" + keyValPair_PI + "</CollInp_PI>";
    }

    
}
