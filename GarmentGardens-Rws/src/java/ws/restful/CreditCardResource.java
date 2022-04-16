/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.CreditCardEntity;
import entity.ProductEntity;
import entity.UserEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserNotFoundException;
import ws.datamodel.CreateCreditCardReq;
import ws.datamodel.UpdateCreditCardReq;

/**
 * REST Web Service
 *
 * @author brennanlee
 */
@Path("CreditCard")
public class CreditCardResource {
    
    private final CreditCardEntitySessionBeanLocal creditCardEntitySessionBeanLocal;
    private final UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CreditCardResource
     */
    private final SessionBeanLookup sessionBeanLookup;
    
    public CreditCardResource() {
        sessionBeanLookup = new SessionBeanLookup();
        
        creditCardEntitySessionBeanLocal = sessionBeanLookup.lookupCreditCardEntitySessionBeanLocal();
        userEntitySessionBeanLocal = sessionBeanLookup.lookupUserEntitySessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.CreditCardResource
     *
     * @return an instance of java.lang.String
     */
    @Path("retrieveAllCreditCards")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCreditCards(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            
            List<CreditCardEntity> creditCardEntities = creditCardEntitySessionBeanLocal.retrieveCreditCardByCreditUserId(userEntity.getUserId());
            for (CreditCardEntity creditCardEntity : creditCardEntities) {
                //creditCardEntity.getAdvertiser();
                creditCardEntity.setUser(null);
                System.out.println("********** CreditCardResource.retrieveAllCreditCards(): User " + userEntity.getUserId() + " has card ID " + creditCardEntity.getCreditCardId());
            }
            System.out.println("********** CreditCardResource.retrieveAllCreditCards(): User has total of " + creditCardEntities.size() + " Credit Card ");
            GenericEntity<List<CreditCardEntity>> genericEntity = new GenericEntity<List<CreditCardEntity>>(creditCardEntities) {
            };
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
        
    }
    
    @Path("retrieveCreditCard/{creditCardId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCard(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("creditCardId") Long creditCardId) {
        
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            
            CreditCardEntity creditCardEntity = creditCardEntitySessionBeanLocal.retrieveCreditCardByCreditCardId(creditCardId);
            creditCardEntity.getAdvertiser();
            creditCardEntity.getUser();
            
            GenericEntity<CreditCardEntity> genericEntity = new GenericEntity<CreditCardEntity>(creditCardEntity) {
            };
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (CreditCardNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCreditCard(CreateCreditCardReq createCreditCardReq) {
        if (createCreditCardReq != null) {
            try {
                UserEntity userEntity = userEntitySessionBeanLocal.userLogin(createCreditCardReq.getUsername(), createCreditCardReq.getPassword());
                System.out.println("********** CreditCardResource.createCreditCard(): User " + createCreditCardReq.getUsername() + " login remotely via web service");
                CreditCardEntity creditCardEntity = createCreditCardReq.getNewCC();
                creditCardEntity.setBillingAddress(userEntity.getAddress());
                creditCardEntity.setUser(userEntity);
                
                CreditCardEntity creditCardId = creditCardEntitySessionBeanLocal.createNewCreditCardEntity(creditCardEntity);
                System.out.println("Successfully Created Credit Card");
                
                return Response.status(Response.Status.OK).entity(creditCardId.getCreditCardId()).build();
            } catch (CreateNewCreditCardException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new product request").build();
        }
    }
    
    @Path("{userId}/{creditCardId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCard(@PathParam("userId") Long userId, @PathParam("creditCardId") Long creditCardId) 
    {
        try 
        {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserByUserId(userId);
            System.out.println("********** CreditCardResource.deleteCreditCard(): User " + userId + " login remotely via web service");
            System.out.println("********** CreditCardResource.deleteCreditCard(): User " + userId + " wants to remove Credit Card " + creditCardId);
            List<CreditCardEntity> cards = creditCardEntitySessionBeanLocal.retrieveCreditCardByCreditUserId(userId);
            for (CreditCardEntity cc: cards) {
                System.out.println("********** CreditCardResource.deleteCreditCard(): User " + userId + " has card ID " + cc.getCreditCardId());
            }
            creditCardEntitySessionBeanLocal.deleteCreditCard(userEntity, creditCardId);
            return Response.status(Status.OK).build();
        } 
        catch (UserNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
        catch (CreditCardNotFoundException | DeleteCreditCardException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } 
        catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
}
