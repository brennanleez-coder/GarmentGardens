/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author wong
 */
public class CreateNewStaffException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewStaffException</code> without
     * detail message.
     */
    public CreateNewStaffException() {
    }

    /**
     * Constructs an instance of <code>CreateNewStaffException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewStaffException(String msg) {
        super(msg);
    }
}
