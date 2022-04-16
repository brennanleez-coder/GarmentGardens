/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CartEntity;
import entity.LineItemEntity;
import entity.ProductEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.LineItemNotFoundException;
import util.exception.UpdateLineItemException;

/**
 *
 * @author ryyant
 */
@Stateless
public class LineItemEntitySessionBean implements LineItemEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public LineItemEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public LineItemEntity createLineItem(LineItemEntity newLineItemEntity) throws InputDataValidationException {
        Set<ConstraintViolation<LineItemEntity>> constraintViolations = validator.validate(newLineItemEntity);

        if (constraintViolations.isEmpty()) {
            entityManager.persist(newLineItemEntity);
            entityManager.flush();

            return newLineItemEntity;
        } else {
            System.out.println(constraintViolations);
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public LineItemEntity deleteLineItem(LineItemEntity lineItem) {
        entityManager.remove(lineItem);
        return lineItem;
    }

    @Override
    public LineItemEntity retrieveLineItemById(Long lineItemId) throws LineItemNotFoundException {
        LineItemEntity lineItem = entityManager.find(LineItemEntity.class, lineItemId);
        if (lineItem != null) {
            return lineItem;
        } else {
            throw new LineItemNotFoundException("Line Item Not Found, ID: " + lineItemId);
        }
    }

    @Override
    public LineItemEntity updateLineItem(LineItemEntity lineItem, int qtyToAdd) throws UpdateLineItemException, LineItemNotFoundException {
        if (lineItem != null && lineItem.getLineItemId() != null) {
            LineItemEntity lineItemToUpdate = retrieveLineItemById(lineItem.getLineItemId());

            if (lineItemToUpdate.getLineItemId().equals(lineItem.getLineItemId())) {
                int currentQuantity = lineItemToUpdate.getQuantity();
                BigDecimal unitPrice = lineItemToUpdate.getUnitPrice();
                BigDecimal currentSubTotal = lineItemToUpdate.getSubTotal();

                lineItemToUpdate.setQuantity(currentQuantity + qtyToAdd);
                lineItemToUpdate.setSubTotal(currentSubTotal.add(unitPrice.multiply(new BigDecimal(qtyToAdd))));

                return lineItemToUpdate;
            } else {
                throw new UpdateLineItemException("ID of Line Item to be updated does not match the existing record");
            }
        } else {
            throw new LineItemNotFoundException("Line Item ID cannot be found");
        }
    }
    

    @Override
    public boolean checkProductExistInCart(Long cartId, Long productId) {
        if (productId != null && cartId != null) {

            CartEntity cart = entityManager.find(CartEntity.class, cartId);
            ProductEntity product = entityManager.find(ProductEntity.class, productId);

            boolean exists = false;

            for (LineItemEntity li : cart.getCartLineItems()) {
                if (Objects.equals(li.getProduct().getProductId(), product.getProductId())) {
                    exists = true;
                }
            }

            return exists;
        }
        return false;
    }

    @Override
    public LineItemEntity getLineItemByUserProduct(Long cartId, Long productId) throws LineItemNotFoundException {
        if (productId != null && cartId != null) {

            CartEntity cart = entityManager.find(CartEntity.class, cartId);
            ProductEntity product = entityManager.find(ProductEntity.class, productId);

            LineItemEntity lineItem = null;

            for (LineItemEntity li : cart.getCartLineItems()) {
                if (Objects.equals(li.getProduct().getProductId(), product.getProductId())) {
                    lineItem = li;
                }
            }

            return lineItem;
        } else {
            throw new LineItemNotFoundException("Line Item Not Found. ");
        }
    }

    // USE FOR FRONT END REMOVE LINE ITEM FROM CART (INDV)
    @Override
    public LineItemEntity removeCartLineItemByUser(UserEntity userEntity, LineItemEntity lineItemEntity) throws UpdateLineItemException {

        LineItemEntity lineItem = entityManager.find(LineItemEntity.class, lineItemEntity.getLineItemId());
        UserEntity user = entityManager.find(UserEntity.class, userEntity.getUserId());

        if (lineItem != null && user != null) {

            // DIASSOCIATE LINE ITEM FROM CART
            CartEntity cart = user.getIndividualCart();
            List<LineItemEntity> cartLineItems = cart.getCartLineItems();
            cartLineItems.remove(lineItem);

            // EDIT CART DETAILS
            Integer currentTotalQuantity = cart.getTotalQuantity();
            BigDecimal currentTotalAmount = cart.getTotalAmount();
            Integer currentTotalCartItems = cart.getTotalCartItems();
            cart.setTotalQuantity(currentTotalQuantity - lineItem.getQuantity());
            cart.setTotalCartItems(currentTotalCartItems - 1);
            cart.setTotalAmount(currentTotalAmount.subtract(lineItem.getSubTotal()));

            // DISASSOCIATE PRODUCT FROM LINE ITEM
            lineItem.getProduct().getLineItems().remove(lineItem);
            entityManager.remove(lineItem);
            entityManager.flush();

            return lineItem;
        } else {
            throw new UpdateLineItemException("Cant remove!");

        }
    }

    // USE FOR FRONT END CLEAR ALL LINE ITEM FROM CART (INDV)
    @Override
    public CartEntity clearCart(UserEntity userEntity) throws UpdateLineItemException {

        Query query = entityManager.createQuery("SELECT c FROM CartEntity c WHERE c.customer.userId = ?1");
        query.setParameter(1, userEntity.getUserId());

        CartEntity cart = (CartEntity) query.getSingleResult();

        UserEntity user = entityManager.find(UserEntity.class, userEntity.getUserId());

        if (cart != null && user != null) {

            // CLEAR CART DETAILS
            cart.setTotalQuantity(0);
            cart.setTotalCartItems(0);
            cart.setTotalAmount(new BigDecimal(0));

            // DIASSOCIATE LINE ITEM FROM CART
            List<LineItemEntity> cartLineItems = user.getIndividualCart().getCartLineItems();
            cartLineItems.clear();

            // DISASSOCIATE PRODUCT FROM LINE ITEM
            for (LineItemEntity li : cart.getCartLineItems()) {
                li.getProduct().getLineItems().remove(li);
                entityManager.remove(li);
            }

            entityManager.flush();

            return cart;
        } else {
            throw new UpdateLineItemException("Cant remove!");

        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<LineItemEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
