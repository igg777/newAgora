package controller.ga;

import java.util.ArrayList;

import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Problem;

public class ProblemManagerController {

	private static ProblemManagerController singleton = null;
	
	private ProblemManagerController() {
	}
	
	public static ProblemManagerController instance()  {
		if (singleton == null)
			singleton = new ProblemManagerController();
		return singleton;
	}
	
	public ArrayList<Problem> getProblems() {
		ConnectionToPostgres connection = PersistentManager.getConnectionToPostgreSQL();
		try {
			connection.connect();
			ArrayList<Problem> problems = connection.getProblems();
			connection.disconnect();
			return problems;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}			
	}
	
}
