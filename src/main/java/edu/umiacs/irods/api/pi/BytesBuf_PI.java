/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * #define BytesBuf_PI "int buflen; char *buf(buflen);"
 * @author toaster
 */
public class BytesBuf_PI implements IRodsPI {

    char[] buffer;

    public BytesBuf_PI(char[] buffer)
    {
        this.buffer = buffer;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public char[] getBuffer()
    {
        //TODO: should this clone
        return buffer;
    }
    
}
