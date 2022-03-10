/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RatingEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.RatingNotFoundException;
import util.exception.UpdateRatingException;

/**
 *
 * @author ryyant
 */
@Local
public interface RatingEntitySessionBeanLocal {

    public List<RatingEntity> retrieveAllRatingsByProductId(Long productId);

    public RatingEntity deleteRating(RatingEntity rating);

    public RatingEntity createRating(RatingEntity newRatingEntity);
    
    public void updateRating(RatingEntity ratingEntity) throws InputDataValidationException, RatingNotFoundException, UpdateRatingException;

    public RatingEntity retrieveRatingByRatingId(Long ratingId) throws RatingNotFoundException;
    
}
