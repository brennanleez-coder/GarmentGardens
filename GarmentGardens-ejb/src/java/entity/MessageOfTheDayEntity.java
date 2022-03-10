/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author wong
 */
@Entity
public class MessageOfTheDayEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long motdId;
    private String title;
    private String message;
    private Date messageDate;

    public MessageOfTheDayEntity() {
    }

    public MessageOfTheDayEntity(String title, String message,  Date messageDate) {
        this.title = title;
        this.message = message;
        this.messageDate = messageDate;
    }
    

    public Long getMotdId() {
        return motdId;
    }

    public void setMotdId(Long motdId) {
        this.motdId = motdId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (motdId != null ? motdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the motdId fields are not set
        if (!(object instanceof MessageOfTheDayEntity)) {
            return false;
        }
        MessageOfTheDayEntity other = (MessageOfTheDayEntity) object;
        if ((this.motdId == null && other.motdId != null) || (this.motdId != null && !this.motdId.equals(other.motdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MessageOfTheDayEntity[ id=" + motdId + " ]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
    
}
