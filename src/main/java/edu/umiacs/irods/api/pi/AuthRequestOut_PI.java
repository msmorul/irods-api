/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 *
 * @author toaster
 */
public class AuthRequestOut_PI implements IRodsPI
{

    private String challenge;

    public AuthRequestOut_PI(String challenge)
    {
        this.challenge = challenge;
    }

    public AuthRequestOut_PI(ProtocolToken pt) throws ParseException
    {
        pt.checkName("authRequestOut_PI");
        ProtocolToken childPt = ProtocolToken.parseToken(pt.getValue());
        childPt.checkName("challenge");
        challenge = childPt.getValue();
    }

    public String getChallenge()
    {
        return challenge;
    }

    public byte[] getBytes()
    {
        //TODO: getBytes on AuthRequestOut_PI, probably never needed
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
