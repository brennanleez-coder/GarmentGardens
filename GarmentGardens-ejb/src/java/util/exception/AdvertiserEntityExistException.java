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
public class AdvertiserEntityExistException extends Exception {

    /**
     * Creates a new instance of <code>AdvertiserEntityExistException</code>
     * without detail message.
     */
    public AdvertiserEntityExistException() {
    }

    /**
     * Constructs an instance of <code>AdvertiserEntityExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AdvertiserEntityExistException(String msg) {
        super(msg);
    }
}
