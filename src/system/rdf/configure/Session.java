package system.rdf.configure;

/**
 * 
 * @author Armando
 */
public class Session {

	protected AuthenticatedUser authUser;
	
	public Session() {
	}

	public Session(AuthenticatedUser authUser) {
		super();
		this.authUser = authUser;
	}


	public AuthenticatedUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(AuthenticatedUser authUser) {
		this.authUser = authUser;
	}

}
