/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.log4j.Logger;

/**
 *
 * @author toaster
 */
public class LocalFileReceiver implements BulkFileHandler
{

    private static final Logger LOG = Logger.getLogger(LocalFileReceiver.class);
    private File baseDirectory;
    private RandomAccessFile raf;

    public LocalFileReceiver(File baseDirectory)
    {
        this.baseDirectory = baseDirectory;
    }

    public void openFile(String path, long length) throws IOException
    {
        File newDir = new File(baseDirectory, path);
        raf = new RandomAccessFile(newDir, "rw");
        raf.setLength(length);
    }

    public void ioError(IOException e)
    {
        try
        {
            if ( raf != null )
            {
                raf.close();
                raf = null;
            }
        }
        catch ( IOException ioe )
        {
            LOG.error("error closing ", ioe);
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeFile()
    {
        try
        {
            if ( raf != null )
            {
                raf.close();
                raf = null;
            }
        }
        catch ( IOException ioe )
        {
            LOG.error("error closing ", ioe);
        }
    }
    public boolean processItem(String path, boolean isDirectory)
    {
        return true;
    }

    public boolean mkdir(String directory)
    {
        File newDir = new File(baseDirectory, directory);
        return (newDir.isDirectory() || newDir.mkdir());
    }

    public void writeBytes(byte[] bytes, long offset, int length) throws IOException
    {
        try
        {
            if ( raf != null )
            {
                raf.seek(offset);
                raf.write(bytes, 0, length);
            }
        }
        catch ( IOException ioe )
        {
            closeFile();
            throw ioe;
        }
    }
}
