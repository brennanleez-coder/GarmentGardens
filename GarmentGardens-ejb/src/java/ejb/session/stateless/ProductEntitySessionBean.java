/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.ProductEntity;
import entity.TagEntity;
import entity.UserEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewProductException;
import util.exception.DeleteProductException;
import util.exception.InputDataValidationException;
import util.exception.ProductInsufficientQuantityOnHandException;
import util.exception.ProductNotFoundException;
import util.exception.ProductSkuCodeExistException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateProductException;

/**
 *
 * @author wong
 */
@Stateless
public class ProductEntitySessionBean implements ProductEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB(name = "TagEntitySessionBeanLocal")
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ProductEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // Updated in v4.1
    // Updated in v4.2 with bean validation
    // Updated in v5.0 to include association with new category entity
    // Updated in v5.1 with category entity and tag entity processing
    @Override
    public ProductEntity createNewProduct(ProductEntity newProductEntity, Long categoryId, List<Long> tagIds) throws ProductSkuCodeExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProductException {
        Set<ConstraintViolation<ProductEntity>> constraintViolations = validator.validate(newProductEntity);

        if (constraintViolations.isEmpty()) {
            try {
                if (categoryId == null) {
                    throw new CreateNewProductException("The new product must be associated a leaf category");
                }

                CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

                if (!categoryEntity.getSubCategories().isEmpty()) {
                    throw new CreateNewProductException("Selected category for the new product is not a leaf category");
                }

                entityManager.persist(newProductEntity);
                newProductEntity.setCategory(categoryEntity);
                newProductEntity.setIsListed(Boolean.TRUE);

                if (tagIds != null && (!tagIds.isEmpty())) {
                    for (Long tagId : tagIds) {
                        TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                        newProductEntity.addTag(tagEntity);

                    }
                }

                entityManager.flush();

                return newProductEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ProductSkuCodeExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (CategoryNotFoundException | TagNotFoundException ex) {
                throw new CreateNewProductException("An error has occurred while creating the new product: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public ProductEntity createNewProductFrontEnd(ProductEntity newProductEntity, Long categoryId, List<Long> tagIds, UserEntity seller) throws ProductSkuCodeExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProductException {
        Set<ConstraintViolation<ProductEntity>> constraintViolations = validator.validate(newProductEntity);

        if (constraintViolations.isEmpty()) {
            try {
                if (categoryId == null) {
                    throw new CreateNewProductException("The new product must be associated a leaf category");
                }

                CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

                if (!categoryEntity.getSubCategories().isEmpty()) {
                    throw new CreateNewProductException("Selected category for the new product is not a leaf category");
                }

                entityManager.persist(newProductEntity);
                newProductEntity.setCategory(categoryEntity);
                newProductEntity.setIsListed(Boolean.TRUE);
                newProductEntity.setSeller(seller);

                if (tagIds != null && (!tagIds.isEmpty())) {
                    for (Long tagId : tagIds) {
                        TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                        newProductEntity.addTag(tagEntity);

                    }
                }

                entityManager.flush();

                return newProductEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ProductSkuCodeExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (CategoryNotFoundException | TagNotFoundException ex) {
                throw new CreateNewProductException("An error has occurred while creating the new product: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<ProductEntity> retrieveAllProducts() {
        Query query = entityManager.createQuery("SELECT p FROM ProductEntity p ORDER BY p.skuCode ASC");
        List<ProductEntity> productEntities = query.getResultList();

        for (ProductEntity productEntity : productEntities) {
            productEntity.getCategory();
            productEntity.getTags().size();
            productEntity.getRatings().size();
            productEntity.getLineItems().size();
            productEntity.getSeller();
        }

        return productEntities;
    }

    @Override
    public List<ProductEntity> searchProductsByName(String searchString) {
        Query query = entityManager.createQuery("SELECT p FROM ProductEntity p WHERE p.name LIKE :inSearchString ORDER BY p.skuCode ASC");
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<ProductEntity> productEntities = query.getResultList();

        for (ProductEntity productEntity : productEntities) {
            productEntity.getCategory();
            productEntity.getTags().size();
        }

        return productEntities;
    }

    @Override
    public List<ProductEntity> filterProductsByCategory(Long categoryId) throws CategoryNotFoundException {
        try {
            CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
            List<ProductEntity> productEntities = new ArrayList<>();

            if (categoryEntity.getSubCategories().isEmpty()) {
                productEntities.addAll(categoryEntity.getProducts());
            } else {
                for (CategoryEntity subCategoryEntity : categoryEntity.getSubCategories()) {
                    productEntities.addAll(addSubCategoryProducts(subCategoryEntity));
                }
            }

            for (ProductEntity productEntity : productEntities) {
                productEntity.getCategory();
                productEntity.getSeller();
                productEntity.getLineItems();
                productEntity.getTags().size();
            }

            Collections.sort(productEntities, new Comparator<ProductEntity>() {
                public int compare(ProductEntity pe1, ProductEntity pe2) {
                    return pe1.getSkuCode().compareTo(pe2.getSkuCode());
                }
            });

            return productEntities;
        } catch (Exception ex) {
            throw new CategoryNotFoundException("Category does not exist!");

        }
    }

    private List<ProductEntity> addSubCategoryProducts(CategoryEntity categoryEntity) {
        List<ProductEntity> productEntities = new ArrayList<>();

        if (categoryEntity.getSubCategories().isEmpty()) {
            productEntities.addAll(categoryEntity.getProducts());
        } else {
            for (CategoryEntity subCategoryEntity : categoryEntity.getSubCategories()) {
                productEntities.addAll(addSubCategoryProducts(subCategoryEntity));
            }
        }

        return productEntities;
    }

    @Override
    public List<ProductEntity> filterProductsByTags(List<Long> tagIds, String condition) {
        List<ProductEntity> productEntities = new ArrayList<>();

        if (tagIds == null || tagIds.isEmpty() || (!condition.equals("AND") && !condition.equals("OR"))) {
            return productEntities;
        } else {
            if (condition.equals("OR")) {
                Query query = entityManager.createQuery("SELECT DISTINCT pe FROM ProductEntity pe, IN (pe.tags) te WHERE te.tagId IN :inTagIds ORDER BY pe.skuCode ASC");
                query.setParameter("inTagIds", tagIds);
                productEntities = query.getResultList();
            } else // AND
            {
                String selectClause = "SELECT pe FROM ProductEntity pe";
                String whereClause = "";
                Boolean firstTag = true;
                Integer tagCount = 1;

                for (Long tagId : tagIds) {
                    selectClause += ", IN (pe.tags) te" + tagCount;

                    if (firstTag) {
                        whereClause = "WHERE te1.tagId = " + tagId;
                        firstTag = false;
                    } else {
                        whereClause += " AND te" + tagCount + ".tagId = " + tagId;
                    }

                    tagCount++;
                }

                String jpql = selectClause + " " + whereClause + " ORDER BY pe.skuCode ASC";
                Query query = entityManager.createQuery(jpql);
                productEntities = query.getResultList();
            }

            for (ProductEntity productEntity : productEntities) {
                productEntity.getCategory();
                productEntity.getTags().size();
            }

            Collections.sort(productEntities, new Comparator<ProductEntity>() {
                public int compare(ProductEntity pe1, ProductEntity pe2) {
                    return pe1.getSkuCode().compareTo(pe2.getSkuCode());
                }
            });

            return productEntities;
        }
    }

    @Override
    public ProductEntity retrieveProductByProductId(Long productId) throws ProductNotFoundException {
        ProductEntity productEntity = entityManager.find(ProductEntity.class, productId);

        if (productEntity != null) {
            productEntity.getCategory();
            productEntity.getTags().size();
            productEntity.getRatings().size();
            productEntity.getRatings().size();
            return productEntity;
        } else {
            throw new ProductNotFoundException("Product ID " + productId + " does not exist!");
        }
    }

    @Override
    public ProductEntity retrieveProductByProductSkuCode(String skuCode) throws ProductNotFoundException {
        Query query = entityManager.createQuery("SELECT p FROM ProductEntity p WHERE p.skuCode = :inSkuCode");
        query.setParameter("inSkuCode", skuCode);

        try {
            ProductEntity productEntity = (ProductEntity) query.getSingleResult();
            productEntity.getCategory();
            productEntity.getTags().size();
            productEntity.getRatings().size();

            return productEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ProductNotFoundException("Sku Code " + skuCode + " does not exist!");
        }
    }

    @Override
    public List<ProductEntity> retrieveProductsBySellerId(Long userId) {
        Query query = entityManager.createQuery("SELECT p FROM ProductEntity p WHERE p.seller.userId = :inUserId");
        query.setParameter("inUserId", userId);
        List<ProductEntity> productEntities = query.getResultList();

        for (ProductEntity p : productEntities) {
            p.getCategory();
            p.getTags().size();
            p.getRatings().size();
        }

        return productEntities;

    }

    @Override
    public void updateProduct(ProductEntity productEntity, Long categoryId, List<Long> tagIds) throws ProductNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdateProductException, InputDataValidationException {
        if (productEntity != null && productEntity.getProductId() != null) {
            Set<ConstraintViolation<ProductEntity>> constraintViolations = validator.validate(productEntity);

            if (constraintViolations.isEmpty()) {
                ProductEntity productEntityToUpdate = retrieveProductByProductId(productEntity.getProductId());

                if (productEntityToUpdate.getSkuCode().equals(productEntity.getSkuCode())) {
                    if (categoryId != null && (!productEntityToUpdate.getCategory().getCategoryId().equals(categoryId))) {
                        CategoryEntity categoryEntityToUpdate = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

                        if (!categoryEntityToUpdate.getSubCategories().isEmpty()) {
                            throw new UpdateProductException("Selected category for the new product is not a leaf category");
                        }

                        productEntityToUpdate.setCategory(categoryEntityToUpdate);
                    }

                    if (tagIds != null) {
                        for (TagEntity tagEntity : productEntityToUpdate.getTags()) {
                            tagEntity.getProducts().remove(productEntityToUpdate);
                        }

                        productEntityToUpdate.getTags().clear();

                        for (Long tagId : tagIds) {
                            TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                            productEntityToUpdate.addTag(tagEntity);
                        }
                    }

                    productEntityToUpdate.setName(productEntity.getName());
                    productEntityToUpdate.setDescription(productEntity.getDescription());
                    productEntityToUpdate.setQuantityOnHand(productEntity.getQuantityOnHand());
                    productEntityToUpdate.setUnitPrice(productEntity.getUnitPrice());
                    productEntityToUpdate.setImageLink(productEntity.getImageLink());
                } else {
                    throw new UpdateProductException("SKU Code of product record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ProductNotFoundException("Product ID not provided for product to be updated");
        }
    }

    @Override
    public void deleteProduct(Long productId) throws ProductNotFoundException, DeleteProductException {

        ProductEntity productEntityToRemove = retrieveProductByProductId(productId);

        if (productEntityToRemove == null) {
            throw new ProductNotFoundException("Product is not found in database, ID: " + productId);
        } else {
            if (!productEntityToRemove.getLineItems().isEmpty()) {
                throw new DeleteProductException("Product ID " + productId + " is associated with existing sale transaction line item(s) and cannot be deleted!");
            } else {
                entityManager.remove(productEntityToRemove);
            }

        }
    }

    @Override
    public void debitQuantityOnHand(Long productId, Integer quantityToDebit) throws ProductNotFoundException, ProductInsufficientQuantityOnHandException {
        ProductEntity productEntity = retrieveProductByProductId(productId);

        if (productEntity.getQuantityOnHand() >= quantityToDebit) {
            productEntity.setQuantityOnHand(productEntity.getQuantityOnHand() - quantityToDebit);
        } else {
            throw new ProductInsufficientQuantityOnHandException("Product " + productEntity.getSkuCode() + " quantity on hand is " + productEntity.getQuantityOnHand() + " versus quantity to debit of " + quantityToDebit);
        }
    }

    @Override
    public void creditQuantityOnHand(Long productId, Integer quantityToCredit) throws ProductNotFoundException {
        ProductEntity productEntity = retrieveProductByProductId(productId);
        productEntity.setQuantityOnHand(productEntity.getQuantityOnHand() + quantityToCredit);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ProductEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
