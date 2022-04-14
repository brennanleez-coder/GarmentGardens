/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdvertisementEntity;
import entity.AdvertiserEntity;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AdvertisementEntityExistException;
import util.exception.AdvertiserEntityNotFoundException;
import util.exception.CreateNewAdvertiserEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author brennanlee
 */
@Stateless
public class AdvertisementEntitySessionBean implements AdvertisementEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    @EJB(name = "AdvertiserEntitySessionBeanLocal")
    private AdvertiserEntitySessionBeanLocal advertiserEntitySessionBeanLocal;

    @EJB(name = "StaffEntitySessionBeanLocal")
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AdvertisementEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public AdvertisementEntity createNewAdvertisementEntity(AdvertisementEntity advertisementEntity, Long advertiserId) throws AdvertiserEntityNotFoundException, AdvertisementEntityExistException, UnknownPersistenceException, InputDataValidationException, CreateNewAdvertiserEntityException {
        Set<ConstraintViolation<AdvertisementEntity>> constraintViolations = validator.validate(advertisementEntity);

        if (constraintViolations.isEmpty()) {
            try {

                AdvertiserEntity advertiserEntityToAssociate = advertiserEntitySessionBeanLocal.retrieveAdvertiserEntityByAdvertiserId(advertiserId);

                if (advertiserEntityToAssociate == null) {
                    throw new AdvertiserEntityNotFoundException("The advertiser does not exist!");
                }

                advertisementEntity.setAdvertiser(advertiserEntityToAssociate);
                entityManager.persist(advertisementEntity);
                entityManager.flush();

                return advertisementEntity;

            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new AdvertisementEntityExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }

        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));

        }
    }

    //        @Override
    //        public AdvertisementEntity createNewAdvertiserEntity
    //        (AdvertisementEntity advertisementEntity, Long advertiserId) throws AdvertiserEntityNotFoundException, UnknownPersistenceException, InputDataValidationException, CreateNewAdvertisementException {
    //            Set<ConstraintViolation<AdvertisementEntity>> constraintViolations = validator.validate(advertisementEntity);
    //
    //            if (constraintViolations.isEmpty()) {
    //
    //                AdvertiserEntity advertiserEntityToAssociate = advertiserEntitySessionBeanLocal.retrieveAdvertiserEntityByAdvertiserId(advertiserId);
    //
    //                if (advertiserEntityToAssociate == null) {
    //                    throw new AdvertiserEntityNotFoundException("The advertiser does not exist!");
    //                }
    //
    //                try {
    //                    advertisementEntity.setAdvertiser(advertiserEntityToAssociate);
    //                    entityManager.persist(advertisementEntity);
    //                    entityManager.flush();
    //
    //                    return advertisementEntity;
    //
    //                } catch (Exception ex) {
    //                    throw new CreateNewAdvertisementException("There was an error creating the Advertisement");
    //                }
    //            } else {
    //                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
    //            }
    //
    //        }
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AdvertisementEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
