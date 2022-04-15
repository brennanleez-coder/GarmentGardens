/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.DeliveryStatusEnum;

/**
 *
 * @author wong
 */
@Entity
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Integer totalOrderItem;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private LocalDateTime transactionDateTime;
    private Boolean voidRefund = false;
    private DeliveryStatusEnum deliveryStatus = DeliveryStatusEnum.ONGOING;

    @OneToMany //because uni directional
    private List<LineItemEntity> lineItems;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private DisputeEntity dispute;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private UserEntity customer;

    public OrderEntity() {
        this.lineItems = new ArrayList<LineItemEntity>();
        this.transactionDateTime = LocalDateTime.now();
        this.totalOrderItem = 0;
        this.totalQuantity = 0;
        this.totalAmount = new BigDecimal(0);
    }

    public OrderEntity(Integer totalOrderItem, Integer totalQuantity, BigDecimal totalAmount, LocalDateTime transactionDateTime) {
        this();
        this.totalOrderItem = totalOrderItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
    }

    public OrderEntity(Integer totalOrderItem, Integer totalQuantity, BigDecimal totalAmount) {
        this();
        this.totalOrderItem = totalOrderItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = LocalDateTime.now();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderId != null ? orderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the orderId fields are not set
        if (!(object instanceof OrderEntity)) {
            return false;
        }
        OrderEntity other = (OrderEntity) object;
        if ((this.orderId == null && other.orderId != null) || (this.orderId != null && !this.orderId.equals(other.orderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OrderEntity[ id=" + orderId + " ]";
    }

    public Integer getTotalOrderItem() {
        return totalOrderItem;
    }

    public void setTotalOrderItem(Integer totalOrderItem) {
        this.totalOrderItem = totalOrderItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Boolean getVoidRefund() {
        return voidRefund;
    }

    public void setVoidRefund(Boolean voidRefund) {
        this.voidRefund = voidRefund;
    }

    public DeliveryStatusEnum getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatusEnum deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    /**
     * @return the lineItems
     */
    public List<LineItemEntity> getLineItems() {
        return lineItems;
    }

    /**
     * @param lineItems the lineItems to set
     */
    public void setLineItems(List<LineItemEntity> lineItems) {
        this.lineItems = lineItems;
    }

    /**
     * @return the dispute
     */
    public DisputeEntity getDispute() {
        return dispute;
    }

    /**
     * @param dispute the dispute to set
     */
    public void setDispute(DisputeEntity dispute) {
        this.dispute = dispute;
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

}
