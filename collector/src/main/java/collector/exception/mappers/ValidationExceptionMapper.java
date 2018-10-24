package collector.exception.mappers;

import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import collector.model.ErrorMessage;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

	@Override
	public Response toResponse(ValidationException exception) {
		ErrorMessage message = new ErrorMessage("Values cannot be null", Status.BAD_REQUEST.getStatusCode());

		return Response
				.status(Status.BAD_REQUEST)
				.entity(message)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
