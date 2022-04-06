package ws.restful;

import ejb.session.stateless.TagEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.StaffEntity;
import entity.TagEntity;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;



@Path("Tag")
public class TagResource
{    
    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    private final TagEntitySessionBeanLocal tagEntitySessionBeanLocal;

    
    
    public TagResource() 
    {
        sessionBeanLookup = new SessionBeanLookup();
        
        staffEntitySessionBeanLocal = sessionBeanLookup.lookupStaffEntitySessionBeanLocal();
        tagEntitySessionBeanLocal = sessionBeanLookup.lookupTagEntitySessionBeanLocal();
    }
    
    
    
    @Path("retrieveAllTags")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTags(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try
        {
            StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
            System.out.println("********** TagResource.retrieveAllTags(): Staff " + staffEntity.getUsername() + " login remotely via web service");

            List<TagEntity> tagEntities = tagEntitySessionBeanLocal.retrieveAllTags();
            
            for(TagEntity tagEntity:tagEntities)
            {                
                tagEntity.getProducts().clear();
            }
            
            GenericEntity<List<TagEntity>> genericEntity = new GenericEntity<List<TagEntity>>(tagEntities) {
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