/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RewardEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewRewardException;
import util.exception.DeleteRewardException;
import util.exception.InputDataValidationException;
import util.exception.RewardNotFoundException;
import util.exception.UpdateRewardException;

/**
 *
 * @author rilwa
 */
@Stateless
public class RewardEntitySessionBean implements RewardEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RewardEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public RewardEntity createNewRewardEntity(RewardEntity newRewardEntity) throws InputDataValidationException, CreateNewRewardException {
        Set<ConstraintViolation<RewardEntity>> constraintViolations = validator.validate(newRewardEntity);

        if (constraintViolations.isEmpty()) {
            try {
                entityManager.persist(newRewardEntity);
                entityManager.flush();

                return newRewardEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewRewardException("Reward with same name already exist");
                } else {
                    throw new CreateNewRewardException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewRewardException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<RewardEntity> retrieveAvailableRewards() {

        List<RewardEntity> allRewards = retrieveAllRewards();
        Predicate<RewardEntity> available = x -> !x.getRewardName().contains("(REDEEMED)");
        

        List<RewardEntity> availableRewards = allRewards.stream().filter(available).collect(Collectors.toList());
        return availableRewards;
    }

    @Override
    public List<RewardEntity> retrieveAllRewards() {
        Query query = entityManager.createQuery("SELECT r FROM RewardEntity r");
        List<RewardEntity> rewardEntities = query.getResultList();

        for (RewardEntity rewardEntity : rewardEntities) {
            rewardEntity.getCustomer();
            rewardEntity.getStaff();
        }

        return rewardEntities;
    }

    @Override
    public RewardEntity retrieveRewardByRewardId(Long rewardId) throws RewardNotFoundException {
        RewardEntity rewardEntity = entityManager.find(RewardEntity.class, rewardId);

        if (rewardEntity != null) {
            return rewardEntity;
        } else {
            throw new RewardNotFoundException("Reward ID " + rewardId + " does not exist!");
        }
    }

    @Override
    public void updateReward(RewardEntity rewardEntity) throws InputDataValidationException, RewardNotFoundException, UpdateRewardException {
        Set<ConstraintViolation<RewardEntity>> constraintViolations = validator.validate(rewardEntity);

        if (constraintViolations.isEmpty()) {
            if (rewardEntity.getRewardId() != null) {
                RewardEntity rewardEntityToUpdate = retrieveRewardByRewardId(rewardEntity.getRewardId());
                rewardEntityToUpdate.setRewardName(rewardEntity.getRewardName());
                rewardEntityToUpdate.setCustomer(rewardEntity.getCustomer());
                rewardEntityToUpdate.setExpiryDate(rewardEntity.getExpiryDate());
                rewardEntityToUpdate.setStaff(rewardEntity.getStaff());
            } else {
                throw new RewardNotFoundException("Reward ID not provided for tag to be updated");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void deleteReward(Long rewardId) throws RewardNotFoundException, DeleteRewardException {
        RewardEntity rewardEntityToRemove = retrieveRewardByRewardId(rewardId);

        if (rewardEntityToRemove.getCustomer() != null) {
            throw new DeleteRewardException("Reward ID " + rewardId + " is associated with existing customer and cannot be deleted!");
        } else {
            StaffEntity staff = rewardEntityToRemove.getStaff();
            staff.getCreatedRewards().remove(rewardEntityToRemove);
            entityManager.remove(rewardEntityToRemove);
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RewardEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
