/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.api.pi;

import java.util.Map;

/**
 *  lib/core/include/rodsGenQuery.h
 * 
 * typedef struct GenQueryInp {
 *  int maxRows;                max number of rows to return, if 0 
 *                              close out the SQL statement call (i.e. instead
 *                              of getting more rows until it is finished). 
 *  int continueInx;            if non-zero, this is the value returned in
 *                              the genQueryOut structure and the current
 *                              call is to get more rows.  In this case, the
 *                              selectInp & sqlCondInp arguments are ignored.
 *  int rowOffset;              if positive, return rows starting with
 *                              this index (skip earlier ones), 0-origin  
 *  int options;                Bits for special options, currently:
 *                              If RETURN_TOTAL_ROW_COUNT is set, the total
 *                              number of available rows will be returned
 *                              in totalRowCount (causes a little overhead
 *                              so only request it if needed).  If rowOffset
 *                              is also used, totalRowCount will include
 *                              the skipped rows. 
 *  keyValPair_t condInput;
 *  inxIvalPair_t selectInp;    1st int array is columns to return (select),
 *                              2nd int array has bits for special options:
 *                              currently ORDER_BY and ORDER_BY_DESC 
 *  inxValPair_t sqlCondInp;    1st array is columns for conditions (where),
 *                              2nd array has strings for the conditions. 
 * } genQueryInp_t;
 *
 * #define GenQueryInp_PI "int maxRows; int continueInx; int partialStartIndex; 
 * int options; struct KeyValPair_PI; struct InxIvalPair_PI; struct InxValPair_PI;"
 * 
 * @author toaster
 */
public class GenQueryInp_PI implements IRodsPI
{

    private int maxRows;
    private int continueInx;
    private int partialStartIndex;
    private int options;
    private KeyValPair_PI keyValPair_PI;
    private InxIvalPair_PI inxIValPair_PI;
    private InxValPair_PI inxValPair_PI;
    private byte[] bytes;

    /**
     * 
     * @param maxRows
     * @param continueInx
     * @param partialStartIndex
     * @param options
     * @param keyValPair_PI key val pair, may be null
     * @param inxIValPair_PI selects, may be null
     * @param inxValPair_PI query, may be null
     */
    public GenQueryInp_PI(int maxRows, int continueInx, int partialStartIndex,
            int options, KeyValPair_PI keyValPair_PI, InxIvalPair_PI inxIValPair_PI,
            InxValPair_PI inxValPair_PI)
    {
        this.maxRows = maxRows;
        this.continueInx = continueInx;
        this.partialStartIndex = partialStartIndex;
        this.options = options;
        
        if ( keyValPair_PI == null )
        {
            this.keyValPair_PI = new KeyValPair_PI((Map)null);
        }
        else
        {
            this.keyValPair_PI = keyValPair_PI;
        }

        
        if ( inxIValPair_PI == null )
        {
            inxIValPair_PI = new InxIvalPair_PI(null);
        }
        else
        {
            this.inxIValPair_PI = inxIValPair_PI;
        }
        
        
        if ( inxValPair_PI == null )
        {
            inxValPair_PI = new InxValPair_PI(null);
        }
        else
        {
            this.inxValPair_PI = inxValPair_PI;
        }
    }

    public GenQueryInp_PI(ProtocolToken pt)
    {
        throw new UnsupportedOperationException("Client command, should not need to parse");
    }

    public byte[] getBytes()
    {

        if ( bytes == null )
        {
            bytes = toString().getBytes();
        }
        return bytes;

    }

    @Override
    public String toString()
    {
        return "<GenQueryInp_PI><maxRows>" + maxRows + "</maxRows><continueInx>" +
                continueInx + "</continueInx><partialStartIndex>" +
                partialStartIndex + "</partialStartIndex><options>" + options +
                "</options>" + keyValPair_PI + inxIValPair_PI + inxValPair_PI +
                "</GenQueryInp_PI>";
    }

    public int getMaxRows()
    {
        return maxRows;
    }

    public int getContinueInx()
    {
        return continueInx;
    }

    public int getPartialStartIndex()
    {
        return partialStartIndex;
    }

    public int getOptions()
    {
        return options;
    }

    public KeyValPair_PI getKeyValPair_PI()
    {
        return keyValPair_PI;
    }

    public InxIvalPair_PI getInxIValPair_PI()
    {
        return inxIValPair_PI;
    }

    public InxValPair_PI getInxValPair_PI()
    {
        return inxValPair_PI;
    }
}
