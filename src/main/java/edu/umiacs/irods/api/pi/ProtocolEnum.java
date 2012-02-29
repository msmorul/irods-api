/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * protocal type defined in startupPack
 * typedef enum {
 * NATIVE_PROT,
 * XML_PROT
 * } irodsProt_t;
 * @author toaster
 */
public enum ProtocolEnum
{

    NATIVE_PROT(0),
    XML_PROT(1);
    private int type;

    ProtocolEnum(int type)
    {
        this.type = type;
    }
    public int getType()
    {
        return type;
    }
}
