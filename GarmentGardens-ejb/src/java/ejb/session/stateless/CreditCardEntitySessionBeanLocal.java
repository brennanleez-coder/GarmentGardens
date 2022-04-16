/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.UserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCreditCardException;

/**
 *
 * @author rilwa
 */
@Local
public interface CreditCardEntitySessionBeanLocal {

    public CreditCardEntity createNewCreditCardEntity(CreditCardEntity newCreditCardEntity) throws InputDataValidationException, CreateNewCreditCardException;

    public List<CreditCardEntity> retrieveAllCreditCards();

    public CreditCardEntity retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException;

    public void updateCreditCard(CreditCardEntity creditCard) throws InputDataValidationException, CreditCardNotFoundException, UpdateCreditCardException;

    public void deleteCreditCard(UserEntity user, Long creditCardId) throws CreditCardNotFoundException, DeleteCreditCardException;

    public List<CreditCardEntity> retrieveCreditCardByCreditUserId(Long userId) throws CreditCardNotFoundException;
    
}
