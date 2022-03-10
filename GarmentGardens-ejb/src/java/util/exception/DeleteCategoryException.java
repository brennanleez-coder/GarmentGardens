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
public class DeleteCategoryException extends Exception {

    /**
     * Creates a new instance of <code>DeleteCategoryException</code> without
     * detail message.
     */
    public DeleteCategoryException() {
    }

    /**
     * Constructs an instance of <code>DeleteCategoryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteCategoryException(String msg) {
        super(msg);
    }
}
