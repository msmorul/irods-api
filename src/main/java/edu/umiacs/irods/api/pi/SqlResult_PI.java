/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;

/**
 *#define SqlResult_PI "int attriInx; int reslen; str *value(rowCnt)(reslen);"  
 * @author toaster
 */
public class SqlResult_PI implements IRodsPI
{

    private GenQueryEnum attriInx;
    private int reslen; // this always appears to return 50
    List<String> value;

    public SqlResult_PI(ProtocolToken pt, int totalAttributes) throws ParseException
    {
        pt.checkName("SqlResult_PI");
        List<ProtocolToken> contents = pt.getTokenListValue();

        contents.get(0).checkName("attriInx");
        attriInx = GenQueryEnum.valueOf(contents.get(0).getIntValue());

        contents.get(1).checkName("reslen");
        reslen = contents.get(1).getIntValue();

//        if ( totalAttributes != (contents.size() - 2) )
//        {
//            throw new ParseException("Expected " + totalAttributes +
//                    " values, got: " + (contents.size() - 2));
//        }
        
        value = new ArrayList<String>();
        for (int i = 2; i < contents.size(); i++)
        {
            value.add(contents.get(i).getValue());
        }
    }

    /**
     * Return type of this sql result list
     * @return
     */
    public GenQueryEnum getAttriInx()
    {
        return attriInx;
    }

    public int getReslen()
    {
        return reslen;
    }

    /**
     * Returns values for this result, currently does not copy internal list
     * 
     * @return
     */
    public List<String> getValues()
    {
        return value;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
