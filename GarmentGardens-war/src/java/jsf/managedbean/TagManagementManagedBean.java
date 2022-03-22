/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.TagEntitySessionBeanLocal;
import entity.TagEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;


/**
 *
 * @author wong
 */
@Named(value = "tagManagementManagedBean")
@ViewScoped
public class TagManagementManagedBean implements Serializable {

    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    
    private TagEntity newTagEntity;
    
    private TagEntity selectedTagEntityToUpdate;
    
    private List<TagEntity> tagEntities;
    private List<TagEntity> filteredTagEntities;
    
    @Inject
    private ViewTagManagedBean viewTagManagedBean;
    
    public TagManagementManagedBean() {
        newTagEntity = new TagEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        setTagEntities(getTagEntitySessionBeanLocal().retrieveAllTags());
    }
    
    public void viewTagDetails(ActionEvent event) throws IOException {
        Long tagIdToView = (Long) event.getComponent().getAttributes().get("tagId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("tagIdToView", tagIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewTagDetails.xhtml");
    }
    
    public void createNewTag(ActionEvent event) {

        try {
            TagEntity te = getTagEntitySessionBeanLocal().createNewTagEntity(getNewTagEntity());
            getTagEntities().add(te);

            if (getFilteredTagEntities() != null) {
                getFilteredTagEntities().add(te);
            }

            setNewTagEntity(new TagEntity());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New tag created successfully (Tag ID: " + te.getTagId() + ")", null));
        } catch (InputDataValidationException | CreateNewTagException  ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new product: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdateTag(ActionEvent event) {
        selectedTagEntityToUpdate = (TagEntity) event.getComponent().getAttributes().get("tagEntityToUpdate");

//        categoryIdUpdate = selectedProductEntityToUpdate.getCategory().getCategoryId();
//        tagIdsUpdate = new ArrayList<>();

//        for (TagEntity tagEntity : selectedProductEntityToUpdate.getTags()) {
//            tagIdsUpdate.add(tagEntity.getTagId());
//        }
    }
    
    public void updateTag(ActionEvent event) {
        
        try {
            getTagEntitySessionBeanLocal().updateTag(getSelectedTagEntityToUpdate());

//            for (TagEntity te : tagEntities) {
//                if (tagIdsUpdate.contains(te.getTagId())) {
//                    selectedProductEntityToUpdate.getTags().add(te);
//                }
//            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tag updated successfully", null));
        } catch (TagNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating tag: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteTag(ActionEvent event) {
        try {
            TagEntity tagEntityToDelete = (TagEntity) event.getComponent().getAttributes().get("tagEntityToDelete");
            getTagEntitySessionBeanLocal().deleteTag(tagEntityToDelete.getTagId());

            getTagEntities().remove(tagEntityToDelete);

            if (getFilteredTagEntities() != null) {
                getFilteredTagEntities().remove(tagEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tag deleted successfully", null));
        } catch (TagNotFoundException | DeleteTagException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting tag: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public TagEntitySessionBeanLocal getTagEntitySessionBeanLocal() {
        return tagEntitySessionBeanLocal;
    }

    public void setTagEntitySessionBeanLocal(TagEntitySessionBeanLocal tagEntitySessionBeanLocal) {
        this.tagEntitySessionBeanLocal = tagEntitySessionBeanLocal;
    }

    public TagEntity getNewTagEntity() {
        return newTagEntity;
    }

    public void setNewTagEntity(TagEntity newTagEntity) {
        this.newTagEntity = newTagEntity;
    }

    public TagEntity getSelectedTagEntityToUpdate() {
        return selectedTagEntityToUpdate;
    }

    public void setSelectedTagEntityToUpdate(TagEntity selectedTagEntityToUpdate) {
        this.selectedTagEntityToUpdate = selectedTagEntityToUpdate;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public List<TagEntity> getFilteredTagEntities() {
        return filteredTagEntities;
    }

    public void setFilteredTagEntities(List<TagEntity> filteredTagEntities) {
        this.filteredTagEntities = filteredTagEntities;
    }

    public ViewTagManagedBean getViewTagManagedBean() {
        return viewTagManagedBean;
    }

    public void setViewTagManagedBean(ViewTagManagedBean viewTagManagedBean) {
        this.viewTagManagedBean = viewTagManagedBean;
    }
}
