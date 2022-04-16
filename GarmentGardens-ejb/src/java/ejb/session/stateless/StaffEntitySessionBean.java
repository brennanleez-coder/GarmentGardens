package ejb.session.stateless;

import entity.StaffEntity;
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
import util.exception.ChangePasswordException;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateStaffException;
import util.security.CryptographicHelper;
//import util.security.CryptographicHelper;

@Stateless

public class StaffEntitySessionBean implements StaffEntitySessionBeanLocal {

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    // Added in v4.2 for bean validation
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StaffEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // Updated in v4.1
    // Updated in v4.2 with bean validation
    @Override
    public Long createNewStaff(StaffEntity newStaffEntity) throws StaffUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<StaffEntity>> constraintViolations = validator.validate(newStaffEntity);

        if (constraintViolations.isEmpty()) {
            try {
                entityManager.persist(newStaffEntity);
                entityManager.flush();

                return newStaffEntity.getStaffId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new StaffUsernameExistException();
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
    public List<StaffEntity> retrieveAllStaffs() {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");

        return query.getResultList();
    }

    @Override
    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);

        if (staffEntity != null) {
            return staffEntity;
        } else {
            throw new StaffNotFoundException("Staff ID " + staffId + " does not exist!");
        }
    }

    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (StaffEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }

    // Updated in v4.5 to include password hashing
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staffEntity.getSalt()));
            //String passwordHash = String.valueOf(password.hashCode());
            if (staffEntity.getPassword().equals(passwordHash)) {
                staffEntity.getDisputes().size();
                return staffEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (StaffNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    // Updated in v4.1 to update selective attributes instead of merging the entire state passed in from the client
    // Also check for existing staff before proceeding with the update
    // Updated in v4.2 with bean validation
    @Override
    public void updateStaff(StaffEntity staffEntity) throws StaffNotFoundException, UpdateStaffException, InputDataValidationException {
        if (staffEntity != null && staffEntity.getStaffId() != null) {
            Set<ConstraintViolation<StaffEntity>> constraintViolations = validator.validate(staffEntity);

            if (constraintViolations.isEmpty()) {
                StaffEntity staffEntityToUpdate = retrieveStaffByStaffId(staffEntity.getStaffId());

                if (staffEntityToUpdate.getUsername().equals(staffEntity.getUsername())) {
                    staffEntityToUpdate.setFirstName(staffEntity.getFirstName());
                    staffEntityToUpdate.setLastName(staffEntity.getLastName());
                    staffEntityToUpdate.setAccessRightEnum(staffEntity.getAccessRightEnum());
                    // Username and password are deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
                } else {
                    throw new UpdateStaffException("Username of staff record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new StaffNotFoundException("Staff ID not provided for staff to be updated");
        }
    }

    // Updated in v4.1
    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException, DeleteStaffException {
        StaffEntity staffEntityToRemove = retrieveStaffByStaffId(staffId);

        if (staffEntityToRemove.getDisputes().isEmpty()) {
            entityManager.remove(staffEntityToRemove);
        } else {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteStaffException("Staff ID " + staffId + " is associated with existing dispute(s) and cannot be deleted!");
        }
    }

    @Override
    public void staffChangePassword(String username, String oldPassword, String newPassword) throws ChangePasswordException, InvalidLoginCredentialException {

        StaffEntity staffEntity = staffLogin(username, oldPassword);

        if (!newPassword.isEmpty() && newPassword != null) {
            staffEntity.setPassword(newPassword);
        } else {
            throw new ChangePasswordException("Password is not provided");
        }

    }

    // Added in v4.2
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<StaffEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
