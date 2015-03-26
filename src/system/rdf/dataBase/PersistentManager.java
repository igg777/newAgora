package system.rdf.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.collections.map.HashedMap;

public class PersistentManager implements PersistentManagerInterface {

	public final static String POSTGRES_DRIVER = "org.postgresql.Driver";
	public final static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * Static connection to PostgreSQL Database
	 */
	public static ConnectionToPostgres connectionPostgres = new ConnectionToPostgres();;
	public static ConnectionToVirtuoso connectionVirtuoso = new ConnectionToVirtuoso();;

	public static RDFSerializator serializator  = new RDFSerializator();
	public static ArrayList save = new ArrayList();


	/**
	 * Gets the ConnectionToPostgres instance
	 * @return {@link ConnectionToPostgres} instance
	 */
	public static ConnectionToPostgres getConnectionToPostgreSQL(){
		return connectionPostgres;
	}
	
	/**
	 * Gets the ConnectionToVirtuoso instance
	 * @return {@link ConnectionToVirtuoso} instance
	 */
	public static ConnectionToVirtuoso getConnectionToVirtuoso(){
		return connectionVirtuoso;
	}

	/**
	 * 
	 */
	public static void ConfigureConnectionToPostgres(String host,String port,String database,
			String user, String pass){
		connectionPostgres.setDB_HOST(host);
		connectionPostgres.setDB_PORT(port);
		connectionPostgres.setDB(database);
		connectionPostgres.setDB_USER(user);
		connectionPostgres.setDB_PASSWD(pass);
		String db_url =  "jdbc:postgresql://"+host+":"+port+"/"+database;
		connectionPostgres.setDB_URL(db_url);
	}



	/* (non-Javadoc)
	 * @see system.rdf.dataBase.DataBaseManagerInterface#createDataBase(java.lang.String)
	 */
	public void createDataBase(String name) {
		String create = "CREATE DATABASE "+name+" WITH OWNER = postgres ENCODING ='UTF8'"+
		"TABLESPACE = pg_default";
		Statement stmt;
		try {
			stmt = ((Connection) connectionPostgres).createStatement();
			stmt.execute(create);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see system.rdf.dataBase.DataBaseManagerInterface#postgresDriver()
	 */
	public String postgresDriver() {
		return POSTGRES_DRIVER;
	}
	/* (non-Javadoc)
	 * @see system.rdf.dataBase.DataBaseManagerInterface#conectToGestor(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void conectToGestor(String host, String port, String user,String pass){

		try {
			String url = "jdbc:postgresql://"+host+":"+port;
			Class.forName(POSTGRES_DRIVER).newInstance();
			Properties props = new Properties();
			props.setProperty("user",user);
			props.setProperty("password",pass );
			connectionPostgres = new ConnectionToPostgres();

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * @return HashedMap
	 */
	public HashedMap getProblemWithAlternative(){

		HashedMap result = new HashedMap();
		ArrayList alternative = new ArrayList();

		try {
			ConnectionToPostgres connection = new ConnectionToPostgres();
			connection.connect();
			ResultSet rs = connection.select("problem");

			ArrayList<ExtractData>tmp = new ArrayList<ExtractData>();
			while(rs.next()){

				tmp.add(new ExtractData(rs.getInt("id_problem"),rs.getString("name")));
				ResultSet rsAlt = connection.selectAlternatives(rs.getInt("id_problem"));

				while(rsAlt.next()){
					alternative.add(new ExtractData(rsAlt.getInt("id_solution"),
							rsAlt.getString("name")));
					result.put(tmp, alternative);
				}
			}
			Iterator i = result.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}


		return result;
	}


	public ComboBoxModel getSolutions(int idProblem) throws SQLException{
		ConnectionToPostgres connection = new ConnectionToPostgres();
		connection.connect();
		ResultSet result = connection.select(idProblem);

		ArrayList<ExtractData>tmp = new ArrayList<ExtractData>();
		while(result.next()){
			tmp.add(new ExtractData(result.getInt("id_problem"),result.getString("name")));
		}

		return new DefaultComboBoxModel(tmp.toArray()) ;

	}
	/**
	 * Gets the RDFSerializator instances
	 * @return RDFSerializator instance
	 */
	public static RDFSerializator getSerializator() {
		return serializator;
	}

}
