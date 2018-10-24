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

import collector.entities.StandardSet;
import collector.servicejpa.SetServiceJPA;

@Path("/sets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SetsResource {
	SetServiceJPA sService = new SetServiceJPA();

	@GET
	public Response getSets() {
		List<StandardSet> sets = sService.getSets();
		GenericEntity<List<StandardSet>> entity = new GenericEntity<List<StandardSet>>(sets) {
		};

		return Response.ok().entity(entity).build();
	}

	@POST
	public Response addSet(@Valid StandardSet set) {
		StandardSet newSet = sService.addSet(set);

		return Response.status(Status.CREATED)
				.entity(newSet)
				.build();
	}

	@Path("/{setId}")
	@GET
	public Response getSet(@PathParam("setId") Integer setId) {
		StandardSet set = sService.getSet(setId);

		return Response.ok()
				.entity(set)
				.build();
	}
	@Path("/{setId}/cards")
	public CardsResource getSetCards() {
		return new CardsResource();
	}
	

}
