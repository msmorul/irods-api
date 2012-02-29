/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.List;

/**
 * #define CollOprStat_PI "int filesCnt; int totalFileCnt; double bytesWritten; str lastObjPath[MAX_NAME_LEN];"

 * @author toaster
 */
public class CollOprStat_PI implements IRodsPI {

    private int filesCnt;
    private int totalFileCnt;
    private long bytesWritten;
    private String lastObjPath;
    private byte[] bytes;

    public CollOprStat_PI(int filesCnt, int totalFileCnt, long bytesWritten, String lastObjPath, byte[] bytes) {
        this.filesCnt = filesCnt;
        this.totalFileCnt = totalFileCnt;
        this.bytesWritten = bytesWritten;
        this.lastObjPath = lastObjPath;
    }

    public CollOprStat_PI(ProtocolToken pt)  throws ParseException {
        pt.checkName("CollOprStat_PI");
        List<ProtocolToken> tokens = ProtocolToken.parseTokens(pt.getValue());

        tokens.get(0).checkName("filesCnt");
        filesCnt = tokens.get(0).getIntValue();

        tokens.get(1).checkName("totalFileCnt");
        totalFileCnt = tokens.get(1).getIntValue();

        tokens.get(2).checkName("bytesWritten");
        bytesWritten = tokens.get(2).getLongValue();

        tokens.get(3).checkName("lastObjPath");
        lastObjPath = tokens.get(3).getValue();

    }

    public byte[] getBytes() {
        if (bytes == null) {
            bytes = toString().getBytes();
        }
        return bytes;
    }

    public int getTotalFileCnt() {
        return totalFileCnt;
    }

    public String getLastObjPath() {
        return lastObjPath;
    }

    public int getFilesCnt() {
        return filesCnt;
    }

    public long getBytesWritten() {
        return bytesWritten;
    }

    @Override
    public String toString() {
        return "<CollOprStat_PI><filesCnt>" + filesCnt + "</filesCnt>"
                + "<totalFileCnt>" + totalFileCnt + "</totalFileCnt>"
                + "<bytesWritten>" + bytesWritten + "</bytesWritten>"
                + "<lastObjPath>" + lastObjPath + "</lastObjPath></CollOprStat_PI>";
    }
}
