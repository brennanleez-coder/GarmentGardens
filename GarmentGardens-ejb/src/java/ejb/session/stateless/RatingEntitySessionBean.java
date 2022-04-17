/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ProductEntity;
import entity.RatingEntity;
import entity.UserEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewRatingException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;
import util.exception.RatingNotFoundException;
import util.exception.UpdateRatingException;
import util.exception.UserNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class RatingEntitySessionBean implements RatingEntitySessionBeanLocal {

    @EJB(name = "UserEntitySessionBeanLocal")
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB(name = "ProductEntitySessionBeanLocal")
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RatingEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public RatingEntity createRating(RatingEntity newRatingEntity, Long userId) throws UserNotFoundException {
        UserEntity customer = userEntitySessionBeanLocal.retrieveUserByUserId(userId);
        if (customer != null) {
            newRatingEntity.setCustomer(customer);
            entityManager.persist(newRatingEntity);
            entityManager.flush();

            return newRatingEntity;
        } else {
            throw new UserNotFoundException("User cannot be found: " + userId);
        }

    }

    @Override
    public RatingEntity rateProduct(UserEntity userEntity, ProductEntity productEntity, RatingEntity ratingEntity) throws CreateNewRatingException, ProductNotFoundException, UserNotFoundException {
        UserEntity customer = userEntitySessionBeanLocal.retrieveUserByUserId(userEntity.getUserId());
        ProductEntity product = productEntitySessionBeanLocal.retrieveProductByProductId(productEntity.getProductId());
        
        if (customer != null && product != null && ratingEntity != null) {

            RatingEntity newRating = new RatingEntity(ratingEntity.getDescription(), ratingEntity.getNumberOfStars(), new Date());
            newRating.setCustomer(customer);

            entityManager.persist(newRating);
            entityManager.flush();

            product.getRatings().add(newRating);

            // ADD CHLOROPHYLL
            int chlorophyll = customer.getChlorophyll();
            customer.setChlorophyll(chlorophyll + 2);
            
            return newRating;
        } else {
            throw new CreateNewRatingException("Rate product failed!");
        }

    }

    @Override
    public List<RatingEntity> retrieveAllRatingsByProductId(Long productId) {
        Query query = entityManager.createQuery("SELECT r FROM RatingEntity r WHERE r.productId = ?1")
                .setParameter(1, productId);
        List<RatingEntity> ratings = query.getResultList();
        for (RatingEntity rating : ratings) {
            rating.getCustomer();
        }
        return ratings;
    }

    @Override
    public Double retrieveRatingScore(Long productId) throws ProductNotFoundException {
        ProductEntity product = productEntitySessionBeanLocal.retrieveProductByProductId(productId);
        if (product != null) {
            Integer totalRating = 0;
            for (RatingEntity rating : product.getRatings()) {
                totalRating += rating.getNumberOfStars();
            }

            return (totalRating * 1.0) / product.getRatings().size();
        } else {
            throw new ProductNotFoundException("Product cannot be found, ID:" + productId);
        }

    }

    @Override
    public RatingEntity deleteRating(RatingEntity rating) {
        RatingEntity ratingEntity = entityManager.find(RatingEntity.class, rating.getRatingId());
        entityManager.remove(ratingEntity);
        return rating;
    }

    @Override
    public RatingEntity retrieveRatingByRatingId(Long ratingId) throws RatingNotFoundException {
        RatingEntity ratingEntity = entityManager.find(RatingEntity.class, ratingId);

        if (ratingEntity != null) {
            return ratingEntity;
        } else {
            throw new RatingNotFoundException("Rating ID " + ratingId + " does not exist!");
        }
    }

    @Override
    public List<RatingEntity> retrieveRatingsByUserId(Long userId) {
        List<RatingEntity> ratingEntities = entityManager.createQuery("SELECT r FROM RatingEntity r WHERE r.customer.userId = ?1")
                .setParameter(1, userId)
                .getResultList();
        return ratingEntities;
    }

    @Override
    public void updateRating(RatingEntity ratingEntity) throws InputDataValidationException, RatingNotFoundException, UpdateRatingException {
        Set<ConstraintViolation<RatingEntity>> constraintViolations = validator.validate(ratingEntity);

        if (constraintViolations.isEmpty()) {
            if (ratingEntity.getRatingId() != null) {
                RatingEntity ratingEntityToUpdate = retrieveRatingByRatingId(ratingEntity.getRatingId());

                Query query = entityManager.createQuery("SELECT r FROM RatingEntity r WHERE r.ratingId = :inRatingId AND r.dateOfRating = :inDate");
                query.setParameter("inRatingId", ratingEntity.getRatingId());
                query.setParameter("inDate", ratingEntity.getDateOfRating());

                if (!query.getResultList().isEmpty()) {
                    throw new UpdateRatingException("The name of the rating to be updated is duplicated");
                }

                ratingEntityToUpdate.setDescription(ratingEntity.getDescription());
                ratingEntityToUpdate.setNumberOfStars(ratingEntity.getNumberOfStars());
                ratingEntityToUpdate.setDateOfRating(ratingEntity.getDateOfRating());
            } else {
                throw new RatingNotFoundException("Rating ID not provided for rating to be updated");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RatingEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
