/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.AdvertiserEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

@Named(value = "viewAdvertiserManagedBean")
@ViewScoped
public class ViewAdvertiserManagedBean implements Serializable {

    private AdvertiserEntity advertiserEntityToView;
    
    public ViewAdvertiserManagedBean() {
       advertiserEntityToView = new AdvertiserEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
    }

    public AdvertiserEntity getAdvertiserEntityToView() {
        return advertiserEntityToView;
    }

    public void setAdvertiserEntityToView(AdvertiserEntity advertiserEntityToView) {
        this.advertiserEntityToView = advertiserEntityToView;
    }
    
}
