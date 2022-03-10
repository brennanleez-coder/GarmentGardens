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
public class UpdateMessageOfTheDayException extends Exception {

    /**
     * Creates a new instance of <code>UpdateMessageOfTheDayException</code>
     * without detail message.
     */
    public UpdateMessageOfTheDayException() {
    }

    /**
     * Constructs an instance of <code>UpdateMessageOfTheDayException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateMessageOfTheDayException(String msg) {
        super(msg);
    }
}
