package collector.exception.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import collector.exception.ConstraintViolationException;
import collector.model.ErrorMessage;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		ErrorMessage message = new ErrorMessage(exception.getMessage(), Status.CONFLICT.getStatusCode());
		return Response.status(Status.CONFLICT).entity(message).build();
	}

}