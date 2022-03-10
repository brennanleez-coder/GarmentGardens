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
public class CreateNewTagException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewTagException</code> without
     * detail message.
     */
    public CreateNewTagException() {
    }

    /**
     * Constructs an instance of <code>CreateNewTagException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewTagException(String msg) {
        super(msg);
    }
}
