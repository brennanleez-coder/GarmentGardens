package jsf.managedbean;

import ejb.session.stateless.ProductEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import entity.ProductEntity;
import entity.TagEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;



@Named(value = "filterProductsByTagsManagedBean")
@ViewScoped

public class FilterProductsByTagsManagedBean implements Serializable
{
    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    @EJB
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;
    
    @Inject
    private ViewProductManagedBean viewProductManagedBean;
    
    private String condition;
    private List<Long> selectedTagIds;
    private List<SelectItem> selectItems;
    private List<ProductEntity> productEntities;
    
    
    
    public FilterProductsByTagsManagedBean()
    {
        condition = "OR";
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        List<TagEntity> tagEntities = tagEntitySessionBeanLocal.retrieveAllTags();
        selectItems = new ArrayList<>();
        
        for(TagEntity tagEntity:tagEntities)
        {
            selectItems.add(new SelectItem(tagEntity.getTagId(), tagEntity.getName(), tagEntity.getName()));
        }
        
        
        // Optional demonstration of the use of custom converter
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter_tagEntities", tagEntities);
        
        condition = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productFilterCondition");        
        selectedTagIds = (List<Long>)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productFilterTags");
        
        filterProduct();
    }
    
    
    
    @PreDestroy
    public void preDestroy()
    {
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter_tagEntities", null);
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("TagEntityConverter_tagEntities", null);
    }
    
    
    
    public void filterProduct()
    {        
        if(selectedTagIds != null && selectedTagIds.size() > 0)
        {
            productEntities = productEntitySessionBeanLocal.filterProductsByTags(selectedTagIds, condition);
        }
        else
        {
            productEntities = productEntitySessionBeanLocal.retrieveAllProducts();
        }
    }
    
    
    
    public void viewProductDetails(ActionEvent event) throws IOException
    {
        Long productIdToView = (Long)event.getComponent().getAttributes().get("productId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterProductsByTags");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }

    
    
    public ViewProductManagedBean getViewProductManagedBean() {
        return viewProductManagedBean;
    }

    public void setViewProductManagedBean(ViewProductManagedBean viewProductManagedBean) {
        this.viewProductManagedBean = viewProductManagedBean;
    }
    
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) 
    {
        this.condition = condition;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productFilterCondition", condition);
    }
    
    public List<Long> getSelectedTagIds() {
        return selectedTagIds;
    }

    public void setSelectedTagIds(List<Long> selectedTagIds) 
    {
        this.selectedTagIds = selectedTagIds;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productFilterTags", selectedTagIds);
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }    

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
}
