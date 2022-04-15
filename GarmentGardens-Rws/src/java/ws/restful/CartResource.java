/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CartEntitySessionBeanLocal;
import ejb.session.stateless.LineItemEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.CartEntity;
import entity.LineItemEntity;
import entity.ProductEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CartNotFoundException;
import util.exception.InputDataValidationException;
import ws.datamodel.UpdateCartReq;

/**
 * REST Web Service
 *
 * @author ryyant
 */
@Path("Cart")
public class CartResource {

    @Context
    private UriInfo context;
    private final SessionBeanLookup sessionBeanLookup;
    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    private final LineItemEntitySessionBeanLocal lineItemEntitySessionBeanLocal;
    private final UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    private final CartEntitySessionBeanLocal cartEntitySessionBeanLocal;

    /**
     * Creates a new instance of RewardResource
     */
    public CartResource() {
        sessionBeanLookup = new SessionBeanLookup();
        staffEntitySessionBeanLocal = sessionBeanLookup.lookupStaffEntitySessionBeanLocal();
        lineItemEntitySessionBeanLocal = sessionBeanLookup.lookupLineItemEntitySessionBeanLocal();
        userEntitySessionBeanLocal = sessionBeanLookup.lookupUserEntitySessionBeanLocal();
        cartEntitySessionBeanLocal = sessionBeanLookup.lookupCartEntitySessionBeanLocal();
    }

    // RETRIEVE ALL USEFUL CART INFORMATION 
    @Path("retrieveCart/{cartId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartByCartId(@PathParam("cartId") Long cartId
    ) {
        System.out.println("Retrieving Cart");

        try {
            CartEntity cart = cartEntitySessionBeanLocal.retrieveCartByCartId(cartId);

            List<LineItemEntity> cartLineItems = cart.getCartLineItems();
            for (LineItemEntity lineItem : cartLineItems) {
                lineItem.getProduct().setSeller(null);
                lineItem.getProduct().setCategory(null);
                lineItem.getProduct().getTags().clear();
                lineItem.getProduct().getRatings().clear();
            }

            List<UserEntity> groupCustomers = cart.getGroupCustomers();
            for (UserEntity gc : groupCustomers) {
                gc.getCreditCards().clear();
                gc.getOrders().clear();
                gc.getRewards().clear();
                gc.setGroupCart(null);
                gc.setIndividualCart(null);
            }

            if (cart.getGroupOwner() != null) {
                cart.getGroupOwner().getCreditCards().clear();
                cart.getGroupOwner().getOrders().clear();
                cart.getGroupOwner().getRewards().clear();
                cart.getGroupOwner().setGroupCart(null);
                cart.getGroupOwner().setIndividualCart(null);
            }

            cart.getCustomer().getCreditCards().clear();
            cart.getCustomer().getOrders().clear();
            cart.getCustomer().getRewards().clear();
            cart.getCustomer().setGroupCart(null);
            cart.getCustomer().setIndividualCart(null);

            System.out.println("Retrieved Cart");
            System.out.println(cart);

            return Response.status(Status.OK).entity(cart).build();
        } catch (CartNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    // RETRIEVE MY CART WITH INFO
    @Path("retrieveMyCart/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveMyCart(@PathParam("userId") Long userId
    ) {
        System.out.println("Retrieving Cart");

        try {
            CartEntity cart = cartEntitySessionBeanLocal.retrieveIndividualCartByUserId(userId);

            List<LineItemEntity> cartLineItems = cart.getCartLineItems();
            for (LineItemEntity lineItem : cartLineItems) {
                lineItem.getProduct().setSeller(null);
                lineItem.getProduct().setCategory(null);
                lineItem.getProduct().getTags().clear();
                lineItem.getProduct().getRatings().clear();
            }

            List<UserEntity> groupCustomers = cart.getGroupCustomers();
            for (UserEntity gc : groupCustomers) {
                gc.getCreditCards().clear();
                gc.getOrders().clear();
                gc.getRewards().clear();
                gc.setGroupCart(null);
                gc.setIndividualCart(null);
            }

            if (cart.getGroupOwner() != null) {
                cart.getGroupOwner().getCreditCards().clear();
                cart.getGroupOwner().getOrders().clear();
                cart.getGroupOwner().getRewards().clear();
                cart.getGroupOwner().setGroupCart(null);
                cart.getGroupOwner().setIndividualCart(null);
            }

            cart.getCustomer().getCreditCards().clear();
            cart.getCustomer().getOrders().clear();
            cart.getCustomer().getRewards().clear();
            cart.getCustomer().setGroupCart(null);
            cart.getCustomer().setIndividualCart(null);

            System.out.println("Retrieved Cart");
            System.out.println(cart);

            return Response.status(Status.OK).entity(cart).build();
        } catch (CartNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("addToCart")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToCart(UpdateCartReq updateCartReq) {

        if (updateCartReq != null) {
            try {
                UserEntity userEntity = updateCartReq.getCurrentUser();
                ProductEntity productToAdd = updateCartReq.getProductToAdd();
                BigDecimal unitPrice = productToAdd.getUnitPrice();
                int qtyToAdd = updateCartReq.getQtyToAdd();

                // CREATE NEW LINE ITEM AND PERSIST
                LineItemEntity newLineItem = new LineItemEntity(qtyToAdd, unitPrice);
                newLineItem.setProduct(productToAdd);
                lineItemEntitySessionBeanLocal.createLineItem(newLineItem);
                System.out.println(lineItemEntitySessionBeanLocal.retrieveLineItemById(newLineItem.getLineItemId()));
                
                // ADD LINE ITEM TO THE CART
                cartEntitySessionBeanLocal.addLineItemToCart(newLineItem, userEntity);
                                
                System.out.println("Added to cart successfully ****");
                return Response.status(Response.Status.OK).build();
            } catch (InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid add to cart request").build();
        }
    }
}
