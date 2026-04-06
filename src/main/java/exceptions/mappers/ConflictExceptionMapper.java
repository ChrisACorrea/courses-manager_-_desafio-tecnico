package exceptions.mappers;

import java.util.List;

import exceptions.ConflictException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import results.Result;

@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {

    @Override
    public Response toResponse(ConflictException exception) {
        var response = Result.failure(List.of(exception.getMessage()));

        return Response.status(Response.Status.CONFLICT)
                .entity(response)
                .build();
    }
}
