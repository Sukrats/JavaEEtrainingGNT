package collector.service;

import java.util.ArrayList;

import collector.database.DBConstants;
import collector.database.DBUtility;
import collector.entities.Trade;
import collector.exception.ConstraintViolationException;

import java.sql.*;

public class TradesService {

	public ArrayList<Trade> getTrades(Integer userId) {
		ArrayList<Trade> trades = new ArrayList<Trade>();

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "SELECT * FROM " + DBConstants.TRADE_TABLE_NAME + " WHERE " + DBConstants.TRADE_COLLUMN_USER_ID
				+ " = ? ";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Trade trade = new Trade();
				trade.setTrade_id(resultSet.getInt(DBConstants.TRADE_COLLUMN_ID));
				trade.setUser_id(resultSet.getInt(DBConstants.TRADE_COLLUMN_USER_ID));
				trade.setCard_id(resultSet.getInt(DBConstants.TRADE_COLLUMN_CARD_ID));
				trade.setRef_trade_id(resultSet.getInt(DBConstants.TRADE_COLLUMN_REF_TRADE_ID));
				trade.setCreated(resultSet.getDate(DBConstants.TRADE_COLLUMN_CREATED));
				trades.add(trade);
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
		return trades;
	}

	public Trade getTrade(Integer tradeId) {
		Trade trade = null;

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "SELECT * FROM " + DBConstants.TRADE_TABLE_NAME + " WHERE " + DBConstants.TRADE_COLLUMN_ID
				+ " = ? ";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setInt(1, tradeId);
			statement.executeQuery();

			ResultSet resultSet = statement.getResultSet();

			if (resultSet.next()) {
				trade = new Trade();
				trade.setTrade_id(resultSet.getInt(DBConstants.TRADE_COLLUMN_ID));
				trade.setUser_id(resultSet.getInt(DBConstants.TRADE_COLLUMN_USER_ID));
				trade.setCard_id(resultSet.getInt(DBConstants.TRADE_COLLUMN_CARD_ID));
				trade.setRef_trade_id(resultSet.getInt(DBConstants.TRADE_COLLUMN_REF_TRADE_ID));
				trade.setCreated(resultSet.getDate(DBConstants.TRADE_COLLUMN_CREATED));
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
		return trade;
	}

	public Trade addTrade(Trade trade) {

		Connection connection = null;
		PreparedStatement statement = null;

		String query = "INSERT INTO " + DBConstants.TRADE_TABLE_NAME 
				+ "( " + DBConstants.TRADE_COLLUMN_USER_ID + "," 
				+ DBConstants.TRADE_COLLUMN_CARD_ID + "," 
				+ DBConstants.TRADE_COLLUMN_REF_TRADE_ID + " )" 
				+ "VALUES( ?, ?, ? )";

		Trade postedTrade = null;
		Integer generatedId = null;

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query,new String[] { DBConstants.TRADE_COLLUMN_ID });
			statement.setInt(1, trade.getUser_id());
			statement.setInt(2, trade.getCard_id());
			statement.setInt(3, trade.getRef_trade_id());
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

		postedTrade = getTrade(generatedId);

		return postedTrade;
	}
}
