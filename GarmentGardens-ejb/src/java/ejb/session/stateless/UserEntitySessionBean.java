/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CartEntity;
import entity.LineItemEntity;
import entity.OrderEntity;
import entity.ProductEntity;
import entity.RatingEntity;
import entity.RewardEntity;
import entity.UserEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
import util.enumeration.CartTypeEnum;
import util.enumeration.RoleEnum;
import util.exception.ChangePasswordException;
import util.exception.DeleteRewardException;
import util.exception.DeleteUserException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.RewardNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;
import util.exception.UserUsernameExistException;

/**
 *
 * @author rilwa
 */
@Stateless
public class UserEntitySessionBean implements UserEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    @EJB(name = "RewardEntitySessionBeanLocal")
    private RewardEntitySessionBeanLocal rewardEntitySessionBeanLocal;

    @EJB(name = "RatingEntitySessionBeanLocal")
    private RatingEntitySessionBeanLocal ratingEntitySessionBeanLocal;

    @EJB(name = "ProductEntitySessionBeanLocal")
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;

    @EJB(name = "CartEntitySessionBeanLocal")
    private CartEntitySessionBeanLocal cartEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public UserEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewUser(UserEntity newUserEntity) throws UserUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(newUserEntity);

        if (constraintViolations.isEmpty()) {
            try {
                if (newUserEntity.getRole() == RoleEnum.CUSTOMER) {

                    CartEntity newCartEntity = new CartEntity();
                    newCartEntity.setCustomer(newUserEntity);
                    newCartEntity.setCartType(CartTypeEnum.INDIVIDUALCART);
                    entityManager.persist(newCartEntity);

                    newUserEntity.setIndividualCart(newCartEntity);
                }

                entityManager.persist(newUserEntity);
                entityManager.flush();

                return newUserEntity.getUserId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new UserUsernameExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<UserEntity> retrieveAllUsers() {
        Query query = entityManager.createQuery("SELECT u FROM UserEntity u");
        List<UserEntity> users = query.getResultList();

        for (UserEntity u : users) {
            u.getCreditCards().size();
            u.getRewards().size();
            u.getOrders().size();
            u.getIndividualCart();
            u.getGroupCart();
        }

        return users;
    }

    @Override
    public UserEntity retrieveUserByUserId(Long userId) throws UserNotFoundException {

        try {
            UserEntity staffEntity = entityManager.find(UserEntity.class, userId);

            staffEntity.getCreditCards().size();
            staffEntity.getRewards().size();
            staffEntity.getOrders().size();
            staffEntity.getIndividualCart();
            staffEntity.getGroupCart();

            return staffEntity;

        } catch (Exception ex) {
            throw new UserNotFoundException("User ID " + userId + " does not exist!");

        }

    }

    // FOR FRONTEND CUSTOMER, ONLY LOAD THE ORDERS    
    @Override
    public List<OrderEntity> retrieveUserOrdersOnly(Long userId) throws UserNotFoundException {
        UserEntity customer = entityManager.find(UserEntity.class, userId);
        if (customer != null) {
            List<OrderEntity> orders = customer.getOrders();
            for (OrderEntity order : orders) {
                order.getDispute();
                order.getCustomer();
                List<LineItemEntity> lineItems = order.getLineItems();
                for (LineItemEntity lineItem : lineItems) {
                    lineItem.getProduct();
                }
            }
            System.out.println("retrieveUserOrdersOnly: " + orders);

            return orders;
        } else {
            throw new UserNotFoundException("User ID " + userId + " does not exist!");

        }
    }

    @Override
    public UserEntity retrieveUserByUsername(String username) throws UserNotFoundException {
        Query query = entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (UserEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("User with Username " + username + " does not exist!");
        }
    }

    @Override
    public UserEntity userLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            UserEntity userEntity = retrieveUserByUsername(username);
            //String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password));
            String passwordHash = userEntity.getPassword();
            if (password.equals(passwordHash)) {
                userEntity.getOrders().size();
                return userEntity;
            } else {
                throw new InvalidLoginCredentialException("Invalid Login Credentials: Please try again!!");
            }
        } catch (UserNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist! Please sign up for an account first");
        }
    }

    @Override
    public void updateUser(UserEntity userEntity) throws UserNotFoundException, UpdateUserException, InputDataValidationException {

        if (userEntity != null && userEntity.getUserId() != null) {
            Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

            if (constraintViolations.isEmpty()) {
                UserEntity userEntityToUpdate = retrieveUserByUserId(userEntity.getUserId());

                if (userEntityToUpdate.getUsername().equals(userEntity.getUsername())) {
                    userEntityToUpdate.setFirstName(userEntity.getFirstName());
                    userEntityToUpdate.setLastName(userEntity.getLastName());
                    userEntityToUpdate.setEmail(userEntity.getEmail());
                    userEntityToUpdate.setDateOfBirth(userEntity.getDateOfBirth());
                    userEntityToUpdate.setAddress(userEntity.getAddress());
                    userEntityToUpdate.setCreditCards(userEntity.getCreditCards());
                    userEntityToUpdate.setRewards(userEntity.getRewards());
                    userEntityToUpdate.setOrders(userEntity.getOrders());

                } else {
                    throw new UpdateUserException("Username of user record to be updated does not match the existing record");
                }

            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new UserNotFoundException("User ID not provided for user to be updated");
        }
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException, DeleteUserException {
        UserEntity userEntityToRemove = retrieveUserByUserId(userId);

        if (userEntityToRemove.getOrders().isEmpty()) {
            for (RewardEntity reward : userEntityToRemove.getRewards()) {
                try {
                    rewardEntitySessionBeanLocal.deleteReward(reward.getRewardId());
                } catch (RewardNotFoundException | DeleteRewardException ex) {
                    Logger.getLogger(UserEntitySessionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (userEntityToRemove.getIndividualCart() != null) {
                userEntityToRemove.getIndividualCart().setCustomer(null);
                userEntityToRemove.setIndividualCart(null);
            }
            if (userEntityToRemove.getGroupCart() != null) {
                userEntityToRemove.getGroupCart().getGroupCustomers().clear();
                userEntityToRemove.setGroupCart(null);
            }

            for (RatingEntity rating : ratingEntitySessionBeanLocal.retrieveRatingsByUserId(userId)) {
                ratingEntitySessionBeanLocal.deleteRating(rating);
            }

            for (ProductEntity product : productEntitySessionBeanLocal.retrieveProductsBySellerId(userId)) {
                product.setSeller(null);
            }

            entityManager.remove(userEntityToRemove);

        } else {
            throw new DeleteUserException("User ID " + userId + " is associated with existing order(s) and cannot be deleted!");
        }
    }
    
    public void userChangePassword(UserEntity userEntity, String newPassword) throws UserNotFoundException, ChangePasswordException, InputDataValidationException {

        if (userEntity != null && userEntity.getUserId() != null) {
            Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

            if (constraintViolations.isEmpty()) {
                UserEntity userEntityToUpdate = retrieveUserByUserId(userEntity.getUserId());

                if (userEntityToUpdate.getUsername().equals(userEntity.getUsername())) {
                    userEntityToUpdate.setPassword(newPassword);

                } else {
                    throw new ChangePasswordException("New Password provided is not valid");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new UserNotFoundException("User ID not provided for user to chnage password");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<UserEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
