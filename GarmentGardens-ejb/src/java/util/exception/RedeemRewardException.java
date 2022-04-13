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
public class RedeemRewardException extends Exception {

    /**
     * Creates a new instance of <code>RedeemRewardException</code> without
     * detail message.
     */
    public RedeemRewardException() {
    }

    /**
     * Constructs an instance of <code>RedeemRewardException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RedeemRewardException(String msg) {
        super(msg);
    }
}
