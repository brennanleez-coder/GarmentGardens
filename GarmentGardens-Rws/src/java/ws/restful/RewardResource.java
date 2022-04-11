/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.RewardEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.RewardEntity;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.RewardNotFoundException;

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

    /**
     * Creates a new instance of RewardResource
     */
    public RewardResource() {
        sessionBeanLookup = new SessionBeanLookup();
        staffEntitySessionBeanLocal = sessionBeanLookup.lookupStaffEntitySessionBeanLocal();
        rewardEntitySessionBeanLocal = sessionBeanLookup.lookupRewardEntitySessionBeanLocal();
    }

    @Path("retrieveAllRewards")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRewards() {
        try {

            List<RewardEntity> listOfRewards = rewardEntitySessionBeanLocal.retrieveAllRewards();

            for (RewardEntity reward : listOfRewards) {

                reward.getCustomer();
                reward.getStaff();
            }

            GenericEntity<List<RewardEntity>> genericEntity = new GenericEntity<List<RewardEntity>>(listOfRewards) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveReward/{rewardId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRewardByRewardId(@PathParam("rewardId") Long rewardId) {
        try {

            RewardEntity reward = rewardEntitySessionBeanLocal.retrieveRewardByRewardId(rewardId);

            reward.getCustomer();
            reward.getStaff();

            return Response.status(Status.OK).entity(reward).build();
        } catch (RewardNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

}
