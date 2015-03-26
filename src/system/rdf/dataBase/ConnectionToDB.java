package system.rdf.dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

/**/
public abstract class ConnectionToDB {
	/**
	Server Name
	*/
	protected String server;
	/**
	User Name.
	*/
	protected String user;
	/**
	Password to connect 
	*/
	protected String howToConnect;
	/**
	Data Base name.
	*/
	protected String dataBase;
	/**
	Port number
	*/
	protected String port;
	/**
	Kind of the DB server. A read only value. Every child class is responsible to manage this
	value
	*/
	protected String serverKind;
		
	public ConnectionToDB() {
		super();
	}

	/**
	Constructor that inicialize the class 	
	*/
	
	public ConnectionToDB(String server, String user, String howToConnect,
			String dataBase, String port, String serverKind) {
		super();
		this.server = server;
		this.user = user;
		this.howToConnect = howToConnect;
		this.dataBase = dataBase;
		this.port = port;
		this.serverKind = serverKind;
	}

	 /**
     * Return the kind of server at wich is connected 
     * @return
     */
	public String getServer() {
		return server;
	}
    
	/**
	 * Set the kind of server at wich is connected 
	 * @param server
	 */
	public void setServer(String server) {
		this.server = server;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getHowToConnect() {
		return howToConnect;
	}

	public void setHowToConnect(String howToConnect) {
		this.howToConnect = howToConnect;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	/**
	 * Return the port number 
	*/
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getServerKind() {
		return serverKind;
	}

	public void setServerKind(String serverKind) {
		this.serverKind = serverKind;
	}
	
	/*
	Abstract Methods that will be define in the especifics classes for each DB manager
	*/
	abstract public void connect() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException;
	abstract public void disconnect();
	abstract public ResultSet executeSql(String sql) throws SQLException;
		
}
	

