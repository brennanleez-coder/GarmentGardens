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
public class MessageOfTheDayNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>MessageOfTheDayNotFoundException</code>
     * without detail message.
     */
    public MessageOfTheDayNotFoundException() {
    }

    /**
     * Constructs an instance of <code>MessageOfTheDayNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MessageOfTheDayNotFoundException(String msg) {
        super(msg);
    }
}
