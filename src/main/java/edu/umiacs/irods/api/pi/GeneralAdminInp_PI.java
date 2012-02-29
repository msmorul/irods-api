/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 * #define generalAdminInp_PI "str *arg0; str *arg1; 
 * str *arg2; str *arg3; str *arg4; str *arg5; str *arg6; 
 * str *arg7;  str *arg8;  str *arg9;"
 * @author toaster
 */
public class GeneralAdminInp_PI implements IRodsPI
{

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
    private byte[] bytes;

    public GeneralAdminInp_PI(String arg0, String arg1, String arg2,
            String arg3, String arg4, String arg5, String arg6, String arg7,
            String arg8, String arg9)
    {
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.arg5 = arg5;
        this.arg6 = arg6;
        this.arg7 = arg7;
        this.arg8 = arg8;
        this.arg9 = arg9;
    }

    @Override
    public String toString()
    {

        StringBuffer sb = new StringBuffer("<generalAdminInp_PI><arg0>");

        sb.append(arg0);
        sb.append("</arg0><arg1>");
        sb.append(arg1);
        sb.append("</arg1><arg2>");
        sb.append(arg2);
        sb.append("</arg2><arg3>");
        sb.append(arg3);
        sb.append("</arg3><arg4>");
        sb.append(arg4);
        sb.append("</arg4><arg5>");
        sb.append(arg5);
        sb.append("</arg5><arg6>");
        sb.append(arg6);
        sb.append("</arg6><arg7>");
        sb.append(arg7);
        sb.append("</arg7><arg8>");
        sb.append(arg8);
        sb.append("</arg8><arg9>");
        sb.append(arg9);
        sb.append("</arg9></generalAdminInp_PI>");
        return sb.toString();
    }

    public byte[] getBytes()
    {
        if ( bytes == null )
        {
            bytes = toString().getBytes();
        }
        return bytes;
    }

    public String getArg0()
    {
        return arg0;
    }

    public String getArg1()
    {
        return arg1;
    }

    public String getArg2()
    {
        return arg2;
    }

    public String getArg3()
    {
        return arg3;
    }

    public String getArg4()
    {
        return arg4;
    }

    public String getArg5()
    {
        return arg5;
    }

    public String getArg6()
    {
        return arg6;
    }

    public String getArg7()
    {
        return arg7;
    }

    public String getArg8()
    {
        return arg8;
    }

    public String getArg9()
    {
        return arg9;
    }
}
