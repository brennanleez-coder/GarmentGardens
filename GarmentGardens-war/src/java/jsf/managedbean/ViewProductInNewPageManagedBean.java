/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author brennanlee
 */
@Named(value = "viewProductInNewPageManagedBean")
@ViewScoped
public class ViewProductInNewPageManagedBean implements Serializable{

    /**
     * Creates a new instance of ViewProductInNewPageManagedBean
     */
    @Inject
    private RatingManagedBean ratingManagedBean;
    
    private ProductEntity productEntityFullDetailsToView;
    
    
    public ViewProductInNewPageManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        productEntityFullDetailsToView = (ProductEntity) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("ProductToView");
        
    }
    
    public void back(ActionEvent event) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("productManagement.xhtml");
    }
    
    
    

    public ProductEntity getProductEntityFullsDetailsToView() {
        return productEntityFullDetailsToView;
    }

    public void setProductEntityFullDetailsToView(ProductEntity productEntityFulLDetailsToView) {
        this.productEntityFullDetailsToView = productEntityFulLDetailsToView;
    }
    
}
