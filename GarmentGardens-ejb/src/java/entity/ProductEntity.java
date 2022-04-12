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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author wong
 */
@Entity
public class ProductEntity implements Serializable {

    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String skuCode;
    private String name;
    private String description;
    private Integer quantityOnHand;
    private BigDecimal unitPrice;
    private Boolean isListed;
    private String imageLink;
    
    @ManyToMany(mappedBy = "products")
    private List<TagEntity> tags;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CategoryEntity category;
    
    @OneToMany
    private List<RatingEntity> ratings;
    
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity seller;

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY)
    private List<LineItemEntity> lineItems;
    
    public ProductEntity() {
        this.ratings = new ArrayList<RatingEntity>();
        this.tags = new ArrayList<TagEntity>();
        this.lineItems = new ArrayList<LineItemEntity>();
    }

    public ProductEntity(String skuCode, String name, String description, Integer quantityOnHand, BigDecimal unitPrice, Boolean isListed, String imageLink) {
        this();
        this.skuCode = skuCode;
        this.name = name;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.unitPrice = unitPrice;
        this.isListed = isListed;
        this.imageLink = imageLink;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the productId fields are not set
        if (!(object instanceof ProductEntity)) {
            return false;
        }
        ProductEntity other = (ProductEntity) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductEntity[ id=" + productId + " ]";
    }
    
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getIsListed() {
        return isListed;
    }

    public void setIsListed(Boolean isListed) {
        this.isListed = isListed;
    }

    /**
     * @return the tags
     */
    public List<TagEntity> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    /**
     * @return the category
     */
    public CategoryEntity getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(CategoryEntity category) {
        if(this.category != null)
        {
            if(this.category.getProducts().contains(this))
            {
                this.category.getProducts().remove(this);
            }
        }
        
        this.category = category;
        
        if(this.category != null)
        {
            if(!this.category.getProducts().contains(this))
            {
                this.category.getProducts().add(this);
            }
        }
    }

    /**
     * @return the ratings
     */
    public List<RatingEntity> getRatings() {
        return ratings;
    }

    /**
     * @param ratings the ratings to set
     */
    public void setRatings(List<RatingEntity> ratings) {
        this.ratings = ratings;
    }

    /**
     * @return the seller
     */
    public UserEntity getSeller() {
        return seller;
    }

    /**
     * @param seller the seller to set
     */
    public void setSeller(UserEntity seller) {
        this.seller = seller;
    }
    
    public void addTag(TagEntity tagEntity)
    {
        if(tagEntity != null)
        {
            if(!this.tags.contains(tagEntity))
            {
                this.tags.add(tagEntity);
                
                if(!tagEntity.getProducts().contains(this))
                {                    
                    tagEntity.getProducts().add(this);
                }
            }
        }
    }
    
    
    
    public void removeTag(TagEntity tagEntity)
    {
        if(tagEntity != null)
        {
            if(this.tags.contains(tagEntity))
            {
                this.tags.remove(tagEntity);
                
                if(tagEntity.getProducts().contains(this))
                {
                    tagEntity.getProducts().remove(this);
                }
            }
        }
    }
    
//    public List<String> getImages() {
//        return images;
//    }
//
//    public void setImages(List<String> images) {
//        this.images = images;
//    }

    public List<LineItemEntity> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItemEntity> lineItems) {
        this.lineItems = lineItems;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    
}
