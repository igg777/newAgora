package system.rdf.dataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import system.config.AppConfiguration;
import system.rdf.ui.GraphEd;
import system.rename.Tools;

/**
 * @author Hector
 * Provides methods to connect with virtuoso
 *
 */
public class ConnectionToVirtuoso extends ConnectionToDB {
	private Connection connection;

	// Constants to connect to data base
	public static String DB;

	public static String DB_USER;

	public static String DB_PASSWD;

	public static String DB_HOST;

	public static String DB_PORT;

	public static String DB_URL;

	public final static String DBDRIVER_CLASS = "virtuoso.jdbc4.Driver";
	
	private Tools tools = new Tools();


	public ConnectionToVirtuoso(String server, String user,
			String howToConnect, String dataBase, String port, String serverKind) {
		super(server, user, howToConnect, dataBase, port, serverKind);
		try {
			connection = null;
			this.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor
	 */
	public ConnectionToVirtuoso() {
		super();
		try {
			AppConfiguration config = new AppConfiguration();

			DB = config.getVirtuosoDataBase();

			DB_USER = config.getVirtuosoUser();

			DB_PASSWD = config.getVirtuosoPassword();

			DB_HOST = config.getVirtuosoHost();

			DB_PORT = config.getVirtuosoPort();

			DB_URL = "jdbc:virtuoso://" + DB_HOST + ":" + DB_PORT + "/UID="
				+ DB_USER + "/PWD=" + DB_PASSWD;

		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

	/**
	 * Try if connection is able
	 * 
	 * @return
	 */
	public boolean tryConnection() {
		boolean flag = true;
		try {
			Class.forName(DBDRIVER_CLASS).newInstance();
			connection = DriverManager.getConnection(DB_URL);

		} catch (NullPointerException e1) {
			flag = false;
			GraphEd.errorPane.printMessage(new Exception(
					"The file to configure the database is missing !!!"));
		} catch (Exception e) {
			flag = false;
			GraphEd.errorPane.printMessage(e);

		}

		return flag;
	}

	/**
	 * Open a connection to Database
	 * 
	 * @see system.rdf.dataBase.ConnectionToDB#connect()
	 */

	public void connect() {
		boolean flag = tryConnection();
	}

	public void disconnect() {
		try {
			if (!(connection == null))
				if (!connection.isClosed()) {
					connection.close();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResultSet executeSql(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
