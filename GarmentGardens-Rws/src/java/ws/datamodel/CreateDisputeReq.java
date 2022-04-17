/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.DisputeEntity;

/**
 *
 * @author wong
 */
public class CreateDisputeReq {
    
    private DisputeEntity newDisputeEntity;
    private String username;
    private String password;
    private String orderId;
    private String title;
    private String description;

    public CreateDisputeReq() {
    }

    public CreateDisputeReq(DisputeEntity newDisputeEntity, String username, String password, String orderId, String title, String description) {
        this.newDisputeEntity = newDisputeEntity;
        this.username = username;
        this.password = password;
        this.orderId = orderId;
        this.title = title;
        this.description = description;
    }

    public DisputeEntity getNewDisputeEntity() {
        return newDisputeEntity;
    }

    public void setNewDisputeEntity(DisputeEntity newDisputeEntity) {
        this.newDisputeEntity = newDisputeEntity;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
