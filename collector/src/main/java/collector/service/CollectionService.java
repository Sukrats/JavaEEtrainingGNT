package collector.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import collector.database.DBConstants;
import collector.database.DBUtility;
import collector.entities.Card;
import collector.entities.Collection;
import collector.exception.ConstraintViolationException;

public class CollectionService {

	public ArrayList<Collection> getCollections(Integer userId) {
		ArrayList<Collection> cardCollection = new ArrayList<Collection>();

		Connection connection = null;
		PreparedStatement statement = null;

		String query = "SELECT * FROM " + DBConstants.COLLECTION_TABLE_NAME + " as c " 
				+ " INNER JOIN " + DBConstants.CARD_TABLE_NAME + " as ca " 
				+ " ON c.card_id = ca." + DBConstants.CARD_COLLUMN_ID
				+ " WHERE c." + DBConstants.COLLECTION_COLLUMN_USER_ID + "= ? ";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			statement.executeQuery();

			ResultSet resultSet = statement.getResultSet();
			while (resultSet.next()) {
				Card card = new Card();
				card.setCardId(resultSet.getInt(DBConstants.CARD_COLLUMN_ID));
				card.setCardName(resultSet.getString(DBConstants.CARD_COLLUMN_NAME));
				card.setCardType(resultSet.getString(DBConstants.CARD_COLLUMN_CARD_TYPE));
				card.setPrice(resultSet.getFloat(DBConstants.CARD_COLLUMN_PRICE));
				card.setSetId(resultSet.getInt(DBConstants.CARD_COLLUMN_SET_ID));

				Collection collection = new Collection();
				collection.setCopies(resultSet.getInt(DBConstants.COLLECTION_COLLUMN_COPIES));
				collection.setUser_id(resultSet.getInt(DBConstants.COLLECTION_COLLUMN_USER_ID));
				collection.setCard_id(card.getCardId());
				collection.setCard(card);

				cardCollection.add(collection);
			}
			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cardCollection;
	}

	public Collection getCollection(Integer cardId, Integer userId) {
		Collection collection = null;

		Connection connection = null;
		PreparedStatement statement = null;
		
		String query = "SELECT * FROM " + DBConstants.COLLECTION_TABLE_NAME + " WHERE "
				+ DBConstants.COLLECTION_COLLUMN_USER_ID + " = ? " + " AND "
				+ DBConstants.COLLECTION_COLLUMN_CARD_ID + " = ? ";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			statement.setInt(2, cardId);
			statement.executeQuery();

			ResultSet resultSet = statement.getResultSet();

			if (resultSet.next()) {
				collection = new Collection();
				collection.setUser_id(resultSet.getInt(DBConstants.COLLECTION_COLLUMN_USER_ID));
				collection.setCard_id(resultSet.getInt(DBConstants.COLLECTION_COLLUMN_CARD_ID));
				collection.setCopies(resultSet.getInt(DBConstants.COLLECTION_COLLUMN_COPIES));
			}

			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return collection;
	}

	public Collection addCollection(Integer userId, Collection collection) {
		Collection postedCollection = null;

		String query = "";

		if (!checkDuplicates(userId, collection.getCard_id()))
			query = "INSERT INTO " + DBConstants.COLLECTION_TABLE_NAME + "( " + DBConstants.COLLECTION_COLLUMN_USER_ID
					+ "," + DBConstants.COLLECTION_COLLUMN_CARD_ID + " )" 
					+ "VALUES( ? ,? )";
		else {
			query = "UPDATE " + DBConstants.COLLECTION_TABLE_NAME 
					+ " SET " + DBConstants.COLLECTION_COLLUMN_COPIES + " = " + DBConstants.COLLECTION_COLLUMN_COPIES + " + 1" 
					+ " WHERE "
					+ DBConstants.COLLECTION_COLLUMN_USER_ID + " = ? AND "
					+ DBConstants.COLLECTION_COLLUMN_CARD_ID + " = ? ";
		}

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			statement.setInt(2, collection.getCard_id());
			statement.executeUpdate();

			statement.close();
			connection.close();

		} catch (SQLException e) {
			if (e.getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
				throw new ConstraintViolationException(e.getMessage());
			} else {
				e.printStackTrace();
			}
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		postedCollection = getCollection(collection.getCard_id(), userId);
		return postedCollection;
	}

	private boolean checkDuplicates(int userid, int cardid) {
		Connection connection = DBUtility.createConnection();
		CallableStatement callable = null;
		boolean out = false;
		try {
			callable = connection.prepareCall("{? = call Check_duplicate_card(?, ?)}");
			callable.setInt(2, userid);
			callable.setInt(3, cardid);
			ResultSet rs = callable.executeQuery();
			if (rs.next()) {
				out = rs.getBoolean(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}

	public Collection deleteCollection(Integer cardId, Integer userId) {
		return null;
	}
}
