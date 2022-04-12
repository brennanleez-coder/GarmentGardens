/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.RewardEntitySessionBeanLocal;
import entity.RewardEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.exception.CreateNewRewardException;
import util.exception.DeleteRewardException;
import util.exception.InputDataValidationException;
import util.exception.RewardNotFoundException;

/**
 *
 * @author wong
 */
@Named(value = "rewardManagementManagedBean")
@ViewScoped
public class RewardManagementManagedBean implements Serializable {

    @EJB(name = "RewardEntitySessionBeanLocal")
    private RewardEntitySessionBeanLocal rewardEntitySessionBeanLocal;

    private RewardEntity newRewardEntity;

    private RewardEntity selectedRewardEntityToUpdate;

    private List<RewardEntity> rewardEntities;
    private List<RewardEntity> filteredRewardEntities;

    @Inject
    private ViewRewardManagedBean viewRewardManagedBean;

    public RewardManagementManagedBean() {
        newRewardEntity = new RewardEntity();
    }

    @PostConstruct
    public void postConstruct() {
        setRewardEntities(rewardEntitySessionBeanLocal.retrieveAllRewards());

    }

    public void viewRewardDetails(ActionEvent event) throws IOException {
        Long rewardIdToView = (Long) event.getComponent().getAttributes().get("rewardId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("rewardIdToView", rewardIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewRewardDetails.xhtml");
    }

    public void createNewReward(ActionEvent event) {

        try {
            RewardEntity re = rewardEntitySessionBeanLocal.createNewRewardEntity(newRewardEntity);
            rewardEntities.add(re);

            if (filteredRewardEntities != null) {
                filteredRewardEntities.add(re);
            }

            setNewRewardEntity(new RewardEntity());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New reward created successfully (Reward ID: " + re.getRewardId() + ")", null));
        } catch (InputDataValidationException | CreateNewRewardException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new reward: " + ex.getMessage(), null));
        }
    }

    public void doUpdateReward(ActionEvent event) {
        selectedRewardEntityToUpdate = (RewardEntity) event.getComponent().getAttributes().get("rewardEntityToUpdate");
    }

    public void updateReward(ActionEvent event) {

        try {
            rewardEntitySessionBeanLocal.updateReward(getSelectedRewardEntityToUpdate());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reward updated successfully", null));
        } catch (RewardNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating reward: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteReward(ActionEvent event) {
        try {
            RewardEntity rewardEntityToDelete = (RewardEntity) event.getComponent().getAttributes().get("rewardEntityToDelete");
            rewardEntitySessionBeanLocal.deleteReward(rewardEntityToDelete.getRewardId());

            getRewardEntities().remove(rewardEntityToDelete);

            if (filteredRewardEntities != null) {
                filteredRewardEntities.add(rewardEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reward deleted successfully", null));
        } catch (RewardNotFoundException | DeleteRewardException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting reward: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public RewardEntity getNewRewardEntity() {
        return newRewardEntity;
    }

    public void setNewRewardEntity(RewardEntity newRewardEntity) {
        this.newRewardEntity = newRewardEntity;
    }

    public RewardEntity getSelectedRewardEntityToUpdate() {
        return selectedRewardEntityToUpdate;
    }

    public void setSelectedRewardEntityToUpdate(RewardEntity selectedRewardEntityToUpdate) {
        this.selectedRewardEntityToUpdate = selectedRewardEntityToUpdate;
    }

    public List<RewardEntity> getRewardEntities() {
        return rewardEntities;
    }

    public void setRewardEntities(List<RewardEntity> rewardEntities) {
        this.rewardEntities = rewardEntities;
    }

    public ViewRewardManagedBean getViewRewardManagedBean() {
        return viewRewardManagedBean;
    }

    public void setViewRewardManagedBean(ViewRewardManagedBean viewRewardManagedBean) {
        this.viewRewardManagedBean = viewRewardManagedBean;
    }

    public List<RewardEntity> getFilteredRewardEntities() {
        return filteredRewardEntities;
    }

    public void setFilteredRewardEntities(List<RewardEntity> filteredRewardEntities) {
        this.filteredRewardEntities = filteredRewardEntities;
    }

}
