/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.DisputeEntitySessionBeanLocal;
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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
    
    /**
     * Creates a new instance of DisputeResource
     */
    public DisputeResource() {
        sessionBeanLookup = new SessionBeanLookup();
        userEntitySessionBeanLocal = sessionBeanLookup.lookupUserEntitySessionBeanLocal();
        disputeEntitySessionBeanLocal = sessionBeanLookup.lookupDisputeEntitySessionBeanLocal();
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
    
    
    


    
}
