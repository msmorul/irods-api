/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 *
 * @author toaster
 */
public enum VersionEnum
{

    RODS0_9("rods0.9", "b"),
    RODS1_0("rods1.0", "b"),
    RODS1_1("rods1.1", "d"),
    RODS2_0("rods2.0", "d"),
    RODS2_0_1("rods2.0.1", "d"),
    RODS2_1("rods2.1", "d"),
    RODS2_2("rods2.2", "d"),
    RODS2_3("rods2.3", "d"),
    RODS2_4("rods2.4", "d"),
    RODS2_4_1("rods2.4.1", "d"),
    RODS2_5("rods2.5", "d");
    public static final String API_B = "b";
    public static final String API_D = "d";
    public static final String REL_0_9 = "rods0.9";
    public static final String REL_1_0 = "rods1.0";
    public static final String REL_1_1 = "rods1.1";
    public static final String REL_2_0 = "rods2.0";
    public static final String REL_2_0_1 = "rods2.0.1";
    public static final String REL_2_1 = "rods2.1";
    public static final String REL_2_2 = "rods2.2";
    public static final String REL_2_3 = "rods2.3";
    public static final String REL_2_4 = "rods2.4";
    public static final String REL_2_4_1 = "rods2.4.1";
    public static final String REL_2_5 = "rods2.5";
    private String relVersion;
    private String apiVersion;

    private VersionEnum(String relVersion, String apiVersion)
    {
        this.relVersion = relVersion;
        this.apiVersion = apiVersion;
    }

    public static VersionEnum valueOf(Version_PI body)
    {
        if (API_B.equals(body.getApiVersion()))
        {
            if (REL_1_0.equals(body.getRelVersion()))
            {
                return RODS1_0;
            } else if (REL_0_9.equals(body.getRelVersion()))
            {
                return RODS0_9;
            }
        } else if (API_D.equals(body.getApiVersion()))
        {
            if (REL_1_1.equals(body.getRelVersion()))
            {
                return RODS1_1;
            }
            if (REL_2_0.equals(body.getRelVersion()))
            {
                return RODS2_0;
            }
            if (REL_2_0_1.equals(body.getRelVersion()))
            {
                return RODS2_0_1;
            }
            if (REL_2_1.equals(body.getRelVersion()))
            {
                return RODS2_1;
            }
            if (REL_2_2.equals(body.getRelVersion()))
            {
                return RODS2_2;
            }
            if (REL_2_3.equals(body.getRelVersion()))
            {
                return RODS2_3;
            }
            if (REL_2_4.equals(body.getRelVersion()))
            {
                return RODS2_4;
            }
            if (REL_2_4_1.equals(body.getRelVersion()))
            {
                return RODS2_4_1;
            }
            if (REL_2_5.equals(body.getRelVersion()))
            {
                return RODS2_5;
            }


        }
        return null;
    }

    public String getRelVersion()
    {
        return relVersion;
    }

    public String getApiVersion()
    {
        return apiVersion;
    }

    public boolean is201Compat()
    {
        return ( RODS0_9 == this || RODS2_0  == this || RODS1_1 == this || RODS1_0 == this );
    }
}
