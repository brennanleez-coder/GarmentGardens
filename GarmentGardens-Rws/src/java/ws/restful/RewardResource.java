/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.RewardEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.RewardEntity;
import entity.StaffEntity;
import entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InputDataValidationException;
import util.exception.RedeemRewardException;
import util.exception.RewardNotFoundException;
import util.exception.UpdateRewardException;
import util.exception.UpdateUserException;
import util.exception.UseRewardException;
import util.exception.UserNotFoundException;
import ws.datamodel.UpdateRewardReq;

/**
 * REST Web Service
 *
 * @author brennanlee
 */
@Path("Reward")
public class RewardResource {

    @Context
    private UriInfo context;
    private final SessionBeanLookup sessionBeanLookup;
    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    private final RewardEntitySessionBeanLocal rewardEntitySessionBeanLocal;
    private final UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    /**
     * Creates a new instance of RewardResource
     */
    public RewardResource() {
        sessionBeanLookup = new SessionBeanLookup();
        staffEntitySessionBeanLocal = sessionBeanLookup.lookupStaffEntitySessionBeanLocal();
        rewardEntitySessionBeanLocal = sessionBeanLookup.lookupRewardEntitySessionBeanLocal();
        userEntitySessionBeanLocal = sessionBeanLookup.lookupUserEntitySessionBeanLocal();
    }

    @Path("retrieveAllRewards")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRewards() {
        try {

            List<RewardEntity> listOfRewards = rewardEntitySessionBeanLocal.retrieveAllRewards();

            for (RewardEntity reward : listOfRewards) {
                reward.setCustomer(null);
                reward.setStaff(null);
            }

            GenericEntity<List<RewardEntity>> genericEntity = new GenericEntity<List<RewardEntity>>(listOfRewards) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveRewardsByUserId/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRewardsByUserId(@PathParam("userId") Long userId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserByUserId(userId);
            userEntity.getOrders().clear();
            userEntity.getCreditCards().clear();
            userEntity.setIndividualCart(null);
            userEntity.setGroupCart(null);

            for (RewardEntity reward : userEntity.getRewards()) {
                reward.setCustomer(null);
                reward.setStaff(null);
            }

            GenericEntity<List<RewardEntity>> genericEntity = new GenericEntity<List<RewardEntity>>(userEntity.getRewards()) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (UserNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();

        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAvailableRewards")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAvailableRewards() {
        try {

            List<RewardEntity> listOfRewards = rewardEntitySessionBeanLocal.retrieveAvailableRewards();

            for (RewardEntity reward : listOfRewards) {
                reward.setCustomer(null);
                reward.setStaff(null);
            }

            GenericEntity<List<RewardEntity>> genericEntity = new GenericEntity<List<RewardEntity>>(listOfRewards) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("redeemReward/{rewardId}/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response redeemReward(@PathParam("rewardId") Long rewardId,
            @PathParam("userId") Long userId) {

        try {
            rewardEntitySessionBeanLocal.redeemReward(rewardId, userId);

            return Response.status(Response.Status.OK).build();
        } catch (RewardNotFoundException | UpdateRewardException | UpdateUserException | RedeemRewardException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @Path("useReward/{rewardId}/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response useReward(@PathParam("rewardId") Long rewardId,
            @PathParam("userId") Long userId) {

        try {
            rewardEntitySessionBeanLocal.useReward(rewardId, userId);
            return Response.status(Response.Status.OK).build();
        } catch (RewardNotFoundException | UseRewardException | UpdateUserException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @Path("retrieveReward/{rewardId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRewardByRewardId(@PathParam("rewardId") Long rewardId
    ) {
        try {

            RewardEntity reward = rewardEntitySessionBeanLocal.retrieveRewardByRewardId(rewardId);

            reward.setCustomer(null);
            reward.setStaff(null);

            return Response.status(Status.OK).entity(reward).build();
        } catch (RewardNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("getRewardByCode/{promoCode}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRewardByCode(@PathParam("promoCode") Integer promoCode
    ) {
        try {
            RewardEntity reward = rewardEntitySessionBeanLocal.retrieveRewardByPromoCode(promoCode);

            reward.setCustomer(null);
            reward.setStaff(null);

            return Response.status(Status.OK).entity(reward).build();
        } catch (RewardNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

}
