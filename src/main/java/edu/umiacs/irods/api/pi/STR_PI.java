/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * #define STR_PTR_PI "str *myStr;" 
 * @author toaster
 */
public class STR_PI implements IRodsPI {

    private String myStr;
    private byte[] bytes;

    public STR_PI(String myStr) {
        this.myStr = myStr;
    }

    public STR_PI(ProtocolToken pt) throws ParseException {
        pt.checkName("STR_PI");
        pt.getTokenListValue().get(0).checkName("myStr");
        myStr = pt.getTokenListValue().get(0).getValue();

    }

    public byte[] getBytes() {
        if (bytes == null) {
            bytes = toString().getBytes();
        }
        return bytes;
    }

    public String getMyStr() {
        return myStr;
    }

    @Override
    public String toString() {
        return "<STR_PI><myStr>" + myStr + "</myStr></STR_PI>";
    }
}
