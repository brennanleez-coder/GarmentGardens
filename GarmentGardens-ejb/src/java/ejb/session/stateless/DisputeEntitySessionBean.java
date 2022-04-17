/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DisputeEntity;
import entity.OrderEntity;
import entity.ProductEntity;
import entity.StaffEntity;
import entity.UserEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.DisputeStatusEnum;
import util.exception.ApproveDisputeException;
import util.exception.DeleteDisputeException;
import util.exception.DisputeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OrderNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateDisputeException;
import util.exception.UserNotFoundException;
import util.exception.CreateNewDisputeException;

/**
 *
 * @author brennanlee
 */
@Stateless
public class DisputeEntitySessionBean implements DisputeEntitySessionBeanLocal {

    @EJB(name = "UserEntitySessionBeanLocal")
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @EJB(name = "StaffEntitySessionBeanLocal")
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public DisputeEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewDispute(DisputeEntity newDisputeEntity, Long staffId, Long orderId) throws OrderNotFoundException, CreateNewDisputeException, InputDataValidationException {
        Set<ConstraintViolation<DisputeEntity>> constraintViolations = validator.validate(newDisputeEntity);

        if (constraintViolations.isEmpty()) {
            try {
                OrderEntity orderToAssociate = orderEntitySessionBeanLocal.retrieveOrderByOrderId(orderId);
                newDisputeEntity.setOrder(orderToAssociate);

                entityManager.persist(newDisputeEntity);
                entityManager.flush();
                return newDisputeEntity.getDisputeId();
            } catch (OrderNotFoundException ex) {
                throw new OrderNotFoundException("Order " + orderId + " does not exist!");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<DisputeEntity> retrieveAllDisputes() {
        Query query = entityManager.createQuery("SELECT d FROM DisputeEntity d");
        List<DisputeEntity> listOfDisputes = (List<DisputeEntity>) query.getResultList();

        for (DisputeEntity dispute : listOfDisputes) {
            dispute.getStaff();
            dispute.getOrder();
        }

        return listOfDisputes;
    }

    @Override
    public List<DisputeEntity> viewMyDisputes(Long userId) {

        Query query = entityManager.createQuery("SELECT d FROM DisputeEntity d WHERE d.order.customer.userId = :inSearch");

        query.setParameter("inSearch", userId);
        List<DisputeEntity> disputeEntities = query.getResultList();
        for (DisputeEntity de : disputeEntities) {
            System.out.println(de.getTitle());
        }
        return disputeEntities;
    }

    @Override
    public DisputeEntity retrieveDisputeByDisputeId(Long disputeId) throws DisputeNotFoundException {
        DisputeEntity disputeEntity = entityManager.find(DisputeEntity.class, disputeId);

        if (disputeEntity != null) {
            return disputeEntity;
        } else {
            throw new DisputeNotFoundException("Dispute ID " + disputeId + " does not exist!");
        }
    }

    @Override
    public DisputeEntity updateDispute(DisputeEntity disputeEntity) throws UpdateDisputeException, DisputeNotFoundException {
        
        DisputeEntity disputeEntityToUpdate = retrieveDisputeByDisputeId(disputeEntity.getDisputeId());
        if (disputeEntityToUpdate == null) {
            if (disputeEntityToUpdate.getTitle().equals(disputeEntity.getTitle())) {
                disputeEntityToUpdate.setTitle(disputeEntity.getTitle());
                disputeEntityToUpdate.setDescription(disputeEntity.getDescription());
                disputeEntityToUpdate.setDisputeStatus(disputeEntity.getDisputeStatus());
                return disputeEntityToUpdate;

            } else {
                throw new UpdateDisputeException("Dispute Id to be updated does not match the existing record");
            }
        } else {
            throw new DisputeNotFoundException("Dispute ID: " + disputeEntity.getDisputeId() + "cannot be found");
        }
    }

    @Override
    public void approveDispute(DisputeEntity disputeEntity, Long staffId, Long orderId) throws ApproveDisputeException, DisputeNotFoundException, StaffNotFoundException, OrderNotFoundException {
        DisputeEntity dispute = retrieveDisputeByDisputeId(disputeEntity.getDisputeId());
        StaffEntity staff = staffEntitySessionBeanLocal.retrieveStaffByStaffId(staffId);
        OrderEntity order = orderEntitySessionBeanLocal.retrieveOrderByOrderId(orderId);
        if (dispute == null) {
            throw new DisputeNotFoundException("Dispute Not Found, ID:" + dispute.getDisputeId());
        } else {
            if(dispute.getDisputeStatus().equals(DisputeStatusEnum.RESOLVED)) {
                throw new ApproveDisputeException("Dispute has already been approved");
            } else {
                dispute.setDisputeStatus(DisputeStatusEnum.RESOLVED);
                dispute.setStaff(staff);
                dispute.setOrder(order);
            }
        }

    }

    @Override
    public void deleteDispute(Long disputeId) throws DisputeNotFoundException, DeleteDisputeException {
        DisputeEntity disputeEntityToRemove = retrieveDisputeByDisputeId(disputeId);
        if (disputeEntityToRemove != null) {
            if (disputeEntityToRemove.getStaff() == null && !disputeEntityToRemove.getDisputeStatus().equals(DisputeStatusEnum.RESOLVED)) {
                disputeEntityToRemove.getOrder().setDispute(null);
                entityManager.remove(disputeEntityToRemove);
            } else {
                // New in v4.1 to prevent deleting staff with existing sale transaction(s)
                throw new DeleteDisputeException("Dispute ID " + disputeId + " is associated with existing Staff or has already been RESOLVED and cannot be deleted!");
            }
        } else {
            throw new DisputeNotFoundException("Dispute cannot be found, ID: " + disputeId);
        }

    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<DisputeEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
