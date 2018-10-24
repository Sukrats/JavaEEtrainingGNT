package collector.resources;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import collector.entities.Trade;
import collector.jwt.JWTTokenNeeded;
import collector.servicejpa.TradeServiceJPA;

@Path("/")
@JWTTokenNeeded
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TradesResource {
	TradeServiceJPA tService = new TradeServiceJPA();

	@GET
	public Response getTrades(@PathParam("userId") Integer userId, @QueryParam("user") Integer tradedUserId) {
		List<Trade> trades = null;
		if (tradedUserId == null || tradedUserId == 0)
			trades = tService.getTrades(userId);
		else
			trades = tService.getTradesWithUser(userId, tradedUserId);

		GenericEntity<List<Trade>> entity = new GenericEntity<List<Trade>>(trades) {
		};

		return Response.ok().entity(entity).build();
	}

	@POST
	public Response registerTrade(@PathParam("userId") Integer userId, @Valid Trade trade) {
		Trade postedTrades = tService.addTrade(trade);

		return Response.status(Status.CREATED).entity(postedTrades).build();
	}

	@Path("/{tradeId}")
	@GET
	public Response getTrade(@PathParam("tradeId") Integer tradeId) {
		Trade trade = tService.getTrade(tradeId);

		return Response.ok().entity(trade).build();
	}

}
