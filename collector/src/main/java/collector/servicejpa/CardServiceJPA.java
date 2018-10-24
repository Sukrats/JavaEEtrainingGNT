package collector.servicejpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import collector.entities.Card;
import collector.entities.StandardSet;
import collector.exception.ConstraintViolationException;
import collector.exception.DataNotFoundException;

public class CardServiceJPA {

	String cardTableName = Card.class.getName();
	String setTableName = StandardSet.class.getName();

	@SuppressWarnings("unchecked")
	public List<Card> getCards(Integer setId) {
		List<Card> cards = null;
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();

		entityManager.getTransaction().begin();
		try {
			Query query = null;
			if (setId == null)
				query = entityManager.createQuery("SELECT c FROM " + cardTableName + " c");
			else {
				query = entityManager.createQuery("SELECT c FROM " + cardTableName + " c WHERE c.set_id = :set_id");
				query.setParameter("set_id", setId);
			}

			cards = query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			handleThrownException(e);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
		emFactory.close();

		if (cards == null || cards.isEmpty())
			throw new DataNotFoundException("No Cards to Fetch");
		else
			return cards;
	}

	public Card getCard(Integer userId) {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();

		Card card = entityManager.find(Card.class, userId);

		if (card == null)
			throw new DataNotFoundException("Card Not Found");
		else
			return card;
	}

	public Card addCard(Card card) {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();
		Card postedCard = null;
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(card);
			entityManager.flush();
			entityManager.getTransaction().commit();

		} catch (PersistenceException e) {
			handleThrownException(e);
		}
		entityManager.close();
		emFactory.close();

		postedCard = getCard(card.getCardId());

		if (postedCard == null)
			throw new DataNotFoundException("Card:" + card.getCardName() + " Not Found");
		else
			return postedCard;
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
