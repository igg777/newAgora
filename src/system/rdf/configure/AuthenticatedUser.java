package system.rdf.configure;

/**
 * @author Irlan
 * This Class represent a user logged in into the system
 */
public class AuthenticatedUser {

	protected String userName;
	protected String userPassword;
	protected String userRole;
	protected boolean isValid = false;

	/**
	 * Empty constructor
	 */
	public AuthenticatedUser() {
		super();
	}

	public AuthenticatedUser(String userName, String userPassword) {
		super();
		this.userName = userName;
		this.userPassword = userPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * Is the user is valid
	 * @return true or false
	 */
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

}
