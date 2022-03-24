/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import entity.MessageOfTheDayEntity;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.InputDataValidationException;

/**
 *
 * @author brennanlee
 */
@Named(value = "motdManagement")
@ViewScoped
public class MotdManagement implements Serializable {

    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    private MessageOfTheDayEntity newMessageOfTheDay;
    
    /**
     * Creates a new instance of MotdManagement
     */
    public MotdManagement() {
        this.newMessageOfTheDay = new MessageOfTheDayEntity();
    }
    
    public void createNewMotd(ActionEvent event) {
        try {
            messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(getNewMessageOfTheDay());
            
        } catch (InputDataValidationException ex) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Message Of The Day: " + ex.getMessage(), null));
        }
    }

    public MessageOfTheDayEntity getNewMessageOfTheDay() {
        return newMessageOfTheDay;
    }

    public void setNewMessageOfTheDay(MessageOfTheDayEntity newMessageOfTheDay) {
        this.newMessageOfTheDay = newMessageOfTheDay;
    }
    
}
