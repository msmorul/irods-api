/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define OpenedDataObjInp_PI "int l1descInx; int len; int whence;
 * int oprType; double offset; double bytesWritten; struct KeyValPair_PI;"

 * @author toaster
 */
public class OpenedDataObjInp_PI implements IRodsPI {

    private int l1descInx;
    private int len;
    private int whence;
    private int oprType;
    private long offset;
    private long bytesWritten;
    private KeyValPair_PI keyValPair_PI;
    private byte[] bytes;

    public OpenedDataObjInp_PI(int l1descInx, int len, int whence, int oprType,
            long offset, long bytesWritten, KeyValPair_PI keyValPair_PI) {
        this.l1descInx = l1descInx;
        this.len = len;
        this.whence = whence;
        this.oprType = oprType;
        this.offset = offset;
        this.bytesWritten = bytesWritten;
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

    public int getWhence() {
        return whence;
    }

    public long getOffset() {
        return offset;
    }

    public int getLen() {
        return len;
    }

    public int getL1descInx() {
        return l1descInx;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }


    public int getOprType() {
        return oprType;
    }
    

    @Override
    public String toString()
    {
//        #define OpenedDataObjInp_PI "int l1descInx; int len; int whence;
// * int oprType; double offset; double bytesWritten; struct KeyValPair_PI;"
        return "<OpenedDataObjInp_PI><l1descInx>"+l1descInx+"</l1descInx><len>"+len+"</len>" +
                "<whence>"+whence+"</whence><oprType>"+oprType+"</oprType>" +
                "<offset>"+offset+"</offset><bytesWritten>"+bytesWritten+"</bytesWritten>"
                + keyValPair_PI + "</OpenedDataObjInp_PI>";
    }

    
}
