/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author wong
 */
@Entity
public class RatingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;
    private String description;
    private Integer numberOfStars;
    private Date dateOfRating;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private UserEntity customer;

    public RatingEntity() {
    }

    public RatingEntity(String description, Integer numberOfStars, Date dateOfRating) {
        this.description = description;
        this.numberOfStars = numberOfStars;
        this.dateOfRating = dateOfRating;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ratingId != null ? ratingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the ratingId fields are not set
        if (!(object instanceof RatingEntity)) {
            return false;
        }
        RatingEntity other = (RatingEntity) object;
        if ((this.ratingId == null && other.ratingId != null) || (this.ratingId != null && !this.ratingId.equals(other.ratingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RatingEntity[ id=" + ratingId + " ]";
    }
    
    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(Integer numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public Date getDateOfRating() {
        return dateOfRating;
    }

    public void setDateOfRating(Date dateOfRating) {
        this.dateOfRating = dateOfRating;
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
