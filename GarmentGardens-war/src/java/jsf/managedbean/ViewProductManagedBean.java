/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ProductEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

/**
 *
 * @author brennanlee
 */
@Named(value = "viewProductManagedBean")
@ViewScoped
public class ViewProductManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewProductManagedBean
     */
    
    @Inject
    private ViewProductInNewPageManagedBean viewProductInNewPageManagedBean;
    
    
    private ProductEntity productEntityToView;

    public ViewProductManagedBean() {
        productEntityToView = new ProductEntity();
    }

    @PostConstruct
    public void postConstruct() {
    }
    
    public void viewProductInNewPage(ActionEvent event) throws IOException {
        System.out.println("************ viewProductInNewPage");
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ProductToView", productEntityToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductInNewPage.xhtml"); 
    }

    public ProductEntity getProductEntityToView() {
        return productEntityToView;
    }

    public void setProductEntityToView(ProductEntity productEntityToView) {
        this.productEntityToView = productEntityToView;
    }

    public ViewProductInNewPageManagedBean getViewProductInNewPageManagedBean() {
        return viewProductInNewPageManagedBean;
    }

    public void setViewProductInNewPageManagedBean(ViewProductInNewPageManagedBean viewProductInNewPageManagedBean) {
        this.viewProductInNewPageManagedBean = viewProductInNewPageManagedBean;
    }

}
