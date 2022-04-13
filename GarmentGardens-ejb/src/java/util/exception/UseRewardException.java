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
public class UseRewardException extends Exception {

    /**
     * Creates a new instance of <code>UseRewardException</code> without detail
     * message.
     */
    public UseRewardException() {
    }

    /**
     * Constructs an instance of <code>UseRewardException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UseRewardException(String msg) {
        super(msg);
    }
}
