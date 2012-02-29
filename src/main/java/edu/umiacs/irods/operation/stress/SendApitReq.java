/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation.stress;

import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.ResultMessage;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.DataObjInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.HeaderTypeEnum;
import edu.umiacs.irods.api.pi.IRodsPI;
import edu.umiacs.irods.api.pi.KeyValPair_PI;
import edu.umiacs.irods.api.pi.OprTypeEnum;
import edu.umiacs.irods.operation.ConnectOperation;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author toaster
 */
public class SendApitReq {

    public static void main(String[] args) throws IOException {
//        ApiNumberEnum apiNumber = ApiNumberEnum.DATA_OBJ_CHKSUM_AN;
                ApiNumberEnum apiNumber = ApiNumberEnum.OBJ_STAT_AN;

        IRodsPI body = new DataObjInp_PI("/chron-umiacs/home/duracloud/newspace3/northcap.jpg", 0, 0, 0, 0, 0, OprTypeEnum.NO_OPR_TYPE, new KeyValPair_PI(new HashMap<String, String>()));



        ConnectOperation co = new ConnectOperation("chron-mcat.umiacs.umd.edu", 1247,
                "duracloud", "duracloud", "chron-umiacs");

        IRodsConnection conn = co.getConnection();

        ResultMessage rm = conn.sendMessage(HeaderTypeEnum.RODS_API_REQ, apiNumber, body);

        System.out.println("Received Header: " + rm.getHeader());
        int resp = rm.getHeader().getIntInfo();
        try {

            if (rm.getHeader().getIntInfo() < 0) {
                System.out.println("Error response: " + ErrorEnum.valueOf(resp));
            }
            ApiNumberEnum reNum = ApiNumberEnum.valueOf(resp);
            System.out.println("MessageType " + reNum);
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown message response: " + resp);
        }

        System.out.println("Got Body: " + rm.getBodyToken());

    }
}
