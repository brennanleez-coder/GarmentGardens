/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.StaffEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author wong
 */
@Named(value = "viewStaffManagedBean")
@ViewScoped
public class ViewStaffManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewStaffManagedBean
     */
    private StaffEntity staffEntityToView;
    
    public ViewStaffManagedBean() {
       staffEntityToView = new StaffEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
    }

    public StaffEntity getStaffEntityToView() {
        return staffEntityToView;
    }

    public void setStaffEntityToView(StaffEntity staffEntityToView) {
        this.staffEntityToView = staffEntityToView;
    }
    
}
