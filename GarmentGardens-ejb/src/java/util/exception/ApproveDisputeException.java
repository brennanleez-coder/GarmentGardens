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
public class ApproveDisputeException extends Exception {

    /**
     * Creates a new instance of <code>ApproveDisputeException</code> without
     * detail message.
     */
    public ApproveDisputeException() {
    }

    /**
     * Constructs an instance of <code>ApproveDisputeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ApproveDisputeException(String msg) {
        super(msg);
    }
}
