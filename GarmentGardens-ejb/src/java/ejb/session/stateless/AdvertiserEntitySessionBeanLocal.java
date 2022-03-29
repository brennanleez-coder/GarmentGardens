/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdvertiserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AdvertiserEntityExistException;
import util.exception.AdvertiserEntityNotFoundException;
import util.exception.CreateNewAdvertiserEntityException;
import util.exception.DeleteAdvertiserEntityException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author brennanlee
 */
@Local
public interface AdvertiserEntitySessionBeanLocal {

    public AdvertiserEntity createNewAdvertiserEntity(AdvertiserEntity advertiserEntity, List<Long> creditCardIds, List<Long> advertisementIds) throws AdvertiserEntityExistException, UnknownPersistenceException, InputDataValidationException, CreateNewAdvertiserEntityException;

    public List<AdvertiserEntity> retrieveAllAdvertiserEntity();

    public AdvertiserEntity retrieveAdvertiserEntityByAdvertiserId(Long advertiserId) throws AdvertiserEntityNotFoundException;

    public AdvertiserEntity updateAdvertiserEntity(AdvertiserEntity advertiserEntity) throws AdvertiserEntityNotFoundException;

    public AdvertiserEntity deleteAdvertiserEntity(AdvertiserEntity advertiserEntity) throws AdvertiserEntityNotFoundException, DeleteAdvertiserEntityException;

    
    
}
