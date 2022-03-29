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

/**
 *
 * @author rilwa
 */
@Named(value = "orderManagementManagedBean")
@ViewScoped
public class OrderManagementManagedBean implements Serializable {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    /**
     * Creates a new instance of OrderManagementManagedBean
     */
    
    private List<OrderEntity> orderEntities;
    private List<OrderEntity> filteredOrderEntities;
    
    public OrderManagementManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        orderEntities = orderEntitySessionBeanLocal.retrieveAllOrders();
    }

    /**
     * @return the orderEntities
     */
    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    /**
     * @param orderEntities the orderEntities to set
     */
    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    /**
     * @return the filteredOrderEntities
     */
    public List<OrderEntity> getFilteredOrderEntities() {
        return filteredOrderEntities;
    }

    /**
     * @param filteredOrderEntities the filteredOrderEntities to set
     */
    public void setFilteredOrderEntities(List<OrderEntity> filteredOrderEntities) {
        this.filteredOrderEntities = filteredOrderEntities;
    }
    
}
