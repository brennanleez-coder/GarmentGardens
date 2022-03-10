/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CartEntity;
import entity.LineItemEntity;
import entity.UserEntity;
import java.util.Objects;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CartNotFoundException;
import util.exception.CreateNewCartException;
import util.exception.InputDataValidationException;
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
    public CartEntity createNewIndividualCart(Long groupOwnerId, CartEntity newCartEntity) throws InputDataValidationException, UserNotFoundException, CreateNewCartException {
        if (newCartEntity != null) {
            try {
                UserEntity customerEntity = userEntitySessionBeanLocal.retrieveUserByUserId(groupOwnerId);
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

            return cartEntity;
        } else {
            throw new CartNotFoundException("Cart ID " + cartId + " does not exist!");
        }
    }

    @Override
    public void updateCart(CartEntity cartEntity) {
        entityManager.merge(cartEntity);
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
