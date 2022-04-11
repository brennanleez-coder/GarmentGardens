/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OrderEntitySessionBeanLocal;
import entity.OrderEntity;
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
import util.exception.OrderAlreadyVoidRefundedException;
import util.exception.OrderNotFoundException;

/**
 *
 * @author rilwa
 */
@Named(value = "orderManagementManagedBean")
@ViewScoped
public class OrderManagementManagedBean implements Serializable {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;
    @Inject
    private ViewOrderManagedBean viewOrderManagedBean;

    private OrderEntity selectedOrderEntityToVoid;

    private List<OrderEntity> orderEntities;
    private List<OrderEntity> filteredOrderEntities;

    public OrderManagementManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("pstConstruct");
        orderEntities = getOrderEntitySessionBeanLocal().retrieveAllOrders();
    }

    public void voidRefundOrder(ActionEvent event) {
        try {
            setSelectedOrderEntityToVoid((OrderEntity) event.getComponent().getAttributes().get("orderEntityToVoid"));
            getOrderEntitySessionBeanLocal().voidRefundOrder(getSelectedOrderEntityToVoid().getOrderId());
            setSelectedOrderEntityToVoid(null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order voided successfully", null));
        } catch (OrderAlreadyVoidRefundedException | OrderNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public List<OrderEntity> getFilteredOrderEntities() {
        return filteredOrderEntities;
    }

    public void setFilteredOrderEntities(List<OrderEntity> filteredOrderEntities) {
        this.filteredOrderEntities = filteredOrderEntities;
    }

    public OrderEntitySessionBeanLocal getOrderEntitySessionBeanLocal() {
        return orderEntitySessionBeanLocal;
    }

    public void setOrderEntitySessionBeanLocal(OrderEntitySessionBeanLocal orderEntitySessionBeanLocal) {
        this.orderEntitySessionBeanLocal = orderEntitySessionBeanLocal;
    }

    public OrderEntity getSelectedOrderEntityToVoid() {
        return selectedOrderEntityToVoid;
    }

    public void setSelectedOrderEntityToVoid(OrderEntity selectedOrderEntityToVoid) {
        this.selectedOrderEntityToVoid = selectedOrderEntityToVoid;
    }

    public ViewOrderManagedBean getViewOrderManagedBean() {
        return viewOrderManagedBean;
    }

    public void setViewOrderManagedBean(ViewOrderManagedBean viewOrderManagedBean) {
        this.viewOrderManagedBean = viewOrderManagedBean;
    }

}
