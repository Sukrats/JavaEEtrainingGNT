package collector.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import collector.database.DBConstants;
import collector.database.DBUtility;
import collector.entities.User;
import collector.exception.ConstraintViolationException;
import collector.exception.DataNotFoundException;

public class UserService {


	public ArrayList<User> getUsers() {
		ArrayList<User> users = new ArrayList<User>();

		Connection connection = null;
		Statement statement = null;
		String query = "SELECT * FROM " + DBConstants.USER_TABLE_NAME;

		try {
			connection = DBUtility.createConnection();

			statement = connection.createStatement();
			statement.executeQuery(query);

			ResultSet resultSet = statement.getResultSet();
			while (resultSet.next()) {
				User user = new User();
				user.setUser_id(resultSet.getInt(DBConstants.USER_COLLUMN_ID));
				user.setUsername(resultSet.getString(DBConstants.USER_COLLUMN_UNAME));
				user.setPassword(" ");
				user.setCreated(resultSet.getDate(DBConstants.USER_COLLUMN_CREATED));
				users.add(user);
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
		if (users.isEmpty())
			throw new DataNotFoundException("No Users to Fetch");
		else
			return users;
	}

	public User getUser(Integer userId) {
		User user = null;

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "SELECT * FROM " + DBConstants.USER_TABLE_NAME + " WHERE " + DBConstants.USER_COLLUMN_ID + " = ? ";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setInt(1, userId);
			statement.executeQuery();

			ResultSet resultSet = statement.getResultSet();

			if (resultSet.next()) {
				user = new User();
				user.setUser_id(resultSet.getInt(DBConstants.USER_COLLUMN_ID));
				user.setUsername(resultSet.getString(DBConstants.USER_COLLUMN_UNAME));
				user.setPassword("");
				user.setCreated(resultSet.getDate(DBConstants.USER_COLLUMN_CREATED));
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
		if (user == null)
			throw new DataNotFoundException("User Not Found");
		else
			return user;
	}

	public User addUser(User user) {

		Connection connection = null;
		PreparedStatement statement = null;
		String query = "INSERT INTO " + DBConstants.USER_TABLE_NAME 
				+ "( " + DBConstants.USER_COLLUMN_UNAME + "," + DBConstants.USER_COLLUMN_PASSWORD + " )"
				+ "VALUES( ?,? )";

		User postedUser = null;
		Integer generatedId = null;
		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query,new String[] { DBConstants.USER_COLLUMN_ID });
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.executeUpdate();
			
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next())
				generatedId = rs.getInt(1);

			statement.close();
			connection.close();

		} catch (SQLException e) {
			if (e.getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
				throw new ConstraintViolationException(e.getMessage());
			}
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

		postedUser = getUser(generatedId);

		return postedUser;
	}
	
	

}
