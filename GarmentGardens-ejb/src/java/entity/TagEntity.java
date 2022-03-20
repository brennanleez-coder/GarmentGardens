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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author wong
 */
@Entity
public class TagEntity implements Serializable {



    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;
    private String name;
    
    @ManyToMany
    private List<ProductEntity> products;
    
    public TagEntity() {
        this.products = new ArrayList<ProductEntity>();
    }

    public TagEntity(String name) {
        this();
        this.name = name;
    }
    
    

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tagId != null ? tagId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the tagId fields are not set
        if (!(object instanceof TagEntity)) {
            return false;
        }
        TagEntity other = (TagEntity) object;
        if ((this.tagId == null && other.tagId != null) || (this.tagId != null && !this.tagId.equals(other.tagId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TagEntity[ id=" + tagId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
        /**
     * @return the products
     */
    public List<ProductEntity> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
    
}
