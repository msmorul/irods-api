/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 *#define CollInfo_PI "double collId; str collName[MAX_NAME_LEN]; 
 * str collParentName[MAX_NAME_LEN]; str collOwnerName[NAME_LEN]; 
 * str collOwnerZone[NAME_LEN]; int collMapId; str collComments[LONG_NAME_LEN]; 
 * str collInheritance[LONG_NAME_LEN]; str collExpiry[TIME_LEN]; 
 * str collCreate[TIME_LEN]; str collModify[TIME_LEN]; str collAccess[NAME_LEN]; 
 * int collAccessInx; str collType[NAME_LEN]; str collInfo1[MAX_NAME_LEN]; 
 * str collInfo2[MAX_NAME_LEN]; int *next;"
 * 
 * @author toaster
 */
public class CollInfo_PI implements IRodsPI {

    private double collId;
    private String collName;
    private String collParentName;
    private String collOwner;
    private String collOwnerZone;
    private int collMapId;
    private String collComments;
    private String collInheritance;
    private String collExpiry; //date?
    private String collCreate; //date?
    private String collmodify; //date?
    private String collAccess;
    private int collAccessInx;
    private String collType;
    private String collInfo1;
    private String collInfo2;
    private int next; //?

    public CollInfo_PI(double collId, String collName, String collParentName, String collOwner, String collOwnerZone, int collMapId, String collComments, String collInheritance, String collExpiry, String collCreate, String collmodify, String collAccess, int collAccessInx, String collType, String collInfo1, String collInfo2, int next)
    {
        this.collId = collId;
        this.collName = collName;
        this.collParentName = collParentName;
        this.collOwner = collOwner;
        this.collOwnerZone = collOwnerZone;
        this.collMapId = collMapId;
        this.collComments = collComments;
        this.collInheritance = collInheritance;
        this.collExpiry = collExpiry;
        this.collCreate = collCreate;
        this.collmodify = collmodify;
        this.collAccess = collAccess;
        this.collAccessInx = collAccessInx;
        this.collType = collType;
        this.collInfo1 = collInfo1;
        this.collInfo2 = collInfo2;
        this.next = next;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getCollId()
    {
        return collId;
    }

    public String getCollName()
    {
        return collName;
    }

    public String getCollParentName()
    {
        return collParentName;
    }

    public String getCollOwner()
    {
        return collOwner;
    }

    public String getCollOwnerZone()
    {
        return collOwnerZone;
    }

    public int getCollMapId()
    {
        return collMapId;
    }

    public String getCollComments()
    {
        return collComments;
    }

    public String getCollInheritance()
    {
        return collInheritance;
    }

    public String getCollExpiry()
    {
        return collExpiry;
    }

    public String getCollCreate()
    {
        return collCreate;
    }

    public String getCollmodify()
    {
        return collmodify;
    }

    public String getCollAccess()
    {
        return collAccess;
    }

    public int getCollAccessInx()
    {
        return collAccessInx;
    }

    public String getCollType()
    {
        return collType;
    }

    public String getCollInfo1()
    {
        return collInfo1;
    }

    public String getCollInfo2()
    {
        return collInfo2;
    }

    public int getNext()
    {
        return next;
    }
    
}
