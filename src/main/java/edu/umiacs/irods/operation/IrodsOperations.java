/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.*;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.CollInpNew_PI;
import edu.umiacs.irods.api.pi.CollInp_PI;
import edu.umiacs.irods.api.pi.DataObjInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.api.pi.KeyValPair_PI;
import edu.umiacs.irods.api.pi.MiscSvrInfo_PI;
import edu.umiacs.irods.api.pi.OprTypeEnum;
import edu.umiacs.irods.api.pi.RodsObjStat_PI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Simple irods operations 
 * 
 * @author toaster
 */
public class IrodsOperations {

    private static final Logger LOG = Logger.getLogger(IrodsOperations.class);
    private ConnectOperation connection;

    public IrodsOperations(ConnectOperation connection) {
        if (connection == null) {
            throw new NullPointerException("Connection null");
        }
        this.connection = connection;
    }

//    public String getDigest(String path) throws IRodsRequestException {
//        try {
//
//            DataObjInp_PI inPi =
//                    new DataObjInp_PI(path, 0, 0, 0, 0, 0,
//                    OprTypeEnum.NO_OPR_TYPE, new KeyValPair_PI((Map<String,String>) null));
//
//
//            IrodsApiRequest apiReq =
//                    new IrodsApiRequest(ApiNumberEnum.OBJ_STAT_AN, inPi, null);
//
//            int status = apiReq.sendRequest(connection.getConnection());
//
//            if (status
//                    == ErrorEnum.USER_FILE_DOES_NOT_EXIST.getInt()) {
//                return null;
//            }
//            if (status < 0) {
//                throw new IRodsRequestException(ErrorEnum.valueOf(status));
//            }
//
//            return apiReq.getResultPI(STR_PI.class).getMyStr();
//        } catch (IOException ex) {
//            silentReconnect();
//            if (ex instanceof IRodsRequestException) {
//                throw (IRodsRequestException) ex;
//            }
//            throw new IRodsRequestException(
//                    "Communication error sending request", ex);
//        }
//
//    }

    public void rmdir(String path, boolean recurse) throws IRodsRequestException {
        try {
            IrodsApiRequest apiReq;

            Map map = null;
            if (recurse) {
                map = new HashMap<String, String>();
                map.put("recursiveOpr", "");

            }

            CollInpNew_PI body = new CollInpNew_PI(path, 0, 0, new KeyValPair_PI(map));
            apiReq = new IrodsApiRequest(ApiNumberEnum.RM_COLL_AN, body, null);

            int status = apiReq.sendRequest(connection.getConnection());

            if (status < 0) {
                throw new IRodsRequestException(ErrorEnum.valueOf(status));
            }

        } catch (IOException ex) {
            silentReconnect();
            if (ex instanceof IRodsRequestException) {
                throw (IRodsRequestException) ex;
            }
            throw new IRodsRequestException(
                    "Communication error sending request", ex);
        }
    }

    public void rm(String file) throws IRodsRequestException {
        //DATA_OBJ_UNLINK_AN
        try {
            IrodsApiRequest apiReq;
            DataObjInp_PI body = new DataObjInp_PI(file, 0, 0, 0, 0, 0, OprTypeEnum.NO_OPR_TYPE, new KeyValPair_PI((Map) null));
            apiReq =
                    new IrodsApiRequest(ApiNumberEnum.DATA_OBJ_UNLINK_AN, body, null);

            int status = apiReq.sendRequest(connection.getConnection());

            if (status < 0) {
                throw new IRodsRequestException(ErrorEnum.valueOf(status));
            }

        } catch (IOException ex) {
            silentReconnect();
            if (ex instanceof IRodsRequestException) {
                throw (IRodsRequestException) ex;
            }
            throw new IRodsRequestException(
                    "Communication error sending request", ex);
        }
    }
    public void reg(String src, String dest, String resource) throws IRodsRequestException{
        try {
                Map<String, String> map = new HashMap<String, String>();
                map.put("dataType", "generic");
                map.put("destRescName", resource);
                map.put("filePath", src);
                DataObjInp_PI obj2 = new DataObjInp_PI(dest, 0, 0, 0, 0, 0, OprTypeEnum.NO_OPR_TYPE, new KeyValPair_PI(map));

                IrodsApiRequest apiReq = new IrodsApiRequest(ApiNumberEnum.PHY_PATH_REG_AN, obj2, null);
                int result = apiReq.sendRequest(connection.getConnection());
                LOG.debug("Result code: " + result);
                if (result < 0) {
                    LOG.debug("Error: " + ErrorEnum.valueOf(result));
                    System.out.println("Resource: "+resource+" Error: "+ErrorEnum.valueOf(result));
                }
            

        } catch (IOException ex) {
            silentReconnect();
            if (ex instanceof IRodsRequestException) {
                throw (IRodsRequestException) ex;
            }
            throw new IRodsRequestException(
                    "Communication error sending request", ex);
        }
    }

