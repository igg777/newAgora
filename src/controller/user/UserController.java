package controller.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import model.user.User;

import system.rdf.dataBase.ExtractData;
import system.rdf.dataBase.PersistentManager;
import system.rdf.ui.GraphEd;

public class UserController {
	private User user;

	public UserController() {
		user = new User();
	}

	public UserController(String name, String password, ExtractData role,
			ExtractData enterprise) {
		user = new User(name, password, role, enterprise);
	}

	public UserController(String name) {
		user = new User(name);
	}

	public Object[] getCBModel() throws Exception {
		ArrayList<ExtractData> toReturn = new ArrayList<ExtractData>();
		try {
			String sql = "select name, id_enterprise from enterprise";
			ResultSet result = PersistentManager.getConnectionToPostgreSQL()
					.executeSql(sql);
			while (result.next()) {
				toReturn.add(new ExtractData(result.getInt("id_enterprise"),
						result.getString("name")));
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return toReturn.toArray();
	}

	public ArrayList<ExtractData> getRoles() throws SQLException {
		ArrayList<ExtractData> roles = new ArrayList<ExtractData>();
		ResultSet rToGet = PersistentManager.getConnectionToPostgreSQL()
				.select("users_role");
		while (rToGet.next()) {
			roles.add(new ExtractData(rToGet.getInt("id_user_role"), rToGet
					.getString("role")));
		}
		return roles;
	}

	/**
	 * this method select all the names of the users in the BD for fill the
	 * table in the CRUD view
	 * 
	 * @return the table model whit all the user names
	 */
	public DefaultTableModel loadTableModel() throws SQLException {
		String[] columnNames = { "User Name", "Role" };
		String[][] userData;
		String sql = "select user_name, role "
				+ "from system_users, users_role where"
				+ " system_users.id_user_rolefk = users_role.id_user_role";
		ResultSet result = null;
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			try {
				try {
					result = PersistentManager.getConnectionToPostgreSQL()
							.executeSql(sql);
				} catch (NullPointerException e) {
					GraphEd.errorPane
							.printMessage(new Exception(
									"The file to configure the database is missing !!!"));
				}
			} catch (Exception e) {
				GraphEd.errorPane.printMessage(e);
			}
		} else if (GraphEd.errorPane.getText().equals(
				"The file to configure the database is missing !!!") == false)
			GraphEd.errorPane.printMessage(new Exception(
					"No database connection found!!"));

		String sqlCount = "select count(*) as users_number from system_users";
		ResultSet resultCount = null;
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			try {
				try {
					resultCount = PersistentManager.getConnectionToPostgreSQL()
							.executeSql(sqlCount);
				} catch (NullPointerException e) {
					GraphEd.errorPane
							.printMessage(new Exception(
									"The file to configure the database is missing !!!"));
				}
			} catch (Exception e) {
				GraphEd.errorPane.printMessage(e);
			}
		} else if (GraphEd.errorPane.getText().equals(
				"The file to configure the database is missing !!!") == false)
			GraphEd.errorPane.printMessage(new Exception(
					"No database connection found!!"));
		resultCount.next();
		int rowNumber = resultCount.getInt("users_number");

		userData = new String[rowNumber][2];

		int i = 0;
		while (result.next()) {
			userData[i] = new String[] { result.getString("user_name"),
					result.getString("role") };
			i++;
		}
		return new DefaultTableModel(userData, columnNames);
	}

	public void inserUser() throws SQLException {
		try {
			user.insertUser();
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}

	}

	/**
	 * delete the user from DB
	 */
	public void deleteUser() throws SQLException {
		try {
			user.deleteFromDB();
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

	public void updateUser(String oldUserName) throws SQLException {
		try {
			user.updateInDB(oldUserName);
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}
}
