package collector.exception.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import collector.model.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;

@Provider
public class ExpiredJwtExceptionMapper implements ExceptionMapper<ExpiredJwtException>{

	@Override
	public Response toResponse(ExpiredJwtException exception) {
		Integer status = Response.Status.UNAUTHORIZED.getStatusCode();
		ErrorMessage message = new ErrorMessage(exception.getMessage(), status );
		
		return Response.status(status)
				.entity(message)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
