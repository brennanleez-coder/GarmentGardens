/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DisputeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteDisputeException;
import util.exception.DisputeNotFoundException;
import util.exception.OrderNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateDisputeException;

/**
 *
 * @author brennanlee
 */
@Local
public interface DisputeEntitySessionBeanLocal {

    public Long createNewDispute(DisputeEntity newDisputeEntity, Long staffId, Long orderId) throws StaffNotFoundException, OrderNotFoundException;

    public List<DisputeEntity> retrieveAllDisputes();

    public DisputeEntity retrieveDisputeByDisputeId(Long disputeId) throws DisputeNotFoundException;

    public void deleteDispute(Long disputeId) throws DisputeNotFoundException, DeleteDisputeException;

    public DisputeEntity updateDispute(DisputeEntity disputeEntity) throws UpdateDisputeException, DisputeNotFoundException;
    
}
