/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.ProductEntitySessionBeanLocal;
import ejb.session.stateless.RatingEntitySessionBeanLocal;
import entity.ProductEntity;
import entity.RatingEntity;
import entity.UserEntity;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UserUsernameExistException;
import ws.datamodel.CreateRatingReq;

/**
 * REST Web Service
 *
 * @author brennanlee
 */
@Path("Rating")
public class RatingResource {

    @Context
    private UriInfo context;
    private final SessionBeanLookup sessionBeanLookup;
    private final RatingEntitySessionBeanLocal ratingEntitySessionBeanLocal;
    private final ProductEntitySessionBeanLocal productEntitySessionBeanLocal;

    /**
     * Creates a new instance of RatingResource
     */
    public RatingResource() {
        sessionBeanLookup = new SessionBeanLookup();
        ratingEntitySessionBeanLocal = sessionBeanLookup.lookupRatingEntitySessionBeanLocal();
        productEntitySessionBeanLocal = sessionBeanLookup.lookupProductEntitySessionBeanLocal();
    }

    @Path("retrieveRatingsByProductId/{productId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRewardsByUserId(@PathParam("productId") Long productId) {
        try {
            ProductEntity productEntity = productEntitySessionBeanLocal.retrieveProductByProductId(productId);
            List<RatingEntity> listOfRatings = productEntity.getRatings();

            for (RatingEntity rating : listOfRatings) {
                UserEntity customer = rating.getCustomer();
                customer.getRewards().clear();
                customer.getCreditCards().clear();
                customer.setIndividualCart(null);
                customer.setGroupCart(null);
                customer.getOrders().clear();
            }

            GenericEntity<List<RatingEntity>> genericEntity = new GenericEntity<List<RatingEntity>>(listOfRatings) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();

        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("rateProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response rateProduct(CreateRatingReq createRatingReq) {
        if (createRatingReq != null) {
            try {
                System.out.println(createRatingReq);

                RatingEntity rating = ratingEntitySessionBeanLocal.rateProduct(createRatingReq.getUser(), createRatingReq.getProduct(), createRatingReq.getNewRating());
                rating.setCustomer(null);
                System.out.println(rating);
                return Response.status(Response.Status.OK).entity(rating).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new user request").build();
        }
    }
}
