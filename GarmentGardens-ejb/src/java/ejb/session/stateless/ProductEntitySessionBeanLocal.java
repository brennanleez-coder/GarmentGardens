/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ProductEntity;
import entity.UserEntity;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface ProductEntitySessionBeanLocal {

    public ProductEntity createNewProduct(ProductEntity newProductEntity, Long categoryId, List<Long> tagIds) throws ProductSkuCodeExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProductException;

    public List<ProductEntity> retrieveAllProducts();

    public List<ProductEntity> searchProductsByName(String searchString);

    public List<ProductEntity> filterProductsByCategory(Long categoryId) throws CategoryNotFoundException;

    public List<ProductEntity> filterProductsByTags(List<Long> tagIds, String condition);

    public ProductEntity retrieveProductByProductId(Long productId) throws ProductNotFoundException;

    public ProductEntity retrieveProductByProductSkuCode(String skuCode) throws ProductNotFoundException;

    public void updateProduct(ProductEntity productEntity, Long categoryId, List<Long> tagIds) throws ProductNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdateProductException, InputDataValidationException;

    public void deleteProduct(Long productId) throws ProductNotFoundException, DeleteProductException;

    public void debitQuantityOnHand(Long productId, Integer quantityToDebit) throws ProductNotFoundException, ProductInsufficientQuantityOnHandException;

    public void creditQuantityOnHand(Long productId, Integer quantityToCredit) throws ProductNotFoundException;

    public List<ProductEntity> retrieveProductsBySellerId(Long userId);
    
    public ProductEntity createNewProductFrontEnd(ProductEntity newProductEntity, Long categoryId, List<Long> tagIds, UserEntity seller) throws ProductSkuCodeExistException, UnknownPersistenceException, InputDataValidationException, CreateNewProductException;
    
}
