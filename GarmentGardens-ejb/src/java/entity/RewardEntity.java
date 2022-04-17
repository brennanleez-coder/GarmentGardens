/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import util.enumeration.RewardEnum;

/**
 *
 * @author wong
 */
@Entity
public class RewardEntity implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardId;
    private String rewardName;
    private RewardEnum rewardEnum;
    private Date expiryDate;
    private Integer promoCode;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    //@JoinColumn(nullable = false)
    private StaffEntity staff;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    //@JoinColumn(nullable = false)
    private UserEntity customer;

    public RewardEntity() {
        this.promoCode =  ("PrOmOcOdE" + new Random().nextInt(500)).hashCode();
    }

    public RewardEntity(String rewardName, RewardEnum rewardEnum, Date expiryDate,StaffEntity staff, UserEntity customer) {
        this.rewardName = rewardName;
        this.rewardEnum = rewardEnum;
        this.expiryDate = expiryDate;
        this.staff = staff;
        this.customer = customer;
    }



    public Long getRewardId() {
        return rewardId;
    }
    
    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }


    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rewardId != null ? rewardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rewardId fields are not set
        if (!(object instanceof RewardEntity)) {
            return false;
        }
        RewardEntity other = (RewardEntity) object;
        if ((this.rewardId == null && other.rewardId != null) || (this.rewardId != null && !this.rewardId.equals(other.rewardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RewardEntity[ id=" + rewardId + " ]";
    }

    public RewardEnum getRewardEnum() {
        return rewardEnum;
    }

    public void setRewardEnum(RewardEnum rewardEnum) {
        this.rewardEnum = rewardEnum;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return the staff
     */
    public StaffEntity getStaff() {
        return staff;
    }

    /**
     * @param staff the staff to set
     */
    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    /**
     * @return the customer
     */
    public UserEntity getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public int getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(Integer promoCode) {
        this.promoCode = promoCode;
    }
    
}
