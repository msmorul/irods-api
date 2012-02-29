/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import org.apache.log4j.Logger;

/**
 *
 * @author toaster
 */
public class RodsUtil
{

    private static final Logger LOG = Logger.getLogger(RodsUtil.class);
    static final int CHALLENGE_LENGTH = 64;
    static final int MAX_PASSWORD_LENGTH = 50;

//    public static void pokeICat(IRodsConnection ic, IRodsPI command)
//    {
//        ResultMessage rm = ic.sendMessage(HeaderTypeEnum.RODS_API_REQ, command);
//        LOG.debug("Header: " + )
//    }
    /**
     * Taken from Jargon v2.0
     * 
     * Add the password to the end of the challenge string,
     * pad to the correct length, and take the md5 of that.
     */
    public static String challengeResponse(String challenge, String password)
            throws SecurityException, IOException
    {
        // Convert base64 string to a byte array
        byte[] chal = null;
        byte[] temp = Base64.decode(challenge);
        //new sun.misc.BASE64Decoder().decodeBuffer(challenge);

        if ( password.length() < MAX_PASSWORD_LENGTH )
        {
            //pad the end with zeros to MAX_PASSWORD_LENGTH
            chal = new byte[CHALLENGE_LENGTH + MAX_PASSWORD_LENGTH];
        }
        else
        {
            throw new IllegalArgumentException("Password is too long");
        }

        //add the password to the end
        System.arraycopy(temp, 0, chal, 0, temp.length);
        temp = password.getBytes();
        System.arraycopy(temp, 0, chal, CHALLENGE_LENGTH, temp.length);

        //get the md5 of the challenge+password
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            chal = digest.digest(chal);
        }
        catch ( GeneralSecurityException e )
        {
            SecurityException se = new SecurityException();
            se.initCause(e);
            throw se;
        }

        //after md5 turn any 0 into 1
        for ( int i = 0; i < chal.length; i++ )
        {
            if ( chal[i] == 0 )
            {
                chal[i] = 1;
            }
        }

        //return to Base64
        return Base64.encode(chal);
    //new sun.misc.BASE64Encoder().encode( chal );
    }

    public static byte[] renderInt(int cookie)
    {

        byte[] length = new byte[4];
        length[0] = (byte) (cookie >> 24);
        length[1] = (byte) (cookie >> 16);
        length[2] = (byte) (cookie >> 8);
        length[3] = (byte) (cookie);
        return length;

    }
    
    public static byte[] renderLong(long l)
    {

        byte[] length = new byte[8];
        length[0] = (byte) (l >> 56);
        length[1] = (byte) (l >> 48);
        length[2] = (byte) (l >> 40);
        length[3] = (byte) (l >> 32);
        length[4] = (byte) (l >> 24);
        length[5] = (byte) (l >> 16);
        length[6] = (byte) (l >> 8);
        length[7] = (byte) (l);
        return length;

    }
    
    public static int parseInt(byte[] bytes)
    {
        return (((int) bytes[0] & 0x00FF) << 24) |
                (((int) bytes[1] & 0x00FF) << 16) |
                (((int) bytes[2] & 0x00FF) << 8) |
                ((int) bytes[3] & 0x00FF);
    }
    
    public static long parseLong(byte[] bytes)
    {
        return (((long) bytes[0] & 0x00FF) << 56) |
                (((long) bytes[1] & 0x00FF) << 48) |
                (((long) bytes[2] & 0x00FF) << 40) |
                (((long) bytes[3] & 0x00FF) << 32) |
                (((long) bytes[4] & 0x00FF) << 24) |
                (((long) bytes[5] & 0x00FF) << 16) |
                (((long) bytes[6] & 0x00FF) << 8) |
                ((long) bytes[7] & 0x00FF);
    }
    
    public static byte[] readBytes(int numToRead, DataInputStream is) throws IOException
    {
        byte[] bytes = new byte[numToRead];
//        int read = 0;
        is.readFully(bytes);
//        if ( (read = is.readFully(bytes)) != numToRead )
//        {
//            LOG.debug("short content: " + new String(bytes));
//            throw new IOException("Attempted to read " + numToRead +
//                    " but only found: " + read);
//        }

        return bytes;
    }
}
