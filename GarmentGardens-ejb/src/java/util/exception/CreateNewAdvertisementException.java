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
public class CreateNewAdvertisementException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewAdvertisementException</code>
     * without detail message.
     */
    public CreateNewAdvertisementException() {
    }

    /**
     * Constructs an instance of <code>CreateNewAdvertisementException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewAdvertisementException(String msg) {
        super(msg);
    }
}
