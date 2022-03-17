/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;
import entity.UserEntity;
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
import util.exception.DeleteUserException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;
import util.exception.UserUsernameExistException;
import util.security.CryptographicHelper;


/**
 *
 * @author rilwa
 */
@Stateless
public class UserEntitySessionBean implements UserEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public UserEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Long createNewUser(UserEntity newUserEntity) throws UserUsernameExistException, UnknownPersistenceException, InputDataValidationException
    {
        Set<ConstraintViolation<UserEntity>>constraintViolations = validator.validate(newUserEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                entityManager.persist(newUserEntity);
                entityManager.flush();

                return newUserEntity.getUserId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new UserUsernameExistException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<UserEntity> retrieveAllUsers()
    {
        Query query = entityManager.createQuery("SELECT u FROM UserEntity u");
        
        return query.getResultList();
    }
    
    @Override
    public UserEntity retrieveUserByUserId(Long userId) throws UserNotFoundException
    {
        UserEntity staffEntity = entityManager.find(UserEntity.class, userId);
        
        if(staffEntity != null)
        {
            return staffEntity;
        }
        else
        {
            throw new UserNotFoundException("User ID " + userId + " does not exist!");
        }
    }
    
    @Override
    public UserEntity retrieveUserByUsername(String username) throws UserNotFoundException
    {
        Query query = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (UserEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new UserNotFoundException("User with Username " + username + " does not exist!");
        }
    }
    
    @Override
    public UserEntity userLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            UserEntity userEntity = retrieveUserByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password));            
            if(userEntity.getPassword().equals(passwordHash))
            {
                userEntity.getOrders().size();                
                return userEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(UserNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public void updateUser(UserEntity userEntity) throws UserNotFoundException, UpdateUserException, InputDataValidationException
    {
        if(userEntity != null && userEntity.getUserId() != null)
        {
            Set<ConstraintViolation<UserEntity>>constraintViolations = validator.validate(userEntity);
        
            if(constraintViolations.isEmpty())
            {
                UserEntity userEntityToUpdate = retrieveUserByUserId(userEntity.getUserId());

                if(userEntityToUpdate.getUsername().equals(userEntity.getUsername()))
                {
                    userEntityToUpdate.setFirstName(userEntity.getFirstName());
                    userEntityToUpdate.setLastName(userEntity.getLastName());
                    userEntityToUpdate.setEmail(userEntity.getEmail());
                    userEntityToUpdate.setDateOfBirth(userEntity.getDateOfBirth());
                    userEntityToUpdate.setAddress(userEntity.getAddress());
                }
                else
                {
                    throw new UpdateUserException("Username of user record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new UserNotFoundException("User ID not provided for user to be updated");
        }
    }
    
    @Override
    public void deleteUser(Long userId) throws UserNotFoundException, DeleteUserException
    {
        UserEntity userEntityToRemove = retrieveUserByUserId(userId);
        
        if(userEntityToRemove.getOrders().isEmpty())
        {
            entityManager.remove(userEntityToRemove);
        }
        else
        {
            throw new DeleteUserException("User ID " + userId + " is associated with existing order(s) and cannot be deleted!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<UserEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
    
}