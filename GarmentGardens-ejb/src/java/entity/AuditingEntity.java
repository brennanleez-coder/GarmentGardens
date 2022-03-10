/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author rilwa
 */
@Entity
public class AuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditingId;

    public Long getAuditingId() {
        return auditingId;
    }

    public void setAuditingId(Long auditingId) {
        this.auditingId = auditingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (auditingId != null ? auditingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the auditingId fields are not set
        if (!(object instanceof AuditingEntity)) {
            return false;
        }
        AuditingEntity other = (AuditingEntity) object;
        if ((this.auditingId == null && other.auditingId != null) || (this.auditingId != null && !this.auditingId.equals(other.auditingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AuditingEntity[ id=" + auditingId + " ]";
    }
    
}
