package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import entity.CategoryEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.exception.CreateNewCategoryException;
import util.exception.InputDataValidationException;

/**
 *
 * @author rilwa
 */
@Named(value = "categoryManagementManagedBean")
@ViewScoped
public class CategoryManagementManagedBean implements Serializable {

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    private List<CategoryEntity> categoryEntities;
    private List<CategoryEntity> filteredCategoryEntities;

    private List<CategoryEntity> rootCategoryEntities;

    private CategoryEntity newCategoryEntity;
    private Long categoryIdNew;

    private CategoryEntity selectedCategoryEntityToUpdate;
    private Long categoryIdUpdate;

    @Inject
    private ViewCategoryManagedBean viewCategoryManagedBean;

    @Inject
    private FilterCategoriesByCategoryManagedBean filterCategoriesByCategoryManagedBean;

    public CategoryManagementManagedBean() {
        this.newCategoryEntity = new CategoryEntity();
    }

    @PostConstruct
    public void postConstruct() {
        categoryEntities = categoryEntitySessionBeanLocal.retrieveAllLeafCategories();
        rootCategoryEntities = categoryEntitySessionBeanLocal.retrieveAllRootCategories();
    }

//    public void viewCategoryDetails(ActionEvent event) throws IOException {
//        Long categoryIdToView = (Long) event.getComponent().getAttributes().get("categoryId");
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("categoryIdToView", categoryIdToView);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewCategoryDetails.xhtml");
//    }

    public void createNewCategory(ActionEvent event) {

        try {
            CategoryEntity parentCategoryEntity = (CategoryEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("parentCategorySelected");
            
            CategoryEntity ce = categoryEntitySessionBeanLocal.createNewCategoryEntity(newCategoryEntity, parentCategoryEntity);
            categoryEntities.add(ce);

            if (filteredCategoryEntities != null) {
                filteredCategoryEntities.add(ce);
            }

            setNewCategoryEntity(new CategoryEntity());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Category created successfully (Category ID: " + ce.getCategoryId() + ")", null));
        } catch (InputDataValidationException | CreateNewCategoryException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Category: " + ex.getMessage(), null));
        }

        // update category tree
        filterCategoriesByCategoryManagedBean.resetTree();
    }

    // SET THE SELECTED CATEGORY TO UPDATE ATTRIBUTE ON CLICKING ON THE UPDATE BUTTON
    public void doUpdateCategory(ActionEvent event) {
        selectedCategoryEntityToUpdate = ((CategoryEntity) event.getComponent().getAttributes().get("categoryEntityToUpdate"));
        categoryIdUpdate = selectedCategoryEntityToUpdate.getCategoryId();

    }

    // CALL UPDATE ON THE SELECTED CATEGORY TO UPDATE
    public void updateCategory(ActionEvent event) {

        try {
            categoryEntitySessionBeanLocal.updateCategory(selectedCategoryEntityToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category updated successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

        // update category tree
        filterCategoriesByCategoryManagedBean.resetTree();
    }

    public void deleteCategory(ActionEvent event) {
        try {
            CategoryEntity categoryEntityToDelete = (CategoryEntity) event.getComponent().getAttributes().get("categoryEntityToDelete");
            categoryEntitySessionBeanLocal.deleteCategory(categoryEntityToDelete.getCategoryId());
            
            categoryEntities.remove(categoryEntityToDelete);

            if (filteredCategoryEntities != null) {
                filteredCategoryEntities.remove(categoryEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category deleted successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

        // update category tree
        filterCategoriesByCategoryManagedBean.resetTree();
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<CategoryEntity> getFilteredCategoryEntities() {
        return filteredCategoryEntities;
    }

    public void setFilteredCategoryEntities(List<CategoryEntity> filteredCategoryEntities) {
        this.filteredCategoryEntities = filteredCategoryEntities;
    }

    public CategoryEntity getNewCategoryEntity() {
        return newCategoryEntity;
    }

    public void setNewCategoryEntity(CategoryEntity newCategoryEntity) {
        this.newCategoryEntity = newCategoryEntity;
    }

    public Long getCategoryIdNew() {
        return categoryIdNew;
    }

    public void setCategoryIdNew(Long categoryIdNew) {
        this.categoryIdNew = categoryIdNew;
    }

    public ViewCategoryManagedBean getViewCategoryManagedBean() {
        return viewCategoryManagedBean;
    }

    public void setViewCategoryManagedBean(ViewCategoryManagedBean viewCategoryManagedBean) {
        this.viewCategoryManagedBean = viewCategoryManagedBean;
    }

    public CategoryEntity getSelectedCategoryEntityToUpdate() {
        return selectedCategoryEntityToUpdate;
    }

    public void setSelectedCategoryEntityToUpdate(CategoryEntity selectedCategoryEntityToUpdate) {
        this.selectedCategoryEntityToUpdate = selectedCategoryEntityToUpdate;
    }

    public List<CategoryEntity> getRootCategoryEntities() {
        return rootCategoryEntities;
    }

    public void setRootCategoryEntities(List<CategoryEntity> rootCategoryEntities) {
        this.rootCategoryEntities = rootCategoryEntities;
    }

    public Long getCategoryIdUpdate() {
        return categoryIdUpdate;
    }

    public void setCategoryIdUpdate(Long categoryIdUpdate) {
        this.categoryIdUpdate = categoryIdUpdate;
    }

}
