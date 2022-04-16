/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.UserEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCreditCardException;
import util.exception.UserNotFoundException;

/**
 *
 * @author rilwa
 */
@Stateless
public class CreditCardEntitySessionBean implements CreditCardEntitySessionBeanLocal {

    @EJB(name = "UserEntitySessionBeanLocal")
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CreditCardEntitySessionBean() {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CreditCardEntity createNewCreditCardEntity(CreditCardEntity newCreditCardEntity) throws InputDataValidationException, CreateNewCreditCardException {
        Set<ConstraintViolation<CreditCardEntity>> constraintViolations = validator.validate(newCreditCardEntity);

        if (constraintViolations.isEmpty()) {
            try {
                entityManager.persist(newCreditCardEntity);
                entityManager.flush();

                return newCreditCardEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewCreditCardException("Credit Card with same Credit Card number already exist");
                } else {
                    throw new CreateNewCreditCardException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<CreditCardEntity> retrieveAllCreditCards() {
        Query query = entityManager.createQuery("SELECT c FROM CreditCardEntity c ORDER BY c.holderName ASC");
        List<CreditCardEntity> creditCardEntities = query.getResultList();

        for (CreditCardEntity creditCardEntity : creditCardEntities) {
            creditCardEntity.getAdvertiser();
            creditCardEntity.getUser();
        }

        return creditCardEntities;
    }

    @Override
    public CreditCardEntity retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException {
        try {
            System.out.println("*********CreditCardEntitySessionBean:: retrieveCreditCardByCreditCardId " + creditCardId);
            CreditCardEntity creditCardEntity = entityManager.find(CreditCardEntity.class, creditCardId);
            return creditCardEntity;
        } catch (Exception ex) {
            throw new CreditCardNotFoundException("Credit Card ID " + creditCardId + " does not exist!");
        }
    }

    @Override
    public List<CreditCardEntity> retrieveCreditCardByCreditUserId(Long userId) throws CreditCardNotFoundException {

        System.out.println("*********CreditCardEntitySessionBean:: retrieveCreditCardByCreditUserId " + userId);

        Query query = entityManager.createQuery("SELECT c FROM CreditCardEntity c WHERE c.user.userId = :inUserId");
        query.setParameter("inUserId", userId);

        return query.getResultList();
    }

    @Override
    public void updateCreditCard(CreditCardEntity creditCard) throws InputDataValidationException, CreditCardNotFoundException, UpdateCreditCardException {
        Set<ConstraintViolation<CreditCardEntity>> constraintViolations = validator.validate(creditCard);

        if (constraintViolations.isEmpty()) {
            if (creditCard.getCreditCardId() != null) {
                CreditCardEntity CreditCardEntityToUpdate = retrieveCreditCardByCreditCardId(creditCard.getCreditCardId());

                Query query = entityManager.createQuery("SELECT c FROM CreditCardEntity c WHERE c.creditCardNumber = :inNumber");
                query.setParameter("inNumber", creditCard.getCreditCardNumber());

                if (!query.getResultList().isEmpty()) {
                    throw new UpdateCreditCardException("The Credit Card Number of the card to be updated is duplicated");
                }

                CreditCardEntityToUpdate.setBillingAddress(creditCard.getBillingAddress());
                CreditCardEntityToUpdate.setExpiryDate(creditCard.getExpiryDate());
            } else {
                throw new CreditCardNotFoundException("Credit Card ID not provided for Credit Card to be updated");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    public void deleteCreditCard(UserEntity user, Long creditCardId) throws CreditCardNotFoundException, DeleteCreditCardException {

        System.out.println("*********Attempting to retieve credit card for removal " + creditCardId);
        CreditCardEntity creditCardEntityToRemove = retrieveCreditCardByCreditCardId(creditCardId);
        System.out.println("********Attempting to remove credit card " + creditCardId);
        user.getCreditCards().remove(creditCardEntityToRemove);

        entityManager.remove(creditCardEntityToRemove);
        entityManager.flush();

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CreditCardEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
