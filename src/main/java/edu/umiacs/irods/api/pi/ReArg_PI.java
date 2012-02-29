/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

import java.util.List;

/**
 * #define ReArg_PI "int myArgc; str *myArgv[myArgc];"
 * @author toaster
 */
public class ReArg_PI implements IRodsPI {

    private List<String> args;

    public ReArg_PI(List<String> args)
    {
        this.args = args;
    }
    
    
    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
