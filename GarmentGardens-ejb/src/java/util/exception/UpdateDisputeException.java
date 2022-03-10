/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author brennanlee
 */
public class UpdateDisputeException extends Exception {

    /**
     * Creates a new instance of <code>UpdateDisputeException</code> without
     * detail message.
     */
    public UpdateDisputeException() {
    }

    /**
     * Constructs an instance of <code>UpdateDisputeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateDisputeException(String msg) {
        super(msg);
    }
}
