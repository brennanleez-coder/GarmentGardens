/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LineItemEntity;
import entity.OrderEntity;
import entity.UserEntity;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewOrderException;
import util.exception.InputDataValidationException;
import util.exception.OrderAlreadyVoidRefundedException;
import util.exception.OrderNotFoundException;
import util.exception.ProductInsufficientQuantityOnHandException;
import util.exception.ProductNotFoundException;
import util.exception.UserNotFoundException;

/**
 *
 * @author ryyant
 */
@Stateless
public class OrderEntitySessionBean implements OrderEntitySessionBeanLocal {

    
    @PersistenceContext(unitName = "GarmentGardens-ejbPU")
    private EntityManager entityManager;
    @Resource
    private EJBContext eJBContext;
    

    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    @EJB
    private ProductEntitySessionBeanLocal productEntitySessionBeanLocal;
    @EJB
    private LineItemEntitySessionBeanLocal lineItemEntitySessionBeanLocal;
    
    public OrderEntitySessionBean()
    {
    }
    
    
    // Updated in v4.1
    
    @Override
    public OrderEntity createNewOrder(Long customerId, OrderEntity newOrderEntity) throws InputDataValidationException, UserNotFoundException, ProductNotFoundException, ProductInsufficientQuantityOnHandException, CreateNewOrderException
    {
        if(newOrderEntity != null)
        {
            try
            {
                UserEntity customerEntity = userEntitySessionBeanLocal.retrieveUserByUserId(customerId);
                newOrderEntity.setCustomer(customerEntity);
                entityManager.persist(newOrderEntity);

                for(LineItemEntity lineItemEntity:newOrderEntity.getLineItems())
                {
                    productEntitySessionBeanLocal.debitQuantityOnHand(lineItemEntity.getProduct().getProductId(), lineItemEntity.getQuantity());
                    lineItemEntitySessionBeanLocal.createLineItem(lineItemEntity);
                }

                entityManager.flush();
 
                return newOrderEntity;
            }
            catch(InputDataValidationException | UserNotFoundException | ProductNotFoundException | ProductInsufficientQuantityOnHandException ex)
            {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();
                
                throw new CreateNewOrderException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewOrderException("Sale transaction information not provided");
        }
    }
    
    
    
    @Override
    public List<OrderEntity> retrieveAllOrders()
    {
        Query query = entityManager.createQuery("SELECT o FROM OrderEntity o");
        
        return query.getResultList();
    }
    
    
    
    // Added in v4.1
    
//    @Override
//    public List<OrderEntity> retrieveLineItemsByProductId(Long productId)
//    {
//        Query query = entityManager.createNamedQuery("selectAllSaleTransactionLineItemsByProductId");
//        query.setParameter("inProductId", productId);
//        
//        return query.getResultList();
//    }
    
    
    
    @Override
    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException
    {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, orderId);
        
        if(orderEntity != null)
        {
            orderEntity.getLineItems().size();
            
            return orderEntity;
        }
        else
        {
            throw new OrderNotFoundException("Order ID " + orderId + " does not exist!");
        }                
    }
    
    
    
    @Override
    public void updateOrder(OrderEntity orderEntity)
    {
        entityManager.merge(orderEntity);
    }
    
    
    
    // Updated in v4.1
    
    @Override
    public void voidRefundOrder(Long orderId) throws OrderNotFoundException, OrderAlreadyVoidRefundedException
    {
        OrderEntity orderEntity = retrieveOrderByOrderId(orderId);
        
        if(!orderEntity.getVoidRefund())
        {
            for(LineItemEntity lineItemEntity:orderEntity.getLineItems())
            {
                try
                {
                    productEntitySessionBeanLocal.creditQuantityOnHand(lineItemEntity.getProduct().getProductId(), lineItemEntity.getQuantity());
                }
                catch(ProductNotFoundException ex)
                {
                    ex.printStackTrace(); // Ignore exception since this should not happen
                }                
            }
            
            orderEntity.setVoidRefund(true);
        }
        else
        {
            throw new OrderAlreadyVoidRefundedException("The sale transaction has aready been voided/refunded");
        }
    }
    
    
    
    @Override
    public void deleteOrder(OrderEntity orderEntity)
    {
        throw new UnsupportedOperationException();
    }
}
