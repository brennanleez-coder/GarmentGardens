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
public class UpdateProductException extends Exception {

    /**
     * Creates a new instance of <code>UpdateProductException</code> without
     * detail message.
     */
    public UpdateProductException() {
    }

    /**
     * Constructs an instance of <code>UpdateProductException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateProductException(String msg) {
        super(msg);
    }
}
