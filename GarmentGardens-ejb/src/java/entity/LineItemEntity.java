/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class LineItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineItemId;
    private Integer serialNumber;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ProductEntity product;

    public LineItemEntity() {
        this.quantity = 0;
        this.unitPrice = new BigDecimal(0);
        this.subTotal = new BigDecimal(0);
    }

    public LineItemEntity(Integer quantity, BigDecimal unitPrice) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public LineItemEntity(Integer quantity, BigDecimal unitPrice, BigDecimal subTotal) {
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lineItemId != null ? lineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the lineItemId fields are not set
        if (!(object instanceof LineItemEntity)) {
            return false;
        }
        LineItemEntity other = (LineItemEntity) object;
        if ((this.lineItemId == null && other.lineItemId != null) || (this.lineItemId != null && !this.lineItemId.equals(other.lineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LineItemEntity[ id=" + lineItemId + " ]";
    }

    /**
     * @return the serialNumber
     */
    public Integer getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the subTotal
     */
    public BigDecimal getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    /**
     * @return the product
     */
    public ProductEntity getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(ProductEntity product) {
        this.product = product;
    }

}
