/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.RewardEntity;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;

/**
 *
 * @author wong
 */
@Named(value = "viewRewardManagedBean")
@ViewScoped
public class ViewRewardManagedBean implements Serializable {

    private RewardEntity rewardEntityToView;
    
    public ViewRewardManagedBean() {
        rewardEntityToView = new RewardEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
    }
    
    public RewardEntity getRewardEntityToView() {
        return rewardEntityToView;
    }

    public void setRewardEntityToView(RewardEntity rewardEntityToView) {
        this.rewardEntityToView = rewardEntityToView;
    }
    
}
