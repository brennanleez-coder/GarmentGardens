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
public class UpdateCategoryException extends Exception {

    /**
     * Creates a new instance of <code>UpdateCategoryException</code> without
     * detail message.
     */
    public UpdateCategoryException() {
    }

    /**
     * Constructs an instance of <code>UpdateCategoryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateCategoryException(String msg) {
        super(msg);
    }
}
