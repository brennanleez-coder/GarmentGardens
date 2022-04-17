/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.DisputeEntitySessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import entity.DisputeEntity;
import entity.OrderEntity;
import entity.StaffEntity;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.enumeration.DisputeStatusEnum;
import util.exception.ApproveDisputeException;
import util.exception.CreateNewDisputeException;
import util.exception.DeleteDisputeException;
import util.exception.DisputeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OrderNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateDisputeException;

/**
 *
 * @author brennanlee
 */
@Named(value = "disputeManagementManagedBean")
@ViewScoped
public class DisputeManagementManagedBean implements Serializable {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @EJB(name = "DisputeEntitySessionBeanLocal")
    private DisputeEntitySessionBeanLocal disputeEntitySessionBeanLocal;

    private DisputeEntity newDisputeEntity;
    private DisputeEntity disputeToApprove;
    private List<DisputeEntity> disputeEntities;
    private List<DisputeEntity> filteredDisputeEntities;
    private List<Long> listOfOrders;
    private Long orderIdToCreateDispute;
    /**
     * Creates a new instance of DisputeManagementManagedBean
     */
    @Inject
    private ViewDisputeManagedBean viewDisputeManagedBean;

    public DisputeManagementManagedBean() {
        newDisputeEntity = new DisputeEntity();
        listOfOrders = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {
        setDisputeEntities(disputeEntitySessionBeanLocal.retrieveAllDisputes());
        List<OrderEntity> listOfOrderEntities = orderEntitySessionBeanLocal.retrieveAllOrders();
        System.out.println(listOfOrderEntities);
        if (listOfOrderEntities.isEmpty()) {
            listOfOrders.add(new Long(-1));
        } else {
            for (OrderEntity order : listOfOrderEntities) {
                System.out.println(order.getOrderId());
                listOfOrders.add(order.getOrderId());
            }
        }

    }

    public void createNewDispute(ActionEvent event) throws CreateNewDisputeException, InputDataValidationException {

        try {
            StaffEntity staff = (StaffEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStaffEntity");
            Long de = disputeEntitySessionBeanLocal.createNewDispute(newDisputeEntity, staff.getStaffId(), orderIdToCreateDispute);
            DisputeEntity re = disputeEntitySessionBeanLocal.retrieveDisputeByDisputeId(de);
            disputeEntities.add(re);

            if (filteredDisputeEntities != null) {
                filteredDisputeEntities.add(re);
            }

            setNewDisputeEntity(new DisputeEntity());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Dispute created successfully (Dispute ID: " + re.getDisputeId() + ")", null));
        } catch (DisputeNotFoundException | OrderNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new dispute: " + ex.getMessage(), null));
        }
    }

    public void approveDispute(ActionEvent event) {
        try {
            setDisputeToApprove((DisputeEntity) event.getComponent().getAttributes().get("disputeEntityToApprove"));
            System.out.println(disputeToApprove);
            StaffEntity staff = (StaffEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStaffEntity");
            System.out.println(staff);

            disputeEntitySessionBeanLocal.approveDispute(disputeToApprove, staff.getStaffId(), disputeToApprove.getOrder().getOrderId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dispute resolved successfully, ID: " + disputeToApprove.getDisputeId(), null));

        } catch (ApproveDisputeException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while resolving dispute: " + getDisputeToApprove().getDisputeId() + " error: " + ex.getMessage(), null));
        } catch (StaffNotFoundException | OrderNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving Staff or Order: " + getDisputeToApprove().getDisputeId() + " error: " + ex.getMessage(), null));

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + getDisputeToApprove().getDisputeId() + " error: " + ex.getMessage(), null));
        }
    }

    public void deleteDispute(ActionEvent event) {
        try {
            DisputeEntity disputeEntityToDelete = (DisputeEntity) event.getComponent().getAttributes().get("disputeEntityToDelete");
            disputeEntitySessionBeanLocal.deleteDispute(disputeEntityToDelete.getDisputeId());

            getDisputeEntities().remove(disputeEntityToDelete);

            if (getFilteredDisputeEntities() != null) {
                getFilteredDisputeEntities().add(disputeEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dispute deleted successfully " + disputeEntityToDelete.getDisputeId(), null));
        } catch (DisputeNotFoundException | DeleteDisputeException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting Dispute: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public DisputeEntity getNewDisputeEntity() {
        return newDisputeEntity;
    }

    public void setNewDisputeEntity(DisputeEntity newDisputeEntity) {
        this.newDisputeEntity = newDisputeEntity;
    }

    public DisputeEntity getDisputeToApprove() {
        return disputeToApprove;
    }

    public void setDisputeToApprove(DisputeEntity disputeToApprove) {
        this.disputeToApprove = disputeToApprove;
    }

    public List<DisputeEntity> getDisputeEntities() {
        return disputeEntities;
    }

    public void setDisputeEntities(List<DisputeEntity> disputeEntities) {
        this.disputeEntities = disputeEntities;
    }

    public List<DisputeEntity> getFilteredDisputeEntities() {
        return filteredDisputeEntities;
    }

    public void setFilteredDisputeEntities(List<DisputeEntity> filteredDisputeEntities) {
        this.filteredDisputeEntities = filteredDisputeEntities;
    }

    public ViewDisputeManagedBean getViewDisputeManagedBean() {
        return viewDisputeManagedBean;
    }

    public void setViewDisputeManagedBean(ViewDisputeManagedBean viewDisputeManagedBean) {
        this.viewDisputeManagedBean = viewDisputeManagedBean;
    }

    public List<Long> getListOfOrders() {
        return listOfOrders;
    }

    public void setListOfOrders(List<Long> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }

    public Long getOrderIdToCreateDispute() {
        return orderIdToCreateDispute;
    }

    public void setOrderIdToCreateDispute(Long orderIdToCreateDispute) {
        this.orderIdToCreateDispute = orderIdToCreateDispute;
    }

}
