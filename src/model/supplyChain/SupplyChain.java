package model.supplyChain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import system.rdf.dataBase.PersistentManager;

public class SupplyChain {
	String name;
	String comments;
	int id = -1;

	/**
	 * empty supply chain constructor
	 */
	public SupplyChain() {
	}

	/**
	 * Aupply_chain constructor
	 * 
	 * @param name
	 */
	public SupplyChain(String name) {
		this.name = name;
	}

	/**
	 * supply_chain constructor
	 * 
	 * @param comments
	 * @param name
	 */
	public SupplyChain(String name, String comments) {
		super();
		this.comments = comments;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String description) {
		this.comments = description;
	}

	/**
	 * insert the supply_chain in the DB
	 * 
	 * @throws SQLException
	 */
	public void insertInDB() throws SQLException {
		Hashtable<String, Object> toInsert = new Hashtable<String, Object>();
		toInsert.put("name", this.getName());
		toInsert.put("comments", this.getComments());
		try {
			PersistentManager.getConnectionToPostgreSQL().insert(
					"supply_chain", toInsert);
		} catch (SQLException ex) {
			throw new SQLException(ex.getMessage());
		}
	}

	/**
	 * change this supply_chain for another in the DB
	 * 
	 * @param the
	 *            name of the supply_chain to change
	 * @throws SQLException
	 */
	public void updateInDB(String nameOldsuppluChain) throws SQLException {
		Hashtable<String, Object> toInsert = new Hashtable<String, Object>();
		Hashtable<String, Object> conditions = new Hashtable<String, Object>();
		toInsert.put("name", this.getName());
		toInsert.put("comments", this.getComments());
		conditions.put("name", nameOldsuppluChain);
		try {
			PersistentManager.getConnectionToPostgreSQL().update(
					"supply_chain", toInsert, conditions);
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * delete an supply_chain from the DB
	 * 
	 * @throws SQLException
	 */
	public void deleteFromDB() throws SQLException {
		String condition = "name = '" + this.getName() + "'";
		try {
			PersistentManager.getConnectionToPostgreSQL().delete(
					"supply_chain", condition);
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * get the supply chain id in the database
	 * 
	 * @return the supply_chain id
	 * @throws SQLException
	 */
	public int getIdInDB() throws SQLException {
		int toReturn = -1;
		String condition = "name = '" + this.getName() + "'";
		try {
			ResultSet rs = PersistentManager.getConnectionToPostgreSQL()
			.select("supply_chain", condition);
			while (rs.next()) {
				toReturn = rs.getInt("id_supply_chain");
			}
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
		return toReturn;
	}

	public void addEnterprice(int id_enterprise) throws SQLException {
		try {
			if (id == -1)
				id = getIdInDB();
			Hashtable<String, Object> toInsert = new Hashtable<String, Object>();
			toInsert.put("id_enterprise", id_enterprise);
			toInsert.put("id_supply_chain", id);
			PersistentManager.getConnectionToPostgreSQL().insert(
					"supply_chain_enterprise", toInsert);
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

	}

	public void getCommentsFromDB() throws Exception {
		String sql = "select comments from supply_chain where name = '" + name
		+ "'";
		ResultSet rs = PersistentManager.getConnectionToPostgreSQL()
		.executeSql(sql);
		while (rs.next()) {
			setComments(rs.getString("comments"));
		}

	}

	public void deleteAllEnterprises() throws Exception {
		if (id == -1)
			id = getIdInDB();
		Hashtable<String, Object> conditions = new Hashtable<String, Object>();
        conditions.put("id_supply_chain", id);
        PersistentManager.getConnectionToPostgreSQL().delete("supply_chain_enterprise", conditions);
	}

}
