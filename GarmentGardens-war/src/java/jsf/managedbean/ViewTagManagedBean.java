/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.TagEntity;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wong
 */
@Named(value = "viewTagManagedBean")
@ViewScoped
public class ViewTagManagedBean implements Serializable {

    private TagEntity tagEntityToView;
    
    public ViewTagManagedBean() {
        tagEntityToView = new TagEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
    }

    public TagEntity getTagEntityToView() {
        return tagEntityToView;
    }

    public void setTagEntityToView(TagEntity tagEntityToView) {
        this.tagEntityToView = tagEntityToView;
    }
    
}
