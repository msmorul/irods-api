/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.api.pi.GenQueryInp_PI;
import edu.umiacs.irods.api.pi.InxIvalPair_PI;
import edu.umiacs.irods.api.pi.InxValPair_PI;
import java.util.HashMap;
import java.util.Map;

/**
 * Class structured on the hibernate criterion filtering. This class may be 
 * reused multiple times. 
 * 
 * @author toaster
 */
public class QueryBuilder {

    private GenQueryEnum[] selects;
    Map<Integer, String> queries = new HashMap<Integer, String>();

    /**
     * Create a new Query builder
     * 
     * @param selects list of data types to be returned by this query
     */
    public QueryBuilder(GenQueryEnum... selects) {
        this.selects = selects;
    }
    /**
     * Execute this query.
     * 
     * @param ic connection to run this query against.
     * @return query restults
     * 
     * @throws edu.umiacs.irods.api.IRodsRequestException
     */
    public QueryResult execute(IRodsConnection ic) throws IRodsRequestException {
        GenQueryInp_PI body = null;

        InxValPair_PI Iquery;
        InxIvalPair_PI Iselects;

        // create query
        Iquery = new InxValPair_PI(queries);

        // create selected
        Map<Integer, Integer> inxIMap = new HashMap<Integer, Integer>();
        for (GenQueryEnum e : selects) {
            inxIMap.put(e.getInt(), 1);
        }
        Iselects = new InxIvalPair_PI(inxIMap);

        // create message pack instruction
        body = new GenQueryInp_PI(500, 0, 0, 0, null, Iselects, Iquery);

        return new QueryResult(body, ic);

    }

    /********
     * Add new equals criteria to query. 
     * 
     * @param col attribute to test
     * @param value value to compare to
     * @return this query builder
     */
    public QueryBuilder eq(GenQueryEnum col, String value) {
        String v = checkString(value);
        queries.put(col.getInt(), " = '" + v + "'");
        return this;
    }

    public QueryBuilder gt(GenQueryEnum col, String value) {
        long i = checkLong(value);
        queries.put(col.getInt(), " > '" + i + "'");
        return this;
    }

    public QueryBuilder lt(GenQueryEnum col, String value) {
        long i = checkLong(value);
        queries.put(col.getInt(), " < '" + i + "'");
        return this;
    }

    public QueryBuilder like(GenQueryEnum col, String value) {
        String i = checkString(value);
        queries.put(col.getInt(), " like '" + i + "'");
        return this;
    }

    /**
     * Compare the given column to multiple conditions. All conditions are OR'd together
     * 
     */
    public QueryBuilder mCmp(GenQueryEnum col, Condition... values) {
        StringBuilder cmpString = new StringBuilder();
        if (values == null || values.length == 0) {
            return this;
        }

        cmpString.append(values[0].operation.getOperator());
        cmpString.append("'");
        cmpString.append(values[0].getValue());
        cmpString.append("'");
        for (int i = 1; i < values.length; i++) {
            cmpString.append("||");
            cmpString.append(values[i].operation.getOperator());
            cmpString.append("'");
            cmpString.append(values[i].getValue());
            cmpString.append("'");
        }
        queries.put(col.getInt(), cmpString.toString());
        return this;
    }
//    public QueryBuilder ne(GenQueryEnum col, String value)
//    {
//        String v = checkString(value);
//        queries.put(col.getInt(), " != '" + v + "'");
//        return this;
//    }

    /**
     * Filter characters in a string
     * 
     * @param value
     * @return
     */
    private static String checkString(String value) {
        return value;
    }

    private long checkLong(String value) {
        return Long.parseLong(value);
    }

    public static enum ConditionType {

        LIKE(" like "),
        EQ(" = "),
        LT(" &lt; "),
        GT(" &gt; ");
        private String operator;

        private ConditionType(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }
    }

    public static class Condition {

        private ConditionType operation;
        private String value;

        public Condition(ConditionType operation, String value) {
            this.operation = operation;
            this.value = checkString(value);
        }

        public ConditionType getOperation() {
            return operation;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT: [");
        for (GenQueryEnum en : selects) {
            sb.append(en);
            sb.append("(");
            sb.append(en.getInt());
            sb.append(") ");
        }
        sb.append("] QUERY: ");
        for (Map.Entry<Integer, String> value : queries.entrySet()) {
            sb.append(GenQueryEnum.valueOf(value.getKey()));
            sb.append(" \"");
            sb.append(value.getValue());
            sb.append("\"");
        }
        return sb.toString();
    }
}
