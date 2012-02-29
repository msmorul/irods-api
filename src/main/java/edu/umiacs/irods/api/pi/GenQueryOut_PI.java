/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

import java.util.ArrayList;
import java.util.List;

/**
 *#define GenQueryOut_PI "int rowCnt; int attriCnt; int continueInx; 
 * int totalRowCount; struct SqlResult_PI[MAX_SQL_ATTR];"
 * @author toaster
 */
public class GenQueryOut_PI implements IRodsPI {

    public static final int MAX_SQL_ATTR = 50;
    private int rowCnt;
    private int attriCnt;
    private int continueInx;
    private int totalRowCount;
    private List<SqlResult_PI> sqlResult_PI;

    public GenQueryOut_PI(int rowCnt, int attriCnt, int continueInx, int totalRowCount, List<SqlResult_PI> sqlResult_PI)
    {
        this.rowCnt = rowCnt;
        this.attriCnt = attriCnt;
        this.continueInx = continueInx;
        this.totalRowCount = totalRowCount;
        this.sqlResult_PI = sqlResult_PI;
    }
    
    public GenQueryOut_PI(ProtocolToken pt) throws ParseException
    {
        pt.checkName("GenQueryOut_PI");
        List<ProtocolToken> tokens = pt.getTokenListValue();
        
        tokens.get(0).checkName("rowCnt");
        rowCnt = tokens.get(0).getIntValue();
        
        tokens.get(1).checkName("attriCnt");
        attriCnt = tokens.get(1).getIntValue();
        
        tokens.get(2).checkName("continueInx");
        continueInx = tokens.get(2).getIntValue();
        
        tokens.get(3).checkName("totalRowCount");
        totalRowCount = tokens.get(3).getIntValue();
        
        sqlResult_PI = new ArrayList<SqlResult_PI>(rowCnt);
        
        for ( int i = 4; i < (4 + attriCnt) ; i++)
        {
//            ProtocolToken sqlres = tokens.get(i).getTokenValue();
//            System.out.println("token name: " + sqlres.getName());
            sqlResult_PI.add(new SqlResult_PI(tokens.get(i), attriCnt));
        }
        //ProtocolToken ptSql = ProtocolToken
    }
    
    public byte[] getBytes()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getRowCnt()
    {
        return rowCnt;
    }

    public int getAttriCnt()
    {
        return attriCnt;
    }

    public int getContinueInx()
    {
        return continueInx;
    }

    public int getTotalRowCount()
    {
        return totalRowCount;
    }

    public List<SqlResult_PI> getSqlResult_PI()
    {
        return new ArrayList<SqlResult_PI>(sqlResult_PI);
    }

}
