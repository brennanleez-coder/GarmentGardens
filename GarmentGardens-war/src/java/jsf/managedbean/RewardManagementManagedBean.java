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

    public List<RewardEntity> getFilteredRewardEntities() {
        return filteredRewardEntities;
    }

    public void setFilteredRewardEntities(List<RewardEntity> filteredRewardEntities) {
        this.filteredRewardEntities = filteredRewardEntities;
    }

    public ViewRewardManagedBean getViewRewardManagedBean() {
        return viewRewardManagedBean;
    }

    public void setViewRewardManagedBean(ViewRewardManagedBean viewRewardManagedBean) {
        this.viewRewardManagedBean = viewRewardManagedBean;
    }
    
    public RewardManagementManagedBean() {
        newRewardEntity = new RewardEntity();
    }
    
    
    private Date date2;
    private Date dateDe;
    private Date dateTimeDe;
    private List<Date> multi;
    private List<Date> range;
    private List<Date> invalidDates;
    private List<Integer> invalidDays;
    private Date minDate;
    private Date maxDate;
    private Date minTime;
    private Date maxTime;
    private Date minDateTime;
    private Date maxDateTime;

    @PostConstruct
    public void postConstruct() {
        setRewardEntities(rewardEntitySessionBeanLocal.retrieveAllRewards());
        setInvalidDates(new ArrayList<>());
        Date today = new Date();
        getInvalidDates().add(today);
        long oneDay = 24 * 60 * 60 * 1000;
        for (int i = 0; i < 5; i++) {
            getInvalidDates().add(new Date(getInvalidDates().get(i).getTime() + oneDay));
        }
 
        setInvalidDays(new ArrayList<>());
        getInvalidDays().add(0); /* the first day of week is disabled */
        getInvalidDays().add(3);
 
        setMinDate(new Date(today.getTime() - (365 * oneDay)));
        setMaxDate(new Date(today.getTime() + (365 * oneDay)));
 
        Calendar tmp = Calendar.getInstance();
        tmp.set(Calendar.HOUR_OF_DAY, 9);
        tmp.set(Calendar.MINUTE, 0);
        tmp.set(Calendar.SECOND, 0);
        tmp.set(Calendar.MILLISECOND, 0);
        setMinTime(tmp.getTime());
 
        tmp = Calendar.getInstance();
        tmp.set(Calendar.HOUR_OF_DAY, 17);
        tmp.set(Calendar.MINUTE, 0);
        tmp.set(Calendar.SECOND, 0);
        tmp.set(Calendar.MILLISECOND, 0);
        setMaxTime(tmp.getTime());
 
        setMinDateTime(new Date(today.getTime() - (7 * oneDay)));
        setMaxDateTime(new Date(today.getTime() + (7 * oneDay)));
 
        setDateDe(GregorianCalendar.getInstance().getTime());
        setDateTimeDe(GregorianCalendar.getInstance().getTime());
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

            if (getFilteredRewardEntities() != null) {
                getFilteredRewardEntities().add(re);
            }

            setNewRewardEntity(new RewardEntity());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New reward created successfully (Reward ID: " + re.getRewardId() + ")", null));
        } catch (InputDataValidationException | CreateNewRewardException  ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new reward: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateReward(ActionEvent event) {
        selectedRewardEntityToUpdate = (RewardEntity) event.getComponent().getAttributes().get("rewardEntityToUpdate");
    }
    
    public void updateReward(ActionEvent event) {
        
        try {
            rewardEntitySessionBeanLocal.updateReward(getSelectedRewardEntityToUpdate());

//            for (TagEntity te : tagEntities) {
//                if (tagIdsUpdate.contains(te.getTagId())) {
//                    selectedProductEntityToUpdate.getTags().add(te);
//                }
//            }

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

            if (getFilteredRewardEntities() != null) {
                getFilteredRewardEntities().remove(rewardEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reward deleted successfully", null));
        } catch (RewardNotFoundException | DeleteRewardException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting reward: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDateDe() {
        return dateDe;
    }

    public void setDateDe(Date dateDe) {
        this.dateDe = dateDe;
    }

    public Date getDateTimeDe() {
        return dateTimeDe;
    }

    public void setDateTimeDe(Date dateTimeDe) {
        this.dateTimeDe = dateTimeDe;
    }

    public List<Date> getMulti() {
        return multi;
    }

    public void setMulti(List<Date> multi) {
        this.multi = multi;
    }

    public List<Date> getRange() {
        return range;
    }

    public void setRange(List<Date> range) {
        this.range = range;
    }

    public List<Date> getInvalidDates() {
        return invalidDates;
    }

    public void setInvalidDates(List<Date> invalidDates) {
        this.invalidDates = invalidDates;
    }

    public List<Integer> getInvalidDays() {
        return invalidDays;
    }

    public void setInvalidDays(List<Integer> invalidDays) {
        this.invalidDays = invalidDays;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Date getMinTime() {
        return minTime;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    public Date getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    public Date getMinDateTime() {
        return minDateTime;
    }

    public void setMinDateTime(Date minDateTime) {
        this.minDateTime = minDateTime;
    }

    public Date getMaxDateTime() {
        return maxDateTime;
    }

    public void setMaxDateTime(Date maxDateTime) {
        this.maxDateTime = maxDateTime;
    }
    
    
    
    
    
    
}
