/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * #define InxValPair_PI "int isLen; int *inx(isLen); str *svalue[isLen];" 
 * @author toaster
 */
public class InxValPair_PI implements IRodsPI
{

    private List<Integer> inx;
    private List<String> svalue;
    private byte[] bytes;

    public InxValPair_PI(Map<Integer, String> inxMap)
    {
        if ( inxMap != null && inxMap.size() > 0 )
        {
            inx = new ArrayList<Integer>();
            svalue = new ArrayList<String>();
            for ( Integer key : inxMap.keySet() )
            {
                inx.add(key);
                svalue.add(inxMap.get(key));
            }
        }
    }

    public byte[] getBytes()
    {
        if ( bytes == null )
        {
            bytes = toString().getBytes();
        }
        return bytes;
    }

    public String getSValue(int i)
    {
        return svalue.get(i);
    }
    
    public int getInx(int i)
    {
        return inx.get(i);
    }
    
    public int getIsLen()
    {
        if ( inx != null )
        {
            return inx.size();
        }
        return 0;
    }

    @Override
    public String toString()
    {
        String retString = "<InxValPair_PI><isLen>" + getIsLen() + "</isLen>";
        if ( inx != null )
        {
            for ( Integer word : inx )
            {
                retString += "<inx>" + word + "</inx>";
            }
            for ( String word : svalue )
            {
                retString += "<svalue>" + word + "</svalue>";
            }
        }
        retString += "</InxValPair_PI>";
        return retString;
    }
}
