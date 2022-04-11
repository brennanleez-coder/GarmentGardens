/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author wong
 */
@Local
public interface CategoryEntitySessionBeanLocal {

    public CategoryEntity createNewCategoryEntity(CategoryEntity newCategoryEntity, CategoryEntity parentCategoryEntity) throws InputDataValidationException, CreateNewCategoryException;

    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;

    public void updateCategory(CategoryEntity categoryEntity) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException;

    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;

    public List<CategoryEntity> retrieveAllCategoriesWithoutProduct();

    public List<CategoryEntity> retrieveAllLeafCategories();

    public List<CategoryEntity> retrieveAllRootCategories();

    public List<CategoryEntity> retrieveAllCategories();

    public List<CategoryEntity> filterCategoriesByCategory(Long categoryId) throws CategoryNotFoundException;

    public List<CategoryEntity> getSubCategories(Long categoryId) throws CategoryNotFoundException;
    
}
