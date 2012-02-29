/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.operation.stress;

import edu.umiacs.irods.operation.ConnectOperation;
import edu.umiacs.irods.operation.IrodsProxyInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author toaster
 */
public class InputStreamTest {

    public static final void main(String[] args) throws IOException, NoSuchAlgorithmException
    {
        ConnectOperation co = new ConnectOperation("chron-mcat.umiacs.umd.edu", 1247,
                "rods", "lizard51775", "chron-umiacs");

        String file = "/chron-umiacs/home/rods/jj/file-1";///chron-umiacs/home/duracloud/newspace3/northcap.jpg";

        InputStream iis = new IrodsProxyInputStream(file, co.getConnection());
        MessageDigest digest = MessageDigest.getInstance("MD5");
        DigestInputStream dis = new DigestInputStream(iis, digest);
        byte[] b = new byte[1040000];
        int read = 0;
        int i = 0;
        long total = 0;
//        OutputStream os = new FileOutputStream(new File("/tmp/ncap.jpg"));
        long start = System.currentTimeMillis();
        while ( (read = iis.read(b)) != -1)
        {
//            os.write(b, 0, read);
            digest.update(b,0,read);
            total += read;
//            System.out.println(i+" Read blocks " + read + " " + total);
            i++;
        }
         System.out.println(" Read blocks " + i + " " + total + " time "+ (System.currentTimeMillis()-start));
//        os.close();
        
        iis.close();
//        digest.digest();
       System.out.println(asHexString(digest.digest()));
    }

     public static String asHexString( byte[] value ) {
        String str = new BigInteger(1, value).toString(16);
        if ( str.length() < value.length * 2 ) {
            str =repeat('0', value.length * 2 - str.length()) + str;
        }
        return str;
    }
      public static String repeat(char value, int times)
    {

        if ( times == 0 )
            return "";
        char[] c = new char[times];
        for ( int i = 0; i < times; i++ )
        {
            c[i] = value;
        }
        return new String(c);
    }
}
