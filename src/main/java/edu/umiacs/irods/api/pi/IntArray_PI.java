/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;

/**
 *#define IntArray_PI "int len; int *value(len);" 
 * 
 * @author toaster
 */
public class IntArray_PI implements IRodsPI
{

    public List<Integer> intList;

    public IntArray_PI(List<Integer> intList)
    {
        this.intList = intList;
    }

    public IntArray_PI(ProtocolToken pt) throws ParseException
    {
        pt.checkName("IntArray_PI");
        List<ProtocolToken> items = pt.getTokenListValue();
        
        int size = items.get(0).getIntValue();
        
        if ( items.size() != (size + 1) )
        {
            throw new ParseException("Item length is: " + size + " but actual is: " +
                    (items.size() - 1));
        }

        intList = new ArrayList<Integer>(size);
        for ( int i = 1; i < items.size(); i++ )
        {
            intList.set(i, items.get(i).getIntValue());
        }
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Integer> getIntList()
    {
        return new ArrayList<Integer>(intList);
    }
}
