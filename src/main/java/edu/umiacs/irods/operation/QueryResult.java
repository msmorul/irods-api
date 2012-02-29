/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.api.pi.GenQueryInp_PI;
import edu.umiacs.irods.api.pi.GenQueryOut_PI;
import edu.umiacs.irods.api.pi.SqlResult_PI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * A resultset-like interface for the returned irods queries. It assumes that
 * the source outputQuery has no duplicate columns
 * 
 *  TODO: result continuation
 * 
 * @author toaster
 */
public class QueryResult {

    private static final Logger LOG = Logger.getLogger(QueryResult.class);
    private IRodsConnection connection;
    private GenQueryEnum[] columnOrder;
    private List<Map<GenQueryEnum, String>> rowValues =
            new ArrayList<Map<GenQueryEnum, String>>();
    private int continueIndex = 0;
    private int pointer = -1;
    private GenQueryInp_PI query; // original query
    private int maxReturned = 0;
    private int numReturned = 0;

    QueryResult(GenQueryInp_PI query, IRodsConnection connection) throws IRodsRequestException {

        this.connection = connection;
        this.query = query;
        runQuery(query);

    }

    private GenQueryInp_PI createContinueQuery() {
        return new GenQueryInp_PI(query.getMaxRows(), 1,
                query.getPartialStartIndex(),
                query.getOptions(), query.getKeyValPair_PI(),
                query.getInxIValPair_PI(),
                query.getInxValPair_PI());
    }

    private void runQuery(GenQueryInp_PI query) throws IRodsRequestException {
        try {
            IrodsApiRequest apiRequest =
                    new IrodsApiRequest(ApiNumberEnum.GEN_QUERY_AN, query, null);


            int status = apiRequest.sendRequest(connection);
            LOG.debug("Query status: " + status);

            // if there are no rows found, an error code is returned. Since this
            // is expected, handle here rather than passing upward
            if (status == ErrorEnum.CAT_NO_ROWS_FOUND.getInt()) {
                return;
            } else if (status < 0) {
                throw new IRodsRequestException(ErrorEnum.valueOf(status));
            } else {
                loadQuery(apiRequest.getResultPI(GenQueryOut_PI.class));
            }

        } catch (IOException ex) {
            if (ex instanceof IRodsRequestException) {
                throw (IRodsRequestException) ex;
            }
            throw new IRodsRequestException("Communication error sending request",
                    ex);
        }

    }

    private void loadQuery(GenQueryOut_PI outputQuery) {
        boolean fillColumnDescriptions = false;

        if (columnOrder == null) {
            columnOrder = new GenQueryEnum[outputQuery.getAttriCnt()];
            fillColumnDescriptions = true;
        }

        continueIndex = outputQuery.getContinueInx();

        int rowCount = outputQuery.getRowCnt();
        int prefix = rowValues.size();
        // Add new rows to column cache
        for (int i = 0; i < rowCount; i++) {
            rowValues.add(new HashMap<GenQueryEnum, String>());
        }


        // outputQuery is grouped by column, so we iterate over each column
        // filling in the appropriate row..
        // attriCnt determines how many columns
        for (int i = 0; i < outputQuery.getAttriCnt(); i++) {
            SqlResult_PI column = outputQuery.getSqlResult_PI().get(i);
            GenQueryEnum columnType = column.getAttriInx();

            // if this is the first load, we need to load the column descriptions
            // as well
            if (fillColumnDescriptions) {
                columnOrder[i] = columnType;
            }


            // iterate over each column value, assigning to appropriate row.
            // row is prefix + j
            for (int j = 0; j < rowCount; j++) {
                String cellValue = column.getValues().get(j);
                rowValues.get(prefix + j).put(columnType, cellValue);
            }
        }
    }

    /*********************************
     * Column information
     */
    /**
     * get number of columns
     * 
     * @return Number of columns
     */
    public int getColumnCount() {
        return columnOrder.length;
    }

    /**
     * Return column types in order
     * 
     * @return column types
     */
    public List<GenQueryEnum> getColumDescriptions() {
        return Arrays.asList(columnOrder);
    }

    /*********************************
     * Cursor navigation
     */
    /**
     * Are there any more 
     * @return
     */
    public boolean hasNext() {
        if (maxReturned > 0 && numReturned >= maxReturned)
            return false;
        
        if (continueIndex == 1 && pointer == (rowValues.size() - 1)) {
            try {
                runQuery(createContinueQuery());
            } catch (IRodsRequestException ire) {
                throw new RuntimeException(ire);
            }
        }


        if (pointer == (rowValues.size() - 1)) {
            return false;
        } else {
            return true;
        }

    }

    public int getNumReturned() {
        return numReturned;
    }

    public int getMaxReturned() {
        return maxReturned;
    }

    public void setMaxReturned(int maxReturned) {
        this.maxReturned = maxReturned;
    }

    public void resetReturnCount()
    {
        numReturned = 0;
    }

    /**
     * Move curser to next item in this result set. If more results are needed
     * from the irods server, it will be queried.
     * 
     * @return true if the new current row is valid; false if there are no more rows
     */
    public boolean next() {

        if (hasNext()) {
            numReturned++;
            pointer++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Move curser to first item in list
     * 
     * @return true if pointer is at first, false if there are no results
     */
    public boolean first() {
        if (rowValues.size() > 0) {
            pointer = 0;
            return true;
        } else {
            pointer = -1;
            return false;
        }
    }

    /*********************************
     * Column retrieval
     */
    /**
     * Return 
     * @param column
     * @return
     */
//    public int getInt(int column)
//    {
//
//    }
    public String getValue(GenQueryEnum column) {
        checkPointer();
        return rowValues.get(pointer).get(column);
    }

    public int getIntValue(GenQueryEnum column) {
        checkPointer();
        return Integer.parseInt(rowValues.get(pointer).get(column));
    }

    public long getLongValue(GenQueryEnum column) {
        return Long.parseLong(rowValues.get(pointer).get(column));
    }

    public Date getDateValue(GenQueryEnum column) {
        return new Date(getLongValue(column) * 1000);
    }

    private void checkPointer() {
        if (pointer < 0) {
            throw new RuntimeException("Pointer before first element, call next()");
        }
    }
}
