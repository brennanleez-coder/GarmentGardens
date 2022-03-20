/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import util.enumeration.AccessRightEnum;
import util.security.CryptographicHelper;

/**
 *
 * @author wong
 */
@Entity
public class StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
    private String firstName;
    private String lastName;
    private AccessRightEnum accessRightEnum;
    private String username;
    private String password;
    
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    
    @OneToMany(mappedBy="staff", fetch = FetchType.LAZY)
    private List<RewardEntity> createdRewards;
            
    @OneToMany(mappedBy = "staff",fetch = FetchType.LAZY )
    private List<DisputeEntity> disputes;

    public StaffEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.disputes = new ArrayList<DisputeEntity>();
    }

    public StaffEntity(String firstName, String lastName, AccessRightEnum accessRightEnum, String username, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRightEnum = accessRightEnum;
        this.username = username;
        //this.password = password;
        setPassword(password);
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the staffId fields are not set
        if (!(object instanceof StaffEntity)) {
            return false;
        }
        StaffEntity other = (StaffEntity) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StaffEntity[ id=" + staffId + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AccessRightEnum getAccessRightEnum() {
        return accessRightEnum;
    }

    public void setAccessRightEnum(AccessRightEnum accessRightEnum) {
        this.accessRightEnum = accessRightEnum;
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
        if(password != null)
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        }
        else
        {
            this.password = null;
        }
    }

    /**
     * @return the disputes
     */
    public List<DisputeEntity> getDisputes() {
        return disputes;
    }

    /**
     * @param disputes the disputes to set
     */
    public void setDisputes(List<DisputeEntity> disputes) {
        this.disputes = disputes;
    }
    
    public String getSalt() {
        return salt;
    }

    // Newly added in v4.5
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<RewardEntity> getCreatedRewards() {
        return createdRewards;
    }

    public void setCreatedRewards(List<RewardEntity> createdRewards) {
        this.createdRewards = createdRewards;
    }
    
}
