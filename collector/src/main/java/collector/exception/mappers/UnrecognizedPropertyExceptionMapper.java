package collector.exception.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import collector.model.ErrorMessage;


@Provider
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException>{

    @Override
    public Response toResponse(UnrecognizedPropertyException exception)
    {
    	String message = "This is an invalid request. The field " + exception.getPropertyName() + " is not recognized.";
    	
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(message,Response.Status.BAD_REQUEST.getStatusCode()))
                .type( MediaType.APPLICATION_JSON)
                .build();
    }
    
}