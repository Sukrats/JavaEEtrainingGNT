package collector.exception.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonParseException;

import collector.model.ErrorMessage;

@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException>{

    @Override
    public Response toResponse(JsonParseException exception)
    {
    	String message = "This is an invalid json. The request can not be parsed";
    	
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(message,Response.Status.BAD_REQUEST.getStatusCode() ))
                .type( MediaType.APPLICATION_JSON)
                .build();
    }
    
}
