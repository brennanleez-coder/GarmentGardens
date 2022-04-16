/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CartEntity;
import entity.LineItemEntity;
import entity.UserEntity;
import javax.ejb.Local;
import util.exception.CartNotFoundException;
import util.exception.CreateNewCartException;
import util.exception.InputDataValidationException;
import util.exception.LineItemNotFoundException;
import util.exception.UserNotFoundException;

/**
 *
 * @author ryyant
 */
@Local
public interface CartEntitySessionBeanLocal {

    public CartEntity createNewIndividualCart(Long groupOwnerId, CartEntity newCartEntity) throws InputDataValidationException, UserNotFoundException, CreateNewCartException;

    public CartEntity createNewGroupCart(Long groupOwnerId, CartEntity newCartEntity) throws InputDataValidationException, UserNotFoundException, CreateNewCartException;

    public CartEntity retrieveCartByCartId(Long cartId) throws CartNotFoundException;

    public void updateCart(CartEntity cartEntity);

    public void LeaveGroupCart(Long groupMemberId, CartEntity cartEntity) throws UnsupportedOperationException, CartNotFoundException;

    public void deleteGroupCart(Long groupOwnerId, CartEntity cartEntity) throws UnsupportedOperationException, CartNotFoundException;

    public CartEntity retrieveIndividualCartByUserId(Long userId) throws CartNotFoundException;

    public void addLineItemToCart(LineItemEntity lineItemEntity, UserEntity userEntity) throws CartNotFoundException, LineItemNotFoundException;

    public void updateLineItemInCart(LineItemEntity lineItemEntity, UserEntity userEntity) throws CartNotFoundException, LineItemNotFoundException;

}
