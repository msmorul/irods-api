/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.gui;

import edu.umiacs.irods.api.IRodsRequestException;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.GenQueryEnum;
import edu.umiacs.irods.api.pi.GeneralAdminInp_PI;
import edu.umiacs.irods.operation.ConnectOperation;
import edu.umiacs.irods.operation.IrodsApiRequest;
import edu.umiacs.irods.operation.QueryBuilder;
import edu.umiacs.irods.operation.QueryResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author toaster
 */
public class UserTableModel implements TableModel
{

    private List<TableModelListener> listeners =
            new ArrayList<TableModelListener>();
    private ConnectOperation co;
    private boolean editable;
    private List<UserBean> data = new ArrayList<UserBean>();
    private UserColumns[] columns;

    public UserTableModel(ConnectOperation co, boolean editable, UserColumns... columns)
    {

        init(co, editable, columns);
    }

    public UserTableModel(ConnectOperation co, UserColumns... columns)
    {
        init(co, false, columns);
    }

    private void init(ConnectOperation co, boolean editable, UserColumns... columns)
    {
        this.co = co;
        this.editable = editable;
        this.columns = new UserColumns[columns.length];
        System.arraycopy(columns, 0, this.columns, 0, columns.length);
        loadUsers();
    }

    private void loadUsers()
    {
        QueryBuilder qb;
        QueryResult qr;
        try
        {
            data.clear();
            qb = new QueryBuilder(GenQueryEnum.COL_USER_ID,
                    GenQueryEnum.COL_USER_NAME,
                    GenQueryEnum.COL_USER_TYPE,
                    GenQueryEnum.COL_USER_ZONE,
                    GenQueryEnum.COL_USER_DN,
                    GenQueryEnum.COL_USER_INFO,
                    GenQueryEnum.COL_USER_COMMENT,
                    GenQueryEnum.COL_USER_CREATE_TIME,
                    GenQueryEnum.COL_USER_MODIFY_TIME);


            qr = qb.execute(co.getConnection());

            while ( qr.next() )
            {
                UserBean ub = new UserBean();
                ub.comment = qr.getValue(GenQueryEnum.COL_USER_COMMENT);
                ub.id = qr.getIntValue(GenQueryEnum.COL_USER_ID);
                ub.createTime =
                        qr.getDateValue(GenQueryEnum.COL_USER_CREATE_TIME);
                ub.dn = qr.getValue(GenQueryEnum.COL_USER_DN);
                ub.info = qr.getValue(GenQueryEnum.COL_USER_INFO);
                ub.modTime = qr.getDateValue(GenQueryEnum.COL_USER_MODIFY_TIME);
                ub.type = qr.getValue(GenQueryEnum.COL_USER_TYPE);
                ub.username = qr.getValue(GenQueryEnum.COL_USER_NAME);
                ub.zone = qr.getValue(GenQueryEnum.COL_USER_ZONE);

                data.add(ub);
            }


        }
        catch ( IRodsRequestException ex )
        {

            if ( ex.getErrorCode() == ErrorEnum.CAT_NO_ROWS_FOUND )
            {
                System.out.println("No rows found");
                return;
            }
            throw new RuntimeException(ex);
        }
        catch ( IOException ex )
        {
            throw new RuntimeException(ex);
        }
        finally
        {
            tableChanged();
        }
    }

    private void tableChanged()
    {
        TableModelEvent event = new TableModelEvent(this);
        for ( TableModelListener tml : listeners )
        {
            tml.tableChanged(event);
        }
    }

    public UserBean getRowBean(int row)
    {
        return data.get(row);
    }

    public int getRowCount()
    {
        return data.size();
    }

    public int getColumnCount()
    {
        return columns.length;
    }

    public String getColumnName(int columnIndex)
    {
        return columns[columnIndex].getDescription();
    }

    public Class<?> getColumnClass(int columnIndex)
    {
        return columns[columnIndex].getClassType();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if ( !editable )
        {
            return false;
        }
        return columns[columnIndex].isEditable();
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data.get(rowIndex).getColumnValue(columns[columnIndex]);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if ( !isCellEditable(rowIndex, columnIndex) )
        {
            throw new IllegalStateException("Cannot edit value");
        }

    }

    public void addTableModelListener(TableModelListener l)
    {
        listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        listeners.remove(l);
    }

//    public void removeUser(int row)
//    {
//        
//    }
    public void addUser(String user, String userType) throws IOException
    {

        GeneralAdminInp_PI adminPI =
                new GeneralAdminInp_PI("add", "user", user, userType, null, null, null, null, null, null);

        IrodsApiRequest apireq =
                new IrodsApiRequest(ApiNumberEnum.GENERAL_ADMIN_AN, adminPI, null);
        apireq.sendRequest(co.getConnection());

    }

    public enum UserColumns
    {

        ID("ID", GenQueryEnum.COL_USER_ID, false, Integer.class),
        NAME("Username", GenQueryEnum.COL_USER_NAME, false, String.class),
        TYPE("Type", GenQueryEnum.COL_USER_TYPE, true, String.class),
        ZONE("Zone", GenQueryEnum.COL_USER_ZONE, true, String.class),
        DN("DN", GenQueryEnum.COL_USER_DN, true, String.class),
        INFO("Info", GenQueryEnum.COL_USER_INFO, true, String.class),
        COMMENT("Comment", GenQueryEnum.COL_USER_COMMENT, true, String.class),
        CREATE_TIME("Creation Time", GenQueryEnum.COL_USER_CREATE_TIME, false, Date.class),
        MODIFY_TIME("Modification Time", GenQueryEnum.COL_USER_MODIFY_TIME, false, Date.class);
        private boolean editable;
        private String description;
        private GenQueryEnum queryEnum;
        private Class classType;

        UserColumns(String desc, GenQueryEnum queryEnum, boolean editable, Class classType)
        {
            this.description = desc;
            this.queryEnum = queryEnum;
            this.editable = editable;
            this.classType = classType;
        }

        public String getDescription()
        {
            return description;
        }

        public GenQueryEnum getQueryEnum()
        {
            return queryEnum;
        }

        public boolean isEditable()
        {
            return editable;
        }

        public Class getClassType()
        {
            return classType;
        }
    }

    public class UserBean
    {

        private String username;
        private String zone;
        private int id;
        private String dn;
        private String info;
        private String comment;
        private Date createTime;
        private Date modTime;
        private String type;

        public Object getColumnValue(UserColumns column)
        {
            switch (column)
            {
                case ID:
                    return id;
                case COMMENT:
                    return comment;
                case CREATE_TIME:
                    return createTime;
                case DN:
                    return dn;
                case INFO:
                    return info;
                case MODIFY_TIME:
                    return modTime;
                case NAME:
                    return username;
                case TYPE:
                    return type;
                case ZONE:
                    return zone;
                default:
                    throw new RuntimeException("Unhandled column: " + column);
            }
        }
    }
}
