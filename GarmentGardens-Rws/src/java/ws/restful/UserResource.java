package ws.restful;

import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.UserEntity;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.ChangePasswordException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;
import util.exception.UserUsernameExistException;
import ws.datamodel.ChangePasswordReq;
import ws.datamodel.CreateUserReq;
import ws.datamodel.UpdateProfileReq;

/**
 * REST Web Service
 *
 * @author rilwa
 */
@Path("User")
public class UserResource {

    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;

    private final UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    /**
     * Creates a new instance of UserResource
     */
    public UserResource() {

        sessionBeanLookup = new SessionBeanLookup();

        userEntitySessionBeanLocal = sessionBeanLookup.lookupUserEntitySessionBeanLocal();
    }

    @Path("userLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.userLogin(username, password);
            System.out.println("********** UserResource.userLogin(): User " + userEntity.getUsername() + " login remotely via web service");

            // DISASSOCIATE 
            userEntity.setPassword(null);
            userEntity.setPassword(null);
            userEntity.getOrders().clear();
            userEntity.getRewards().clear();
            userEntity.getCreditCards().clear();
            userEntity.setIndividualCart(null);
            userEntity.setGroupCart(null);

            return Response.status(Status.OK).entity(userEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserReq createUserReq) {
        if (createUserReq != null) {
            try {

                Long userId = userEntitySessionBeanLocal.createNewUser(createUserReq.getUserEntity());

                return Response.status(Response.Status.OK).entity(userId).build();
            } catch (UserUsernameExistException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (UnknownPersistenceException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new user request").build();
        }
    }

    @Path("retrieveUserByUserId/{userId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveUserByUserId(@PathParam("userId") Long userId) {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserByUserId(userId);

            return Response.status(Response.Status.OK).entity(userEntity).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("updateProfile")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(UpdateProfileReq updateProfileReq) {

        if (updateProfileReq != null) {

            try {
                UserEntity userEntity = updateProfileReq.getCurrentUser();
                userEntitySessionBeanLocal.updateUser(userEntity);
                
                System.out.println(" Profile updated successfully ****");
                return Response.status(Response.Status.OK).build();
            } catch (UserNotFoundException | InputDataValidationException | UpdateUserException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid user profile update request").build();
        }
    }
    
    @Path("changePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordReq changePasswordReq) {

        if (changePasswordReq != null) {

            try {
                UserEntity userEntity = changePasswordReq.getCurrentUser();
                userEntitySessionBeanLocal.userChangePassword(userEntity, changePasswordReq.getNewPassword());
                
                System.out.println("Password updated successfully ****");
                return Response.status(Response.Status.OK).build();
            } catch (UserNotFoundException | InputDataValidationException | ChangePasswordException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid user profile update request").build();
        }
    }
    
}
