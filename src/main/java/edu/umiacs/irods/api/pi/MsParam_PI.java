/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 *#define MsParam_PI "str *label; piStr *type; ?type *inOutStruct; struct *BinBytesBuf_PI;"
 * @author toaster
 */
public class MsParam_PI implements IRodsPI{
    private String label;
    //private String type; wtf
    //private inOutStruct
    private BinBytesBuf_PI binBytesBuf_PI;

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
