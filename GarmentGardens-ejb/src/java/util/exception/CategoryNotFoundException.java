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
public class CategoryNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CategoryNotFoundException</code> without
     * detail message.
     */
    public CategoryNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CategoryNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CategoryNotFoundException(String msg) {
        super(msg);
    }
}
