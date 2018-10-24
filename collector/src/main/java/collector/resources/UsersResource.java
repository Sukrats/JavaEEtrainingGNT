package collector.resources;

import java.net.URI;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import collector.entities.User;
import collector.jwt.JWTTokenNeeded;
import collector.jwt.SimpleKeyGenerator;
import collector.model.Link;
import collector.servicejpa.UserServiceJPA;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {
	UserServiceJPA uJPAService = new UserServiceJPA();
	
	SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
	SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

	@Context
	UriInfo uriInfo;

	@GET
	@JWTTokenNeeded
	public Response getUsers() {
		List<User> users = uJPAService.getUsers();
		for(User user: users){
			addLinks(user);
		}
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {
		};
		
		return Response.ok().entity(entity).build();
	}

	@POST
	public Response registerUser(@Valid User user) {
		System.out.println(user.toString());
		User postedUser = uJPAService.addUser(user);
		addLinks(postedUser);
		
		return Response.status(Status.CREATED).entity(postedUser).build();
	}

	@Path("/{userId}")
	@GET
	@JWTTokenNeeded
	public Response getUser(@PathParam("userId") Integer userId) {
		User user = uJPAService.getUser(userId);
		
		addLinks(user);

		return Response.ok().entity(user).build();
	}

	@POST
	@Path("/login")
	public Response authenticateUser(@Valid User user) {
		try {

			String token = issueToken(user.getUsername());

			return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}
	
	private void addLinks(User user) {
		user.addLink(new Link(uriForSelf(user),"self"));
		user.addLink(new Link(uriForTrades(user),"trades"));
		user.addLink(new Link(uriForCollection(user),"collection"));
	}

	private String issueToken(String login) {
		Key key;
		String jwtToken = null;
		key = keyGenerator.generateKey(algorithm);
		jwtToken = Jwts.builder()
				.setSubject(login)
				.setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date())
				.setExpiration(
						Date.from(LocalDateTime.now().plusMinutes(45L).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(key, algorithm).compact();
		return jwtToken;
	}

	@Path("/{userId}/trades")
	public TradesResource getTrades() {
		return new TradesResource();
	}

	@Path("/{userId}/collection")
	public CollectionResource getCollection() {
		return new CollectionResource();
	}
	
	private URI uriForSelf( User user) {
		return uriInfo.getBaseUriBuilder()
				.path(UsersResource.class)
				.path(UsersResource.class,"getUser")
				.resolveTemplate("userId", user.getUser_id())
				.build();
	
	}
	
	private URI uriForCollection( User user) {
		return  uriInfo.getBaseUriBuilder()
				.path(UsersResource.class)
				.path(UsersResource.class,"getUser")
				.resolveTemplate("userId", user.getUser_id())
				.path("collection")
				.build();
		 
	}
	private URI uriForTrades( User user) {
		return  uriInfo.getBaseUriBuilder()
				.path(UsersResource.class)
				.path(UsersResource.class,"getUser")
				.resolveTemplate("userId", user.getUser_id())
				.path("trades")
				.build();
		 
	}
}
