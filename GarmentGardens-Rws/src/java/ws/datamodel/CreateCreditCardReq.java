/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.AdvertiserEntity;
import entity.CreditCardEntity;
import entity.UserEntity;
import java.util.Date;

/**
 *
 * @author brennanlee
 */
public class CreateCreditCardReq {

    private CreditCardEntity newCC;
    private String username;
    private String password;

    public CreateCreditCardReq() {
    }

    public CreateCreditCardReq(CreditCardEntity newCC, String username, String password) {
        this.newCC = newCC;
        this.username = username;
        this.password = password;
    }

    /**
     * @return the newCC
     */
    public CreditCardEntity getNewCC() {
        return newCC;
    }

    /**
     * @param newCC the newCC to set
     */
    public void setNewCC(CreditCardEntity newCC) {
        this.newCC = newCC;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
