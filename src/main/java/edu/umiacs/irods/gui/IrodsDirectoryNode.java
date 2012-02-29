/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.gui;

import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.operation.IrodsOperations;
import edu.umiacs.irods.operation.QueryBuilder;
import edu.umiacs.irods.operation.QueryResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author toaster
 */
public class IrodsDirectoryNode
{

    private String path;
    private List<IrodsDirectoryNode> directories;
    private List<IrodsFileNode> files;
    private FileTreeModel parent;
    private String owner, ownerZone;
    private Date creationDate, modDate;

    IrodsDirectoryNode(FileTreeModel parentModel, String path)
    {
        this.parent = parentModel;
        this.path = path;
        loadThisDirectory();
    }

    IrodsDirectoryNode(FileTreeModel parentModel, String path, String owner, 
            String ownerZone, Date creationDate, Date modDate)
    {
        this.parent = parentModel;
        this.path = path;
        this.owner = owner;
        this.ownerZone = ownerZone;
        this.creationDate = creationDate;
        this.modDate = modDate;
    }
    
    public String getPath()
    {
        return path;
    }

    public String getName()
    {
        if ( getPath().length() > 1 )
        {
            return getPath().substring(getPath().lastIndexOf("/") + 1);
        }
        else
        {
            return getPath();
        }
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public int indexOf(Object child)
    {
        loadChildren();
        if ( child instanceof IrodsDirectoryNode )
        {
            return directories.indexOf(child);
        }
        else
        {
            return directories.size() + files.indexOf(child);
        }
    }

    public Object getChild(int index)
    {
        loadChildren();
        if ( index < directories.size() )
        {
            return directories.get(index);
        }
        else
        {
            return files.get(index - directories.size());
        }
    }

    /**
     * Get child by its short name
     * @param name
     * @return
     */
    public Object getChild(String name)
    {
        for ( IrodsDirectoryNode d : directories )
        {
            if ( d.getName().equals(name) )
            {
                return d;
            }
        }
        return null;
    }

    public int getChildCount()
    {
        loadChildren();
        return directories.size() + files.size();
    }

    public void mkdir(String newDir) throws IRodsRequestException
    {
        String newPath = getPath() + "/" + newDir;
        new IrodsOperations(parent.getConnetion()).mkdir(newPath);

        directories.add(new IrodsDirectoryNode(parent,newPath));
        parent.directoryChanged(this);

    }

    public void rmdir() throws IRodsRequestException
    {
        String newPath = getPath() ;
        new IrodsOperations(parent.getConnetion()).rmdir(newPath,true);

//        directories.add(new IrodsDirectoryNode(parent,newPath));
        parent.directoryChanged(this);

    }
//
//    public void delete(boolean recursive)
//    {
//
//    }
    private void loadChildren()
    {
        if ( directories == null )
        {
            loadDirectories();
            loadFiles();
        }
    }

    private void loadThisDirectory()
    {

        QueryBuilder qb;
        QueryResult qr;

        try
        {

            qb = new QueryBuilder(GenQueryEnum.COL_COLL_NAME, GenQueryEnum.COL_COLL_CREATE_TIME,
                    GenQueryEnum.COL_COLL_MODIFY_TIME, GenQueryEnum.COL_COLL_OWNER_NAME,
                    GenQueryEnum.COL_COLL_OWNER_ZONE);


            qb.eq(GenQueryEnum.COL_COLL_NAME,getPath());
            qr = qb.execute(parent.getConnetion().getConnection());


            while ( qr.next() )
            {
                if (qr.getValue(GenQueryEnum.COL_COLL_NAME).equals(getPath()))
                    continue;
                
                owner = qr.getValue(GenQueryEnum.COL_COLL_OWNER_NAME);
                ownerZone = qr.getValue(GenQueryEnum.COL_COLL_OWNER_ZONE);
                creationDate = qr.getDateValue(GenQueryEnum.COL_COLL_CREATE_TIME);
                modDate = qr.getDateValue(GenQueryEnum.COL_COLL_MODIFY_TIME);
                
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
    
    private void loadDirectories()
    {

        QueryBuilder qb;
        QueryResult qr;
        directories = new ArrayList<IrodsDirectoryNode>();
        try
        {

            qb = new QueryBuilder(GenQueryEnum.COL_COLL_NAME, GenQueryEnum.COL_COLL_CREATE_TIME,
                    GenQueryEnum.COL_COLL_MODIFY_TIME, GenQueryEnum.COL_COLL_OWNER_NAME,
                    GenQueryEnum.COL_COLL_OWNER_ZONE);


            qb.eq(GenQueryEnum.COL_COLL_PARENT_NAME,getPath());
            qr = qb.execute(parent.getConnetion().getConnection());


            while ( qr.next() )
            {
                if (qr.getValue(GenQueryEnum.COL_COLL_NAME).equals(getPath()))
                    continue;
                
                String ncolName = qr.getValue(GenQueryEnum.COL_COLL_NAME);
                String nowner = qr.getValue(GenQueryEnum.COL_COLL_OWNER_NAME);
                String nownerZone = qr.getValue(GenQueryEnum.COL_COLL_OWNER_ZONE);
                Date ncreateDate = qr.getDateValue(GenQueryEnum.COL_COLL_CREATE_TIME);
                Date nmodDate = qr.getDateValue(GenQueryEnum.COL_COLL_MODIFY_TIME);
                
                
                directories.add(new IrodsDirectoryNode(parent, ncolName, nowner, 
                        nownerZone, ncreateDate, nmodDate));

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

    private void loadFiles()
    {

        files = new ArrayList<IrodsFileNode>();
        QueryBuilder qb;
        QueryResult qr;
        try
        {
            qb =
                    new QueryBuilder(GenQueryEnum.COL_DATA_SIZE,
                    GenQueryEnum.COL_D_RESC_NAME, GenQueryEnum.COL_DATA_REPL_NUM,
                    GenQueryEnum.COL_DATA_NAME, GenQueryEnum.COL_D_OWNER_NAME, 
                    GenQueryEnum.COL_D_OWNER_ZONE,
                    GenQueryEnum.COL_D_MODIFY_TIME, GenQueryEnum.COL_D_CREATE_TIME);

            qb.eq(GenQueryEnum.COL_COLL_NAME,getPath());
            qr = qb.execute(parent.getConnetion().getConnection());

            while ( qr.next() )
            {
                Date nmodDate = qr.getDateValue(GenQueryEnum.COL_D_MODIFY_TIME);
                Date createDate =
                        qr.getDateValue(GenQueryEnum.COL_D_CREATE_TIME);
                String name = qr.getValue(GenQueryEnum.COL_DATA_NAME);
                String resource = qr.getValue(GenQueryEnum.COL_D_RESC_NAME);
                String nowner = qr.getValue(GenQueryEnum.COL_D_OWNER_NAME);
                String nownerZone = qr.getValue(GenQueryEnum.COL_D_OWNER_ZONE);
                long size = qr.getLongValue(GenQueryEnum.COL_DATA_SIZE);
                int replNum = qr.getIntValue(GenQueryEnum.COL_DATA_REPL_NUM);

                IrodsFileNode ifn = new IrodsFileNode(name, resource, nowner, nownerZone,
                        nmodDate, createDate, size, replNum, parent);
                files.add(ifn);
            }


        }
        catch ( IOException ex )
        {
            throw new RuntimeException(ex);
        }
        
    }

    public String getOwner()
    {
        return owner;
    }

    public String getOwnerZone()
    {
        return ownerZone;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }

    public Date getModDate()
    {
        return modDate;
    }
}
