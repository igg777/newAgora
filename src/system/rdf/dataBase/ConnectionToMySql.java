package system.rdf.dataBase;

import java.sql.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import system.rdf.handlers.ExceptionHandler;

/**
 * Handle the database connection 
 * @author Irlán Grangel
 */
public class ConnectionToMySql extends ConnectionToDB{

	private Connection connection;

	// Constants to connect to data base
	public final static String DB_URL = "jdbc:postgresql://localhost:5432/grafoRdf";

	public final static String DB_USER = "postgres";

	public final static String DB_PASSWD = "jemlymsdqt";

	public final static String DB = "PostgreSQL";

	public final static String DBDRIVER_CLASS = "org.postgresql.Driver";


	
	public ConnectionToMySql() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConnectionToMySql(String server, String user, String howToConnect,
			String dataBase, String port, String serverKind) {
		super(server, user, howToConnect, dataBase, port, serverKind);
		// TODO Auto-generated constructor stub
	}

	public void connect() throws SQLException, InstantiationException,
	IllegalAccessException, ClassNotFoundException {
		try{
			Class.forName(DBDRIVER_CLASS).newInstance();
			Properties props = new Properties();
			props.setProperty("user", DB_USER);
			props.setProperty("password", DB_PASSWD);
			connection = DriverManager.getConnection(DB_URL, props);

		}catch (Exception e) {
			ExceptionHandler ex = new ExceptionHandler();
			ex.error(e);
		}
	}

	public ResultSet executeSql(String strSQL) throws SQLException {
		ResultSet rs;
		Statement Stmt = connection.createStatement();
		rs = Stmt.executeQuery(strSQL);
		return rs;
	}

	/**
	 * For methods insert, delete, modified
	 * 
	 * @param strSQL
	 * @return
	 * @throws SQLException
	 */
	public int executeSqlForMethods(String strSQL) throws SQLException {
		// System.out.println(strSQL);
		Statement Stmt = connection.createStatement();
		return Stmt.executeUpdate(strSQL);
	}

	/**
	 * Este método devuelve ...
	 * 
	 * @param table
	 *            nombre de la tabla
	 * @return conjunto...
	 * @throws SQLException
	 */
	public ResultSet select(String table) throws SQLException {
		String result = "SELECT * FROM " + table;
		return executeSql(result);
	}

	/**
	 * @param tabla
	 * @param condicion
	 * @return
	 * @throws SQLException
	 */
	public ResultSet select(String table, String condition) throws SQLException {
		String result = "SELECT * FROM " + table + " WHERE " + condition;
		// System.out.println(consulta);
		return executeSql(result);
	}

	public ResultSet selectId(String table, String condition)
	throws SQLException {
		String result = "SELECT id FROM " + table + " WHERE " + condition;
		return executeSql(result);
	}

	/**
	 * @param table
	 * @param hastTableToInsert
	 * @throws SQLException
	 *             Se le pasa un arreglo del tipo clave, valor representado por
	 *             un hashtable que permite esto. Las claves corresponden a los
	 *             campos y los valores a los valores reales que se quieren
	 *             insertar en los campos eje clave = nombre , valor = 'pepe',
	 *             se recorre el hashtable y se construye una consulta que se le
	 *             pasa al metodo ejecutar_sql ...
	 */

	public void toInsert(String table,
			Hashtable<String, Object> hastTableToInsert) throws SQLException {
		String fields = "", values = "";
		Iterator it = hastTableToInsert.keySet().iterator();
		int counter = 1;
		while (it.hasNext()) {
			String key = (String) it.next();
			if (counter != hastTableToInsert.size()) {
				fields = fields + key + ",";
				values = values + "'" + hastTableToInsert.get(key).toString()
				+ "',";
			} else {
				fields = fields + key;
				values = values + "'" + hastTableToInsert.get(key).toString()
				+ "'";
			}
			counter++;
		}
		String result = "INSERT INTO " + table + " (" + fields + ") VALUES ("
		+ values + ")";
		// System.out.println(consulta);
		executeSqlForMethods(result);
	}



	@Override
	/**
	 * disconnect to data base 
	 */
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	
}// fin del procedimiento

