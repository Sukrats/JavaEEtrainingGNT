package collector.servicejpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import collector.entities.StandardSet;
import collector.exception.ConstraintViolationException;
import collector.exception.DataNotFoundException;

public class SetServiceJPA {

	@SuppressWarnings("unchecked")
	public List<StandardSet> getSets() {
		List<StandardSet> sets = null;

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();
		entityManager.getTransaction().begin();

		try {
			sets = entityManager.createQuery("SELECT s FROM "+StandardSet.class.getName()+" s").getResultList();
		} catch (Exception e) {
			handleThrownException(e);
		}
		entityManager.close();
		emFactory.close();

		if (sets.isEmpty() || sets == null)
			throw new DataNotFoundException("No Sets to Fetch");
		else
			return sets;
	}

	public StandardSet getSet(Integer set_id) {
		StandardSet set = null;
		
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();
		
		try {
			set = entityManager.find(StandardSet.class, set_id);
		} catch (Exception e) {
			handleThrownException(e);
		}
		
		if (set == null)
			throw new DataNotFoundException("Set Not Found");
		else
			return set;
	}

	public StandardSet addSet(StandardSet set) {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();
		StandardSet postedSet = null;
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(set);
			entityManager.flush();
			entityManager.getTransaction().commit();

		} catch (Exception e) {
			handleThrownException(e);
		}
		entityManager.close();
		emFactory.close();

		postedSet = getSet(set.getSetId());

		if (postedSet == null)
			throw new DataNotFoundException("Set:" + set.getSetName() + " Not Found");
		else
			return postedSet;
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
