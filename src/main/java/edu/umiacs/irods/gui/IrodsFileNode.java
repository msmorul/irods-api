/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.gui;

import java.util.Date;

/** 
 * File node used by the FileTreeModel
 * @author toaster
 */
public class IrodsFileNode
{

    private String name,  resource,  owner, ownerzone;
    private FileTreeModel parent;
    private Date modDate,  createDate;
    private long size;
    private int replNum;

    public IrodsFileNode(String name, String resource, String owner, String ownerzone,
            Date modDate, Date createDate, long size, int replNum, FileTreeModel parent)
    {
        this.name = name;
        this.resource = resource;
        this.owner = owner;
        this.parent = parent;
        this.modDate = modDate;
        this.createDate = createDate;
        this.size = size;
        this.replNum = replNum;
        this.ownerzone = ownerzone;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public String getName()
    {
        return name;
    }

    public String getResource()
    {
        return resource;
    }

    public String getOwnerZone()
    {
        return ownerzone;
    }
    
    public String getOwner()
    {
        return owner;
    }

    public Date getModDate()
    {
        return modDate;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public long getSize()
    {
        return size;
    }

    public int getReplNum()
    {
        return replNum;
    }

    //private void remove
//    public void delete()
//    {
//
//    }
}
