/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import entity.MessageOfTheDayEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.InputDataValidationException;
import util.exception.UpdateMessageOfTheDayException;

/**
 *
 * @author brennanlee
 */
@Named(value = "motdManagementManagedBean")
@ViewScoped
public class MotdManagementManagedBean implements Serializable {

    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    private MessageOfTheDayEntity newMessageOfTheDay;

    private MessageOfTheDayEntity motdToUpdate;
    private List<MessageOfTheDayEntity> motdEntities;
    private List<MessageOfTheDayEntity> filteredMotdEntities;

    /**
     * Creates a new instance of MotdManagement
     */
    public MotdManagementManagedBean() {
        this.newMessageOfTheDay = new MessageOfTheDayEntity();
    }

    @PostConstruct
    public void postConstruct() {
        setMotdEntities(messageOfTheDayEntitySessionBeanLocal.retrieveAllMessagesOfTheDay());
    }

    public void createNewMotd(ActionEvent event) {
        try {
            MessageOfTheDayEntity m = messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(newMessageOfTheDay);
            getMotdEntities().add(m);

            if (getFilteredMotdEntities() != null) {
                getFilteredMotdEntities().add(m);
            }

            setNewMessageOfTheDay(new MessageOfTheDayEntity());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New MOTD created successfully (Message ID: " + newMessageOfTheDay.getMotdId() + ")", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Message Of The Day: " + ex.getMessage(), null));
        }
    }

    public void doUpdateMotd(ActionEvent event) {
        motdToUpdate = (MessageOfTheDayEntity) event.getComponent().getAttributes().get("motdEntityToUpdate");

    }

    public void updateProduct(ActionEvent event) {

        try {
            messageOfTheDayEntitySessionBeanLocal.updateMessageOfTheDay(motdToUpdate);


            motdToUpdate = new MessageOfTheDayEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message Of The Day updated successfully", null));
        } catch (UpdateMessageOfTheDayException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Message Of The Day: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteMotd(ActionEvent event) {
        try {
            MessageOfTheDayEntity motdEntityToDelete = (MessageOfTheDayEntity) event.getComponent().getAttributes().get("motdEntityToDelete");
            messageOfTheDayEntitySessionBeanLocal.deleteMessageOfTheDay(motdEntityToDelete);

            getMotdEntities().remove(motdEntityToDelete);

            if (getFilteredMotdEntities() != null) {
                getFilteredMotdEntities().remove(motdEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message deleted successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

    }

    public MessageOfTheDayEntity getNewMessageOfTheDay() {
        return newMessageOfTheDay;
    }

    public void setNewMessageOfTheDay(MessageOfTheDayEntity newMessageOfTheDay) {
        this.newMessageOfTheDay = newMessageOfTheDay;
    }

    public List<MessageOfTheDayEntity> getMotdEntities() {
        return motdEntities;
    }

    public void setMotdEntities(List<MessageOfTheDayEntity> motdEntities) {
        this.motdEntities = motdEntities;
    }

    public List<MessageOfTheDayEntity> getFilteredMotdEntities() {
        return filteredMotdEntities;
    }

    public void setFilteredMotdEntities(List<MessageOfTheDayEntity> filteredMotdEntities) {
        this.filteredMotdEntities = filteredMotdEntities;
    }

    public MessageOfTheDayEntity getMotdToUpdate() {
        return motdToUpdate;
    }

    public void setMotdToUpdate(MessageOfTheDayEntity motdToUpdate) {
        this.motdToUpdate = motdToUpdate;
    }

}
