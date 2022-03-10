/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author rilwa
 */
public class UserUsernameExistException extends Exception{

    public UserUsernameExistException() {
    }

    public UserUsernameExistException(String string) {
        super(string);
    }
    
    
    
}
