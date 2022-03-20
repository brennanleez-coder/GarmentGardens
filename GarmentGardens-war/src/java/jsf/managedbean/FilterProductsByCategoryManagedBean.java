package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.ProductEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import util.exception.CategoryNotFoundException;



@Named(value = "filterProductsByCategoryManagedBean")
@ViewScoped

public class FilterProductsByCategoryManagedBean implements Serializable
{
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;
    
    @Inject
    private ViewProductManagedBean viewProductManagedBean;
        
    private TreeNode treeNode;
    private TreeNode selectedTreeNode;
    
    private List<ProductEntity> productEntities;
    
    
    
    public FilterProductsByCategoryManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllRootCategories();
        treeNode = new DefaultTreeNode("Root", null);
        
        for(CategoryEntity categoryEntity:categoryEntities)
        {
            createTreeNode(categoryEntity, treeNode);
        }
        
        Long selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productFilterCategory");
        
        if(selectedCategoryId != null)
        {
            for(TreeNode tn:treeNode.getChildren())
            {
                CategoryEntity ce = (CategoryEntity)tn.getData();
                
                if(ce.getCategoryId().equals(selectedCategoryId))
                {
                    selectedTreeNode = tn;
                    break;
                }
                else
                {
                    selectedTreeNode = searchTreeNode(selectedCategoryId, tn);
                }            
            }
        }
        
        filterProduct();
    }
    
    
    
    public void filterProduct()
    {
        if(selectedTreeNode != null)
        {               
            try
            {
                CategoryEntity ce = (CategoryEntity)selectedTreeNode.getData();
                
                productEntities = productEntitySessionBeanLocal.filterProductsByCategory(ce.getCategoryId());
            }
            catch(CategoryNotFoundException ex)
            {
                productEntities = productEntitySessionBeanLocal.retrieveAllProducts();
            }
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
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterProductsByCategory");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }
    
    
    
    private void createTreeNode(CategoryEntity categoryEntity, TreeNode parentTreeNode)
    {
        TreeNode treeNode = new DefaultTreeNode(categoryEntity, parentTreeNode);
                
        for(CategoryEntity ce:categoryEntity.getSubCategories())
        {
            createTreeNode(ce, treeNode);
        }
    }
    
    
    
    private TreeNode searchTreeNode(Long selectedCategoryId, TreeNode treeNode)
    {
        for(TreeNode tn:treeNode.getChildren())
        {
            CategoryEntity ce = (CategoryEntity)tn.getData();
            
            if(ce.getCategoryId().equals(selectedCategoryId))
            {
                return tn;
            }
            else
            {
                return searchTreeNode(selectedCategoryId, tn);
            }            
        }
        
        return null;
    }

    
    
    public ViewProductManagedBean getViewProductManagedBean() {
        return viewProductManagedBean;
    }

    public void setViewProductManagedBean(ViewProductManagedBean viewProductManagedBean) {
        this.viewProductManagedBean = viewProductManagedBean;
    }
    
    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }
    
    public TreeNode getSelectedTreeNode() {
        return selectedTreeNode;
    }

    public void setSelectedTreeNode(TreeNode selectedTreeNode) 
    {
        this.selectedTreeNode = selectedTreeNode;
        
        
        if(selectedTreeNode != null)
        {            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productFilterCategory", ((CategoryEntity)selectedTreeNode.getData()).getCategoryId());
        }
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
}
