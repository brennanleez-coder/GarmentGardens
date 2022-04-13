/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.RewardEntity;
import entity.StaffEntity;
import entity.UserEntity;

/**
 *
 * @author brennanlee
 */
public class UpdateRewardReq {

    private RewardEntity reward;
    private StaffEntity staff;
    private UserEntity customer;

    public UpdateRewardReq() {
        this.reward = new RewardEntity();
        this.staff = new StaffEntity();
        this.customer = new UserEntity();
    }

    public RewardEntity getReward() {
        return reward;
    }

    public void setReward(RewardEntity reward) {
        this.reward = reward;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }
    

}
