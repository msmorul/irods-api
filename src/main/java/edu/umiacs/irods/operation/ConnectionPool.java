/*
 * This class manages a pool of ConnectOperation objects that are used
 * to perform IRODS operations like registering files. A pool is used because
 * ConnectOperation is not thread safe, so if we want to perform multiple operations
 * simultaneously, we need a separate connection for each thread.
 */

package edu.umiacs.irods.operation;

import java.util.concurrent.LinkedBlockingQueue;
/**
 *
 * @author sashah
 */
public class ConnectionPool {
    //parameters needed to open a connection
    String host, username, password, zone;
    int port;
    private volatile LinkedBlockingQueue<ConnectOperation> freeConnections;

    public ConnectionPool(int num, String host, int port, String username, String password, String zone){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.zone = zone;
        freeConnections = new LinkedBlockingQueue<ConnectOperation>(num);
        try{
            for(int counter=0; counter<num; counter++)
                freeConnections.put(new ConnectOperation(host, port, username, password, zone));
            }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
    public  ConnectOperation getConnection() {
        try{
            return freeConnections.take();
        }
        catch(InterruptedException e){
            e.printStackTrace();
            return null;
        }
    }

    public  void releaseConnection(ConnectOperation co){//find the ConnectionOperation that corresponds to 'co' and make it free
        try{
            freeConnections.put(co);
        }
        catch(InterruptedException e){
            e.printStackTrace();            
        }
    }
}
