/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ProductEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

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
    private ProductEntity productEntityToView;

    public ViewProductManagedBean() {
        productEntityToView = new ProductEntity();
    }

    @PostConstruct
    public void postConstruct() {
    }

    public ProductEntity getProductEntityToView() {
        return productEntityToView;
    }

    public void setProductEntityToView(ProductEntity productEntityToView) {
        this.productEntityToView = productEntityToView;
    }

}
