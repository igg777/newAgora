package model.enterprise;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import system.rdf.dataBase.PersistentManager;

public class Enterprise {
String name;
String description;
String email;

/**
 * empty Enterprise constructor
 */
public Enterprise(){	
}

/**
 * Enterprise constructor
 * @param name
 */
public Enterprise(String name){
	this.name = name;
}

/**
 * Enterprise constructor
 * 
 * @param description
 * @param email
 * @param name
 */
public Enterprise(String name, String description, String email) {
	super();
	this.description = description;
	this.email = email;
	this.name = name;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}

/**
 * insert the enterprise in the DB
 * 
 * @throws SQLException
 */
public void insertInDB()throws SQLException{
	Hashtable<String, Object> toInsert = new Hashtable<String, Object>();
	toInsert.put("name", this.getName());
	toInsert.put("email", this.getEmail());
	toInsert.put("description", this.getDescription());
	try{
		PersistentManager.getConnectionToPostgreSQL().insert("enterprise", toInsert);
	}
	catch (SQLException ex) {
		throw new SQLException(ex.getMessage());
	}
}

/**
 * change this enterprise for another in the DB
 * 
 * @param the name of the enterprise to change
 * @throws SQLException
 */
public void updateInDB(String nameOldEnterprise)throws SQLException{
	Hashtable<String, Object> toInsert = new Hashtable<String, Object>();
	Hashtable<String, Object> conditions = new Hashtable<String, Object>();
	toInsert.put("name", this.getName());
	toInsert.put("email", this.getEmail());
	toInsert.put("description", this.getDescription());
	conditions.put("name", nameOldEnterprise);
	try{
		PersistentManager.getConnectionToPostgreSQL().update("enterprise", toInsert, conditions);
	}
	catch (SQLException e) {
		throw new SQLException(e.getMessage());
	}
}

/**
 * delete an enterprise from the DB
 * 
 * @throws SQLException
 */
public void deleteFromDB()throws SQLException{
	String condition = "name = '" + this.getName() + "'";
	try{
		PersistentManager.getConnectionToPostgreSQL().delete("enterprise", condition);
	}
	catch (SQLException e) {
		throw new SQLException(e.getMessage());
	}
}

/**
 * get the enterprise id in the database
 *
 * @return the enterprise's id
 * @throws SQLException
 */
public int getIdInDB()throws SQLException{
	int toReturn = -1;
	String condition = "name = '" + this.getName() + "'";
	try{
		ResultSet rs = PersistentManager.getConnectionToPostgreSQL().select("enterprise", condition);
		while(rs.next()){
			toReturn = rs.getInt("id_enterprise");
		}
	}
	catch (SQLException e) {
		throw new SQLException(e.getMessage());
	}
	return toReturn;
	}



}
