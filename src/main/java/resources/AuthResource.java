package resources;

import dtos.AuthTokenRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import results.Result;
import services.UserService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class AuthResource {

    private final UserService userService;

    @Inject
    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/token")
    public Response createToken(AuthTokenRequestDTO authTokenRequestDTO) {
        var result = userService.authenticate(authTokenRequestDTO);

        if (result.failure()) {
            if (result.errors().stream().anyMatch(e -> e.contains("Credenciais inválidas"))) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(Result.failure(result.errors())).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(Result.failure(result.errors())).build();
        }

        return Response.ok(result.data()).build();
    }
}
