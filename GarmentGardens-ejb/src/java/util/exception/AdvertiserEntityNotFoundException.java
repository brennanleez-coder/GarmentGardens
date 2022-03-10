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
public class AdvertiserEntityNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>AdvertiserEntityNotFoundException</code>
     * without detail message.
     */
    public AdvertiserEntityNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AdvertiserEntityNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AdvertiserEntityNotFoundException(String msg) {
        super(msg);
    }
}
