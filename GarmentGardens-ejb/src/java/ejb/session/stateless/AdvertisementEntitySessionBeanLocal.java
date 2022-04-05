/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdvertisementEntity;
import javax.ejb.Local;
import util.exception.AdvertiserEntityNotFoundException;
import util.exception.CreateNewAdvertisementException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author brennanlee
 */
@Local
public interface AdvertisementEntitySessionBeanLocal {

    public AdvertisementEntity createNewAdvertiserEntity(AdvertisementEntity advertisementEntity, Long advertiserId) throws AdvertiserEntityNotFoundException, UnknownPersistenceException, InputDataValidationException, CreateNewAdvertisementException;
    
}
