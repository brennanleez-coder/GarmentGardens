/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CartEntitySessionBeanLocal;
import ejb.session.stateless.RewardEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.CartEntity;
import entity.LineItemEntity;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CartNotFoundException;

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
    private final RewardEntitySessionBeanLocal rewardEntitySessionBeanLocal;
    private final UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    private final CartEntitySessionBeanLocal cartEntitySessionBeanLocal;

    /**
     * Creates a new instance of RewardResource
     */
    public CartResource() {
        sessionBeanLookup = new SessionBeanLookup();
        staffEntitySessionBeanLocal = sessionBeanLookup.lookupStaffEntitySessionBeanLocal();
        rewardEntitySessionBeanLocal = sessionBeanLookup.lookupRewardEntitySessionBeanLocal();
        userEntitySessionBeanLocal = sessionBeanLookup.lookupUserEntitySessionBeanLocal();
        cartEntitySessionBeanLocal = sessionBeanLookup.lookupCartEntitySessionBeanLocal();
    }


    @Path("retrieveCart/{cartId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCartByCartId(@PathParam("cartId") Long cartId
    ) {
        try {

            CartEntity cart = cartEntitySessionBeanLocal.retrieveCartByCartId(cartId);

            List<LineItemEntity> cartLineItems = cart.getCartLineItems();
            for (LineItemEntity lineItem : cartLineItems) {
                lineItem.getProduct().setSeller(null);
                lineItem.getProduct().setCategory(null);
                lineItem.getProduct().getTags().clear();
                lineItem.getProduct().getRatings().clear();
            }

            return Response.status(Status.OK).entity(cart).build();
        } catch (CartNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
