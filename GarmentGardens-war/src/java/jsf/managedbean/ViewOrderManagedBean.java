/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.OrderEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wong
 */
@Named(value = "viewOrderManagedBean")
@ViewScoped
public class ViewOrderManagedBean implements Serializable {

    private OrderEntity orderEntityToView;
    
    public ViewOrderManagedBean() {
        orderEntityToView = new OrderEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
    }

    public OrderEntity getOrderEntityToView() {
        return orderEntityToView;
    }

    public void setOrderEntityToView(OrderEntity orderEntityToView) {
        this.orderEntityToView = orderEntityToView;
    }
    
}
