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
public class DeleteProductException extends Exception {

    /**
     * Creates a new instance of <code>DeleteProductException</code> without
     * detail message.
     */
    public DeleteProductException() {
    }

    /**
     * Constructs an instance of <code>DeleteProductException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteProductException(String msg) {
        super(msg);
    }
}
