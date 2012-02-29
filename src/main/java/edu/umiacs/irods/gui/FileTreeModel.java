/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.gui;

import edu.umiacs.irods.operation.ConnectOperation;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author toaster
 */
public class FileTreeModel implements TreeModel
{

    private List<TreeModelListener> listeners =
            new ArrayList<TreeModelListener>();
    private ConnectOperation co;
    private IrodsDirectoryNode root;

    public FileTreeModel(ConnectOperation connection, String root)
    {
        this.co = connection;
        if ( root.endsWith("/") && root.length() > 1 )
        {
            root = root.substring(0, root.length() - 2);
        }

        this.root = new IrodsDirectoryNode(this, root);
    }

    public Object getRoot()
    {
        return root;
    }

    public Object getChild(Object parent, int index)
    {
        if ( parent instanceof IrodsDirectoryNode )
        {
            return ((IrodsDirectoryNode) parent).getChild(index);
        }
        else
        {
            throw new IllegalArgumentException("parent is not a directory");
        }
    }

    public int getChildCount(Object parent)
    {

        if ( parent instanceof IrodsDirectoryNode )
        {
            return ((IrodsDirectoryNode) parent).getChildCount();
        }
        else
        {
            throw new IllegalArgumentException("parent is not a directory");
        }
    }

    public boolean isLeaf(Object node)
    {

        if ( node instanceof IrodsDirectoryNode )
        {
            return false;
        }
        if ( node instanceof IrodsFileNode )
        {
            return true;
        }

        throw new IllegalArgumentException("Child is not file or directory " +
                node.getClass());
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        if ( parent instanceof IrodsDirectoryNode )
        {
            return ((IrodsDirectoryNode) parent).indexOf(child);
        }
        else
        {
            throw new IllegalArgumentException("parent is not a directory");
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

    /**
     * Make the connection available to other tree nodes in this package
     * 
     * @return
     */
    ConnectOperation getConnetion()
    {
        return co;
    }

    /**
     * Fire listeners to notify a new directory has been created
     */
    void directoryChanged(IrodsDirectoryNode parent)
    {
        TreePath tp = new TreePath(root);
        String base = parent.getPath().substring(root.getPath().length());
        
        for ( String s : base.split("/") )
        {
            tp =
                    tp.pathByAddingChild(((IrodsDirectoryNode) tp.getLastPathComponent()).getChild(s));
        }

        TreeModelEvent event = new TreeModelEvent(parent, tp);
        for ( TreeModelListener tml : listeners )
        {
            tml.treeStructureChanged(event);
        }
    }
}
