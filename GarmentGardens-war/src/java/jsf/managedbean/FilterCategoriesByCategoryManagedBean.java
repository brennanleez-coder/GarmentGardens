package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import entity.CategoryEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import util.exception.CategoryNotFoundException;

@Named(value = "filterCategoriesByCategoryManagedBean")
@ViewScoped
public class FilterCategoriesByCategoryManagedBean implements Serializable {

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @Inject
    private ViewCategoryManagedBean viewCategoryManagedBean;

    private TreeNode treeNode;
    private TreeNode selectedTreeNode;

    private List<CategoryEntity> categoryEntities;

    public FilterCategoriesByCategoryManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        resetTree();
    }

    public void resetTree() {
        List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllRootCategories();
        treeNode = new DefaultTreeNode("Root", null);

        for (CategoryEntity categoryEntity : categoryEntities) {
            createTreeNode(categoryEntity, treeNode);
        }

        Long selectedCategoryId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("categoryFilterCategory");
        if (selectedCategoryId != null) {
            for (TreeNode tn : treeNode.getChildren()) {
                CategoryEntity ce = (CategoryEntity) tn.getData();

                if (ce.getCategoryId().equals(selectedCategoryId)) {
                    selectedTreeNode = tn;
                    break;
                } else {
                    selectedTreeNode = searchTreeNode(selectedCategoryId, tn);
                }
            }
        }

        filterCategory();
    }

    public void resetFilter() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("categoryFilterCategory", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("parentCategorySelected", null);
        selectedTreeNode = null;
        // RESET ENTIRE TREE
        resetTree();
    }

    public void filterCategory() {
        if (selectedTreeNode != null) {
            try {
                CategoryEntity ce = (CategoryEntity) selectedTreeNode.getData();

                categoryEntities = categoryEntitySessionBeanLocal.filterCategoriesByCategory(ce.getCategoryId());
            } catch (CategoryNotFoundException ex) {
                categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();
            }
        } else {
            categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();
        }
    }

    private void createTreeNode(CategoryEntity categoryEntity, TreeNode parentTreeNode) {
        TreeNode treeNode = new DefaultTreeNode(categoryEntity, parentTreeNode);

        for (CategoryEntity ce : categoryEntity.getSubCategories()) {
            createTreeNode(ce, treeNode);
        }
    }

    private TreeNode searchTreeNode(Long selectedCategoryId, TreeNode treeNode) {
        for (TreeNode tn : treeNode.getChildren()) {
            CategoryEntity ce = (CategoryEntity) tn.getData();

            if (ce.getCategoryId().equals(selectedCategoryId)) {
                return tn;
            } else {
                return searchTreeNode(selectedCategoryId, tn);
            }
        }

        return null;
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

    public void setSelectedTreeNode(TreeNode selectedTreeNode) {
        this.selectedTreeNode = selectedTreeNode;

        if (selectedTreeNode != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("categoryFilterCategory", ((CategoryEntity) selectedTreeNode.getData()).getCategoryId());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("parentCategorySelected", (CategoryEntity) selectedTreeNode.getData());
        }

    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }
}
