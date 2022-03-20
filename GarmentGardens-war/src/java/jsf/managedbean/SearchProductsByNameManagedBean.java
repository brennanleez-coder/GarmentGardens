/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ProductEntitySessionBeanLocal;
import entity.ProductEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author brennanlee
 */
@Named(value = "searchProductsByNameManagedBean")
@ViewScoped
public class SearchProductsByNameManagedBean implements Serializable {

    /**
     * Creates a new instance of SearchProductsByNameManagedBean
     */
    @EJB
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;

    @Inject
    private ViewProductManagedBean viewProductManagedBean;

    private String searchString;
    private List<ProductEntity> productEntities;

    public SearchProductsByNameManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        searchString = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productSearchString");
        if (searchString == null || searchString.trim().length() == 0) {
            productEntities = productEntitySessionBeanLocal.retrieveAllProducts();
        } else {
            productEntities = productEntitySessionBeanLocal.searchProductsByName(searchString);
        }
    }

    public void searchProduct() {
        if (searchString == null || searchString.trim().length() == 0) {
            productEntities = productEntitySessionBeanLocal.retrieveAllProducts();
        } else {
            productEntities = productEntitySessionBeanLocal.searchProductsByName(searchString);
        }
    }

    public ViewProductManagedBean getViewProductManagedBean() {
        return viewProductManagedBean;
    }

    public void setViewProductManagedBean(ViewProductManagedBean viewProductManagedBean) {
        this.viewProductManagedBean = viewProductManagedBean;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productSearchString", searchString);
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
}
