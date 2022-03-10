/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RewardEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewRewardException;
import util.exception.DeleteRewardException;
import util.exception.InputDataValidationException;
import util.exception.RewardNotFoundException;
import util.exception.UpdateRewardException;

/**
 *
 * @author rilwa
 */
@Local
public interface RewardEntitySessionBeanLocal {

    public RewardEntity createNewRewardEntity(RewardEntity newRewardEntity) throws InputDataValidationException, CreateNewRewardException;

    public List<RewardEntity> retrieveAllRewards();

    public RewardEntity retrieveRewardByRewardId(Long rewardId) throws RewardNotFoundException;

    public void updateReward(RewardEntity rewardEntity) throws InputDataValidationException, RewardNotFoundException, UpdateRewardException;

    public void deleteReward(Long rewardId) throws RewardNotFoundException, DeleteRewardException;
    
}
