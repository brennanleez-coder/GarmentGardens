/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.RatingEntitySessionBeanLocal;
import entity.RatingEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author brennanlee
 */
@Named(value = "ratingManagedBean")
@ViewScoped
public class RatingManagedBean implements Serializable{

    @EJB(name = "RatingEntitySessionBeanLocal")
    private RatingEntitySessionBeanLocal ratingEntitySessionBeanLocal;

    
    /**
     * Creates a new instance of RatingManagedBean
     */
    private List<RatingEntity> ratings;

    public RatingManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        Long productId = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("productId");
        setRatings(ratingEntitySessionBeanLocal.retrieveAllRatingsByProductId(productId));
    }

    public List<RatingEntity> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingEntity> ratings) {
        this.ratings = ratings;
    }
    

    
}
