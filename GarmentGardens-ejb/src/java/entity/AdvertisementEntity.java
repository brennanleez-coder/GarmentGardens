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

/**
 *
 * @author rilwa
 */
@Entity
public class AdvertisementEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long advertisementId;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private AdvertiserEntity advertiser;

    @ManyToOne(fetch = FetchType.LAZY)
    private StaffEntity approvedByStaff;

    public AdvertisementEntity() {
    }

    public AdvertisementEntity(String description) {
        this.description = description;
    }

    
    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertismentId) {
        this.advertisementId = advertismentId;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash += (advertisementId != null ? advertisementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the advertismentId fields are not set
        if (!(object instanceof AdvertisementEntity)) {
            return false;
        }
        AdvertisementEntity other = (AdvertisementEntity) object;
        if ((this.advertisementId == null && other.advertisementId != null) || (this.advertisementId != null && !this.advertisementId.equals(other.advertisementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AdvertismentEntity[ id=" + advertisementId + " ]";
    }

    public AdvertiserEntity getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(AdvertiserEntity advertiser) {
        this.advertiser = advertiser;
    }

    public StaffEntity getApprovedByStaff() {
        return approvedByStaff;
    }

    public void setApprovedByStaff(StaffEntity approvedByStaff) {
        this.approvedByStaff = approvedByStaff;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
