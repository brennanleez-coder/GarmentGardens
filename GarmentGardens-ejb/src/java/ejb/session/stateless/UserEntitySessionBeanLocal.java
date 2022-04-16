/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import entity.UserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ChangePasswordException;
import util.exception.DeleteUserException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;
import util.exception.UserUsernameExistException;

/**
 *
 * @author rilwa
 */
@Local
public interface UserEntitySessionBeanLocal {

    public Long createNewUser(UserEntity newUserEntity) throws UserUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<UserEntity> retrieveAllUsers();

    public UserEntity retrieveUserByUserId(Long userId) throws UserNotFoundException;

    public UserEntity retrieveUserByUsername(String username) throws UserNotFoundException;

    public UserEntity userLogin(String username, String password) throws InvalidLoginCredentialException;

    public void updateUser(UserEntity userEntity) throws UserNotFoundException, UpdateUserException, InputDataValidationException;

    public void deleteUser(Long userId) throws UserNotFoundException, DeleteUserException;

    public List<OrderEntity> retrieveUserOrdersOnly(Long userId) throws UserNotFoundException;

    public void userChangePassword(UserEntity userEntity, String newPassword) throws UserNotFoundException, ChangePasswordException, InputDataValidationException;
    
}
