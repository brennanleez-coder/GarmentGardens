/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.CategoryEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author rilwa
 */
@Named(value = "viewCategoryManagedBean")
@ViewScoped
public class ViewCategoryManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewCategoryManagedBean
     */
    
    private CategoryEntity categoryEntityToView;
    
    
    public ViewCategoryManagedBean() {
        categoryEntityToView = new CategoryEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
    }

    /**
     * @return the categoryEntityToView
     */
    public CategoryEntity getCategoryEntityToView() {
        return categoryEntityToView;
    }

    /**
     * @param categoryEntityToView the categoryEntityToView to set
     */
    public void setCategoryEntityToView(CategoryEntity categoryEntityToView) {
        this.categoryEntityToView = categoryEntityToView;
    }
    
}
