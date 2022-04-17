/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.RoleEnum;
import util.enumeration.TierEnum;

/**
 *
 * @author rilwa
 */
@Entity
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Date dateOfBirth;
    private String address;
    private RoleEnum role;
    private TierEnum tiering;
    private Integer chlorophyll;
    private BigDecimal wallet;
    //private Integer chlorophyll = RoleEnum.CUSTOMER.equals(role)? new Integer(0): null;
    //private BigDecimal wallet = RoleEnum.SELLER.equals(role)? new BigDecimal(0): null;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CreditCardEntity> creditCards;
    
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<RewardEntity> rewards;
    
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<OrderEntity> orders;
    
    @OneToOne(fetch = FetchType.LAZY, optional = true, mappedBy = "customer")
    private CartEntity individualCart;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private CartEntity groupCart;

    public UserEntity() {
        this.creditCards = new ArrayList<CreditCardEntity>();
        this.rewards = new ArrayList<RewardEntity>();
        this.orders = new ArrayList<OrderEntity>();
        this.tiering = TierEnum.TIER1;
        this.chlorophyll = 0;
        this.wallet = new BigDecimal(0);
    }
    

    public UserEntity(String firstName, String lastName, String email, String username, String password, Date dateOfBirth, String address, RoleEnum role) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.role = role;
        this.chlorophyll = RoleEnum.CUSTOMER.equals(role)? new Integer(0): null;
        this.wallet = RoleEnum.SELLER.equals(role)? new BigDecimal(0): null;
    }
   

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the userId fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UserEntity[ id=" + userId + " ]";
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the role
     */
    public RoleEnum getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(RoleEnum role) {
        this.role = role;
    }

    /**
     * @return the tiering
     */
    public TierEnum getTiering() {
        return tiering;
    }

    /**
     * @param tiering the tiering to set
     */
    public void setTiering(TierEnum tiering) {
        this.tiering = tiering;
    }

    /**
     * @return the chlorophyll
     */
    public Integer getChlorophyll() {
        return chlorophyll;
    }

    /**
     * @param chlorophyll the chlorophyll to set
     */
    public void setChlorophyll(Integer chlorophyll) {
        this.chlorophyll = chlorophyll;
    }

    /**
     * @return the wallet
     */
    public BigDecimal getWallet() {
        return wallet;
    }

    /**
     * @param wallet the wallet to set
     */
    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    /**
     * @return the creditCards
     */
    public List<CreditCardEntity> getCreditCards() {
        return creditCards;
    }

    /**
     * @param creditCards the creditCards to set
     */
    public void setCreditCards(List<CreditCardEntity> creditCards) {
        this.creditCards = creditCards;
    }

    /**
     * @return the rewards
     */
    public List<RewardEntity> getRewards() {
        return rewards;
    }

    /**
     * @param rewards the rewards to set
     */
    public void setRewards(List<RewardEntity> rewards) {
        this.rewards = rewards;
    }

    /**
     * @return the orders
     */
    public List<OrderEntity> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    /**
     * @return the individualCart
     */
    public CartEntity getIndividualCart() {
        return individualCart;
    }

    /**
     * @param individualCart the individualCart to set
     */
    public void setIndividualCart(CartEntity individualCart) {
        this.individualCart = individualCart;
    }

    /**
     * @return the groupCart
     */
    public CartEntity getGroupCart() {
        return groupCart;
    }

    /**
     * @param groupCart the groupCart to set
     */
    public void setGroupCart(CartEntity groupCart) {
        this.groupCart = groupCart;
    }
    
}
