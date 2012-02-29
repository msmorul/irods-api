/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api;

import java.io.OutputStream;

/**
 *
 * @author toaster
 */
public interface BitstreamCallback
{

    /**
     * Get number of bytes writeBitstream will write to the output stream
     * @return number of bytes to write
     */
    public long getTotalBytes();

    /**
     * Write bytes to the supplied output stream. Total number of bytes written
     * will not be allowed to exceed getTotalBytes from above. A client should call
     * close when writing has finished. If this is used in conjunction w/ the 
     * non-blocking sendMessage and close is not called, your connection will be frozen.
     * If you use the blocking call, you must write all output in this call as
     * the output stream will be closed when this returns
     * 
     * @param os outputstream to write data to
     */
    public void writeBitstream(OutputStream os);
}
