/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CartEntity;
import entity.LineItemEntity;
import entity.OrderEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CheckoutException;
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
    @EJB
    private CartEntitySessionBeanLocal cartEntitySessionBeanLocal;

    public OrderEntitySessionBean() {
    }

    // Updated in v4.1
    @Override
    public OrderEntity createNewOrder(Long customerId, OrderEntity newOrderEntity) throws InputDataValidationException, UserNotFoundException, ProductNotFoundException, ProductInsufficientQuantityOnHandException, CreateNewOrderException {
        if (newOrderEntity != null) {
            try {
                UserEntity customerEntity = userEntitySessionBeanLocal.retrieveUserByUserId(customerId);
                newOrderEntity.setCustomer(customerEntity);
                entityManager.persist(newOrderEntity);

                for (LineItemEntity lineItemEntity : newOrderEntity.getLineItems()) {
                    productEntitySessionBeanLocal.debitQuantityOnHand(lineItemEntity.getProduct().getProductId(), lineItemEntity.getQuantity());
                    lineItemEntitySessionBeanLocal.createLineItem(lineItemEntity);
                }

                entityManager.flush();

                return newOrderEntity;
            } catch (InputDataValidationException | UserNotFoundException | ProductNotFoundException | ProductInsufficientQuantityOnHandException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();

                throw new CreateNewOrderException(ex.getMessage());
            }
        } else {
            throw new CreateNewOrderException("Sale transaction information not provided");
        }
    }

    @Override
    public List<OrderEntity> retrieveAllOrders() {
        Query query = entityManager.createQuery("SELECT o FROM OrderEntity o");
        
        return query.getResultList();
    }

    @Override
    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException {
        OrderEntity orderEntity = entityManager.find(OrderEntity.class, orderId);

        if (orderEntity != null) {
            orderEntity.getLineItems().size();

            return orderEntity;
        } else {
            throw new OrderNotFoundException("Order ID " + orderId + " does not exist!");
        }
    }

//    @Override
//    public void updateOrder(OrderEntity orderEntity) throws OrderNotFoundException
//    {
//        try {
//            entityManager.find(OrderEntity.class, orderEntity.getOrderId());
//            entityManager.merge(orderEntity);
//            entityManager.flush();
//        } catch (Exception ex) {
//            throw new OrderNotFoundException("Order ID " + orderEntity.getOrderId() + " does not exist!");
//        }
//    }
//    
    @Override
    public void voidRefundOrder(Long orderId) throws OrderNotFoundException, OrderAlreadyVoidRefundedException {
        OrderEntity orderEntity = retrieveOrderByOrderId(orderId);

        if (!orderEntity.getVoidRefund()) {
            for (LineItemEntity lineItemEntity : orderEntity.getLineItems()) {
                try {
                    productEntitySessionBeanLocal.creditQuantityOnHand(lineItemEntity.getProduct().getProductId(), lineItemEntity.getQuantity());
                } catch (ProductNotFoundException ex) {
                    ex.printStackTrace(); // Ignore exception since this should not happen
                }
            }

            orderEntity.setVoidRefund(true);
            entityManager.flush();
        } else {
            throw new OrderAlreadyVoidRefundedException("The sale transaction has aready been voided/refunded");
        }
    }

    public List<List<OrderEntity>> retrieveAllOrdersInPastYear() {
        List<List<OrderEntity>> orderEntities = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            orderEntities.add(new ArrayList<>());
        }

        LocalDateTime now = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);

        for (int i = 11; i >= 0; i--) {
            Query query = entityManager.createQuery("SELECT o FROM OrderEntity o WHERE o.transactionDateTime BETWEEN :inStartDate AND :inEndDate ORDER BY o.transactionDateTime ASC");
            query.setParameter("inStartDate", now);
            query.setParameter("inEndDate", now.plusMonths(1));
            orderEntities.set(i, query.getResultList());

            now = now.minusMonths(1);
        }

        return orderEntities;

    }

    @Override
    public void deleteOrder(OrderEntity orderEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkout(UserEntity userEntity, String promoCode) throws CheckoutException {
        try {
            // PROMO CODE LOGIC HERE

            // GET PERSISTED ENTITIES
            UserEntity user = userEntitySessionBeanLocal.retrieveUserByUserId(userEntity.getUserId());
            CartEntity cart = cartEntitySessionBeanLocal.retrieveIndividualCartByUserId(userEntity.getUserId());

            // GET ALL CART INFO
            List<LineItemEntity> lineItems = cart.getCartLineItems();
            int totalCartitems = cart.getTotalCartItems();
            int totalQuantity = cart.getTotalQuantity();
            BigDecimal totalAmount = cart.getTotalAmount();
            
            // CREATE NEW ORDER & FILL ORDER DETAILS
            OrderEntity order = new OrderEntity();
            order.setLineItems(lineItems);
            order.setTotalOrderItem(totalCartitems);
            order.setTotalQuantity(totalQuantity);
            order.setTotalAmount(totalAmount);
            
            // ASSOCIATE ORDER TO USER
            order.setCustomer(user);
            
            // CLEAR CART ENTITY
            cart.getCartLineItems().clear();
            cart.setTotalCartItems(0);
            cart.setTotalQuantity(0);
            cart.setTotalAmount(new BigDecimal(0));
            
            entityManager.persist(order);
            entityManager.flush();
            
        } catch (Exception ex) {
            throw new CheckoutException("The checkout failed!");
        }

    }

}
