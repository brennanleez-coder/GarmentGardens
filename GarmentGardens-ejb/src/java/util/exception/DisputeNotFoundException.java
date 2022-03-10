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
public class DisputeNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>DisputeNotFoundException</code> without
     * detail message.
     */
    public DisputeNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DisputeNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DisputeNotFoundException(String msg) {
        super(msg);
    }
}
