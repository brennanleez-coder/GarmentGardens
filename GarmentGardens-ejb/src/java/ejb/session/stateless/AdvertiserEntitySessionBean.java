/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdvertisementEntity;
import static entity.AdvertisementEntity_.advertisementId;
import entity.AdvertiserEntity;
import entity.CreditCardEntity;
import entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AdvertiserEntityExistException;
import util.exception.AdvertiserEntityNotFoundException;
import util.exception.CreateNewAdvertiserEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateAdvertiserEntityException;

/**
 *
 * @author brennanlee
 */
@Stateless
public class AdvertiserEntitySessionBean implements AdvertiserEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AdvertiserEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public AdvertiserEntity createNewAdvertiserEntity(AdvertiserEntity advertiserEntity, List<Long> creditCardIds, List<Long> advertisementIds) throws AdvertiserEntityExistException, UnknownPersistenceException, InputDataValidationException, CreateNewAdvertiserEntityException {
        Set<ConstraintViolation<AdvertiserEntity>> constraintViolations = validator.validate(advertiserEntity);

        if (constraintViolations.isEmpty()) {
            try {
                List<CreditCardEntity> listOfCreditCards = new ArrayList<>();
                List<AdvertisementEntity> listOfAdvertisements = new ArrayList<>();

                if (entityManager.find(AdvertiserEntity.class, advertiserEntity.getAdvertiserId()) != null) {
                    throw new AdvertiserEntityExistException("AdvertiserEntity already exists, ID: " + advertiserEntity.getAdvertiserId());
                }
                for (Long advertisementId : advertisementIds) {
                    AdvertisementEntity advertisementEntityToAssociate = entityManager.find(AdvertisementEntity.class, advertisementId);
                    listOfAdvertisements.add(advertisementEntityToAssociate);
                    advertisementEntityToAssociate.setAdvertiser(advertiserEntity);
                }

                creditCardIds.stream().map(creditCardId -> entityManager.find(CreditCardEntity.class, creditCardId)).map(creditCardToAssociate -> {
                    creditCardToAssociate.setAdvertiser(advertiserEntity);
                    return creditCardToAssociate;
                }).forEachOrdered(creditCardToAssociate -> {
                    listOfCreditCards.add(creditCardToAssociate);
                });

                advertiserEntity.setCreditCards(listOfCreditCards);
                advertiserEntity.setAdvertisements(listOfAdvertisements);

                entityManager.persist(advertiserEntity);
                entityManager.flush();

                return advertiserEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new AdvertiserEntityExistException();
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

    @Override
    public List<AdvertiserEntity> retrieveAllAdvertiserEntity() {
        Query query = entityManager.createQuery("SELECT a FROM AdvertiserEntity a");

        List<AdvertiserEntity> list = (List<AdvertiserEntity>) query.getResultList();

        for (AdvertiserEntity a : list) {
            a.getAdvertisements().size();
            a.getCreditCards().size();
        }

        return list;
    }

    @Override
    public AdvertiserEntity retrieveAdvertiserEntityByAdvertiserId(Long advertiserId) throws AdvertiserEntityNotFoundException {
        Query query = entityManager.createQuery("Select a FROM AdvertiserEntity WHERE a.advertiserId = :inAdvertiserId");
        query.setParameter("inAdvertiserId", advertiserId);

        try {
            AdvertiserEntity advertiserEntity = (AdvertiserEntity) query.getSingleResult();

            advertiserEntity.getAdvertisements().size();
            advertiserEntity.getCreditCards().size();

            return advertiserEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AdvertiserEntityNotFoundException("Advertiser Entity cannot be found, ID: " + advertiserId);
        }
    }

    
    /*
    public AdvertiserEntity deleteAdvertiserEntity(AdvertiserEntity advertiserEntity) {
        AdvertiserEntity advertiserEntityToDelete = entityManager.find(AdvertiserEntity.class, advertiserEntity.getAdvertiserId());
        for (AdvertiserEntity advertisementEntity: advertiserEntity.getAdvertisements()) {
            advertisementEntity.
        }
        advertiserEntity.setAdvertisements(null);
    }

    */
    
    @Override
    public AdvertiserEntity updateAdvertiserEntity(AdvertiserEntity advertiserEntity) throws AdvertiserEntityNotFoundException {
        if (advertiserEntity != null && advertiserEntity.getAdvertiserId() != null) {
            Set<ConstraintViolation<AdvertiserEntity>> constraintViolations = validator.validate(advertiserEntity);

            AdvertiserEntity advertiserEntityToUpdate = retrieveAdvertiserEntityByAdvertiserId(advertiserEntity.getAdvertiserId());

            if (advertiserEntityToUpdate.getAdvertiserId().equals(advertiserEntity.getAdvertiserId())) {
                advertiserEntityToUpdate.setCompanyName(advertiserEntity.getCompanyName());
                advertiserEntityToUpdate.setUsername(advertiserEntity.getUsername());
                advertiserEntityToUpdate.setPassword(advertiserEntity.getPassword());
                advertiserEntityToUpdate.setCreditCards(advertiserEntity.getCreditCards());
                advertiserEntityToUpdate.setAdvertisements(advertiserEntity.getAdvertisements());
                return advertiserEntityToUpdate;
            }
        }
        return null;

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AdvertiserEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
