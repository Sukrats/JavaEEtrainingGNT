package collector.servicejpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import collector.entities.User;
import collector.exception.ConstraintViolationException;
import collector.exception.DataNotFoundException;

public class UserServiceJPA {

	@SuppressWarnings("unchecked")
	public List<User> getUsers() {

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();
		entityManager.getTransaction().begin();

		List<User> users = entityManager.createQuery("SELECT u FROM " + User.class.getName() + " u").getResultList();

		entityManager.close();
		emFactory.close();

		if (users.isEmpty())
			throw new DataNotFoundException("No Users to Fetch");
		else
			return users;
	}

	public User getUser(Integer userId) {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		User user = null;
		EntityManager entityManager = emFactory.createEntityManager();
		try {
			user = entityManager.find(User.class, userId);
		} catch (PersistenceException e) {
			e.printStackTrace();
			handleThrownException(e);
		}
		if (user == null)
			throw new DataNotFoundException("User Not Found");
		else
			return user;
	}

	public User addUser(User user) {
		User postedUser = null;

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();

		try {
			entityManager.getTransaction().begin();
			entityManager.persist(user);
			entityManager.flush();
			entityManager.getTransaction().commit();

		} catch (PersistenceException e) {
			handleThrownException(e);
		}
		entityManager.close();
		emFactory.close();

		postedUser = getUser(user.getUser_id());

		if (postedUser == null)
			throw new DataNotFoundException("User:" + user.getUsername() + " Not Found");
		else
			return postedUser;
	}

	public boolean authenticateUser(String username, String password) {
		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
		EntityManager entityManager = emFactory.createEntityManager();
		entityManager.getTransaction().begin();
		Integer auth = 0;
		try {
			Query query = entityManager.createQuery(
					"SELECT COUNT(u.username) FROM User u WHERE u.username = :username AND u.password= :password");
			query.setParameter("username", username);
			query.setParameter("password", password);

			auth = (int) (long) query.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			handleThrownException(e);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
		emFactory.close();

		return auth > 0;
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
