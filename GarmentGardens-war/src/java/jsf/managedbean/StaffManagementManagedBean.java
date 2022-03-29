/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.StaffEntity;
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
import util.enumeration.AccessRightEnum;
import util.exception.CreateNewProductException;
import util.exception.CreateNewStaffException;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.ProductSkuCodeExistException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author wong
 */
@Named(value = "staffManagementManagedBean")
@ViewScoped
public class StaffManagementManagedBean implements Serializable {

    @EJB(name = "StaffEntitySessionBeanLocal")
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    
    private StaffEntity newStaffEntity;
    
    private List<StaffEntity> staffEntities;
    
    private List<StaffEntity> filteredStaffEntities;
    
    private StaffEntity selectedStaffEntityToUpdate;
    
    @Inject
    private ViewStaffManagedBean viewStaffManagedBean;

    /**
     * Creates a new instance of StaffManagementManagedBean
     */
    public StaffManagementManagedBean() {
        newStaffEntity = new StaffEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        staffEntities = staffEntitySessionBeanLocal.retrieveAllStaffs();
    }

//    public void viewProductDetails(ActionEvent event) throws IOException {
//        Long productIdToView = (Long) event.getComponent().getAttributes().get("staffId");
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("staffIdToView", productIdToView);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
//    }

    public void createNewStaff(ActionEvent event) {
//        if (categoryIdNew == 0) {
//            categoryIdNew = null;
//        }

        try {
            newStaffEntity.setAccessRightEnum(AccessRightEnum.MANAGER);
            Long seId = staffEntitySessionBeanLocal.createNewStaff(newStaffEntity);
            StaffEntity se = staffEntitySessionBeanLocal.retrieveStaffByStaffId(seId);
            staffEntities.add(se);

            if (filteredStaffEntities != null) {
                filteredStaffEntities.add(se);
            }

            newStaffEntity = new StaffEntity();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Manager created successfully (Staff ID: " + se.getStaffId() + ")", null));
        } catch (InputDataValidationException | StaffUsernameExistException | StaffNotFoundException | UnknownPersistenceException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new staff: " + ex.getMessage(), null));
        }
    }

    public void doUpdateStaff(ActionEvent event) {
        selectedStaffEntityToUpdate = (StaffEntity) event.getComponent().getAttributes().get("staffEntityToUpdate");

//        categoryIdUpdate = selectedProductEntityToUpdate.getCategory().getCategoryId();
//        tagIdsUpdate = new ArrayList<>();

//        for (TagEntity tagEntity : selectedProductEntityToUpdate.getTags()) {
//            tagIdsUpdate.add(tagEntity.getTagId());
//        }
    }

    public void updateStaff(ActionEvent event) {
//        if (categoryIdUpdate == 0) {
//            categoryIdUpdate = null;
//        }

        try {
            selectedStaffEntityToUpdate.setAccessRightEnum(AccessRightEnum.MANAGER);
            staffEntitySessionBeanLocal.updateStaff(selectedStaffEntityToUpdate);

//            for (CategoryEntity ce : categoryEntities) {
//                if (ce.getCategoryId().equals(categoryIdUpdate)) {
//                    selectedProductEntityToUpdate.setCategory(ce);
//                    break;
//                }
//            }
//
//            selectedProductEntityToUpdate.getTags().clear();
//
//            for (TagEntity te : tagEntities) {
//                if (tagIdsUpdate.contains(te.getTagId())) {
//                    selectedProductEntityToUpdate.getTags().add(te);
//                }
//            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff updated successfully", null));
        } catch (StaffNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating staff: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteStaff(ActionEvent event) {
        try {
            StaffEntity staffEntityToDelete = (StaffEntity) event.getComponent().getAttributes().get("staffEntityToDelete");
            staffEntitySessionBeanLocal.deleteStaff(staffEntityToDelete.getStaffId());

            staffEntities.remove(staffEntityToDelete);

            if (filteredStaffEntities != null) {
                filteredStaffEntities.remove(staffEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff deleted successfully", null));
        } catch (StaffNotFoundException | DeleteStaffException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting staff: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public ViewStaffManagedBean getViewStaffManagedBean() {
        return viewStaffManagedBean;
    }

    public void setViewStaffManagedBean(ViewStaffManagedBean viewStaffManagedBean) {
        this.viewStaffManagedBean = viewStaffManagedBean;
    }

    public List<StaffEntity> getStaffEntities() {
        return staffEntities;
    }

    public void setStaffEntities(List<StaffEntity> productEntities) {
        this.staffEntities = productEntities;
    }

//    public List<StaffEntity> getFilteredStaffEntities() {
//        return filteredStaffEntities;
//    }
//
//    public void setFilteredStaffEntities(List<StaffEntity> filteredStaffEntities) {
//        this.filteredStaffEntities = filteredStaffEntities;
//    }

    public StaffEntity getNewStaffEntity() {
        return newStaffEntity;
    }

    public void setNewStaffEntity(StaffEntity newStaffEntity) {
        this.newStaffEntity = newStaffEntity;
    }

//    public Long getCategoryIdNew() {
//        return categoryIdNew;
//    }
//
//    public void setCategoryIdNew(Long categoryIdNew) {
//        this.categoryIdNew = categoryIdNew;
//    }
//
//    public List<Long> getTagIdsNew() {
//        return tagIdsNew;
//    }
//
//    public void setTagIdsNew(List<Long> tagIdsNew) {
//        this.tagIdsNew = tagIdsNew;
//    }
//
//    public List<CategoryEntity> getCategoryEntities() {
//        return categoryEntities;
//    }
//
//    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
//        this.categoryEntities = categoryEntities;
//    }
//
//    public List<TagEntity> getTagEntities() {
//        return tagEntities;
//    }
//
//    public void setTagEntities(List<TagEntity> tagEntities) {
//        this.tagEntities = tagEntities;
//    }

    public StaffEntity getSelectedStaffEntityToUpdate() {
        return selectedStaffEntityToUpdate;
    }

    public void setSelectedStaffEntityToUpdate(StaffEntity selectedStaffEntityToUpdate) {
        this.selectedStaffEntityToUpdate = selectedStaffEntityToUpdate;
    }

//    public Long getCategoryIdUpdate() {
//        return categoryIdUpdate;
//    }
//
//    public void setCategoryIdUpdate(Long categoryIdUpdate) {
//        this.categoryIdUpdate = categoryIdUpdate;
//    }
//
//    public List<Long> getTagIdsUpdate() {
//        return tagIdsUpdate;
//    }
//
//    public void setTagIdsUpdate(List<Long> tagIdsUpdate) {
//        this.tagIdsUpdate = tagIdsUpdate;
//    }

    public List<StaffEntity> getFilteredStaffEntities() {
        return filteredStaffEntities;
    }

    public void setFilteredStaffEntities(List<StaffEntity> filteredStaffEntities) {
        this.filteredStaffEntities = filteredStaffEntities;
    }
    
}
