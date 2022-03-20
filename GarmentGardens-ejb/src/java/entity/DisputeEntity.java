/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.DisputeStatusEnum;

/**
 *
 * @author wong
 */
@Entity
public class DisputeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long disputeId;
    private String title;
    private String description;
    private DisputeStatusEnum disputeStatus = DisputeStatusEnum.PENDING;
    
    @OneToOne(fetch = FetchType.LAZY)
    private OrderEntity order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private StaffEntity staff;

    public DisputeEntity() {
    }

    public DisputeEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(Long disputeId) {
        this.disputeId = disputeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (disputeId != null ? disputeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the disputeId fields are not set
        if (!(object instanceof DisputeEntity)) {
            return false;
        }
        DisputeEntity other = (DisputeEntity) object;
        if ((this.disputeId == null && other.disputeId != null) || (this.disputeId != null && !this.disputeId.equals(other.disputeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DisputeEntity[ id=" + disputeId + " ]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DisputeStatusEnum getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(DisputeStatusEnum disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    /**
     * @return the order
     */
    public OrderEntity getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(OrderEntity order) {
        this.order = order;
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
    
}
