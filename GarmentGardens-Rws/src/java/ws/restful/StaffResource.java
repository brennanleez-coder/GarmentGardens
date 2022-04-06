package ws.restful;

import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.StaffEntity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;



@Path("Staff")
public class StaffResource 
{
    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;

    
    
    public StaffResource() 
    {
        sessionBeanLookup = new SessionBeanLookup();
        
        staffEntitySessionBeanLocal = sessionBeanLookup.lookupStaffEntitySessionBeanLocal();
    }
    
    
    
    @Path("staffLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response staffLogin(@QueryParam("username") String username, 
                                @QueryParam("password") String password)
    {
        try
        {
            StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
            System.out.println("********** StaffResource.staffLogin(): Staff " + staffEntity.getUsername() + " login remotely via web service");

            staffEntity.setPassword(null);
            staffEntity.setSalt(null);
            //staffEntity.getSaleTransactions().clear();            
            
            return Response.status(Status.OK).entity(staffEntity).build();
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