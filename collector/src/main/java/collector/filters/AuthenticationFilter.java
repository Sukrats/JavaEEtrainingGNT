package collector.filters;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import collector.jwt.SimpleKeyGenerator;
import collector.model.Credentials;
import collector.model.ErrorMessage;
import collector.servicejpa.UserServiceJPA;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	UserServiceJPA uService = new UserServiceJPA();
	SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
	SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

	private static final String AUTH_URL_PREFIX = "login";
	private static final String AUTH_HEADER_PREFIX = "Basic ";

	private static final String NO_AUTH = "NO AUTHORIZATION";
	private static final String WRONG_AUTH = "UNAUTHORIZED";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		if (requestContext.getUriInfo().getPath().contains(AUTH_URL_PREFIX)) {
			basicAuthentication(requestContext);
		} else {
			jwtAuthentication(requestContext);
		}
	}

	private void basicAuthentication(ContainerRequestContext requestContext) {
		String auth = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (auth == null || auth.length() <= 0) {
			requestContext
					.abortWith(Response.status(Response.Status.UNAUTHORIZED).header("Content-Type", "application/json")
							.entity(new ErrorMessage(NO_AUTH, Status.UNAUTHORIZED.getStatusCode())).build());
			return;
		}
		Credentials credentials = decodeCredsFromHeader(auth);
		if (credentials.getUsername().equals("") || credentials.getPassword().equals("")) {
			requestContext
					.abortWith(Response.status(Response.Status.UNAUTHORIZED).header("Content-Type", "application/json")
							.entity(new ErrorMessage(NO_AUTH, Status.UNAUTHORIZED.getStatusCode())).build());
			return;
		}

		if (!uService.authenticateUser(credentials.getUsername(), credentials.getPassword())) {
			requestContext
					.abortWith(Response.status(Response.Status.UNAUTHORIZED).header("Content-Type", "application/json")
							.entity(new ErrorMessage(WRONG_AUTH, Status.UNAUTHORIZED.getStatusCode())).build());
			return;
		}
	}

	private void jwtAuthentication(ContainerRequestContext requestContext) {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null || authorizationHeader.length() <= 0) {
			requestContext
					.abortWith(Response.status(Response.Status.UNAUTHORIZED).header("Content-Type", "application/json")
							.entity(new ErrorMessage("JWT AUTH: NO AUTHORIZATION", Status.UNAUTHORIZED.getStatusCode()))
							.build());
			return;
		}
		String token = authorizationHeader.substring("Bearer".length()).trim();
		try {
			Key key = keyGenerator.generateKey(algorithm);
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);

		} catch (Exception e) {
			e.printStackTrace();
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					.header("Content-Type", "application/json")
					.entity(new ErrorMessage("JWT AUTH: UNAUTHORIZED", Status.UNAUTHORIZED.getStatusCode())).build());
		}
	}

	private Credentials decodeCredsFromHeader(String value) {

		Credentials cred = new Credentials();
		String encodedStr = value.replaceFirst(AUTH_HEADER_PREFIX, "");
		String decodedStr = Base64.decodeAsString(encodedStr);

		String[] credentials = decodedStr.split(":");
		cred.setUsername(credentials[0]);
		cred.setPassword(credentials[1]);

		return cred;
	}

}
