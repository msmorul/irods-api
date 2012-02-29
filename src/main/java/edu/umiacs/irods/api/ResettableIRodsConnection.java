/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author toaster
 */
public class ResettableIRodsConnection extends IRodsConnection
{

    public ResettableIRodsConnection(Socket socket)
    {
        super(socket);
    }

    public static ResettableIRodsConnection openAuthenticatedConnection(String host, int port)
            throws UnknownHostException, IOException
    {
        ResettableIRodsConnection connection =
                new ResettableIRodsConnection(new Socket(host, port));
        
        return connection;
    }
    
    
}
