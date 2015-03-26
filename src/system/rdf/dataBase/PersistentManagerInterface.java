package system.rdf.dataBase;

public interface PersistentManagerInterface {
	
	public void conectToGestor(String host,String port,String user,String pass);
	public void createDataBase(String name);
	public String postgresDriver();
}
