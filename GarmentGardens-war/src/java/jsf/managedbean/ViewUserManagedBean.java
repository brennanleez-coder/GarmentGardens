/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.UserEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

@Named(value = "viewUserManagedBean")
@ViewScoped
public class ViewUserManagedBean implements Serializable {

    /**
     * Creates a new instance of ViewStaffManagedBean
     */
    private UserEntity userEntityToView;
    
    public ViewUserManagedBean() {
       userEntityToView = new UserEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
    }

    public UserEntity getUserEntityToView() {
        return userEntityToView;
    }

    public void setUserEntityToView(UserEntity userEntityToView) {
        this.userEntityToView = userEntityToView;
    }
    
}
