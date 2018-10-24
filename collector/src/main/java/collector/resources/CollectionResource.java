package collector.resources;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import collector.entities.Collection;
import collector.jwt.JWTTokenNeeded;
import collector.servicejpa.CollectionServiceJPA;

@Path("/")
@JWTTokenNeeded
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CollectionResource {
	CollectionServiceJPA cService = new CollectionServiceJPA();

	@GET
	public Response getCollections(@PathParam("userId") Integer userId) {
		List<Collection> collection = cService.getCollections(userId);
		GenericEntity<List<Collection>> entity = new GenericEntity<List<Collection>>(collection) {
		};
		return Response.ok().entity(entity).build();
	}

	@POST
	public Response addCollection(@PathParam("userId") Integer userId,@Valid Collection collection) {
		Collection postedCollection = cService.addCollection(userId, collection);
		
		return Response.status(Status.CREATED)
				.entity(postedCollection)
				.build();
	}

	@Path("/{cardId}")
	@GET
	public Response getCollection(@PathParam("cardId") Integer cardId, @PathParam("userId") Integer userId) {
		Collection collection = cService.getCollection( userId, cardId);

		return Response.ok()
				.entity(collection)
				.build();
	}


}
