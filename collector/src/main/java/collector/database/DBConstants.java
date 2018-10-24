package collector.database;

public class DBConstants {
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String dbName = "mtg_collections";
	public static final String dbUrl = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&serverTimezone=UTC";

	public static final String dbUser = "mtg.user";
	public static final String dbPwd = "!mtgUser1234";

	public static final String COLLECTION_TABLE_NAME = "collection";
	public static final String COLLECTION_COLLUMN_USER_ID = "user_id";
	public static final String COLLECTION_COLLUMN_CARD_ID = "card_id";
	public static final String COLLECTION_COLLUMN_COPIES = "copies";

	public static final String CARD_TABLE_NAME = "card";
	public static final String CARD_COLLUMN_ID = "card_id";
	public static final String CARD_COLLUMN_NAME = "card_name";
	public static final String CARD_COLLUMN_CARD_TYPE = "card_type";
	public static final String CARD_COLLUMN_PRICE = "price";
	public static final String CARD_COLLUMN_SET_ID = "set_id";

	public static final String SET_TABLE_NAME = "standardset";
	public static final String SET_COLLUMN_ID = "set_id";
	public static final String SET_COLLUMN_NAME = "set_name";
	public static final String SET_COLLUMN_RELEASED = "released";

	public static final String TRADE_TABLE_NAME = "trade";
	public static final String TRADE_COLLUMN_ID = "trade_id";
	public static final String TRADE_COLLUMN_USER_ID = "user_id";
	public static final String TRADE_COLLUMN_CARD_ID = "card_id";
	public static final String TRADE_COLLUMN_REF_TRADE_ID = "ref_trade_id";
	public static final String TRADE_COLLUMN_CREATED = "created";

	public static final String USER_TABLE_NAME = "user";
	public static final String USER_COLLUMN_ID = "user_id";
	public static final String USER_COLLUMN_UNAME = "username";
	public static final String USER_COLLUMN_PASSWORD = "password";
	public static final String USER_COLLUMN_CREATED = "created";
}
