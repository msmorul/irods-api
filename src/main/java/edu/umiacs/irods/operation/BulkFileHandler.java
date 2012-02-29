/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.operation;

import java.io.IOException;

/**
 * Interface to allow bulk processing of files in irods
 * 
 * @author toaster
 */
public interface BulkFileHandler {
    
    public void openFile(String path, long length) throws IOException;
    public void closeFile();
    /**
     * Will be called when a new directory is encountered.
     * 
     * @param directory that is being entered (relative to start)
     * @return true if directory can be used for processing
     */
    public boolean mkdir(String directory);
            
    
    /**
     * Notification when a block of bytes are available. The byte buffer WILL
     * be reused later, so if your app needs access to it outside the call 
     * duration, clone it. Arrays.copyOfRange(bytes, offset, offset + length)
     * 
     * @param bytes buffer containing bytes to read
     * @param offset available bytes start at offset
     * @param length total length to read after offset
     */
    public void writeBytes(byte[] bytes, long offset, int length) throws IOException;
    
    /**
     * Called if there was an error reading or writing (above) a file. This will not result in a
     * call to any bfs listeners. If this is called on a file, you will not see a
     * closeFile in the future.
     * 
     * @param e
     */
    public void ioError(IOException e);

    public boolean processItem(String path, boolean isDirectory);
}
