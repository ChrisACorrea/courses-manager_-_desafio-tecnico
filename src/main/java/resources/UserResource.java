package resources;

import java.net.URI;

import org.eclipse.microprofile.jwt.JsonWebToken;

import dtos.UserCreateDTO;
import dtos.UserReadDTO;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import results.Result;
import services.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class UserResource {

    private final UserService userService;

    @Inject
    JsonWebToken jwt;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    public Response createUser(UserCreateDTO userCreateDTO) {
        var result = userService.createUser(userCreateDTO);

        if (result.failure()) {
            var response = Result.failure(result.errors());
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        }

        var userDto = UserReadDTO.fromEntity(result.data());
        return Response.created(URI.create("/users/" + userDto.id())).entity(userDto).build();
    }

    @GET
    @Path("/me")
    @Authenticated
    public Response getCurrentUser() {
        var user = userService.getUserByEmail(jwt.getName());
        var userDto = UserReadDTO.fromEntity(user);

        return Response.ok(userDto).build();
    }
}
