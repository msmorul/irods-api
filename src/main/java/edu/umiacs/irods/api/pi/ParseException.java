/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.umiacs.irods.api.pi;

/**
 * Exception when a packed instruction cannot be parsed into the expected instruction
 * @author toaster
 */
public class ParseException extends IllegalArgumentException {

    /**
     * Creates a new instance of <code>ParseException</code> without detail message.
     */
    public ParseException() {
    }


    /**
     * Constructs an instance of <code>ParseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ParseException(String msg) {
        super(msg);
    }
}
