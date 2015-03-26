package system.rdf.dataBase;

public class UserModel {
    int id;
    String userName;
    String role;
    
    
    /**
     * @param id
     * @param role
     * @param userName
     */
    public UserModel(int id, String role, String userName) {
	super();
	this.id = id;
	this.role = role;
	this.userName = userName;
    }
    /**
     * @return the id
     */
    protected int getId() {
	return id;
    }
    /**
     * @param id the id to set
     */
    protected void setId(int id) {
	this.id = id;
    }
    /**
     * @return the userName
     */
    protected String getUserName() {
	return userName;
    }
    /**
     * @param userName the userName to set
     */
    protected void setUserName(String userName) {
	this.userName = userName;
    }
    /**
     * @return the role
     */
    protected String getRole() {
	return role;
    }
    /**
     * @param role the role to set
     */
    protected void setRole(String role) {
	this.role = role;
    }


}
