/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import util.enumeration.DisputeStatusEnum;

/**
 *
 * @author brennanlee
 */
@Named(value = "utilManagedBean")
@ApplicationScoped
public class UtilManagedBean implements Serializable{

    /**
     * Creates a new instance of UtilManagedBean
     */
    public UtilManagedBean() {

    }

    public String formatMotdHeader(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        return simpleDateFormat.format(date);
    }
    
    public DisputeStatusEnum[] getDisputeStatus() {
        return DisputeStatusEnum.values();
    }

}
