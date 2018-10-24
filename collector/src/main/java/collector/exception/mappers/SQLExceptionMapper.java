package collector.exception.mappers;

import java.sql.SQLException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import collector.model.ErrorMessage;

@Provider
public class SQLExceptionMapper implements ExceptionMapper<SQLException> {

	@Override
	public Response toResponse(SQLException exception) {
		ErrorMessage message = new ErrorMessage(exception.getMessage(), Status.INTERNAL_SERVER_ERROR.getStatusCode());

		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity(message)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
