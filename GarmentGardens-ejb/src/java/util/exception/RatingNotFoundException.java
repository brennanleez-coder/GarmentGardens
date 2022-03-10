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
public class RatingNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>RatingNotFoundException</code> without
     * detail message.
     */
    public RatingNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RatingNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RatingNotFoundException(String msg) {
        super(msg);
    }
}
