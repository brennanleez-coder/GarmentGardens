/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LineItemEntity;
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

    public LineItemEntity updateLineItem(LineItemEntity lineItem) throws UpdateLineItemException, LineItemNotFoundException;
    
}
