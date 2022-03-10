/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DisputeEntity;
import entity.OrderEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteDisputeException;
import util.exception.DisputeNotFoundException;
import util.exception.OrderNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateDisputeException;

/**
 *
 * @author brennanlee
 */
@Stateless
public class DisputeEntitySessionBean implements DisputeEntitySessionBeanLocal {

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;

    @EJB(name = "StaffEntitySessionBeanLocal")
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;

    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;

    public DisputeEntitySessionBean() {
    }

    
    @Override
    public Long createNewDispute(DisputeEntity newDisputeEntity, Long staffId, Long orderId) throws StaffNotFoundException, OrderNotFoundException {

        try {
            StaffEntity staffToAssociate = staffEntitySessionBeanLocal.retrieveStaffByStaffId(staffId);
            OrderEntity orderToAssociate = orderEntitySessionBeanLocal.retrieveOrderByOrderId(orderId);
            newDisputeEntity.setStaff(staffToAssociate);
            newDisputeEntity.setOrder(orderToAssociate);

            entityManager.persist(newDisputeEntity);
            entityManager.flush();
            return newDisputeEntity.getDisputeId();
        } catch (OrderNotFoundException | StaffNotFoundException ex) {
            Logger.getLogger(DisputeEntitySessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    public List<DisputeEntity> retrieveAllDisputes() {
        Query query = entityManager.createQuery("SELECT d FROM DisputeEntity d");

        return query.getResultList();
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
    public void deleteDispute(Long disputeId) throws DisputeNotFoundException, DeleteDisputeException {
        DisputeEntity disputeEntityToRemove = retrieveDisputeByDisputeId(disputeId);

        if (disputeEntityToRemove.getStaff() == null) {
            disputeEntityToRemove.getStaff().getDisputes().remove(disputeEntityToRemove);
            disputeEntityToRemove.getOrder().setDispute(null);
            entityManager.remove(disputeEntityToRemove);
        } else {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteDisputeException("Dispute ID " + disputeId + " is associated with existing dispute(s) and cannot be deleted!");
        }
    }

}
