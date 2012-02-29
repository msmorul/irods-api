/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * define dataObjCloseInp_PI "int l1descInx; double bytesWritten;"
 * @author toaster
 */
public class DataObjCloseInp_PI implements IRodsPI
{
    private int l1descInx;
    private long bytesWritten;
    
    private byte[] bytes;

    public DataObjCloseInp_PI(int l1descInx, long bytesWritten)
    {
        this.l1descInx = l1descInx;
        this.bytesWritten = bytesWritten;

    }
    
    @Override
    public String toString()
    {
        return "<dataObjCloseInp_PI><l1descInx>" + l1descInx + "</l1descInx><bytesWritten>" +
                bytesWritten + "</bytesWritten></dataObjCloseInp_PI>";

    }

    public byte[] getBytes()
    {

        if (bytes == null)
            bytes = toString().getBytes();
        return bytes;
    }

    public long getBytesWritten()
    {
        return bytesWritten;
    }


    public int getL1descInx()
    {
        return l1descInx;
    }

}
