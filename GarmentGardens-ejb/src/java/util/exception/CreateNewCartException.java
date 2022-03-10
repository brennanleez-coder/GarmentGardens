/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author ryyant
 */
public class CreateNewCartException extends Exception {

    public CreateNewCartException() {
    }

    public CreateNewCartException(String msg) {
        super(msg);
    }
}
