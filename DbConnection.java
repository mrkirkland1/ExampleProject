import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton to manage DB Connection.
 *
 * One never does a new operation on a singleton object. Getting access to the
 * object is via
 *
 * DbConnection dbc = DbConnection.accessDbConnection();
 *
 * if the database properties are in a file database.properties in the current
 * directory, or
 *
 * String myDbPropertiesFile = "..."; // filename of jdbc connection properties
 * DbConnection dbc = DbConnection.accessDbConnection(myDbPropertiesFile);
 *
 * If a DbConnection object exists, a reference to it is returned.
 *
 * If no DbConnection object exists, a new one is created and its reference is
 * returned.
 *
 * Connection conn = dbc.getConnection();
 * returns a jdbc Connection object
 * that is already connected to the database.
 *
 * Note that this implementation makes a lot of assumptions about
 * "only one client at a time." But it does keep one from having to use a shared
 * static Connection field, or passing a Connection object around via parameter.
 *
 * @author jparks
 *
 */
public class DbConnection {

	private static final String DB_PROPERTIES_FILE = "database.properties";

	private static DbConnection me = null;

	private Connection dbConn = null;

	// Note the constructor is private. 
	private DbConnection(String dbPropertiesFile) throws FileNotFoundException, IOException, SQLException {

		Properties p = new Properties();
		p.load(new FileInputStream(dbPropertiesFile));

		String username = "";
		String password = "";
		String url = "";
		String driver = "";
		url = p.getProperty("jdbc.url");
		driver = p.getProperty("jdbc.driver");
		// Class.forName(driver); // not needed on my system

		dbConn = DriverManager.getConnection(url, username, password);
	}

	/**
	 * Get access to a jdbc Connection to a database.  The Connection is already established.
	 *
	 * @return - already established jdbc connection
	 */
	public Connection getConnection() {
		return dbConn;
	}

	/**
	 * Gain access to a DbConnection object that can supply a jdbc Connection to
	 * a database. The database connection parameters are in a file
	 * database.properties in the current directory.
	 *
	 * @return DbConnection object
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static DbConnection accessDbConnection() throws FileNotFoundException, IOException, SQLException {
		return accessDbConnection(DB_PROPERTIES_FILE);
	}

	/**
	 * Gain access to a DbConnection object that can supply a jdbc Connection to
	 * a database.
	 *
	 *
	 * @param propertiesFilename - the name of a file containing the jdbc properties to make
	 *            the database connection.
	 *
	 * @return DbConnection object
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	public static DbConnection accessDbConnection(String propertiesFilename)
			throws FileNotFoundException, IOException, SQLException {
		if (me == null) {
			me = new DbConnection(propertiesFilename);
		}
		return me;
	}

	/**
	 * Disconnect from the database and return the DbConnection object to an
	 * uninitialized state.
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException {
		me = null;
		if (dbConn != null) {
			try {
				dbConn.close();
			} finally {
				dbConn = null;
			}
		}
	}

}
