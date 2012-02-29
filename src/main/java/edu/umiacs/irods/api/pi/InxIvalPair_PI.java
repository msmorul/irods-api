/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * #define InxIvalPair_PI "int iiLen; int *inx(iiLen); int *ivalue(iiLen);" 
 * @author toaster
 */
public class InxIvalPair_PI implements IRodsPI {

    private List<Integer>  inx;
    private List<Integer>   ivalue;
//    private Map<Integer, Integer> keyPairs;
    private byte[] bytes;
    
    public InxIvalPair_PI(Map<Integer, Integer> keyPairs)
    {

        if ( keyPairs != null && keyPairs.size() > 0 )
        {
            inx = new ArrayList<Integer>();
            ivalue = new ArrayList<Integer>();
            for ( Integer key : keyPairs.keySet() )
            {
                inx.add(key);
                ivalue.add(keyPairs.get(key));
            }
        }

    }
    
    public byte[] getBytes()
    {
        if (bytes == null)
            bytes = toString().getBytes();
        return bytes;
    }

    public int getIiLen()
    {
        if (inx != null)
            return inx.size();
        return 0;
    }
    
    @Override
    public String toString()
    {
        String retString = "<InxIvalPair_PI><iiLen>" + getIiLen() + "</iiLen>";
        if ( inx != null )
        {
            for ( Integer word : inx )
            {
                retString += "<inx>" + word + "</inx>";
            }
            for ( Integer word : ivalue )
            {
                retString += "<ivalue>" + word + "</ivalue>";
            }
        }
//        if ( keyPairs != null )
//        {
//            
//            for ( Integer key : keyPairs.keySet() )
//            {
//                retString += "<inx>" + key + "</inx><ivalue>" +
//                        keyPairs.get(key) + "</ivalue>";
//            }
//        }
        retString += "</InxIvalPair_PI>";
        return retString;
    }
}
