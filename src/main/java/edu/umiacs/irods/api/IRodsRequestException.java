/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api;

import edu.umiacs.irods.api.pi.ErrorEnum;
import java.io.IOException;

/**
 *
 * @author toaster
 */
public class IRodsRequestException extends IOException
{

    private ErrorEnum en;

    /**
     * Creates a new instance of <code>IRodsRequestException</code> without detail message.
     */
    public IRodsRequestException(ErrorEnum en)
    {
        super(en.toString() + "(" + en.getInt() + ")");
    }

    public ErrorEnum getErrorCode()
    {
        return en;
    }

    /**
     * Constructs an instance of <code>IRodsRequestException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public IRodsRequestException(String msg)
    {
        super(msg);
    }
    
    public IRodsRequestException(String msg, Throwable t)
    {
        super(msg + t.getMessage());
    }
}
