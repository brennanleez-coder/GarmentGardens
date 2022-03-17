/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OrderEntity;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface OrderEntitySessionBeanLocal {

    public OrderEntity createNewOrder(Long customerId, OrderEntity newOrderEntity) throws InputDataValidationException, UserNotFoundException, ProductNotFoundException, ProductInsufficientQuantityOnHandException, CreateNewOrderException;

    public List<OrderEntity> retrieveAllOrders();

    public OrderEntity retrieveOrderByOrderId(Long orderId) throws OrderNotFoundException;

    public void updateOrder(OrderEntity orderEntity);

    public void voidRefundOrder(Long orderId) throws OrderNotFoundException, OrderAlreadyVoidRefundedException;

    public void deleteOrder(OrderEntity orderEntity);

}