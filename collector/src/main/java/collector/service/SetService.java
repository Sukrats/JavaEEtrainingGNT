package collector.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import collector.database.DBConstants;
import collector.database.DBUtility;
import collector.entities.StandardSet;
import collector.exception.ConstraintViolationException;
import collector.exception.DataNotFoundException;

public class SetService {

	public ArrayList<StandardSet> getSets() {
		ArrayList<StandardSet> sets = new ArrayList<StandardSet>();

		Connection connection = null;
		Statement statement = null;
		String query = "SELECT * FROM " + DBConstants.SET_TABLE_NAME;

		try {
			connection = DBUtility.createConnection();

			statement = connection.createStatement();
			statement.executeQuery(query);

			ResultSet resultSet = statement.getResultSet();
			while (resultSet.next()) {
				StandardSet set = new StandardSet();
				set.setSetId(resultSet.getInt(DBConstants.SET_COLLUMN_ID));
				set.setSetName(resultSet.getString(DBConstants.SET_COLLUMN_NAME));
				set.setReleased(resultSet.getDate(DBConstants.SET_COLLUMN_RELEASED));
				sets.add(set);
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
		if (sets.isEmpty())
			throw new DataNotFoundException("No Sets to Fetch");
		else
			return sets;
	}

	public StandardSet addSet(StandardSet set) {
		StandardSet newSet = null;
		Integer generatedId = null;

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "INSERT INTO " + DBConstants.SET_TABLE_NAME + "( " + DBConstants.SET_COLLUMN_NAME + " )"
				+ "VALUES( ? )";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query, new String[] { DBConstants.SET_COLLUMN_ID });
			statement.setString(1, set.getSetName());
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

		newSet = getSet(generatedId);

		return newSet;
	}

	public StandardSet getSet(Integer setId) {
		StandardSet set = null;

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "SELECT * FROM " + DBConstants.SET_TABLE_NAME 
				+ " WHERE " + DBConstants.SET_COLLUMN_ID + " = ? ";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setInt(1, setId);
			statement.executeQuery();

			ResultSet resultSet = statement.getResultSet();

			if (resultSet.next()) {
				set = new StandardSet();
				set.setSetId(resultSet.getInt(DBConstants.SET_COLLUMN_ID));
				set.setSetName(resultSet.getString(DBConstants.SET_COLLUMN_NAME));
				set.setReleased(resultSet.getDate(DBConstants.SET_COLLUMN_RELEASED));
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
		if (set == null)
			throw new DataNotFoundException("Set with id: "+setId+" not found!");
		else
			return set;
	}

}
