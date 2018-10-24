package collector.servicejpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import collector.entities.Collection;
import collector.entities.CollectionPK;
import collector.exception.ConstraintViolationException;
import collector.exception.DataNotFoundException;

public class CollectionServiceJPA {

	@SuppressWarnings("unchecked")
	public List<Collection> getCollections(Integer userId) {

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();
		entityManager.getTransaction().begin();

		List<Collection> users = entityManager.createQuery("SELECT c FROM " + Collection.class.getName() + " c")
				.getResultList();

		entityManager.close();
		emFactory.close();

		if (users.isEmpty())
			throw new DataNotFoundException("No Collections to Fetch");
		else
			return users;
	}

	public Collection getCollection(Integer userId, Integer cardId) {
		Collection collection = null;

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();

		try {
			collection = entityManager.find(Collection.class, new CollectionPK(userId, cardId));
		} catch (Exception e) {
			e.printStackTrace();
			handleThrownException(e);
		}
		if (collection == null)
			throw new DataNotFoundException("Collection: " + userId + "-" + cardId + " Not Found");
		else
			return collection;
	}

	public Collection addCollection(Integer userId, Collection collection) {
		Collection postedCollection = null;
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();

		try {
			entityManager.getTransaction().begin();

			if (!checkDuplicates(userId, collection.getCard_id(), entityManager))
				entityManager.persist(collection);
			else {
				postedCollection = entityManager.find(Collection.class,	new CollectionPK(userId, collection.getCard_id()));
				int copies = postedCollection.getCopies();
				postedCollection.setCopies(copies + collection.getCopies());
			}
			entityManager.flush();
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			handleThrownException(e);
		}
		entityManager.close();
		emFactory.close();

		postedCollection = getCollection(collection.getUser_id(), collection.getCard_id());

		if (postedCollection == null)
			throw new DataNotFoundException("Card:" + collection.getCard_id() + " Not Found");
		else
			return postedCollection;
	}

	private void handleThrownException(Exception e) {
		Throwable t = null;
		for (t = e.getCause(); t != null; t = t.getCause()) {
			System.out.println(t.getClass());
			if (t.getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class))
				throw new ConstraintViolationException(t.getMessage());

		}

	}

	private boolean checkDuplicates(Integer userid, Integer cardid, EntityManager entityManager) {
		Integer out = 0;
		try {
			Query query = entityManager.createNamedQuery("Check_duplicate_card");
			query.setParameter("userid", userid);
			query.setParameter("cardid", cardid);
			out = (Integer) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			handleThrownException(e);
		}
		return out > 0;
	}

}
