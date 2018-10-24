package collector.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import collector.database.DBConstants;
import collector.database.DBUtility;
import collector.entities.Card;
import collector.exception.ConstraintViolationException;
import collector.exception.DataNotFoundException;

public class CardService {

	public ArrayList<Card> getCards(Integer setId) {
		ArrayList<Card> cards = new ArrayList<Card>();

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "";

		if (setId == null)
			query = "SELECT * FROM " + DBConstants.CARD_TABLE_NAME;
		else {
			query = "SELECT * FROM " + DBConstants.SET_TABLE_NAME + " AS s " + " INNER JOIN "
					+ DBConstants.CARD_TABLE_NAME + " AS c " + " ON c." + DBConstants.CARD_COLLUMN_SET_ID + " = s."
					+ DBConstants.SET_COLLUMN_ID + " WHERE s." + DBConstants.SET_COLLUMN_ID + " = ? ";
		}
		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			if (setId != null)
				statement.setInt(1, setId);
			ResultSet resultCards = statement.executeQuery();

			while (resultCards.next()) {
				Card card = new Card();
				card.setCardId(resultCards.getInt(DBConstants.CARD_COLLUMN_ID));
				card.setCardName(resultCards.getString(DBConstants.CARD_COLLUMN_NAME));
				card.setCardType(resultCards.getString(DBConstants.CARD_COLLUMN_CARD_TYPE));
				card.setPrice(resultCards.getFloat(DBConstants.CARD_COLLUMN_PRICE));
				card.setSetId(resultCards.getInt(DBConstants.CARD_COLLUMN_SET_ID));
				cards.add(card);
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
		if (cards.isEmpty())
			throw new DataNotFoundException("No Cards to Fetch");
		else
			return cards;
	}

	public Card addCard(Card card) {

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "INSERT INTO " + DBConstants.CARD_TABLE_NAME + "( " + DBConstants.CARD_COLLUMN_NAME + ","
				+ DBConstants.CARD_COLLUMN_PRICE + "," + DBConstants.CARD_COLLUMN_SET_ID + ","
				+ DBConstants.CARD_COLLUMN_CARD_TYPE + " )" + "VALUES( ?,?,?,? )";

		Card newCard = null;
		Integer generatedId = null;
		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query, new String[] { DBConstants.CARD_COLLUMN_ID });
			statement.setString(1, card.getCardName());
			statement.setFloat(2, card.getPrice());
			statement.setInt(3, card.getSetId());
			statement.setString(4, card.getCardType());
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next())
				generatedId = rs.getInt(1);
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
		newCard = getCard(generatedId);

		return newCard;
	}

	public Card getCard(Integer cardId) {
		Card card = null;

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "SELECT * FROM " + DBConstants.CARD_TABLE_NAME + " WHERE " + DBConstants.CARD_COLLUMN_ID
				+ " = ? ";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setInt(1, cardId);
			statement.executeQuery();

			ResultSet resultCard = statement.getResultSet();

			if (resultCard.next()) {
				card = new Card();
				card.setCardId(resultCard.getInt(DBConstants.CARD_COLLUMN_ID));
				card.setCardName(resultCard.getString(DBConstants.CARD_COLLUMN_NAME));
				card.setCardType(resultCard.getString(DBConstants.CARD_COLLUMN_CARD_TYPE));
				card.setPrice(resultCard.getFloat(DBConstants.CARD_COLLUMN_PRICE));
				card.setSetId(resultCard.getInt(DBConstants.CARD_COLLUMN_SET_ID));
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
		if (card == null)
			throw new DataNotFoundException("There is no card for id: " + cardId);
		else
			return card;
	}

}
