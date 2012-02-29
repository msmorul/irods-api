/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * #define ReiAndArg_PI "struct *Rei_PI; struct ReArg_PI;"
 * @author toaster
 */
public class ReiAndArg_PI implements IRodsPI
{

    private Rei_PI rei_PI;
    private ReArg_PI reArg_PI;

    public ReiAndArg_PI(Rei_PI rei_PI, ReArg_PI reArg_PI)
    {
        this.rei_PI = rei_PI;
        this.reArg_PI = reArg_PI;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Rei_PI getRei_PI()
    {
        return rei_PI;
    }

    public ReArg_PI getReArg_PI()
    {
        return reArg_PI;
    }
}
