/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 *
 * @author toaster
 */
public class AuthResponseInp_PI implements IRodsPI
{

    private String username;
    private String response;
    private byte[] bytes;

    public AuthResponseInp_PI(String username, String response)
    {
        this.username = username;
        this.response = response;
    }

    public String getResponse()
    {
        return response;
    }

    public String getUsername()
    {
        return username;
    }

    @Override
    public String toString()
    {
        return "<authResponseInp_PI><response>" + response +
                "</response><username>" + username +
                "</username></authResponseInp_PI>";
    }

    public byte[] getBytes()
    {
        if ( bytes == null )
        {
            bytes = toString().getBytes();
        }
        return bytes;
    }

//    public ApiNumberEnum getApiNumber()
//    {
//        return ApiNumberEnum.AUTH_RESPONSE_AN;
//    }
}
