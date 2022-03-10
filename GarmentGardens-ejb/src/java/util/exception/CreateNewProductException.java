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
public class CreateNewProductException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewProductException</code> without
     * detail message.
     */
    public CreateNewProductException() {
    }

    /**
     * Constructs an instance of <code>CreateNewProductException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewProductException(String msg) {
        super(msg);
    }
}
