/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umiacs.irods.operation;

import edu.umiacs.irods.api.IRodsConnection;
import edu.umiacs.irods.api.ResultMessage;
import edu.umiacs.irods.api.RodsUtil;
import edu.umiacs.irods.api.pi.ApiNumberEnum;
import edu.umiacs.irods.api.pi.AuthRequestOut_PI;
import edu.umiacs.irods.api.pi.AuthResponseInp_PI;
import edu.umiacs.irods.api.pi.ErrorEnum;
import edu.umiacs.irods.api.pi.HeaderTypeEnum;
import edu.umiacs.irods.api.pi.StartupPack_PI;
import edu.umiacs.irods.api.pi.VersionEnum;
import edu.umiacs.irods.api.pi.Version_PI;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * A container for holding an irods connection and connection information. It will cache a connection and 
 * return it using getConnection.  
 * @author toaster
 */
public class ConnectOperation {

    private static final Logger LOG =
            Logger.getLogger(ConnectOperation.class);
    private String host;
    private int port;
    private String username;
    private String zone;
    private transient IRodsConnection ic;
    private String password;
    boolean shutdown = false;
    private VersionEnum version = null;

    public ConnectOperation(String host, int port, String username,
            String password, String zone) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.zone = zone;
        this.password = password;

    }

    public ConnectOperation(String host, int port, String username,
            String password, String zone, VersionEnum version) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.zone = zone;
        this.password = password;
        this.version = version;
    }

    @Override
    public ConnectOperation clone() {
        return new ConnectOperation(host, port, username, password, zone, version);
    }

    /**
     * Close and re-establish a connection
     * 
     * @throws java.io.IOException
     */
    public void reconnect() throws IOException {
        if (shutdown) {
            return;
        }

        if (ic != null) {
            ic.closeConnection();
            ic = null;
        }
        getConnection();
    }

    public void shutdown() {
        if (ic != null) {
            ic.closeConnection();
        }
        shutdown = true;
    }

    /**
     * Returns cached connection if its not closed or null, otherwise open a new
     * connection. If an exception occurs during message transmission, the irodsconnection
     * will close the connection, clients should also feel free to close the connection
     * if an unexpected situation occurs
     * 
     * @return connection irods connection
     * @throws java.io.IOException
     */
    public IRodsConnection getConnection() throws IOException {
        if (shutdown) {
            throw new IllegalStateException("Connection already shutdown, will not open new connection");
        }

        if (ic != null && !ic.isClosed()) {
            return ic;
        }

        ResultMessage rm;

        // 0. Ghetto negotiage version
        if (version == null) {
            ic = IRodsConnection.openConnection(host, port);
            rm =
                    ic.sendMessage(HeaderTypeEnum.RODS_VERSION,
                    ApiNumberEnum.NO_API_NUMBER, null);

            ic.closeConnection();
            if (rm.getBodyToken() != null) {
                Version_PI vPi = new Version_PI(rm.getBodyToken());
                version = VersionEnum.valueOf(vPi);
                if (version == null) {
                    throw new IOException("Could not determine server version for: " + vPi.getRelVersion() + " api: " + vPi.getApiVersion());
                }
            } else {
                throw new IOException("IRODS server returned null when asked for version, header: " + rm.getHeader());
            }
        }

        // 1. open connection
        ic = IRodsConnection.openConnection(host, port);
        ic.setVesion(version);


        StartupPack_PI startupPacket =
                new StartupPack_PI(username, zone, version);

        rm =
                ic.sendMessage(HeaderTypeEnum.RODS_CONNECT,
                ApiNumberEnum.NO_API_NUMBER, startupPacket);
        int returnvalue = rm.getHeader().getIntInfo();
        if (returnvalue != 0) {
            throw new IOException("Authentication failed (send startup packet): "
                    + ErrorEnum.valueOf(returnvalue));
        }

        // 3. send challenge
        rm = ic.sendMessage(HeaderTypeEnum.RODS_API_REQ,
                ApiNumberEnum.AUTH_REQUEST_AN);
        returnvalue = rm.getHeader().getIntInfo();
        if (returnvalue != 0) {
            throw new IOException("Authentication failed (send challenge): "
                    + ErrorEnum.valueOf(returnvalue));
        }
        AuthRequestOut_PI request = new AuthRequestOut_PI(rm.getBodyToken());

        String response =
                RodsUtil.challengeResponse(request.getChallenge(), password);
        String myUser = username;
        if (version.compareTo(VersionEnum.RODS2_0) >= 0) {
            myUser = username + "#" + zone;
        }
        AuthResponseInp_PI authResp =
                new AuthResponseInp_PI(myUser, response);
        rm =
                ic.sendMessage(HeaderTypeEnum.RODS_API_REQ,
                ApiNumberEnum.AUTH_RESPONSE_AN, authResp);
        returnvalue = rm.getHeader().getIntInfo();
        if (returnvalue != 0) {
            throw new IOException("Authentication failed (send auth response): "
                    + ErrorEnum.valueOf(returnvalue));
        }
        return ic;

    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getZone() {
        return zone;
    }

    public VersionEnum getVersion() {
        try {
            if (version == null) {
                getConnection();
            }
        } catch (IOException ioe) {
            throw new NullPointerException("Could not access iRODS to get version");
        }
        return version;
    }
}
