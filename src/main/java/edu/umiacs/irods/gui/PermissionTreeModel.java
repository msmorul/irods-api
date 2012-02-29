/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.gui;

import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.operation.ConnectOperation;
import edu.umiacs.irods.operation.QueryBuilder;
import edu.umiacs.irods.operation.QueryResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author toaster
 */
public class PermissionTreeModel implements TreeModel
{

    private String ROOT = "root";
    private List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
    private Map<String, List<String>> permList = new HashMap<String, List<String>>();
    private List<String> userOrder = new ArrayList<String>();

    public PermissionTreeModel(String directory, String file, ConnectOperation co)
    {
        
        QueryBuilder qb = new QueryBuilder(GenQueryEnum.COL_USER_NAME, 
                GenQueryEnum.COL_DATA_ACCESS_NAME);
        if ( file != null && file.length() != 0 )
        {
            qb.eq(GenQueryEnum.COL_DATA_NAME, file);
        }

        qb.eq(GenQueryEnum.COL_COLL_NAME, directory);
        IRodsConnection ic = null;

        try
        {
            ic = co.getConnection();
            QueryResult result = qb.execute(ic);
            while (result.next())
            {
                
                String user = result.getValue(GenQueryEnum.COL_USER_NAME) ;
                String acl = result.getValue(GenQueryEnum.COL_DATA_ACCESS_NAME);
                if (!permList.containsKey(user))
                {
                    permList.put(user, new ArrayList<String>());
                    userOrder.add(user);
                }
                permList.get(user).add(acl);
            }
        }
         catch ( IRodsRequestException ex )
        {
            if ( ex.getErrorCode() == ErrorEnum.CAT_NO_ROWS_FOUND )
            {
                return;
            }
            throw new RuntimeException(ex);
        }
        catch ( IOException ex )
        {
            throw new RuntimeException(ex);
        }
    }

    public void addTreeModelListener(TreeModelListener l)
    {
        listeners.add(l);
    }

    public void removeTreeModelListener(TreeModelListener l)
    {
        listeners.remove(l);
    }

    public Object getRoot()
    {
        return ROOT;
    }

    public Object getChild(Object parent, int index)
    {
        if ( parent == ROOT )
        {
            return userOrder.get(index);
        }
        else
        {
            if ( userOrder.contains(parent) )
            {
                return permList.get(parent).get(index);
            }
        }
        return null;
    }

    public int getChildCount(Object parent)
    {
        if ( parent == ROOT )
        {
            return userOrder.size();
        }
        else if ( userOrder.contains(parent) )
        {
            return permList.get(parent).size();
        }


        return 0;
    }

    public boolean isLeaf(Object node)
    {
        return (!(node == ROOT || userOrder.contains(node)));
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        if ( parent == ROOT )
        {
            return userOrder.indexOf(child);
        }
        else if ( userOrder.contains(parent) )
        {
            return permList.get(parent).indexOf(child);
        }
        return -1;

    }
}
