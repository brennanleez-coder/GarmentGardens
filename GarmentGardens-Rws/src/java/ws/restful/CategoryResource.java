package ws.restful;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.StaffEntity;
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



@Path("Category")
public class CategoryResource
{    
    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    private final CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    
    
    public CategoryResource() 
    {
        sessionBeanLookup = new SessionBeanLookup();
        
        staffEntitySessionBeanLocal = sessionBeanLookup.lookupStaffEntitySessionBeanLocal();
        categoryEntitySessionBeanLocal = sessionBeanLookup.lookupCategoryEntitySessionBeanLocal();
    }
    
    
    
    @Path("retrieveAllCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories(@QueryParam("username") String username, 
                                        @QueryParam("password") String password)
    {
        try
        {
            StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
            System.out.println("********** CategoryResource.retrieveAllCategories(): Staff " + staffEntity.getUsername() + " login remotely via web service");

            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();
            
            for(CategoryEntity categoryEntity:categoryEntities)
            {
                if(categoryEntity.getParentCategory() != null)
                {
                    categoryEntity.getParentCategory().getSubCategories().clear();
                }
                
                categoryEntity.getSubCategories().clear();
                categoryEntity.getProducts().clear();
            }
            
            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
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
    
    
    
    @Path("retrieveAllLeafCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllLeafCategories(@QueryParam("username") String username, 
                                                @QueryParam("password") String password)
    {
        try
        {
            StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
            System.out.println("********** CategoryResource.retrieveAllCategories(): Staff " + staffEntity.getUsername() + " login remotely via web service");

            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllLeafCategories();
            
            for(CategoryEntity categoryEntity:categoryEntities)
            {
                if(categoryEntity.getParentCategory() != null)
                {
                    categoryEntity.getParentCategory().getSubCategories().clear();
                }
                
                categoryEntity.getSubCategories().clear();
                categoryEntity.getProducts().clear();
            }
            
            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
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