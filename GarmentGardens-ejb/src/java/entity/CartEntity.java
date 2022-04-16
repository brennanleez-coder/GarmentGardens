/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.CartTypeEnum;

/**
 *
 * @author rilwa
 */
@Entity
public class CartEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    private Integer totalCartItems;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private CartTypeEnum cartType;
    
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity customer;
    
    @OneToMany(mappedBy = "groupCart")
    private List<UserEntity> groupCustomers;
    
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity groupOwner;
    
    @OneToMany(fetch = FetchType.LAZY)
    private List<LineItemEntity> cartLineItems;

    public CartEntity() {
        this.groupCustomers = new ArrayList<UserEntity>();
        this.cartLineItems = new ArrayList<LineItemEntity>();
        this.totalCartItems = 0;
        this.totalQuantity = 0;
        this.totalAmount = new BigDecimal(0);
    }
    
    public CartEntity(Integer totalCartItems, Integer totalQuantity, BigDecimal totalAmount, CartTypeEnum cartType) {
        this();        
        this.cartType = cartType;
        this.totalCartItems = totalCartItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }
    
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cartId != null ? cartId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cartId fields are not set
        if (!(object instanceof CartEntity)) {
            return false;
        }
        CartEntity other = (CartEntity) object;
        if ((this.cartId == null && other.cartId != null) || (this.cartId != null && !this.cartId.equals(other.cartId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CartEntity[ id=" + cartId + " ]";
    }

    /**
     * @return the totalCartItems
     */
    public Integer getTotalCartItems() {
        return totalCartItems;
    }

    /**
     * @param totalCartItems the totalCartItems to set
     */
    public void setTotalCartItems(Integer totalCartItems) {
        this.totalCartItems = totalCartItems;
    }

    /**
     * @return the totalQuantity
     */
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * @param totalQuantity the totalQuantity to set
     */
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    /**
     * @return the groupCustomers
     */
    public List<UserEntity> getGroupCustomers() {
        return groupCustomers;
    }

    /**
     * @param groupCustomers the groupCustomers to set
     */
    public void setGroupCustomers(List<UserEntity> groupCustomers) {
        this.groupCustomers = groupCustomers;
    }

    /**
     * @return the groupOwner
     */
    public UserEntity getGroupOwner() {
        return groupOwner;
    }

    /**
     * @param groupOwner the groupOwner to set
     */
    public void setGroupOwner(UserEntity groupOwner) {
        this.groupOwner = groupOwner;
    }

    /**
     * @return the cartLineItems
     */
    public List<LineItemEntity> getCartLineItems() {
        return cartLineItems;
    }

    /**
     * @param cartLineItems the cartLineItems to set
     */
    public void setCartLineItems(List<LineItemEntity> cartLineItems) {
        this.cartLineItems = cartLineItems;
    }

    public CartTypeEnum getCartType() {
        return cartType;
    }

    public void setCartType(CartTypeEnum cartType) {
        this.cartType = cartType;
    }

}
