/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AdvertiserEntitySessionBeanLocal;
import entity.AdvertiserEntity;
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
import util.exception.AdvertiserEntityNotFoundException;
import util.exception.DeleteAdvertiserEntityException;

/**
 *
 * @author ryyant
 */
@Named(value = "advertiserManagementManagedBean")
@ViewScoped
public class AdvertiserManagementManagedBean implements Serializable {

    @EJB(name = "AdvertiserEntitySessionBeanLocal")
    private AdvertiserEntitySessionBeanLocal advertiserEntitySessionBeanLocal;

    private List<AdvertiserEntity> advertiserEntities;

    private List<AdvertiserEntity> filteredAdvertiserEntities;

    private AdvertiserEntity selectedAdvertiserEntityToUpdate;

    @Inject
    private ViewAdvertiserManagedBean viewAdvertiserManagedBean;

    public AdvertiserManagementManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        setAdvertiserEntities(advertiserEntitySessionBeanLocal.retrieveAllAdvertiserEntity());
    }

    public void doUpdateAdvertiser(ActionEvent event) {
        setSelectedAdvertiserEntityToUpdate((AdvertiserEntity) event.getComponent().getAttributes().get("advertiserEntityToUpdate"));

//        categoryIdUpdate = selectedProductEntityToUpdate.getCategory().getCategoryId();
//        tagIdsUpdate = new ArrayList<>();
//        for (TagEntity tagEntity : selectedProductEntityToUpdate.getTags()) {
//            tagIdsUpdate.add(tagEntity.getTagId());
//        }
    }

    public void updateAdvertiser(ActionEvent event) {
        try {
            advertiserEntitySessionBeanLocal.updateAdvertiserEntity(getSelectedAdvertiserEntityToUpdate());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Advertiser updated successfully", null));
        } catch (AdvertiserEntityNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Advertiser: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteAdvertiser(ActionEvent event) {
        try {
            AdvertiserEntity advertiserEntityToDelete = (AdvertiserEntity) event.getComponent().getAttributes().get("advertiserEntityToDelete");
            advertiserEntitySessionBeanLocal.deleteAdvertiserEntity(advertiserEntityToDelete);

            getAdvertiserEntities().remove(advertiserEntityToDelete);

            if (getFilteredAdvertiserEntities() != null) {
                getFilteredAdvertiserEntities().remove(advertiserEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Advertiser deleted successfully", null));
        } catch (AdvertiserEntityNotFoundException | DeleteAdvertiserEntityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting advertiser: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<AdvertiserEntity> getAdvertiserEntities() {
        return advertiserEntities;
    }

    public void setAdvertiserEntities(List<AdvertiserEntity> advertiserEntities) {
        this.advertiserEntities = advertiserEntities;
    }

    public List<AdvertiserEntity> getFilteredAdvertiserEntities() {
        return filteredAdvertiserEntities;
    }

    public void setFilteredAdvertiserEntities(List<AdvertiserEntity> filteredAdvertiserEntities) {
        this.filteredAdvertiserEntities = filteredAdvertiserEntities;
    }

    public AdvertiserEntity getSelectedAdvertiserEntityToUpdate() {
        return selectedAdvertiserEntityToUpdate;
    }

    public void setSelectedAdvertiserEntityToUpdate(AdvertiserEntity selectedAdvertiserEntityToUpdate) {
        this.selectedAdvertiserEntityToUpdate = selectedAdvertiserEntityToUpdate;
    }

    public ViewAdvertiserManagedBean getViewAdvertiserManagedBean() {
        return viewAdvertiserManagedBean;
    }

    public void setViewAdvertiserManagedBean(ViewAdvertiserManagedBean viewAdvertiserManagedBean) {
        this.viewAdvertiserManagedBean = viewAdvertiserManagedBean;
    }


}
