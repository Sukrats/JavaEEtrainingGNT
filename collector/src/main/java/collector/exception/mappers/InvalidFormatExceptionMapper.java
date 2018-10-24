package collector.exception.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import collector.model.ErrorMessage;

@Provider
public class InvalidFormatExceptionMapper  implements ExceptionMapper<InvalidFormatException >{

    @Override
    public Response toResponse(InvalidFormatException exception)
    {
    	String message = "This is an invalid request. At least one field format is not readable by the system.";
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(message,Response.Status.BAD_REQUEST.getStatusCode() ))
                .type( MediaType.APPLICATION_JSON)
                .build();
    }
    
}
