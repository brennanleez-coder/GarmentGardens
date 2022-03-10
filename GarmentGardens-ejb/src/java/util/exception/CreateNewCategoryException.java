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
public class CreateNewCategoryException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewCategoryException</code> without
     * detail message.
     */
    public CreateNewCategoryException() {
    }

    /**
     * Constructs an instance of <code>CreateNewCategoryException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewCategoryException(String msg) {
        super(msg);
    }
}
