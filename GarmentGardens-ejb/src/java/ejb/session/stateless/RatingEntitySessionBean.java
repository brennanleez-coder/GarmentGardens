/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RatingEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.RatingNotFoundException;
import util.exception.UpdateRatingException;

/**
 *
 * @author ryyant
 */
@Stateless
public class RatingEntitySessionBean implements RatingEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RatingEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public RatingEntity createRating(RatingEntity newRatingEntity) {
        entityManager.persist(newRatingEntity);
        entityManager.flush();

        return newRatingEntity;

    }

    @Override
    public List<RatingEntity> retrieveAllRatingsByProductId(Long productId) {
        Query query = entityManager.createQuery("SELECT r FROM RatingEntity r WHERE r.productId = ?1")
                .setParameter(1, productId);
        List<RatingEntity> ratings = query.getResultList();
       
        return ratings;
    }

    @Override
    public RatingEntity deleteRating(RatingEntity rating) {
        entityManager.remove(rating);
        return rating;
    }
    
    
    @Override
    public RatingEntity retrieveRatingByRatingId(Long ratingId) throws RatingNotFoundException
    {
        RatingEntity ratingEntity = entityManager.find(RatingEntity.class, ratingId);
        
        if(ratingEntity != null)
        {
            return ratingEntity;
        }
        else
        {
            throw new RatingNotFoundException("Rating ID " + ratingId + " does not exist!");
        }               
    }
    
    @Override
    public void updateRating(RatingEntity ratingEntity) throws InputDataValidationException, RatingNotFoundException, UpdateRatingException 
    {
        Set<ConstraintViolation<RatingEntity>>constraintViolations = validator.validate(ratingEntity);
        
        if(constraintViolations.isEmpty())
            
        {
            if(ratingEntity.getRatingId()!= null)
            {
                RatingEntity ratingEntityToUpdate = retrieveRatingByRatingId(ratingEntity.getRatingId());
                
                Query query = entityManager.createQuery("SELECT r FROM RatingEntity r WHERE r.ratingId = :inRatingId AND r.dateOfRating = :inDate");
                query.setParameter("inRatingId", ratingEntity.getRatingId());
                query.setParameter("inDate", ratingEntity.getDateOfRating());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateRatingException("The name of the rating to be updated is duplicated");
                }
                
                ratingEntityToUpdate.setDescription(ratingEntity.getDescription());
                ratingEntityToUpdate.setNumberOfStars(ratingEntity.getNumberOfStars());
                ratingEntityToUpdate.setDateOfRating(ratingEntity.getDateOfRating());
            }
            else
            {
                throw new RatingNotFoundException("Rating ID not provided for rating to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }  
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RatingEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    

}
