/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api;

import java.io.IOException;

/**
 *
 * @author toaster
 */
public interface ResultCallback
{
    /**
     * Called when a delayed send message returns.
     * 
     * @param rm result from irods call
     */
    public void resultReceived(ResultMessage rm);
    
    public void exceptionReceived(IOException ioe);
}
