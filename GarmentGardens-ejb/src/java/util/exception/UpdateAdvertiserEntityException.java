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
public class UpdateAdvertiserEntityException extends Exception {

    /**
     * Creates a new instance of <code>UpdateAdvertiserEntityException</code>
     * without detail message.
     */
    public UpdateAdvertiserEntityException() {
    }

    /**
     * Constructs an instance of <code>UpdateAdvertiserEntityException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateAdvertiserEntityException(String msg) {
        super(msg);
    }
}
