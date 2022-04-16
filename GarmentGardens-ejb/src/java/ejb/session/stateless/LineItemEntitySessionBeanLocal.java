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
import util.exception.InputDataValidationException;
import util.exception.LineItemNotFoundException;
import util.exception.UpdateLineItemException;

/**
 *
 * @author ryyant
 */
@Local
public interface LineItemEntitySessionBeanLocal {

    public LineItemEntity createLineItem(LineItemEntity newLineItemEntity) throws InputDataValidationException;

    public LineItemEntity deleteLineItem(LineItemEntity lineItem);

    public LineItemEntity retrieveLineItemById(Long lineItemId) throws LineItemNotFoundException;

    public LineItemEntity removeCartLineItemByUser(UserEntity userEntity, LineItemEntity lineItemEntity) throws UpdateLineItemException;

    public CartEntity clearCart(UserEntity userEntity) throws UpdateLineItemException;

    public boolean checkProductExistInCart(Long cartId, Long productId);

    public LineItemEntity getLineItemByUserProduct(Long cartId, Long productId) throws LineItemNotFoundException;

    public LineItemEntity updateLineItem(LineItemEntity lineItem, int qtyToAdd) throws UpdateLineItemException, LineItemNotFoundException;
    
}
