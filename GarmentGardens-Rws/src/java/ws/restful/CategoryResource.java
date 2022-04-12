package ws.restful;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.StaffEntity;
import entity.UserEntity;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CategoryNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Path("Category")
public class CategoryResource {

    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

//    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    private final CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
//    private final UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    public CategoryResource() {
        sessionBeanLookup = new SessionBeanLookup();

//        userEntitySessionBeanLocal = sessionBeanLookup.lookupUserEntitySessionBeanLocal();
        categoryEntitySessionBeanLocal = sessionBeanLookup.lookupCategoryEntitySessionBeanLocal();
    }

    @Path("retrieveCategory/{categoryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCategoryByCategoryId(@PathParam("categoryId") Long categoryId) {
        try {

            CategoryEntity categoryEntity = categoryEntitySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

            categoryEntity.getSubCategories().clear();
            categoryEntity.getProducts().clear();
            categoryEntity.setParentCategory(null);
            

            return Response.status(Status.OK).entity(categoryEntity).build();
        } catch (CategoryNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories() {
        try {

            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllCategories();

            for (CategoryEntity categoryEntity : categoryEntities) {
                if (categoryEntity.getParentCategory() != null) {
                    categoryEntity.getParentCategory().getSubCategories().clear();
                }

                categoryEntity.getSubCategories().clear();
                categoryEntity.getProducts().clear();
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveOnlyParentCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOnlyParentCategories() {
        try {

            List<CategoryEntity> rootCategories = categoryEntitySessionBeanLocal.retrieveAllRootCategories();

            for (CategoryEntity categoryEntity : rootCategories) {
                categoryEntity.getSubCategories().clear();
                categoryEntity.getProducts().clear();
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(rootCategories) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("getSubCategories/{categoryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubCategories(@PathParam("categoryId") Long categoryId) {
        try {

            List<CategoryEntity> subCategories = categoryEntitySessionBeanLocal.getSubCategories(categoryId);
            for (CategoryEntity categoryEntity : subCategories) {
                if (categoryEntity.getParentCategory() != null) {
                    categoryEntity.getParentCategory().getSubCategories().clear();
                    categoryEntity.setParentCategory(null);
                }
                categoryEntity.getSubCategories().clear();
                categoryEntity.getProducts().clear();
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(subCategories) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (CategoryNotFoundException ex) {
            return Response.status(Status.OK).build();
        }
    }

    @Path("retrieveAllLeafCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllLeafCategories() {
        try {

            List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllLeafCategories();

            for (CategoryEntity categoryEntity : categoryEntities) {
                if (categoryEntity.getParentCategory() != null) {
                    categoryEntity.getParentCategory().getSubCategories().clear();
                }

                categoryEntity.getSubCategories().clear();
                categoryEntity.getProducts().clear();
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categoryEntities) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
