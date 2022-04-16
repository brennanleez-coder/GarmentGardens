/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.UserEntity;

/**
 *
 * @author rilwa
 */
public class ChangePasswordReq {
    
    private UserEntity currentUser;
    private String newPassword;

    public ChangePasswordReq() {
    }

    public ChangePasswordReq(UserEntity currentUser, String newPassword) {
        this.currentUser = currentUser;
        this.newPassword = newPassword;
    }

    /**
     * @return the currentUser
     */
    public UserEntity getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    
    
}
