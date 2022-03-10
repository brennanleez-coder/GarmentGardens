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
public class UpdateTagException extends Exception{

    /**
     * Creates a new instance of <code>UpdateTagException</code> without detail
     * message.
     */
    public UpdateTagException() {
    }

    /**
     * Constructs an instance of <code>UpdateTagException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateTagException(String msg) {
        super(msg);
    }
}
