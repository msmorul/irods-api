/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.List;

/**
 *#define RErrMsg_PI "int status; str msg[ERR_MSG_LEN];"
 * @author toaster
 */
public class RErrMsg_PI implements IRodsPI
{

    private int status;
    private String msg;

    public RErrMsg_PI(int status, String msg)
    {
        this.status = status;
        this.msg = msg;
    }

    public RErrMsg_PI(ProtocolToken pt) throws ParseException
    {
        pt.checkName("RErrMsg_PI");
        List<ProtocolToken> tokens = pt.getTokenListValue();

        tokens.get(0).checkName("status");
        status = tokens.get(0).getIntValue();

        tokens.get(1).checkName("msg");
        msg = tokens.get(1).getValue();
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getMsg()
    {
        return msg;
    }

    public int getStatus()
    {
        return status;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<RErrMsg_PI><status>");
        sb.append(status);
        sb.append("</status><msg>");
        sb.append(msg);
        sb.append("</msg></RErrMsg_PI>");
        return sb.toString();
    }
}
