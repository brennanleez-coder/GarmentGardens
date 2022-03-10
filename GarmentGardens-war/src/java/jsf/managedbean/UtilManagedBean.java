/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author brennanlee
 */
@Named(value = "utilManagedBean")
@RequestScoped
public class UtilManagedBean {

    /**
     * Creates a new instance of UtilManagedBean
     */
    public UtilManagedBean() {

    }

    public String formatMotdHeader(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
        return simpleDateFormat.format(date);
    }

}
