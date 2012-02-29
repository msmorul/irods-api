/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

/**
 *#define DataObjInfo_PI "str objPath[MAX_NAME_LEN]; str rescName[NAME_LEN]; 
 * str rescGroupName[NAME_LEN]; str dataType[NAME_LEN]; double dataSize; 
 * str chksum[NAME_LEN]; str version[NAME_LEN]; str filePath[MAX_NAME_LEN]; 
 * str *rescInfo; str dataOwnerName[NAME_LEN]; str dataOwnerZone[NAME_LEN]; 
 * int  replNum; int  replStatus; str statusString[NAME_LEN]; double  dataId; 
 * double collId; int  dataMapId; str dataComments[LONG_NAME_LEN]; 
 * str dataExpiry[TIME_LEN]; str dataCreate[TIME_LEN]; str dataModify[TIME_LEN]; 
 * str dataAccess[NAME_LEN]; int  dataAccessInx; str destRescName[NAME_LEN]; 
 * str backupRescName[NAME_LEN]; str subPath[MAX_NAME_LEN]; int *specColl; int *next;"
 * @author toaster
 */
public class DataObjInfo_PI implements IRodsPI
{

    private String objPath;
    private String rescName;
    private String rescGroupName;
    private String dataType;
    private double dataSize;
    private String chksum;
    private String version;
    private String filePath;
    private int replNum;
    private int replStatus;
    private String statusString;
    private double dataId;
    private double collId;
    private int dataMapId;
    private String dataComments;
    private String dataExpiry; // date?
    private String dataCreate; //date?
    private String dataModify; //date?
    private String dataAccess;
    private int dataAccessInx;
    private String destRescName;
    private String backupRescName;
    private String subPath;
// ?? private int specColl;
    // ?? private int next;
    public DataObjInfo_PI(String objPath, String rescName, String rescGroupName,
            String dataType, double dataSize, String chksum, String version,
            String filePath, int replNum, int replStatus, String statusString,
            double dataId, double collId, int dataMapId, String dataComments,
            String dataExpiry, String dataCreate, String dataModify, String dataAccess,
            int dataAccessInx, String destRescName, String backupRescName, String subPath)
    {
        this.objPath = objPath;
        this.rescName = rescName;
        this.rescGroupName = rescGroupName;
        this.dataType = dataType;
        this.dataSize = dataSize;
        this.chksum = chksum;
        this.version = version;
        this.filePath = filePath;
        this.replNum = replNum;
        this.replStatus = replStatus;
        this.statusString = statusString;
        this.dataId = dataId;
        this.collId = collId;
        this.dataMapId = dataMapId;
        this.dataComments = dataComments;
        this.dataExpiry = dataExpiry;
        this.dataCreate = dataCreate;
        this.dataModify = dataModify;
        this.dataAccess = dataAccess;
        this.dataAccessInx = dataAccessInx;
        this.destRescName = destRescName;
        this.backupRescName = backupRescName;
        this.subPath = subPath;
    }

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
