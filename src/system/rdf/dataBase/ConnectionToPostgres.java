package system.rdf.dataBase;


import java.awt.Point;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.hp.hpl.jena.iri.impl.Main;

import system.config.AppConfiguration;
import system.rdf.dataBase.ConnectionToDB;
import system.rdf.ui.ErrorPane;
import system.rdf.ui.GraphEd;
import system.rdf.utils.TabsComponentManager;
import system.rename.Tools;

/**
 * Handle the database connection
 * 
 * @author Irl�n Grangel
 */
public class ConnectionToPostgres extends ConnectionToDB {

	private Connection connection;

	
	// Constants to connect to data base
	public static String DB ;

	public static String DB_USER;

	public static String DB_PASSWD;

	public static String DB_HOST;

	public static String DB_PORT;

	public static String DB_URL;

	public final static String DBDRIVER_CLASS = "org.postgresql.Driver";
	
	private Tools tools = new Tools();

	public ConnectionToPostgres(String server, String user,
			String howToConnect, String dataBase, String port, String serverKind) {
		super(server, user, howToConnect, dataBase, port, serverKind);
		try {
			connection = null;
			this.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor
	 */
	public ConnectionToPostgres() {
		super();
		try {
			AppConfiguration config = new AppConfiguration();

			DB = config.getDataBase();

			DB_USER = config.getUser();

			DB_PASSWD = config.getPassword();

			DB_HOST = config.getHost();

			DB_PORT = config.getPort();

			DB_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB;
			
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

	/**
	 * Try if connection is able
	 * 
	 * @return
	 */
	public boolean tryConnection() {
		boolean flag = true;
		try {
			Class.forName(DBDRIVER_CLASS).newInstance();
			Properties props = new Properties();
			props.setProperty("user", DB_USER);
			props.setProperty("password", DB_PASSWD);
			connection = DriverManager.getConnection(DB_URL, props);
			
		}
		catch (NullPointerException e1) {
			flag = false;
			GraphEd.errorPane.printMessage(new Exception("The file to configure the database is missing !!!"));
		}
		catch (Exception e) {
			flag = false;
			GraphEd.errorPane.printMessage(e);
			
		}

		return flag;
	}

	/**
	 * Open a connection to Database
	 * 
	 * @see system.rdf.dataBase.ConnectionToDB#connect()
	 */

	public void connect() {
		boolean flag = tryConnection();
	}

	public void disconnect() {
		try {
			if (!(connection == null))
				if (!connection.isClosed()) {
					connection.close();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute the String sql passed by parameter
	 */
	public ResultSet executeSql(String strSQL) throws SQLException {
		ResultSet rs = null;
		Statement Stmt = connection.createStatement();
		rs = Stmt.executeQuery(strSQL);
		return rs;
	}

	public ResultSet createBD(String dataBase) throws SQLException {

		ResultSet rs = null;
		String st = "CREATE DATABASE " + dataBase
		+ " WITH OWNER = postgres ENCODING ='UTF8'"
		+ "TABLESPACE = pg_default";
		Statement Stmt = connection.createStatement();
		Stmt.execute(st);
		return rs;
	}

	/**
	 * Para los metodos de insertar, borrar y actualizar
	 * 
	 * @param strSQL
	 * @return
	 * @throws SQLException
	 */
	public int executeSqlForMethods(String strSQL) throws SQLException {
		Statement Stmt = connection.createStatement();
		return Stmt.executeUpdate(strSQL);
	}

	public ResultSet executeSqlForMethods1(String strSQL) throws SQLException {
		Statement Stmt = null;
		return Stmt.executeQuery(strSQL);
	}

	/**
	 * This method select a ResultSet o Data from the table pass by parameter
	 * 
	 * @param table
	 * @return Call to executeSql
	 * @throws SQLException
	 */
	public ResultSet select(String table) throws SQLException {
		String result = "SELECT * FROM " + table;
		return executeSql(result);
	}

	public ResultSet selectAlternatives(int idProblem) throws SQLException {
		String result = "SELECT * FROM  solution where id_problem ="
			+ idProblem;
		return executeSql(result);
	}

	public ResultSet select(int id) throws SQLException {
		String.valueOf(id);
		String result = "SELECT * FROM solution WHERE id_problem = '"
			+ String.valueOf(id) + "'";
		return executeSql(result);
	}

	/**
	 * @param table
	 * @param condicion
	 * @return
	 * @throws SQLException
	 */
	public ResultSet select(String table, String condition) throws SQLException {
		String result = "SELECT * FROM " + table + " WHERE " + condition;
		return executeSql(result);
	}

	public ResultSet selectId(String table, String condition)
	throws SQLException {
		String result = "SELECT id FROM " + table + " WHERE " + condition;
		return executeSql(result);
	}

	/**
	 * Se le pasa un arreglo del tipo clave, valor representado por un hashtable
	 * que permite esto. Las claves corresponden a los campos y los valores a
	 * los valores reales que se quieren insertar en los campos eje clave =
	 * nombre , valor = 'pepe', se recorre el hashtable y se construye una
	 * consulta que se le
	 * 
	 * @param table
	 * @param hastTableToInsert
	 * @throws SQLException
	 */

	public void insert(String table, Hashtable<String, Object> hastTableToInsert)
	throws SQLException {
		String fields = "", values = "";
		Iterator<String> it = hastTableToInsert.keySet().iterator();
		int counter = 1;
		while (it.hasNext()) {
			String key = (String) it.next();
			if (counter != hastTableToInsert.size()) {
				fields = fields + key + ",";
				values = values + "'" + hastTableToInsert.get(key).toString()
				+ "',";
			} else {
				fields = fields + key;
				values = values + "'" + hastTableToInsert.get(key).toString()
				+ "'";
			}
			counter++;
		}
		String result = "INSERT INTO " + table + " (" + fields + ") VALUES ("
		+ values + ")";
		executeSqlForMethods(result);
	}

	/**
	 * 
	 * @param table
	 * @param values
	 * @param conditions
	 * @throws SQLException
	 */
	public void update(String table, Hashtable<String, Object> values,
			Hashtable<String, Object> conditions) throws SQLException {
		String result = "UPDATE " + table + " SET ";
		Iterator it = values.keySet().iterator();
		int counter = 1;
		while (it.hasNext()) {
			String key = (String) it.next();
			if (counter != values.size())
				result += key + " = '" + values.get(key).toString() + "', ";
			else
				result += key + " = '" + values.get(key).toString() + "' ";
			counter++;
		}
		if (conditions != null && conditions.size() > 0) {
			result += "WHERE ";
			counter = 1;
			it = conditions.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (counter != conditions.size())
					result += key + " ='" + conditions.get(key).toString()
					+ "' AND ";
				else
					result += key + " ='" + conditions.get(key).toString()
					+ "'";
				counter++;
			}
		}
		executeSqlForMethods(result);
	}

	/**
	 * Deleting a row with many conditions
	 * 
	 * @param table
	 * @param conditions
	 * @throws SQLException
	 */
	public void delete(String table, Hashtable<String, Object> conditions)
	throws SQLException {
		Iterator<String> it = conditions.keySet().iterator();
		int counter = 1;
		String result = "DELETE FROM " + table + " WHERE ";
		while (it.hasNext()) {
			String key = (String) it.next();
			if (counter != conditions.size())
				result += key + " = '" + conditions.get(key).toString()
				+ "' AND ";
			else
				result += key + " = '" + conditions.get(key).toString() + "'";
			counter++;
		}
		executeSqlForMethods(result);
	}

	/**
	 * Overload Method to delete a row with only one condition
	 * 
	 * @param table
	 * @param condition
	 * @throws SQLException
	 */
	public void delete(String table, String condition) throws SQLException {
		String result = "DELETE FROM " + table + " WHERE " + condition;
		executeSqlForMethods(result);
	}
	
	public Problem getProblem(String nameProb) {

		String table = "problem";
		String condition = "name = '" + nameProb + "'";
		Problem problem = new Problem();
		ArrayList<RDFTriple> triples = new ArrayList<RDFTriple>();
		ResultSet rsProblem;
		ResultSet rsGraph;
		ResultSet rsTriple;
		ResultSet rsPoints;

		try {
			rsProblem = select(table, condition);
			rsProblem.next();
			int idGraph = rsProblem.getInt("id_graph");
			rsGraph = select("graph", "id_graph =" + String.valueOf(idGraph));
			String name = rsProblem.getString("name");
			rsGraph.next();
			String desc = rsGraph.getString("description");

			rsTriple = select("triple_rdf", "id_graph ="
					+ String.valueOf(idGraph));
			while (rsTriple.next()) {
				RDFTriple triple = new RDFTriple();

				int idTriple = rsTriple.getInt("id_triple");
				String subjectName = rsTriple.getString("subject");
				String objectName = rsTriple.getString("object");

				rsPoints = select("vertex_points", "id_triple="
						+ String.valueOf(idTriple));
				rsPoints.next();
				Point2D subjectPoint = new Point(rsPoints.getInt("subject_x"),
						rsPoints.getInt("subject_y"));
				Point2D objectPoint = new Point(rsPoints.getInt("object_x"),
						rsPoints.getInt("object_y"));
                String[] subjectProcess = rsTriple.getString("subject_process").split("--");
                String[] objectProcess = rsTriple.getString("object_process").split("--");
				triple.setSubject(new Subject(subjectName, subjectPoint));
				triple.setPredicate(new Predicate());
				triple.setObject(new RDFObject(objectName, objectPoint));
                triple.setSubjectProcess(subjectProcess);
                triple.setObjectProcess(objectProcess);
				triples.add(triple);

			}
			problem.setDescription(desc);
			problem.setName(name);
			problem.setTriples(triples);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return problem;
	}

	/**
	 * Gets the Problem specified by the id
	 * 
	 * @param int id of the problem
	 * @return Problem
	 */
	public Problem getProblem(int id) {

		String table = "problem";
		String condition = "id_problem = " + String.valueOf(id);
		Problem problem = new Problem();
		ArrayList<RDFTriple> triples = new ArrayList<RDFTriple>();
		ResultSet rsProblem;
		ResultSet rsGraph;
		ResultSet rsTriple;
		ResultSet rsPoints;

		try {
			rsProblem = select(table, condition);
			rsProblem.next();
			int idGraph = rsProblem.getInt("id_graph");
			rsGraph = select("graph", "id_graph =" + String.valueOf(idGraph));
			String name = rsProblem.getString("name");
			rsGraph.next();
			String desc = rsGraph.getString("description");

			rsTriple = select("triple_rdf", "id_graph ="
					+ String.valueOf(idGraph));
			while (rsTriple.next()) {
				RDFTriple triple = new RDFTriple();

				int idTriple = rsTriple.getInt("id_triple");
				String subjectName = rsTriple.getString("subject");
				String objectName = rsTriple.getString("object");

				rsPoints = select("vertex_points", "id_triple="
						+ String.valueOf(idTriple));
				rsPoints.next();
				Point2D subjectPoint = new Point(rsPoints.getInt("subject_x"),
						rsPoints.getInt("subject_y"));
				Point2D objectPoint = new Point(rsPoints.getInt("object_x"),
						rsPoints.getInt("object_y"));
                String[] subjectProcess = rsTriple.getString("subject_process").split("--");
                String[] objectProcess = rsTriple.getString("object_process").split("--");
				triple.setSubject(new Subject(subjectName, subjectPoint));
				triple.setPredicate(new Predicate());
				triple.setObject(new RDFObject(objectName, objectPoint));
                triple.setSubjectProcess(subjectProcess);
                triple.setObjectProcess(objectProcess);
				triples.add(triple);

			}
			problem.setDescription(desc);
			problem.setName(name);
			problem.setTriples(triples);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return problem;
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 * 
	 * @description: Method to load all problems which have solution and by a specific classification 
	 * 
	 * @return {@link ArrayList} of problem
	 * @throws SQLException
	 */
	public ArrayList<Problem> getProblemsByClassification(String classification) throws SQLException {
		ArrayList<Problem> problems = new ArrayList<Problem>();

		ResultSet rsProblems = this.select("problem where problem_classification ='" + classification + "' and id_problem in (SELECT id_problem FROM problem_has__solution)");
		while (rsProblems.next()) {
			int id = rsProblems.getInt("id_problem");
			Problem problem = getProblem(id);
			problem.setId(id);
			problems.add(problem);
		}

		return problems;
	}

	/**
	 * @param id
	 * @return Solution
	 */
	public Solution getSolution(int id) {
		String table = "solution";
		String condition = "id_solution = " + String.valueOf(id);
		Solution solution = new Solution();
		Problem problem;
		ArrayList<RDFTriple> triples = new ArrayList<RDFTriple>();
		ResultSet rsSolution;
		ResultSet rsGraph;
		ResultSet rsTriple;
		ResultSet rsPoints;

		try {
			rsSolution = select(table, condition);
			rsSolution.next();
			String name = rsSolution.getString("name");
			int idGraph = rsSolution.getInt("id_graph");
			int idProblem = rsSolution.getInt("id_problem");

			rsGraph = select("graph", "id_graph =" + String.valueOf(idGraph));
			problem = getProblem(idProblem);
			rsGraph.next();
			String desc = rsGraph.getString("description");

			rsTriple = select("triple_rdf", "id_graph ="
					+ String.valueOf(idGraph));

			while (rsTriple.next()) {
				RDFTriple triple = new RDFTriple();

				int idTriple = rsTriple.getInt("id_triple");
				String subjectName = rsTriple.getString("subject");
				String objectName = rsTriple.getString("object");
				String predicate = rsTriple.getString("predicate");
                String[] subjectProcess = rsTriple.getString("subject_process").split("--");
                String[] objectProcess = rsTriple.getString("object_process").split("--");
				rsPoints = select("vertex_points", "id_triple="
						+ String.valueOf(idTriple));
				rsPoints.next();
				Point2D subjectPoint = new Point(rsPoints.getInt("subject_x"),
						rsPoints.getInt("subject_y"));
				Point2D objectPoint = new Point(rsPoints.getInt("object_x"),
						rsPoints.getInt("object_y"));

				triple.setSubject(new Subject(subjectName, subjectPoint));
				triple.setPredicate(new Predicate(predicate));
				triple.setObject(new RDFObject(objectName, objectPoint));
                triple.setSubjectProcess(subjectProcess);
                triple.setObjectProcess(objectProcess);
				triples.add(triple);

			}

			solution.setAssociatedProblem(problem);
			solution.setDescription(desc);
			solution.setName(name);
			solution.setTriples(triples);

		} catch (SQLException e) {

		}

		return solution;
	}
	
	
	/**
	 * Gets all problem from database
	 * 
	 * @return {@link ArrayList} of problem
	 * @throws SQLException
	 */
	public ArrayList<Problem> getProblems() throws SQLException {
		ArrayList<Problem> problems = new ArrayList<Problem>();
		ResultSet rsProblems = this.select("problem");
		while (rsProblems.next()) {
			int id = rsProblems.getInt("id_problem");
			Problem problem = getProblem(id);
			problem.setId(id);
			problems.add(problem);
		}

		return problems;
	}

	public ArrayList<Solution> getSolutions() throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		ResultSet rsSolutions = this.select("solution");
		while (rsSolutions.next()) {
			int id = rsSolutions.getInt("id_solution");
			Solution solution = getSolution(id);
			solution.setId(id);
			solutions.add(solution);
		}
		return solutions;
	}

	/**
	 * Return the current inserted Graph id
	 * 
	 * @return int
	 */
	public int getCurrentGraphId() {
		int id = 1;
		try {
			String sql = "select currval('graph_id_graph_seq') as id";
			ResultSet r1 = executeSql(sql);
			r1.next();
			id = r1.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * Return the current Problem id
	 * 
	 * @return int
	 */
	public int getCurrentProblemId() {
		int id = 1;
		try {
			String sql = "select currval('problem_id_problem_seq') as id";
			ResultSet r1 = executeSql(sql);
			r1.next();
			id = r1.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * Return the current Solution id
	 * 
	 * @return int id
	 */
	public int getCurrentSolutionId() {
		int id = 0;
		try {
			String sql = "select currval('solution_id_solution_seq') as id";
			ResultSet r1 = executeSql(sql);
			r1.next();
			id = r1.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * Return the current triple id
	 * 
	 * @return int Id
	 */
	public int getCurrentTripleId() {
		int id = 1;
		try {
			String sql = "select currval('triple_rdf_id_triple_seq') as id";
			ResultSet r1 = executeSql(sql);
			r1.next();
			id = r1.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	// Getter and setter for attributes class

	/**
	 * @return the dB_URL
	 */
	public static String getDB_URL() {
		return DB_URL;
	}

	/**
	 * @param db_url
	 *            the dB_URL to set
	 */
	public static void setDB_URL(String db_url) {
		DB_URL = db_url;
	}

	/**
	 * @return the dB_USER
	 */
	public static String getDB_USER() {
		return DB_USER;
	}

	/**
	 * @param db_user
	 *            the dB_USER to set
	 */
	public static void setDB_USER(String db_user) {
		DB_USER = db_user;
	}

	/**
	 * @return the dB_PASSWD
	 */
	public static String getDB_PASSWD() {
		return DB_PASSWD;
	}

	/**
	 * @param db_passwd
	 *            the dB_PASSWD to set
	 */
	public static void setDB_PASSWD(String db_passwd) {
		DB_PASSWD = db_passwd;
	}

	/**
	 * @return the dB
	 */
	public static String getDB() {
		return DB;
	}

	/**
	 * @param db
	 *            the dB to set
	 */
	public static void setDB(String db) {
		DB = db;
	}

	public static String getDB_HOST() {
		return DB_HOST;
	}

	public static void setDB_HOST(String db_host) {
		DB_HOST = db_host;
	}

	public static String getDB_PORT() {
		return DB_PORT;
	}

	public static void setDB_PORT(String db_port) {
		DB_PORT = db_port;
	}

	/**
	 * @author Leo
	 * @param problem's name
	 * @return problem's id
	 */
	public int getIdProblem(String problemName) throws Exception {
		int toReturn = 0;
		try {
			String sql = "select id_problem from problem where name = '"
				+ problemName + "'";
			ResultSet result = executeSql(sql);
			while (result.next()) {
				toReturn = result.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return toReturn;

	}

	/**
	 * @author Leo
	 * @param alternative's Name
	 * @return the alternative's id
	 */
	public int getIdSolution(String solutionName) throws Exception {
		int toReturn = 0;
		try {
		String sql = "select id_solution from solution where name = '"
			+ solutionName + "'";		
			ResultSet result = executeSql(sql);
			while (result.next()) {
				toReturn = result.getInt("id_solution");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return toReturn;

	}

	/**
	 * @author Leo
	 * @param problem's id
	 * @param alternative's id
	 * Assign a alternative to a problem as solution
	 */
	public void assingSolutionToProblem(int idProblem, int idAlternative)throws Exception {
		try{
		Hashtable<String, Object> toInsert = new Hashtable<String, Object>();
		toInsert.put("id_problem", idProblem);
		toInsert.put("id_solution", idAlternative);
		insert("problem_has__solution", toInsert);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	
	/**
	 * @author Leo
	 * @param problem's name
	 * @return true if in the DB exist a problem whit that name
	 */
	public boolean existProblemInDB(String problemName)throws Exception{
		boolean flag = false;
		try{
		String sql = "select id_problem from problem where name = '"
		+ problemName + "'";
		
		ResultSet result = executeSql(sql);
		 while(result.next()){
			 flag = true;
		 }
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return flag;
	}
	
	/**
	 * @author Leo
	 * @param alternative's name
	 * @param problem's id
	 * @return true if in the DB exist a alternative, for one specific problem, whit that name
	 */
	public boolean existAlternativeInDB(String alternativeName, int idProblem)throws Exception{
		boolean flag = false;
		try{
		String sql = "select id_solution from solution  where name = '" + alternativeName + "' " +
				"and id_problem = '" + idProblem + "'";
		
		ResultSet result = executeSql(sql);
		 while(result.next()){
			 flag = true;
		 }
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return flag;
	}
	
	/**
	 * @author Leo
	 * @param problem's id
	 * @return true if in the DB exist a solution for that problem
	 */
	public boolean existSolutonInDB(int problemId)throws Exception{
		boolean flag = false;
		try{
		String sql = "select id_solution from problem_has__solution where id_problem = '"
		+ problemId + "'";
		
		ResultSet result = executeSql(sql);
		 while(result.next()){
			 flag = true;
		 }
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return flag;
	}
	
	/**
	 * @author Hector
	 * @param problem's id
	 * @return returns the solution's id for the current problem
	 */
	public int getSolutionForProblem(int problemId)throws Exception{
		int id = -1;
		try{
		String sql = "select id_solution from problem_has__solution where id_problem = '"
		+ problemId + "'";
		
		ResultSet result = executeSql(sql);
		 while(result.next()){
			 id = result.getInt("id_solution");
		 }
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return id;
	}

	
	/// testing something in development, this is not use yet
	/**
	 * @author Leo
	 * @param id_problem
	 * @return the alternatives for that problems
	 */
	public ArrayList<Solution> getAlternativesForProblems(int id_problem) {
		ArrayList<Solution> toReturn = new ArrayList<Solution>();
		String table = "solution";
		String condition = "id_problem = " + String.valueOf(id_problem);
		Solution solution = new Solution();
		Problem problem;
		ArrayList<RDFTriple> triples = new ArrayList<RDFTriple>();
		ResultSet rsSolution;
		ResultSet rsGraph;
		ResultSet rsTriple;
		ResultSet rsPoints;

		try {
			rsSolution = select(table, condition);
			
			while(rsSolution.next()){
			String name = rsSolution.getString("name");
			int idGraph = rsSolution.getInt("id_graph");
			int idProblem = rsSolution.getInt("id_problem");

			rsGraph = select("graph", "id_graph =" + String.valueOf(idGraph));
			problem = getProblem(idProblem);
			rsGraph.next();
			String desc = rsGraph.getString("description");

			rsTriple = select("triple_rdf", "id_graph ="
					+ String.valueOf(idGraph));

			while (rsTriple.next()) {
				RDFTriple triple = new RDFTriple();

				int idTriple = rsTriple.getInt("id_triple");
				String subjectName = rsTriple.getString("subject");
				String objectName = rsTriple.getString("object");
				String predicate = rsTriple.getString("predicate");

				rsPoints = select("vertex_points", "id_triple="
						+ String.valueOf(idTriple));
				rsPoints.next();
				Point2D subjectPoint = new Point(rsPoints.getInt("subject_x"),
						rsPoints.getInt("subject_y"));
				Point2D objectPoint = new Point(rsPoints.getInt("object_x"),
						rsPoints.getInt("object_y"));

				triple.setSubject(new Subject(subjectName, subjectPoint));
				triple.setPredicate(new Predicate(predicate));
				triple.setObject(new RDFObject(objectName, objectPoint));

				triples.add(triple);

			}

			solution.setAssociatedProblem(problem);
			solution.setDescription(desc);
			solution.setName(name);
			solution.setTriples(triples);
			toReturn.add(solution);
			}
		} catch (SQLException e) {
			GraphEd.errorPane.printMessage(e);
		}

		return toReturn;
	}
	
	public int getIdGraph(String proName){
		int toReturn = 0;
		try{
		String sql = "select id_graph from problem where name = '"+proName+"'";
		ResultSet result = executeSql(sql);
		while(result.next()){
			toReturn = result.getInt(1);
		}
		}
		catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
		return toReturn;
		
	}
	
	
}// End class

