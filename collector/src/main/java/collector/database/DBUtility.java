package collector.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtility {

	public static Connection createConnection() {
		Connection con = null;
		try {
			System.out.println("Connecting to database...");
			con = DriverManager.getConnection(DBConstants.dbUrl, DBConstants.dbUser, DBConstants.dbPwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public static boolean authenticate(String username, String password) {	    
		Connection connection = null;
		PreparedStatement statement = null;
		String query = "SELECT COUNT(*) FROM " + DBConstants.USER_TABLE_NAME + " WHERE " 
						+ DBConstants.USER_COLLUMN_UNAME + " = ? AND "
						+ DBConstants.USER_COLLUMN_PASSWORD + " = ? ";

		try {
			connection = DBUtility.createConnection();

			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);
			statement.executeQuery();

			ResultSet resultSet = statement.getResultSet();

			if (resultSet.next()) {
				System.out.println("Authenticated Basic: "+ resultSet.getInt(1));
				return resultSet.getInt(1)>0;
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
		
		return false;
	}

}
