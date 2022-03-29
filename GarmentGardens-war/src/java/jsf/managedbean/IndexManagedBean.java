/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.inject.Inject;

/**
 *
 * @author brennanlee
 */
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean implements Serializable {

    @Inject
    private ReportManagedBean reportManagedBean;
    
    public IndexManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
    }
    
    public void generateReport(ActionEvent event) {
        reportManagedBean.generateReport(event);
    }

    
}
