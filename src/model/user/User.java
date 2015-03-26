package model.user;

import java.sql.SQLException;
import java.util.Hashtable;

import system.rdf.dataBase.ExtractData;
import system.rdf.dataBase.PersistentManager;
import system.rdf.utils.Encript;



public class User {
	private String name;
	private String password;
	private ExtractData role;
	private ExtractData enterprise;
	
	

	public User(String name, String password, ExtractData role) {
		super();
		this.name = name;
		this.password = password;
		this.role = role;
	}

	public User(String name, String password, ExtractData role,
			ExtractData enterprise) {
		super();
		this.enterprise = enterprise;
		this.name = name;
		this.password = Encript.encript(password);
		this.role = role;
	}

	public User() {
		super();
	}

	public User(String name) {
		super();
		this.name = name;
	}

	public void insertUser() throws SQLException {
		Hashtable<String, Object> insertUser = new Hashtable<String, Object>();
		insertUser.put("user_name", name);
		insertUser.put("password", password);
		insertUser.put("id_user_rolefk", role.getId());
		insertUser.put("id_enterprise", enterprise.getId());
		try {
			PersistentManager.getConnectionToPostgreSQL().insert(
					"system_users", insertUser);
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}
	
	/**
	 * delete an user from the DB
	 * 
	 * @throws SQLException
	 */
	public void deleteFromDB()throws SQLException{
		String condition = "user_name = '" + name + "'";
		try{
			PersistentManager.getConnectionToPostgreSQL().delete("system_users", condition);
		}
		catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}
	
	/**
	 * change this another user for this in the DB
	 * 
	 * @param the name of the user to be changed
	 * @throws SQLException
	 */
	public void updateInDB(String nameOldUser)throws SQLException{
		Hashtable<String, Object> toInsert = new Hashtable<String, Object>();
		Hashtable<String, Object> conditions = new Hashtable<String, Object>();
		toInsert.put("user_name", name);
		toInsert.put("password", password);
		toInsert.put("id_user_rolefk", role.getId());
		toInsert.put("id_enterprise", enterprise.getId());
		conditions.put("user_name", nameOldUser);
		try{
			PersistentManager.getConnectionToPostgreSQL().update("system_users", toInsert, conditions);
		}
		catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ExtractData getRole() {
		return role;
	}

	public void setRole(ExtractData role) {
		this.role = role;
	}


}
