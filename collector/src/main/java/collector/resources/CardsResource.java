package collector.resources;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import collector.entities.Card;
import collector.servicejpa.CardServiceJPA;

@Path("/cards")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardsResource {

	CardServiceJPA cService = new CardServiceJPA();
	

	@GET
	public Response getCards(@Context UriInfo uriInfo) {
		Integer setId = null;
		if (uriInfo.getPathParameters().containsKey("setId"))
			setId = Integer.parseInt(uriInfo.getPathParameters().get("setId").get(0));
		List<Card> cards = cService.getCards(setId);
		GenericEntity<List<Card>> entity = new GenericEntity<List<Card>>(cards) {
		};

		return Response.ok().entity(entity).build();
	}

	@POST
	public Response addCard(@Valid Card card) {
		Card newCard = cService.addCard(card);

		return Response.status(Status.CREATED).entity(newCard).build();
	}

	@Path("/{cardId}")
	@GET
	public Response getCard(@PathParam("cardId") Integer cardId) {
		Card card = cService.getCard(cardId);

		return Response.ok().entity(card).build();
	}

}
