/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.UserEntity;
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
import util.exception.DeleteUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author ryyant
 */
@Named(value = "userManagementManagedBean")
@ViewScoped
public class UserManagementManagedBean implements Serializable {

    @EJB(name = "UserEntitySessionBeanLocal")
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    private List<UserEntity> userEntities;

    private List<UserEntity> filteredUserEntities;

    private UserEntity selectedUserEntityToUpdate;

    @Inject
    private ViewUserManagedBean viewUserManagedBean;

    public UserManagementManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        userEntities = userEntitySessionBeanLocal.retrieveAllUsers();
    }

    public void doUpdateUser(ActionEvent event) {
        selectedUserEntityToUpdate = (UserEntity) event.getComponent().getAttributes().get("userEntityToUpdate");

//        categoryIdUpdate = selectedProductEntityToUpdate.getCategory().getCategoryId();
//        tagIdsUpdate = new ArrayList<>();
//        for (TagEntity tagEntity : selectedProductEntityToUpdate.getTags()) {
//            tagIdsUpdate.add(tagEntity.getTagId());
//        }
    }

    public void updateUser(ActionEvent event) {
//        if (categoryIdUpdate == 0) {
//            categoryIdUpdate = null;
//        }
        try {
            userEntitySessionBeanLocal.updateUser(selectedUserEntityToUpdate);

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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User updated successfully", null));
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating user: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteUser(ActionEvent event) {
        try {
            UserEntity userEntityToDelete = (UserEntity) event.getComponent().getAttributes().get("userEntityToDelete");
            userEntitySessionBeanLocal.deleteUser(userEntityToDelete.getUserId());

            userEntities.remove(userEntityToDelete);

            if (filteredUserEntities != null) {
                filteredUserEntities.remove(userEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User deleted successfully", null));
        } catch (UserNotFoundException | DeleteUserException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting user: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public ViewUserManagedBean getViewUserManagedBean() {
        return viewUserManagedBean;
    }

    public void setViewUserManagedBean(ViewUserManagedBean viewUserManagedBean) {
        this.viewUserManagedBean = viewUserManagedBean;
    }

    public List<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

//    public List<StaffEntity> getFilteredStaffEntities() {
//        return filteredStaffEntities;
//    }
//
//    public void setFilteredStaffEntities(List<StaffEntity> filteredStaffEntities) {
//        this.filteredStaffEntities = filteredStaffEntities;
//    }
//
    public UserEntity getSelectedUserEntityToUpdate() {
        return selectedUserEntityToUpdate;
    }

    public void setSelectedUserEntityToUpdate(UserEntity selectedUserEntityToUpdate) {
        this.selectedUserEntityToUpdate = selectedUserEntityToUpdate;
    }

    public List<UserEntity> getFilteredUserEntities() {
        return filteredUserEntities;
    }

    public void setFilteredUserEntities(List<UserEntity> filteredUserEntities) {
        this.filteredUserEntities = filteredUserEntities;
    }

}
