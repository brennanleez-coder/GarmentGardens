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
public class ProductSkuCodeExistException extends Exception {

    /**
     * Creates a new instance of <code>ProductSkuCodeExistException</code>
     * without detail message.
     */
    public ProductSkuCodeExistException() {
    }

    /**
     * Constructs an instance of <code>ProductSkuCodeExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ProductSkuCodeExistException(String msg) {
        super(msg);
    }
}
