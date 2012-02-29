/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Class to parse the XML tags received from irods
 * 
 * @author toaster
 */
public class ProtocolToken
{

    private static final Logger LOG = Logger.getLogger(ProtocolToken.class);
    private static int openChar = '<';
    private static int closeChar = '>';
    private String name;
    private String value;

    /**
     * read an open token, returning the discovered token. The reader will be left
     * pointing to the char immediately following the token
     * 
     * 
     * @param r source to read string from
     * @return
     */
    public static final ProtocolToken parseToken(String line) throws ParseException
    {
        int startTag = line.indexOf(openChar);
        int endTag = line.indexOf(closeChar);
        String tagName = line.substring(startTag + 1, endTag);
//        LOG.debug("Start idx: " + startTag + " end idx: " + endTag +
//                " Tag Name: " + tagName);

        int closeTag = line.indexOf("</" + tagName + ">");

        ProtocolToken pt = new ProtocolToken();

        pt.name = tagName;
        pt.value = line.substring(endTag + 1, closeTag);
        return pt;
    }

    /**
     * Look for multiple token in a line
     * 
     * @return
     */
    public static final List<ProtocolToken> parseTokens(String line) throws ParseException
    {
        ArrayList<ProtocolToken> list = new ArrayList<ProtocolToken>();
        String subString = line;
        int startTag = 1, endTag;

        while ( startTag != -1 )
        {
            startTag = subString.indexOf(openChar);
            if ( startTag == -1 )
            {
                continue;
            }

            endTag = subString.indexOf(closeChar);
            String tagName = subString.substring(startTag + 1, endTag);
//        LOG.debug("Start idx: " + startTag + " end idx: " + endTag +
//                " Tag Name: " + tagName);
            String cTag = "</" + tagName + ">";
            int closeTag = subString.indexOf(cTag);

            ProtocolToken pt = new ProtocolToken();

            pt.name = tagName;
            pt.value = subString.substring(endTag + 1, closeTag);
            list.add(pt);
            subString = subString.substring(closeTag + cTag.length());
        }

        return list;

    }

    public boolean hasName(String expected)
    {
        return  !expected.equals(name) ;
    }
    
    public void checkName(String expected) throws ParseException
    {
        if ( !expected.equals(name) )
        {
            throw new ParseException("Expected token name: " + expected +
                    " found: " + name);
        }
    }

    /**
     * Return name of tag
     * 
     * @return tag name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Return value of tag, value is everything between start/end of the named tag
     * May include child tags
     * 
     * @return tag content
     */
    public String getValue()
    {
        return value;
    }

    public ProtocolToken getTokenValue() throws ParseException
    {
        return parseToken(value);
    }
    
    public List<ProtocolToken> getTokenListValue() throws ParseException
    {
        return parseTokens(value);
    }
    
    public int getIntValue() throws ParseException
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch ( NumberFormatException e )
        {
            throw new ParseException("Value not integer: " + value);
        }
    }

    public long getLongValue() throws ParseException
    {
        try
        {
            return Long.parseLong(value);
        }
        catch ( NumberFormatException e )
        {
            throw new ParseException("Value not integer: " + value);
        }
    }
    
    public double getDoubleValue() throws ParseException
    {
        try
        {
            return Double.parseDouble(value);
        }
        catch ( NumberFormatException e )
        {
            throw new ParseException("Value not integer: " + value);
        }
    }
    
    @Override
    public String toString()
    {
        return "Name: " + name + " Value: " + value;
    }
}
