/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation.stress;

import edu.umiacs.irods.operation.ConnectOperation;
import edu.umiacs.irods.operation.UnknownSizeOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author toaster
 */
public class OutputStreamTest {

    public static final void main(String[] args) throws IOException {
        ConnectOperation co = new ConnectOperation("chron-mcat.umiacs.umd.edu", 1247,
                "duracloud", "duracloud", "chron-umiacs");

        String file = "/chron-umiacs/home/duracloud/newspace3/northcap.jpg";
        String resource = "chron-rsrc4-small";

        OutputStream os = new UnknownSizeOutputStream(co.getConnection(), file, resource, true);

        InputStream is = new FileInputStream(new File("/tmp/ncap.jpg"));
        byte[] b = new byte[4096];
        int read = 0;

        while ( (read = is.read(b)) != -1)
        {
            os.write(b, 0, read);
        }

        os.close();
        is.close();
    }
}
