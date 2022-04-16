/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.DisputeEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author brennanlee
 */
@Named(value = "viewDisputeManagedBean")
@ViewScoped
public class ViewDisputeManagedBean implements Serializable {



    /**
     * Creates a new instance of ViewDisputeManagedBean
     */
    private DisputeEntity disputeEntityToView;
    
    public ViewDisputeManagedBean() {
        disputeEntityToView = new DisputeEntity();
    }
        public DisputeEntity getDisputeEntityToView() {
        return disputeEntityToView;
    }

    public void setDisputeEntityToView(DisputeEntity disputeEntityToView) {
        this.disputeEntityToView = disputeEntityToView;
    }
    
}