    public void mkdir(String path) throws IRodsRequestException {
        try {
            IrodsApiRequest apiReq;

            if (connection.getVersion().is201Compat()) {
                CollInp_PI body =
                        new CollInp_PI(path, new KeyValPair_PI((Map) null));


                apiReq =
                        new IrodsApiRequest(ApiNumberEnum.COLL_CREATE201_AN, body, null);
            } else {
                CollInpNew_PI body = new CollInpNew_PI(path, 0, 0, new KeyValPair_PI((Map) null));
                apiReq = new IrodsApiRequest(ApiNumberEnum.COLL_CREATE_AN, body, null);
            }

            int status = apiReq.sendRequest(connection.getConnection());

            if (status < 0 && status != ErrorEnum.CATALOG_ALREADY_HAS_ITEM_BY_THAT_NAME.getInt()) {
                throw new IRodsRequestException(ErrorEnum.valueOf(status));
            }

        } catch (IOException ex) {
            silentReconnect();
            if (ex instanceof IRodsRequestException) {
                throw (IRodsRequestException) ex;
            }
            throw new IRodsRequestException(
                    "Communication error sending request", ex);
        }
    }

    /**
     * stat a path in irods
     * @param path path to check
     * @return result of stat if file exists, null for nonexistent files
     * 
     * @throws IRodsRequestException if server returns status < 0
     */
    public RodsObjStat_PI stat(String path) throws IRodsRequestException {
        try {

            DataObjInp_PI inPi =
                    new DataObjInp_PI(path, 0, 0, 0, 0, 0,
                    OprTypeEnum.NO_OPR_TYPE, new KeyValPair_PI((Map) null));


            IrodsApiRequest apiReq =
                    new IrodsApiRequest(ApiNumberEnum.OBJ_STAT_AN, inPi, null);

            int status = apiReq.sendRequest(connection.getConnection());

            if (status
                    == ErrorEnum.USER_FILE_DOES_NOT_EXIST.getInt()) {
                return null;
            }
            if (status < 0) {
                throw new IRodsRequestException(ErrorEnum.valueOf(status));
            }

            return apiReq.getResultPI(RodsObjStat_PI.class);
        } catch (IOException ex) {
            silentReconnect();
            if (ex instanceof IRodsRequestException) {
                throw (IRodsRequestException) ex;
            }
            throw new IRodsRequestException(
                    "Communication error sending request", ex);
        }

    }

    public MiscSvrInfo_PI getServerInfo() throws IRodsRequestException {
        try {
            IrodsApiRequest apiReq =
                    new IrodsApiRequest(ApiNumberEnum.GET_MISC_SVR_INFO_AN, null,
                    null);

            int status = apiReq.sendRequest(connection.getConnection());

            if (status < 0) {
                throw new IRodsRequestException(ErrorEnum.valueOf(status));
            }

            return apiReq.getResultPI(MiscSvrInfo_PI.class);
        } catch (IOException ex) {
            silentReconnect();
            if (ex instanceof IRodsRequestException) {
                throw (IRodsRequestException) ex;
            }
            throw new IRodsRequestException(
                    "Communication error sending request", ex);
        }

    }

    public String[] listOneField(GenQueryEnum field) throws IRodsRequestException {
        try {
            List<String> l = new ArrayList<String>();
            QueryBuilder qb = new QueryBuilder(field);
            QueryResult qr = qb.execute(connection.getConnection());
            while (qr.next()) {
                l.add(qr.getValue(field));
            }
            return l.toArray(new String[0]);
        } catch (IOException ex) {
            silentReconnect();
            throw new IRodsRequestException("Error communicating with irods", ex);
        }
    }

//    public String[] getResourceList() throws IRodsRequestException
//    {
//        
//
//    }
//    public String[] listTokenTypes() throws IRodsRequestException
//    {
//        try
//        {
//            List<String> l = new ArrayList<String>();
//            QueryBuilder qb =
//                    new QueryBuilder(GenQueryEnum.COL_TOKEN_NAMESPACE);
//            QueryResult qr = qb.execute(connection.getConnection());
//            while ( qr.next() )
//            {
//                l.add(qr.getValue(GenQueryEnum.COL_TOKEN_NAMESPACE));
//            }
//            return l.toArray(new String[0]);
//        }
//        catch ( IOException ex )
//        {
//            silentReconnect();
//            throw new IRodsRequestException("Error communicating with irods", ex);
//        }
//    }
    private void silentReconnect() {
        try {
            connection.reconnect();
        } catch (IOException ex) {
            LOG.error("Error reconnecting", ex);
        }
    }
}
