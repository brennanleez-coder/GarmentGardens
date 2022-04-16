/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CartEntity;
import entity.LineItemEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.util.Objects;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CartNotFoundException;
import util.exception.CreateNewCartException;
import util.exception.InputDataValidationException;
import util.exception.LineItemNotFoundException;
import util.exception.UserNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class CartEntitySessionBean implements CartEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;
    @Resource
    private EJBContext eJBContext;

    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    @EJB
    private LineItemEntitySessionBeanLocal lineItemEntitySessionBeanLocal;

    public CartEntitySessionBean() {
    }

    @Override
    public CartEntity createNewIndividualCart(Long customerId, CartEntity newCartEntity) throws InputDataValidationException, UserNotFoundException, CreateNewCartException {
        if (newCartEntity != null) {
            try {
                UserEntity customerEntity = userEntitySessionBeanLocal.retrieveUserByUserId(customerId);
                newCartEntity.setCustomer(customerEntity);
                entityManager.persist(newCartEntity);

                for (LineItemEntity lineItemEntity : newCartEntity.getCartLineItems()) {
                    lineItemEntitySessionBeanLocal.createLineItem(lineItemEntity);
                }

                entityManager.flush();

                return newCartEntity;
            } catch (UserNotFoundException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();

                throw new CreateNewCartException(ex.getMessage());
            }
        } else {
            throw new CreateNewCartException("Sale transaction information not provided");
        } 
    }

    @Override
    public CartEntity createNewGroupCart(Long groupOwnerId, CartEntity newCartEntity) throws InputDataValidationException, UserNotFoundException, CreateNewCartException {
        if (newCartEntity != null) {
            try {
                UserEntity customerEntity = userEntitySessionBeanLocal.retrieveUserByUserId(groupOwnerId);
                newCartEntity.setGroupOwner(customerEntity);
                newCartEntity.setCustomer(customerEntity);
                newCartEntity.getGroupCustomers().add(customerEntity);
                entityManager.persist(newCartEntity);

                for (LineItemEntity lineItemEntity : newCartEntity.getCartLineItems()) {
                    lineItemEntitySessionBeanLocal.createLineItem(lineItemEntity);
                }

                entityManager.flush();

                return newCartEntity;
            } catch (UserNotFoundException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();

                throw new CreateNewCartException(ex.getMessage());
            }
        } else {
            throw new CreateNewCartException("Sale transaction information not provided");
        }
    }

    @Override
    public CartEntity retrieveCartByCartId(Long cartId) throws CartNotFoundException {
        CartEntity cartEntity = entityManager.find(CartEntity.class, cartId);

        if (cartEntity != null) {
            cartEntity.getCartLineItems().size();
            cartEntity.getGroupCustomers().size();
            cartEntity.getCustomer();
            cartEntity.getGroupOwner();
            for (LineItemEntity cartLineItems : cartEntity.getCartLineItems()) {
                cartLineItems.getProduct();
            }
            return cartEntity;
        } else {
            throw new CartNotFoundException("Cart ID " + cartId + " does not exist!");
        }
    }

    @Override
    public CartEntity retrieveIndividualCartByUserId(Long userId) throws CartNotFoundException {
        Query query = entityManager.createQuery("SELECT c FROM CartEntity c WHERE c.customer.userId = :inUserId");
        query.setParameter("inUserId", userId);
        CartEntity cartEntity = (CartEntity) query.getSingleResult();

        if (cartEntity != null) {
            cartEntity.getCartLineItems().size();
            cartEntity.getGroupCustomers().size();
            cartEntity.getCustomer();
            cartEntity.getGroupOwner();
            for (LineItemEntity cartLineItems : cartEntity.getCartLineItems()) {
                cartLineItems.getProduct();
            }
            return cartEntity;
        } else {
            throw new CartNotFoundException("User " + userId + " does not have a cart!");
        }
    }

    @Override
    public void updateCart(CartEntity cartEntity) {
        entityManager.merge(cartEntity);
    }

    @Override
    public void addLineItemToCart(LineItemEntity lineItemEntity, UserEntity userEntity) throws CartNotFoundException, LineItemNotFoundException {
        try {
            LineItemEntity lineItem = entityManager.find(LineItemEntity.class, lineItemEntity.getLineItemId());
            UserEntity user = entityManager.find(UserEntity.class, userEntity.getUserId());
            CartEntity cart = user.getIndividualCart();
            if (cart != null) {

                CartEntity managedCart = entityManager.find(CartEntity.class, cart.getCartId());
                managedCart.getCartLineItems().add(lineItem);
                
                Integer currentTotalCartItems = managedCart.getTotalCartItems();
                Integer currentTotalQuantity = managedCart.getTotalQuantity();
                BigDecimal currentTotalAmount = managedCart.getTotalAmount();

                managedCart.setTotalCartItems(currentTotalCartItems + 1);
                managedCart.setTotalQuantity(currentTotalQuantity + lineItem.getQuantity());

                BigDecimal subTotalAmount = lineItem.getSubTotal();
                BigDecimal newTotalAmount = currentTotalAmount.add(subTotalAmount);
                cart.setTotalAmount(newTotalAmount);

                System.out.println("added item to cart!");
            }
        } catch (Exception ex) {
            throw new CartNotFoundException("Error adding item to cart!");
        }
    }

    @Override
    public void updateLineItemInCart(LineItemEntity lineItemEntity, UserEntity userEntity) throws CartNotFoundException, LineItemNotFoundException {

        LineItemEntity lineItem = entityManager.find(LineItemEntity.class, lineItemEntity.getLineItemId());
        UserEntity user = entityManager.find(UserEntity.class, userEntity.getUserId());
        if (user != null) {
            CartEntity cart = entityManager.find(CartEntity.class, user.getIndividualCart().getCartId());

            Integer currentTotalCartItems = cart.getTotalCartItems();
            Integer currentTotalQuantity = cart.getTotalQuantity();
            BigDecimal currentTotalAmount = cart.getTotalAmount();

            cart.setTotalCartItems(currentTotalCartItems + 1);
            cart.setTotalQuantity(currentTotalQuantity + lineItem.getQuantity());

            BigDecimal subTotalAmount = lineItem.getSubTotal();
            BigDecimal newTotalAmount = currentTotalAmount.add(subTotalAmount);
            cart.setTotalAmount(newTotalAmount);

            System.out.println("item updated in cart!");
        }
        try {
        } catch (Exception ex) {
            throw new CartNotFoundException("Error updating item to cart!");
        }
    }

    @Override
    // ONLY GROUP MEMBERS CAN LEAVE
    public void LeaveGroupCart(Long groupMemberId, CartEntity cartEntity) throws UnsupportedOperationException, CartNotFoundException {
        if (cartEntity != null) {
            try {
                UserEntity customerEntity = userEntitySessionBeanLocal.retrieveUserByUserId(groupMemberId);

                // DISASSOCIATE IF GROUP MEMBER, ELSE EXCEPTION
                if (!Objects.equals(groupMemberId, cartEntity.getGroupOwner().getUserId())) {
                    cartEntity.getGroupCustomers().remove(customerEntity);
                    customerEntity.setGroupCart(null);
                } else {
                    throw new UnsupportedOperationException();
                }

                entityManager.flush();

            } catch (UserNotFoundException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();

                throw new CartNotFoundException(ex.getMessage());
            }
        } else {
            throw new CartNotFoundException("Cart not provided");
        }
    }

    @Override
    // ONLY GROUP OWNER CAN DELETE ENTIRE CART
    public void deleteGroupCart(Long groupOwnerId, CartEntity cartEntity) throws UnsupportedOperationException, CartNotFoundException {
        if (cartEntity != null) {
            try {
                UserEntity customerEntity = userEntitySessionBeanLocal.retrieveUserByUserId(groupOwnerId);

                // IF NOT GROUP LEADER THROW EXCEPTION
                if (!Objects.equals(groupOwnerId, cartEntity.getGroupOwner().getUserId())) {
                    throw new UnsupportedOperationException();
                }

                // Diassociated all members from group cart (remove everyone)
                cartEntity.getGroupCustomers().clear();

                // Diassociated all line items from group cart
                for (LineItemEntity lineItemEntity : cartEntity.getCartLineItems()) {
                    lineItemEntitySessionBeanLocal.deleteLineItem(lineItemEntity);
                }

                entityManager.remove(cartEntity);

                entityManager.flush();

            } catch (UserNotFoundException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();

                throw new CartNotFoundException(ex.getMessage());
            }
        } else {
            throw new CartNotFoundException("Cart not provided");
        }
    }

}
