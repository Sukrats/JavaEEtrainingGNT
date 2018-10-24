package collector.exception.mappers;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import collector.model.ErrorMessage;


@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException>{

	@Override
	public Response toResponse(WebApplicationException exception) {
		Integer status = exception.getResponse().getStatus();
		ErrorMessage message = new ErrorMessage(exception.getMessage(), status );
		
		return Response.status(status)
				.entity(message)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
