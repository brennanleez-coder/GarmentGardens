/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.AdvertiserEntity;
import entity.UserEntity;
import java.util.Date;

/**
 *
 * @author brennanlee
 */
public class UpdateCreditCardReq {
    private String username;
    private String password;
    private String holderName;
    private String creditCardNumber;
    private String cvv;
    private Date expiryDate;
    private String billingAddress;
    
    private UserEntity user;
    private AdvertiserEntity advertiser;

    public UpdateCreditCardReq() {
    }

    public UpdateCreditCardReq(String username, String password, String holderName, String creditCardNumber, String cvv, Date expiryDate, String billinagAddress, UserEntity user, AdvertiserEntity advertiser) {
        this.username = username;
        this.password = password;
        this.holderName = holderName;
        this.creditCardNumber = creditCardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.billingAddress = billinagAddress;
        this.user = user;
        this.advertiser = advertiser;
    }
    
    

    
    
    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AdvertiserEntity getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(AdvertiserEntity advertiser) {
        this.advertiser = advertiser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
