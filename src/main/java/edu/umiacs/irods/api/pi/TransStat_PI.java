/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 *#define TransStat_PI "int numThreads; double bytesWritten;"
 * @author toaster
 */
public class TransStat_PI implements IRodsPI {

    private int numThreads;
    private double bytesWritten;

    public TransStat_PI(int numThreads, double bytesWritten)
    {
        this.numThreads = numThreads;
        this.bytesWritten = bytesWritten;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getBytesWritten()
    {
        return bytesWritten;
    }

    public int getNumThreads()
    {
        return numThreads;
    }
    
}
