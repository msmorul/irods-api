/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.gui;

import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.operation.QueryResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author toaster
 */
public class ResultTableModel implements TableModel
{

    private QueryResult qr;
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    private List<GenQueryEnum> columns;
    private Map<GenQueryEnum, List<String>> resultMap = new HashMap<GenQueryEnum, List<String>>();
    private int numResults = 0;

    public ResultTableModel(QueryResult qr)
    {
        this.qr = qr;
        if ( qr == null )
        {
            throw new NullPointerException("Query result is null");
        }

        columns = qr.getColumDescriptions();
        if ( qr.first() )
        {
            for (GenQueryEnum col : columns)
            {
                resultMap.put(col, new ArrayList<String>());
                resultMap.get(col).add(qr.getValue(col));

            }
            numResults++;
        }
        
        while ( qr.next() )
        {
            for (GenQueryEnum col : columns)
            {
                resultMap.get(col).add(qr.getValue(col));
            }
            numResults++;

        }
    }

    public int getRowCount()
    {
        return numResults;
    }

    public int getColumnCount()
    {
        return columns.size();
    }

    public String getColumnName(int columnIndex)
    {
        return columns.get(columnIndex).toString();
    }

    public Class<?> getColumnClass(int columnIndex)
    {
        // can this be included in the column descriptions
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return resultMap.get(columns.get(columnIndex)).get(rowIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        throw new UnsupportedOperationException("setValueAt not supported");
    }

    public void addTableModelListener(TableModelListener l)
    {
        listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        listeners.remove(l);
    }
}
