/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.RatingEntitySessionBeanLocal;
import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.ProductNotFoundException;

/**
 *
 * @author brennanlee
 */
@Named(value = "viewProductInNewPageManagedBean")
@ViewScoped
public class ViewProductInNewPageManagedBean implements Serializable {

    @EJB(name = "RatingEntitySessionBeanLocal")
    private RatingEntitySessionBeanLocal ratingEntitySessionBeanLocal;

    private ProductEntity productEntityFullDetailsToView;
    private String ratingScoreForProduct;

    public ViewProductInNewPageManagedBean() {
        productEntityFullDetailsToView = new ProductEntity();
        ratingScoreForProduct = "";
    }

    @PostConstruct
    public void postConstruct() {
        try {
        setProductEntityFullDetailsToView((ProductEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ProductToView"));
        System.out.println(getProductEntityFullDetailsToView());

           setRatingScoreForProduct(String.format("%.2f out of 5", ratingEntitySessionBeanLocal.retrieveRatingScore(getProductEntityFullDetailsToView().getProductId())));
        } catch (ProductNotFoundException ex) {
            Logger.getLogger(ViewProductInNewPageManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void back(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("productManagement.xhtml");
    }


    public String getRatingScoreForProduct() {
        return ratingScoreForProduct;
    }

    public void setRatingScoreForProduct(String ratingScoreForProduct) {
        this.ratingScoreForProduct = ratingScoreForProduct;
    }

    public ProductEntity getProductEntityFullDetailsToView() {
        return productEntityFullDetailsToView;
    }

    public void setProductEntityFullDetailsToView(ProductEntity productEntityFullDetailsToView) {
        this.productEntityFullDetailsToView = productEntityFullDetailsToView;
    }

}
