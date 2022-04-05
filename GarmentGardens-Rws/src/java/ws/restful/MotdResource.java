package ws.restful;

import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.MessageOfTheDayEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;



@Path("Motd")
public class MotdResource 
{    
    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    private final MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    
    
    public MotdResource() 
    {
        sessionBeanLookup = new SessionBeanLookup();
        
        staffEntitySessionBeanLocal = sessionBeanLookup.lookupStaffEntitySessionBeanLocal();
        messageOfTheDayEntitySessionBeanLocal = sessionBeanLookup.lookupMessageOfTheDaySessionBeanLocal();
    }
    
    
    
    @Path("retrieveAllMotds")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllMotds(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try
        {
            StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
            System.out.println("********** MotdResource.retrieveAllMotds(): Staff " + staffEntity.getUsername() + " login remotely via web service");

            List<MessageOfTheDayEntity> motdEntities = messageOfTheDayEntitySessionBeanLocal.retrieveAllMessagesOfTheDay();
            
            GenericEntity<List<MessageOfTheDayEntity>> genericEntity = new GenericEntity<List<MessageOfTheDayEntity>>(motdEntities) {
            };
            
            return Response.status(Status.OK).entity(genericEntity).build();
        }
        catch(InvalidLoginCredentialException ex)
        {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
        catch(Exception ex)
        {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}