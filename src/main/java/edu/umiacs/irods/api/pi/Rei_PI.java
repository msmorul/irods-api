/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 *#define Rei_PI "int status; str statusStr[MAX_NAME_LEN]; int *rsComm;
 * struct *MsParamArray_PI; struct MsParamArray_PI; int l1descInx; 
 * struct *DataObjInp_PI; struct *DataOprInp_PI; struct *fileOpenInp_PI; 
 * struct *DataObjInfo_PI; struct *RescGrpInfo_PI; struct *UserInfo_PI; 
 * struct *UserInfo_PI; struct *CollInfo_PI; struct *DataObjInp_PI; 
 * struct *DataOprInp_PI; struct *fileOpenInp_PI; struct *RescGrpInfo_PI; 
 * struct *UserInfo_PI; struct *KeyValPair_PI; str ruleSet[RULE_SET_DEF_LENGTH]; 
 * int *next;" 
 * @author toaster
 */
public class Rei_PI implements IRodsPI {

    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private int status;
    private String statusStr;
//    private int rsComm;
//    private MsParamArray_PI msParamArray_PI;
//    private 
    
}
