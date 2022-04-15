/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ProductEntity;
import entity.UserEntity;


public class UpdateCartReq {
    
    private UserEntity currentUser;
    private ProductEntity productToAdd;
    private int qtyToAdd;
    private String promoCode;

    public UpdateCartReq() {
    }

    public UpdateCartReq(UserEntity currentUser, ProductEntity productToAdd, int qtyToAdd, String promoCode) {
        this.currentUser = currentUser;
        this.productToAdd = productToAdd;
        this.qtyToAdd = qtyToAdd;
        this.promoCode = promoCode;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public ProductEntity getProductToAdd() {
        return productToAdd;
    }

    public void setProductToAdd(ProductEntity productToAdd) {
        this.productToAdd = productToAdd;
    }

    public int getQtyToAdd() {
        return qtyToAdd;
    }

    public void setQtyToAdd(int qtyToAdd) {
        this.qtyToAdd = qtyToAdd;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    
}
