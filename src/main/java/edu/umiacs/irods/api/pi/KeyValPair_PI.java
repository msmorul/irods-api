/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *#define KeyValPair_PI "int ssLen; str *keyWord[ssLen]; str *svalue[ssLen];" 
 * @author toaster
 */
public class KeyValPair_PI implements IRodsPI
{

    private List<String> keyWord;
    private List<String> svalue;
    private byte[] bytes;

    public KeyValPair_PI(Map<String, String> keyPairs)
    {
        if ( keyPairs != null && keyPairs.size() > 0 )
        {
            keyWord = new ArrayList<String>();
            svalue = new ArrayList<String>();
            for ( String key : keyPairs.keySet() )
            {
                keyWord.add(key);
                if ( keyPairs.get(key) == null )
                {
                    svalue.add("");
                }
                else
                {
                    svalue.add(keyPairs.get(key));
                }
            }
        }

    }

    public KeyValPair_PI(ProtocolToken pt)
    {
        //TODO
        throw new UnsupportedOperationException();
    }

    public int getSsLen()
    {
        if ( keyWord != null )
        {
            return keyWord.size();
        }
        return 0;

    }

    @Override
    public String toString()
    {
        String retString = "<KeyValPair_PI><ssLen>" + getSsLen() + "</ssLen>";
        if ( keyWord != null )
        {
            for ( String word : keyWord )
            {
                retString += "<keyWord>" + word + "</keyWord>";
            }
            for ( String word : svalue )
            {
                retString += "<svalue>" + word + "</svalue>";
            }
        }

        retString += "</KeyValPair_PI>";
        return retString;
    }

    public byte[] getBytes()
    {
        if ( bytes == null )
        {
            bytes = toString().getBytes();
        }

        return bytes;
    }
}
