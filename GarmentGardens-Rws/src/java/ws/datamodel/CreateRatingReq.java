/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ProductEntity;
import entity.RatingEntity;
import entity.UserEntity;

public class CreateRatingReq {

    private ProductEntity product;
    private UserEntity user;
    private RatingEntity newRating;

    public CreateRatingReq() {
    }

    public CreateRatingReq(ProductEntity product, UserEntity user, RatingEntity newRating) {
        this.product = product;
        this.user = user;
        this.newRating = newRating;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public RatingEntity getNewRating() {
        return newRating;
    }

    public void setNewRating(RatingEntity newRating) {
        this.newRating = newRating;
    }

    
    

}
