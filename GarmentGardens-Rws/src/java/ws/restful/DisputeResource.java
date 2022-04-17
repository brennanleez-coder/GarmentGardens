/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.DisputeEntitySessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.DisputeEntity;
import entity.OrderEntity;
import entity.StaffEntity;
import entity.UserEntity;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.enumeration.DisputeStatusEnum;
import util.exception.CreateNewDisputeException;
import util.exception.InvalidLoginCredentialException;
import ws.datamodel.CreateDisputeReq;

/**
 * REST Web Service
 *
 * @author wong
 */
@Path("Dispute")
public class DisputeResource {

    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final DisputeEntitySessionBeanLocal disputeEntitySessionBeanLocal;
    private final UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    private final OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;
    
    /**
     * Creates a new instance of DisputeResource
     */
    public DisputeResource() {
        sessionBeanLookup = new SessionBeanLookup();
        userEntitySessionBeanLocal = sessionBeanLookup.lookupUserEntitySessionBeanLocal();
        disputeEntitySessionBeanLocal = sessionBeanLookup.lookupDisputeEntitySessionBeanLocal();
        orderEntitySessionBeanLocal = sessionBeanLookup.lookupOrderEntitySessionBeanLocal();
    }
    
    @Path("retrieveAllDisputes")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllDisputes() {
        try {

            List<DisputeEntity> disputeEntities = disputeEntitySessionBeanLocal.retrieveAllDisputes();
            //productEntities = productEntities.subList(0, 10);
            for (DisputeEntity disputeEntity : disputeEntities) {
                //StaffEntity staff = staffEntitySessionBeanLocal.retrieveStaffByStaffId(disputeEntity.getStaff().getStaffId());
                
                disputeEntity.setStaff(null);
                disputeEntity.setOrder(null);     
            }
            
            GenericEntity<List<DisputeEntity>> genericEntity = new GenericEntity<List<DisputeEntity>>(disputeEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();

        } catch (Exception ex) {
            System.out.println("Exception");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("viewMyDisputes")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewMyDisputes(@QueryParam("username") String username) {
        try {
            UserEntity user = userEntitySessionBeanLocal.retrieveUserByUsername(username);
            System.out.println(user);
            List<DisputeEntity> disputeEntities = disputeEntitySessionBeanLocal.viewMyDisputes(user.getUserId());
            //productEntities = productEntities.subList(0, 10);
            for (DisputeEntity disputeEntity : disputeEntities) {
                //StaffEntity staff = staffEntitySessionBeanLocal.retrieveStaffByStaffId(disputeEntity.getStaff().getStaffId());
                
                disputeEntity.setStaff(null);
                disputeEntity.setOrder(null);     
            }
            
            GenericEntity<List<DisputeEntity>> genericEntity = new GenericEntity<List<DisputeEntity>>(disputeEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();

        } catch (Exception ex) {
            System.out.println("Exception");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewDispute(CreateDisputeReq createDisputeReq) {
        if (createDisputeReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createDisputeReq.getUsername(), createDisputeReq.getPassword());
                System.out.println("********** DisputeResource.createNewDispute(): User(SELLER) " + userEntity.getUsername() + " login remotely via web service");

                
                OrderEntity order = orderEntitySessionBeanLocal.retrieveOrderByOrderId(Long.valueOf(createDisputeReq.getOrderId()));
                DisputeEntity disputeEntity = new DisputeEntity();
                disputeEntity.setTitle(createDisputeReq.getTitle());
                disputeEntity.setDescription(createDisputeReq.getDescription());
                disputeEntity.setDisputeStatus(DisputeStatusEnum.PENDING);
                
                Long newDisputeEntity = disputeEntitySessionBeanLocal.createNewDispute(disputeEntity, null, order.getOrderId());

                return Response.status(Response.Status.OK).entity(newDisputeEntity).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new dispute request").build();
        }
    }
    
    
    


    
}
