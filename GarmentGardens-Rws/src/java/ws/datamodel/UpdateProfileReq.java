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
public class UpdateProfileReq {
    
    private UserEntity currentUser;
    private Long dateOfBirth;

    public UpdateProfileReq() {
    }

    public UpdateProfileReq(UserEntity currentUser) {
        this.currentUser = currentUser;
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
     * @return the dateOfBirth
     */
    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    
    
}
