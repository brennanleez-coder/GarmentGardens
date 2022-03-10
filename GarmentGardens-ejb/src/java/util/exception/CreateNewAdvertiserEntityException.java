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
public class CreateNewAdvertiserEntityException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewAdvertiserEntityException</code>
     * without detail message.
     */
    public CreateNewAdvertiserEntityException() {
    }

    /**
     * Constructs an instance of <code>CreateNewAdvertiserEntityException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewAdvertiserEntityException(String msg) {
        super(msg);
    }
}
