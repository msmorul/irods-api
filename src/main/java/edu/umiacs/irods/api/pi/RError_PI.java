/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * #define RError_PI "int count; struct *RErrMsg_PI[count];"
 * @author toaster
 */
public class RError_PI implements IRodsPI
{

    List<RErrMsg_PI> errMsg;
    byte[] bytes;

    public RError_PI(List<RErrMsg_PI> errMsg)
    {
        this.errMsg = errMsg;
    }

    public RError_PI(ProtocolToken pt) throws ParseException
    {
//        errMsg = new ArrayList<RErrMsg_PI>();
        int count;
        pt.checkName("RError_PI");

        List<ProtocolToken> tokens = pt.getTokenListValue();

        tokens.get(0).checkName("count");
        count = tokens.get(0).getIntValue();
        errMsg = new ArrayList<RErrMsg_PI>(count);

        for ( int i = 1; i < tokens.size(); i++ )
        {
            errMsg.add(new RErrMsg_PI(tokens.get(i)));
        }
    }

    public List<RErrMsg_PI> getRErrMsgPI()
    {
        if ( errMsg != null )
        {
            return new ArrayList<RErrMsg_PI>(errMsg);
        }
        return Collections.emptyList();
    }

    public int getCount()
    {
        if ( errMsg != null )
        {
            return errMsg.size();
        }
        return 0;
    }

    public byte[] getBytes()
    {
        if ( errMsg == null || errMsg.size() == 0 )
        {
            return new byte[0];
        }

        if ( bytes == null )
        {
            //TODO: generate error pi using children
            bytes = new byte[0];
        }

        return bytes;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<RError_PI><count>");
        sb.append(errMsg.size());
        sb.append("</count>");
        for ( RErrMsg_PI msg : errMsg )
        {
            sb.append(msg.toString());
        }
        sb.append("</RError_PI>");
        return sb.toString();
    }
}
