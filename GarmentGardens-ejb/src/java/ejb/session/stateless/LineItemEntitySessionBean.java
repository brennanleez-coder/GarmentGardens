/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LineItemEntity;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public LineItemEntity updateLineItem(LineItemEntity lineItem) throws UpdateLineItemException, LineItemNotFoundException {
        if (lineItem != null && lineItem.getLineItemId() != null) {
            LineItemEntity lineItemToUpdate = retrieveLineItemById(lineItem.getLineItemId());

            if (lineItemToUpdate.getLineItemId().equals(lineItem.getLineItemId())) {
                lineItemToUpdate.setQuantity(lineItem.getQuantity());
                lineItemToUpdate.setUnitPrice(lineItem.getUnitPrice());
                lineItemToUpdate.setSubTotal(lineItem.getSubTotal());

                return lineItemToUpdate;
            } else {
                throw new UpdateLineItemException("ID of Line Item to be updated does not match the existing record");
            }
        } else {
            throw new LineItemNotFoundException("Line Item ID cannot be found");
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
