/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author wong
 */
@Stateless
public class CategoryEntitySessionBean implements CategoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CategoryEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CategoryEntity createNewCategoryEntity(CategoryEntity newCategoryEntity, CategoryEntity parent) throws InputDataValidationException, CreateNewCategoryException {
        Set<ConstraintViolation<CategoryEntity>> constraintViolations = validator.validate(newCategoryEntity);

        if (constraintViolations.isEmpty()) {
            try {
                if (parent != null) {
                    CategoryEntity parentCategoryEntity = retrieveCategoryByCategoryId(parent.getCategoryId());

                    if (!parentCategoryEntity.getProducts().isEmpty()) {
                        throw new CreateNewCategoryException("Parent category cannot be associated with any product");
                    }

                    // ASSOCIATION
                    parentCategoryEntity.getSubCategories().add(newCategoryEntity);
                    newCategoryEntity.setParentCategory(parentCategoryEntity);
                }

                entityManager.persist(newCategoryEntity);
                entityManager.flush();

                return newCategoryEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewCategoryException("Category with same name already exist");
                } else {
                    throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<CategoryEntity> retrieveAllCategories() {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c ORDER BY c.name ASC");
        List<CategoryEntity> categoryEntities = query.getResultList();

        for (CategoryEntity categoryEntity : categoryEntities) {
            categoryEntity.getParentCategory();
            categoryEntity.getSubCategories().size();
            categoryEntity.getProducts().size();
        }

        return categoryEntities;
    }

    @Override
    public List<CategoryEntity> retrieveAllRootCategories() {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.parentCategory IS NULL ORDER BY c.name ASC");
        List<CategoryEntity> rootCategoryEntities = query.getResultList();

        for (CategoryEntity rootCategoryEntity : rootCategoryEntities) {
            lazilyLoadSubCategories(rootCategoryEntity);
            rootCategoryEntity.getProducts().size();
        }

        return rootCategoryEntities;
    }

    @Override
    public List<CategoryEntity> retrieveAllLeafCategories() {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.subCategories IS EMPTY ORDER BY c.name ASC");
        List<CategoryEntity> leafCategoryEntities = query.getResultList();

        for (CategoryEntity leafCategoryEntity : leafCategoryEntities) {
            leafCategoryEntity.getParentCategory();
            leafCategoryEntity.getProducts().size();
        }

        return leafCategoryEntities;
    }

    @Override
    public List<CategoryEntity> getSubCategories(Long categoryId) throws CategoryNotFoundException {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.parentCategory.categoryId =?1 ORDER BY c.name ASC");
        query.setParameter(1, categoryId);
        List<CategoryEntity> subCategoryEntities = query.getResultList();

        for (CategoryEntity categoryEntity : subCategoryEntities) {
            categoryEntity.getSubCategories().size();
            categoryEntity.getProducts().size();
        }

        if (!subCategoryEntities.isEmpty()) {
            return subCategoryEntities;
        } else {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }
    }

    @Override
    public List<CategoryEntity> retrieveAllCategoriesWithoutProduct() {
        Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.productEntities IS EMPTY ORDER BY c.name ASC");
        List<CategoryEntity> rootCategoryEntities = query.getResultList();

        for (CategoryEntity rootCategoryEntity : rootCategoryEntities) {
            rootCategoryEntity.getParentCategory();
        }

        return rootCategoryEntities;
    }

    @Override
    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = entityManager.find(CategoryEntity.class, categoryId);

        categoryEntity.getParentCategory();
        categoryEntity.getSubCategories().size();
        categoryEntity.getProducts().size();

        if (categoryEntity != null) {
            return categoryEntity;
        } else {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }
    }

    @Override
    public void updateCategory(CategoryEntity categoryEntity) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException {
        Set<ConstraintViolation<CategoryEntity>> constraintViolations = validator.validate(categoryEntity);

        if (constraintViolations.isEmpty()) {
            if (categoryEntity.getCategoryId() != null) {
                CategoryEntity categoryEntityToUpdate = retrieveCategoryByCategoryId(categoryEntity.getCategoryId());

                Query query = entityManager.createQuery("SELECT c FROM CategoryEntity c WHERE c.name = :inName AND c.categoryId <> :inCategoryId");
                query.setParameter("inName", categoryEntity.getName());
                query.setParameter("inCategoryId", categoryEntity.getCategoryId());

                if (!query.getResultList().isEmpty()) {
                    throw new UpdateCategoryException("The name of the category to be updated is duplicated");
                }

                categoryEntityToUpdate.setName(categoryEntity.getName());
                categoryEntityToUpdate.setDescription(categoryEntity.getDescription());

            } else {
                throw new CategoryNotFoundException("Category ID not provided for category to be updated");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException {
        CategoryEntity categoryEntityToRemove = retrieveCategoryByCategoryId(categoryId);

        if (!categoryEntityToRemove.getSubCategories().isEmpty()) {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing sub-categories and cannot be deleted!");
        } else if (!categoryEntityToRemove.getProducts().isEmpty()) {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing products and cannot be deleted!");
        } else {
            CategoryEntity parent = categoryEntityToRemove.getParentCategory();
            if (parent != null) {
                parent.getSubCategories().remove(categoryEntityToRemove);
            }
            categoryEntityToRemove.setParentCategory(null);

            entityManager.remove(categoryEntityToRemove);
            entityManager.flush();
        }
    }

    private void lazilyLoadSubCategories(CategoryEntity categoryEntity) {
        for (CategoryEntity ce : categoryEntity.getSubCategories()) {
            lazilyLoadSubCategories(ce);
        }
    }

    @Override
    public List<CategoryEntity> filterCategoriesByCategory(Long categoryId) throws CategoryNotFoundException {
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        CategoryEntity categoryEntity = retrieveCategoryByCategoryId(categoryId);

        if (categoryEntity.getSubCategories().isEmpty()) {
            categoryEntities.add(categoryEntity);
        } else {
            for (CategoryEntity subCategoryEntity : categoryEntity.getSubCategories()) {
                categoryEntities.addAll(addSubCategories(subCategoryEntity));
            }
        }

        // LAZY LOADING
        for (CategoryEntity ce : categoryEntities) {
            lazilyLoadSubCategories(ce);
        }

        Collections.sort(categoryEntities, new Comparator<CategoryEntity>() {
            public int compare(CategoryEntity pe1, CategoryEntity pe2) {
                return pe1.getCategoryId().compareTo(pe2.getCategoryId());
            }
        });

        return categoryEntities;
    }

    private List<CategoryEntity> addSubCategories(CategoryEntity categoryEntity) {
        List<CategoryEntity> subCategoryEntities = new ArrayList<>();

        if (categoryEntity.getSubCategories().isEmpty()) {
            subCategoryEntities.add(categoryEntity);
            return subCategoryEntities;
        } else {
            for (CategoryEntity subCategoryEntity : categoryEntity.getSubCategories()) {
                subCategoryEntities.add(categoryEntity);
                subCategoryEntities.addAll(addSubCategories(subCategoryEntity));
            }

            return subCategoryEntities;
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CategoryEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
