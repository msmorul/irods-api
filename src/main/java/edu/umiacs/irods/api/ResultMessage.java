/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api;

import edu.umiacs.irods.api.pi.MsgHeaderResponse_PI;
import edu.umiacs.irods.api.pi.ProtocolToken;
import edu.umiacs.irods.api.pi.RError_PI;
import java.io.InputStream;

/**
 * Wrapper contains any response message from irods, Generated from irodsconnection
 * @author toaster
 */
public class ResultMessage
{

    private MsgHeaderResponse_PI header;
    private ProtocolToken bodyToken;
    private RError_PI errorMsg;
    private InputStream is;

    ResultMessage(MsgHeaderResponse_PI header, ProtocolToken bodyToken,
            RError_PI errorMsg, InputStream is)
    {
        this.header = header;
        this.bodyToken = bodyToken;
        this.errorMsg = errorMsg;
        this.is = is;
    }

    public MsgHeaderResponse_PI getHeader()
    {
        return header;
    }

    public ProtocolToken getBodyToken()
    {
        return bodyToken;
    }

    public RError_PI getErrorMsg()
    {
        return errorMsg;
    }

    public InputStream getBitInputStream()
    {
        return is;
    }
}
