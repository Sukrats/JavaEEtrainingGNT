package collector.exception.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import collector.exception.DataNotFoundException;
import collector.model.ErrorMessage;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException exception) {
		
			ErrorMessage message = new ErrorMessage(exception.getMessage(), Status.NOT_FOUND.getStatusCode());
			return Response.status(Status.NOT_FOUND)
					.type(MediaType.APPLICATION_JSON)
					.entity(message)
					.build();
	}

}