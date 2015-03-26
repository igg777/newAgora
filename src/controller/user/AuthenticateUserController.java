package controller.user;

import java.sql.ResultSet;

import javax.swing.JOptionPane;

import model.user.User;
import system.rdf.dataBase.ExtractData;
import system.rdf.dataBase.PersistentManager;
import system.rdf.ui.GraphEd;

public class AuthenticateUserController {

	public AuthenticateUserController() {

	}

	public User seekUserInDB(String userName, String userPassword)
	throws Exception {
		try {
			boolean flag = false;
			// this extractData is initialized whit a non interesting values
			ExtractData role = new ExtractData(0, "");
			PersistentManager.getConnectionToPostgreSQL().tryConnection();
			String sql = "select users_role.role, users_role.id_user_role from users_role,system_users where system_users.user_name ='"
				+ userName
				+ "'"
				+ "and system_users.password = '"
				+ userPassword
				+ "' and system_users.id_user_rolefk = users_role.id_user_role";
			
			System.out.println();
			ResultSet rs = PersistentManager.getConnectionToPostgreSQL()
			.executeSql(sql);
			while (rs.next()) {
				flag = true;
				String user_role = rs.getString("role");
				int role_id = rs.getInt("id_user_role");
				role = new ExtractData(role_id, user_role);
			}
			if (flag) {
				User authenticateUser = new User(userName, userPassword, role);
				return authenticateUser;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return null;
	}
}
