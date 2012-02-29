/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 *
 * @author toaster
 */
public class IntPi implements IRodsPI {

    private byte[] bytes;
    private int myInt;

    public IntPi(int myInt) {
        this.myInt = myInt;
    }

    @Override
    public String toString() {
        return "<INT_PI><myInt>"+myInt+"</myInt></INT_PI>";
    }

    public byte[] getBytes() {
         if ( bytes == null )
        {
            bytes = toString().getBytes();
        }
        return bytes;
    }

}
