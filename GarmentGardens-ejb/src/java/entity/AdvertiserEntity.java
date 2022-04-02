/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author ryyant
 */
@Entity
public class AdvertiserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advertiserId;
    private String companyName;
    private String username;
    private String password;
    private String email;
    
    @OneToMany(mappedBy= "advertiser", fetch = FetchType.LAZY)
    private List<AdvertisementEntity> advertisements;
    
    @OneToMany(mappedBy= "advertiser", fetch = FetchType.LAZY)
    private List<CreditCardEntity> creditCards;

    public AdvertiserEntity() {
        this.advertisements = new ArrayList<>();
        this.creditCards = new ArrayList<>();
    }

   
    public AdvertiserEntity(String companyName, String username, String password, String email) {
        this();
        this.companyName = companyName;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    

    public Long getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(Long advertiserId) {
        this.advertiserId = advertiserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (advertiserId != null ? advertiserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the advertiserId fields are not set
        if (!(object instanceof AdvertiserEntity)) {
            return false;
        }
        AdvertiserEntity other = (AdvertiserEntity) object;
        if ((this.advertiserId == null && other.advertiserId != null) || (this.advertiserId != null && !this.advertiserId.equals(other.advertiserId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AdvertiserEntity[ id=" + advertiserId + " ]";
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AdvertisementEntity> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<AdvertisementEntity> advertisements) {
        this.advertisements = advertisements;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
