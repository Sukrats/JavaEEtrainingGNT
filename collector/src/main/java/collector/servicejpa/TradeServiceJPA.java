package collector.servicejpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import collector.entities.Trade;
import collector.exception.ConstraintViolationException;
import collector.exception.DataNotFoundException;

public class TradeServiceJPA {

	@SuppressWarnings("unchecked")
	public List<Trade> getTrades(Integer userId) {

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<Trade> trades = null;
		try {
			Query query = entityManager.createQuery("SELECT t FROM " + Trade.class.getName() + " t WHERE t.user_id =:user_id");
			query.setParameter("user_id", userId);
			trades = query.getResultList();

		} catch (Exception e) {
			handleThrownException(e);
		}
		entityManager.close();
		emFactory.close();

		if (trades.isEmpty())
			throw new DataNotFoundException("User:" + userId + " has no trades");
		else
			return trades;
	}

	public Trade getTrade(Integer tradeId) {
		Trade trade = null;

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();

		try {
			trade = entityManager.find(Trade.class, tradeId);
		} catch (Exception e) {
			e.printStackTrace();
			handleThrownException(e);
		}
		if (trade == null)
			throw new DataNotFoundException("Trade: " + tradeId + " Not Found");
		else
			return trade;
	}

	public Trade addTrade(Trade trade) {
		Trade postedTrade = null;
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();

		try {
			entityManager.getTransaction().begin();
			entityManager.persist(trade);
			entityManager.flush();
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			handleThrownException(e);
		}
		entityManager.close();
		emFactory.close();

		postedTrade = getTrade(trade.getTrade_id());

		if (postedTrade == null)
			throw new DataNotFoundException("Trade:" + trade.getCard_id() + " Not Found");
		else
			return postedTrade;
	}

	public List<Trade> getTradesWithUser(Integer userId, Integer targetUserId){
		List<Trade> trades = null;
		
		//TODO CHECK DEM JOINS
		//TODO Composite SELECT 
		
		return trades;
	}

	private void handleThrownException(Exception e) {
		Throwable t = null;
		for (t = e.getCause(); t != null; t = t.getCause()) {
			System.out.println(t.getClass());
			if (t.getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class))
				throw new ConstraintViolationException(t.getMessage());

		}

	}

}
