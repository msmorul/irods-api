/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.operation;

/**
 *
 * @author toaster
 */
public interface BulkTransferListener {

    public void startTransfer();
    public void startFile(String fullPath);
    public void bytesWritten(int bytesWritten);
    public void endFile(String fullPath);
    public void endTransfer();
    public void handleException(Throwable t);
}
