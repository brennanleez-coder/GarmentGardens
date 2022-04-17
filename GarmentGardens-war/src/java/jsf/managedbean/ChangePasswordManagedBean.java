/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.StaffEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.ChangePasswordException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateStaffException;

/**
 *
 * @author brennanlee
 */
@Named(value = "changePasswordManagedBean")
@ViewScoped
public class ChangePasswordManagedBean implements Serializable{

    @EJB(name = "StaffEntitySessionBeanLocal")
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    private StaffEntity currentStaff;
    private String currentPassword;
    private String newPassword1;
    private String newPassword2;

    /**
     * Creates a new instance of ChangePasswordManagedBean
     */
    public ChangePasswordManagedBean() {
    }
    
    @PostConstruct
    public void postContruct() {
        setCurrentStaff((StaffEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStaffEntity"));
    }

    
    public void changePassword(ActionEvent event) throws IOException {
        System.out.println("password1 new:" + getNewPassword1());
        System.out.println("password1 new:" + getNewPassword2());
        try {
            if (getNewPassword1().equals(getNewPassword2())) {
                staffEntitySessionBeanLocal.staffChangePassword(currentStaff.getUsername(), currentPassword, newPassword2);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password successfully updated", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null));
            }
        } catch (ChangePasswordException | InvalidLoginCredentialException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occured while changing password. Password is not updated", null));
        }
    }
    
    public void foo() {
        System.out.println("this is working");
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public StaffEntity getCurrentStaff() {
        return currentStaff;
    }

    public void setCurrentStaff(StaffEntity currentStaff) {
        this.currentStaff = currentStaff;
    }
}
