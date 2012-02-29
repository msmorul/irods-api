/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * #define ModAVUMetadataInp_PI "str *arg0; str *arg1; str *arg2; str *arg3;
 * str *arg4; str *arg5; str *arg6; str *arg7;  str *arg8;  str *arg9;"
 *
 * @author toaster
 */
public class ModAVUMetadataInp_PI implements IRodsPI {

    private byte[] bytes;
    private String arg0;
    private String arg1;
    private String arg2;
    private String arg3;
    private String arg4;
    private String arg5;
    private String arg6;
    private String arg7;
    private String arg8;
    private String arg9;

    public ModAVUMetadataInp_PI(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9) {
        this.arg0 = (arg0 == null ? "":arg0);
        this.arg1 = (arg1 == null ? "":arg1);
        this.arg2 = (arg2 == null ? "":arg2);
        this.arg3 = (arg3 == null ? "":arg3);
        this.arg4 = (arg4 == null ? "":arg4);
        this.arg5 = (arg5 == null ? "":arg5);
        this.arg6 = (arg6 == null ? "":arg6);
        this.arg7 = (arg7 == null ? "":arg7);
        this.arg8 = (arg8 == null ? "":arg8);
        this.arg9 = (arg9 == null ? "":arg9);
    }

    @Override
    public String toString() {
        return "<ModAVUMetadataInp_PI><arg0>"+arg0+"</arg0><arg1>"+arg1+"</arg1>" +
                "<arg2>"+arg2+"</arg2><arg3>"+arg3+"</arg3><arg4>"+arg4+"</arg4>" +
                "<arg5>"+arg5+"</arg5><arg6>"+arg6+"</arg6><arg7>"+arg7+"</arg7>" +
                "<arg8>"+arg8+"</arg8><arg9>"+arg9+"</arg9></ModAVUMetadataInp_PI>";
    }

    public byte[] getBytes() {
        if (bytes == null)
            bytes = toString().getBytes();
        return bytes;
    }

    public String getArg0() {
        return arg0;
    }
    /**
     * @return the arg1
     */
    public String getArg1() {
        return arg1;
    }

    /**
     * @return the arg2
     */
    public String getArg2() {
        return arg2;
    }

    /**
     * @return the arg3
     */
    public String getArg3() {
        return arg3;
    }

    /**
     * @return the arg4
     */
    public String getArg4() {
        return arg4;
    }

    /**
     * @return the arg5
     */
    public String getArg5() {
        return arg5;
    }

    /**
     * @return the arg6
     */
    public String getArg6() {
        return arg6;
    }

    /**
     * @return the arg7
     */
    public String getArg7() {
        return arg7;
    }

    /**
     * @return the arg8
     */
    public String getArg8() {
        return arg8;
    }

    /**
     * @return the arg9
     */
    public String getArg9() {
        return arg9;
    }
}
