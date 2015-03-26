package system.rdf.dataBase;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

import system.rdf.CustomCell;
import system.rdf.graph.EdgeManager;
import system.rdf.graph.GraphManager;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.MySolutionEdge;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.handlers.ExceptionHandler;
import system.rdf.ontology.OntologyLoader;
import system.rdf.ui.ErrorPane;
import system.rdf.ui.GraphEd;
import system.rdf.ui.UITabbedPaneEditor;
import system.rdf.utils.TabsComponentManager;
import system.rename.Tools;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class RDFSerializator {
	// model to be serialize
	protected Model rdfModel;
	// Manage the exceptions
	public ExceptionHandler manager;

	// Problem formulate Visual model
	protected static JGraph graph;

	// Classification information
	protected List Roots;

	// Main root of problem formulation
	protected Set mainRoot;

	public DefaultEdge edge;
	/**
	 * Array that contains all the Edges in the Graph
	 */

	// Object to Connect a PostgreSQL DataBase
	ConnectionToPostgres connection;

	public static final String uri = "file:system/rdf/resources/SCOR.owl";

	public RDFSerializator() {
		this.rdfModel = ModelFactory.createDefaultModel();
		this.connection = PersistentManager.getConnectionToPostgreSQL();
	}

	/**
	 * Class constructor
	 */
	public RDFSerializator(JGraph graph) {
		this.rdfModel = ModelFactory.createDefaultModel();
		this.connection = new ConnectionToPostgres();
		this.graph = graph;
		this.edge = edge;
	}

	/**
	 * Saving a problem on Database
	 * 
	 * @param String
	 *            Problem name
	 * @param String
	 *            Problem comments
	 */
	public ArrayList<String> saveProblem(ProblemGraph graph, String name,
			String desc) throws SQLException {
		ArrayList<String> toReturn = new ArrayList<String>();
		Problem problem = new Problem();
		String mainProcess = "";
		try {
			if (graph == null) {
				throw new Exception(
				"The problem can't be saved because the graph is null!");

			}

			List<Object> edgesList = new ArrayList<Object>();

			// Connecting with the database
			try {
				connection.connect();
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
			// Getting all the Roots in the Graph(vertex,edges,ports)
			Object[] roots = graph.getRoots();
			// Loading Decitional Ontology
			// OntModel model = OntologyLoader.loadDecitionalOntology();

			// Array that contains all the vertex in the Graph
			ArrayList<Object> vertex = new ArrayList<Object>();
			// This Cycle is to get all edges in the Graph that is been saving
			for (int i = 0; i < roots.length; i++) {
				Object object = roots[i];
				if (object instanceof DefaultGraphCell) {
					if (graph.getModel().isEdge(object)) {
						edgesList.add(object);
					} else if (graph.getModel().isPort(object)) {
						//
					} else {
						vertex.add(object);
					}
				}
			}
			// Creating HashTables To Insert
			Hashtable<String, Object> insertGraph = new Hashtable<String, Object>();
			Hashtable<String, Object> insertProblem = new Hashtable<String, Object>();
			Hashtable<String, Object> insertTriple = new Hashtable<String, Object>();
			Hashtable<String, Object> insertInferredTriple = new Hashtable<String, Object>();
			Hashtable<String, Object> insertPoints = new Hashtable<String, Object>();

			insertGraph.put("description", desc);

			connection.insert("graph", insertGraph);

			int idInsertedGraph = connection.getCurrentGraphId();

			// Inserting triples
			for (Object object : edgesList) {

				MyProblemCustomCell sourceVertex, targetVertex;
				int obj_x, obj_y, sub_x, sub_y;

				GraphModel gm = graph.getModel();

				sourceVertex = (MyProblemCustomCell) gm.getParent(gm
						.getSource(object));
				targetVertex = (MyProblemCustomCell) gm.getParent(gm
						.getTarget(object));

				AttributeMap mapSource = ((DefaultGraphCell) sourceVertex)
				.getAttributes();
				AttributeMap mapTarget = ((DefaultGraphCell) targetVertex)
				.getAttributes();

				sub_x = (int) GraphConstants.getBounds(mapSource).getX();
				sub_y = (int) GraphConstants.getBounds(mapSource).getY();
				
				obj_x = (int) GraphConstants.getBounds(mapTarget).getX();
				obj_y = (int) GraphConstants.getBounds(mapTarget).getY();

				// populating array list for insert triples
				insertTriple.put("id_graph", idInsertedGraph);
				insertTriple.put("subject", sourceVertex);
				insertTriple.put("predicate", "null");
				insertTriple.put("object", targetVertex);
				insertTriple.put("subject_process", GraphEd.tools.concatenateProcess(sourceVertex.getProcess()));
				insertTriple.put("object_process", GraphEd.tools.concatenateProcess(targetVertex.getProcess()));

				// problem.mapElementsToOnt(sourceVertex.toString(), " ",
				// targetVertex.toString());
				// inserting the triples
				connection.insert("triple_rdf", insertTriple);

				int id = connection.getCurrentTripleId();
				// inserting the cell's points
				insertPoints.put("id_triple", id);
				insertPoints.put("subject_x", sub_x);
				insertPoints.put("subject_y", sub_y);

				insertPoints.put("object_x", obj_x);
				insertPoints.put("object_y", obj_y);

				connection.insert("vertex_points", insertPoints);

			}// end inserting triples

			insertProblem.put("name", name);
			insertProblem.put("problem_classification", graph.classifyProblem());
			insertProblem.put("id_graph", idInsertedGraph);

			connection.insert("problem", insertProblem);

			// After inserted a problem the Inverse Index is created
			int idInsertedProblem = connection.getCurrentProblemId();
			mainProcess = graph.classifyProblem();

			toReturn.add(mainProcess);
			toReturn.add(String.valueOf(idInsertedGraph));

			// insert inferred triples
			// made by Leo
			Object[][] inferred = GraphEd.tools.getInferredTriples();
			for (Object[] objects : inferred) {
				//System.out.println(objects[0].toString());
				//System.out.println(objects[1].toString());
				
				ArrayList<Object> subject = GraphEd.tools.desconcatenateNameAndProcess(objects[0].toString());
				ArrayList<Object> object = GraphEd.tools.desconcatenateNameAndProcess(objects[1].toString());
                for (String string : (String[])subject.get(1)) {
					//System.out.println("estos son los procesos del subject "+ string);
				}
				insertInferredTriple.put("subject", subject.get(0).toString());
				insertInferredTriple.put("predicate", "isCauseOf");
				insertInferredTriple.put("object", object.get(0).toString());
				insertInferredTriple.put("id_problem", idInsertedProblem);
				insertInferredTriple.put("subject_process", GraphEd.tools.concatenateProcess((String[])subject.get(1)));
				insertInferredTriple.put("object_process", GraphEd.tools.concatenateProcess((String[])object.get(1)));
				connection.insert("inferred_rdf_triple", insertInferredTriple);
				insertInferredTriple.clear();
			}

			/*
			 * Get Indicators that belong to a root cause and insert it in table
			 * inverse_index
			 */
			// ArrayList<Object>rootCausesArray = problem.getRootCauses();
			Hashtable<String, Object> inverseIndex = new Hashtable<String, Object>();
			// Inserting the indicator and the problem in Inverse Index
			/*
			 * for (Object indicator : rootCausesArray) {
			 * inverseIndex.put("id_problem",idInsertedProblem);
			 * inverseIndex.put("indicator", indicator);
			 * connection.insert("inverse_index", inverseIndex);
			 * 
			 * // getting class Indicator from Ontology OntClass indicatorClass
			 * = OntologyLoader.modelToScor.getOntClass(OntologyLoader.NS +
			 * "Indicators"); // get all instances of Indicator class
			 * ExtendedIterator iterIndicator = indicatorClass.listInstances();
			 * 
			 * Property belongToKPI =
			 * OntologyLoader.modelToScor.getProperty(OntologyLoader.NS +
			 * "belongToKPI");
			 * 
			 * while (iterIndicator.hasNext()) { Individual object =
			 * (Individual) iterIndicator.next();
			 * if(object.getLocalName().equals(indicator)){ ExtendedIterator
			 * iterator = object.listPropertyValues(belongToKPI); while
			 * (iterator.hasNext()) { Object object2 = (Object) iterator.next();
			 * System.out.println("aqws " + object2); } } } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

		connection.disconnect();
		return toReturn;
	}

	/**
	 * Classify the problem according to the root causes in graph formulation
	 * 
	 * @param idProblem
	 * @throws SQLException
	 */
	
//	  public String classifyProblem(int idProblem) throws SQLException{
//	  
//	  Problem problem = new Problem();
//	  
//	  if(graph == null) graph = (ProblemGraph)
//	  UITabbedPaneEditor.graphs.get(0);
//	  
//	  List<Object> edgesList = new ArrayList<Object>();
//	  
//	  // Getting all the Roots in the Graph(vertex,edges,ports) Object[] roots
//	  = graph.getRoots(); //Loading Decitional Ontology //OntModel model =
//	  OntologyLoader.loadDecitionalOntology();
//	  
//	  // Array that contains all the vertex in the Graph ArrayList<Object>
//	  vertex = new ArrayList<Object>(); //This Cycle is to get all edges in the
//	  Graph that is been saving for (int i = 0; i < roots.length; i++) { Object
//	  object = roots[i]; if (object instanceof DefaultGraphCell) { if
//	  (graph.getModel().isEdge(object)) { edgesList.add(object); } else if
//	  (graph.getModel().isPort(object)) { // } else { vertex.add(object); } } }
//	  
//	  ArrayList<RDFTriple>tripleArray = new ArrayList<RDFTriple>();
//	  
//	  for (Object object : edgesList) {
//	  
//	  Object sourceVertex, targetVertex; GraphModel gm = graph.getModel();
//	  sourceVertex = gm.getParent(gm.getSource(object)); targetVertex =
//	  gm.getParent(gm.getTarget(object));
//	  
//	  RDFTriple triple = new RDFTriple(); triple.setSubject(new
//	  Subject(sourceVertex.toString())); triple.setPredicate(new
//	  Predicate("")); triple.setObject(new RDFObject(targetVertex.toString()));
//	  tripleArray.add(triple); }
//	  
//	  problem.setTriples(tripleArray);
//	  
//	  //Getting Root Causes of Current Problem
//	  ArrayList<RDFTriple>rootCauseArray = new ArrayList<RDFTriple>();
//	  rootCauseArray = problem.getRootCauses(); ArrayList<String>rootCauseInd =
//	  new ArrayList<String>(); //Getting the Subjects(Indicators) that are root
//	  causes for (RDFTriple triple : rootCauseArray) {
//	  rootCauseInd.add(triple.getSubject().getLabel()); }
//	  
//	  int []fiveProcess = new int[5]; // For going into a Map for(String key :
//	  indicatorProcess.keySet()){ for (int i = 0; i < rootCauseInd.size(); i++)
//	  { //If the root cause is found in Map
//	  if(rootCauseInd.get(i).equals(key)){
//	  if(indicatorProcess.get(key).equals("D.Deliver")){ fiveProcess[0]++; }
//	  if(indicatorProcess.get(key).equals("M.Make")){ fiveProcess[1]++; }
//	  if(indicatorProcess.get(key).equals("P.Plan")){ fiveProcess[2]++; }
//	  if(indicatorProcess.get(key).equals("R.Return")){ fiveProcess[3]++; }
//	  if(indicatorProcess.get(key).equals("S.Source")){ fiveProcess[4]++; } } }
//	  }
//	  
//	  int toCompare = -100; int higher,index = 0; for (int i = 0; i <
//	  fiveProcess.length; i++) { if(fiveProcess[i] > toCompare){ higher =
//	  fiveProcess[i]; toCompare = higher; index = i; } } String mainProcess =
//	  ""; switch (index) { case 0: mainProcess = "Deliver"; break; case 1:
//	  mainProcess = "Make"; break; case 2: mainProcess = "Plan"; break; case 3:
//	  mainProcess = "Return"; break; case 4: mainProcess = "Source"; break; }
//	  
//	  
//	  Hashtable<String, Object> classifyProblem = new Hashtable<String,
//	  Object>(); classifyProblem.put("id_problem",idProblem);
//	  classifyProblem.put("main_process", mainProcess);
//	  
//	  connection.insert("problem_classification",classifyProblem); 
//	  return
//	  mainProcess; 
//	  }
	 
	/**
	 * Save on Database a problem alternative
	 * 
	 * @param int problem id
	 * @param String
	 *            comments of the alternative
	 */
	/*public void saveProblemAlternative(int id, String desc, SolutionGraph sgraph) {

		try {
			List<Object> edgesList = new ArrayList<Object>();

			// graph = (ProblemGraph) UITabbedPaneEditor.getProblemGraph();
			// Connecting with the database
			connection.connect();
			// Getting all the Roots in the Graph(vertex,edges,ports)
			Object[] roots = sgraph.getRoots();

			// Array that contains all the vertex in the Graph
			ArrayList vertex = new ArrayList();
			for (int i = 0; i < roots.length; i++) {
				Object object = roots[i];
				if (object instanceof DefaultGraphCell) {
					if (sgraph.getModel().isEdge(object)) {
						edgesList.add(object);
					} else if (sgraph.getModel().isPort(object)) {
						//
					} else {
						vertex.add(object);
					}
				}
			}

			Hashtable<String, Object> insertGraph = new Hashtable<String, Object>();
			Hashtable<String, Object> insertAlternative = new Hashtable<String, Object>();
			Hashtable<String, Object> insertTriple = new Hashtable<String, Object>();

			insertGraph.put("description", desc);

			connection.insert("graph", insertGraph);

			String sql = "select currval('graph_id_graph_seq') as id";
			ResultSet r1 = connection.executeSql(sql);

			// Getting the inserted Id
			r1.next();
			int idInsertedGraph = r1.getInt("id");

			// Inserting triples
			for (Object object : edgesList) {

				Object sourceVertex, targetVertex;
				GraphModel gm = sgraph.getModel();

				sourceVertex = gm.getParent(gm.getSource(object));
				targetVertex = gm.getParent(gm.getTarget(object));

				insertTriple.put("id_graph", idInsertedGraph);
				insertTriple.put("subject", sourceVertex);

				if (!(gm.getValue(object) == null))
					insertTriple.put("predicate", gm.getValue(object)
							.toString());
				else
					insertTriple.put("predicate", "null");

				insertTriple.put("object", targetVertex);

				connection.insert("triple_rdf", insertTriple);

			}
			insertAlternative.put("id_problem", id);
			insertAlternative.put("id_graph", idInsertedGraph);

			connection.insert("problem_alternatives", insertAlternative);
		} catch (Exception e) {
			e.printStackTrace();
		}

		connection.disconnect();
	}*/

	/**
	 * Insert a database on database
	 */
	public void saveSolutionOnDB(int problemId, String name, String desc) {

		// graph = GraphManager.getSolutionGraph();
		List<Object> edgesList = new ArrayList<Object>();

		connection.connect();

		// Getting all the Roots in the Graph(vertex,edges,ports)
		Object[] roots = graph.getRoots();
		// System.out.println("Esto es lo que tiene:"+roots.length);

		// Array that contains all the vertex in the Graph
		ArrayList vertex = new ArrayList();

		for (int i = 0; i < roots.length; i++) {
			Object object = roots[i];
			if (object instanceof DefaultGraphCell) {
				if (graph.getModel().isEdge(object)) {
					edgesList.add(object);
				} else if (graph.getModel().isPort(object)) {
					//
				} else {
					vertex.add(object);
				}
			}
		}
		//System.out.println("Esto es lo que tiene la lista: "+edgesList.size())
		// ;

		Hashtable<String, Object> insert = new Hashtable<String, Object>();

		insert.put("description", desc);

		try {

			connection.insert("graph", insert);

			int idInsertedGraph = connection.getCurrentGraphId();

			Hashtable<String, Object> insertTriple = new Hashtable<String, Object>();
			Hashtable<String, Object> insertSolution = new Hashtable<String, Object>();
			Hashtable<String, Object> insertSolAlt = new Hashtable<String, Object>();
			Hashtable<String, Object> insertPoints = new Hashtable<String, Object>();
			// Inserting triples

			for (Object object : edgesList) {

				Object sourceVertex, targetVertex;
				GraphModel gm = graph.getModel();
				int obj_x, obj_y, sub_x, sub_y;

				sourceVertex = gm.getParent(gm.getSource(object));
				targetVertex = gm.getParent(gm.getTarget(object));

				AttributeMap mapSource = ((DefaultGraphCell) sourceVertex)
				.getAttributes();
				AttributeMap mapTarget = ((DefaultGraphCell) targetVertex)
				.getAttributes();

				sub_x = (int) GraphConstants.getBounds(mapSource).getX();
				sub_y = (int) GraphConstants.getBounds(mapSource).getY();

				obj_x = (int) GraphConstants.getBounds(mapTarget).getX();
				obj_y = (int) GraphConstants.getBounds(mapTarget).getY();

				// populating the array list for insert the triple
				insertTriple.put("id_graph", idInsertedGraph);
				insertTriple.put("subject", sourceVertex);

				if (!(gm.getValue(object) == null)) {
					String predicate = gm.getValue(object).toString();
					if (predicate.contains("<<")) {
						predicate = ((MySolutionEdge)object).getCorrectiveAction();
					} else
						predicate = "No corrective action assigned";
					insertTriple.put("predicate", predicate);
				} else
					insertTriple.put("predicate", "");

				insertTriple.put("object", targetVertex);
				insertTriple.put("subject_process",GraphEd.tools.concatenateProcess(
						((MyProblemCustomCell) sourceVertex).getProcess()));
				insertTriple.put("object_process", GraphEd.tools.concatenateProcess(
						((MyProblemCustomCell) targetVertex).getProcess()));
				connection.insert("triple_rdf", insertTriple);

				int id = connection.getCurrentTripleId();

				insertPoints.put("id_triple", id);
				insertPoints.put("subject_x", sub_x);
				insertPoints.put("subject_y", sub_y);

				insertPoints.put("object_x", obj_x);
				insertPoints.put("object_y", obj_y);

				connection.insert("vertex_points", insertPoints);
			}// Ends of for...

			insertSolution.put("id_graph", idInsertedGraph);
			insertSolution.put("name", name);
			insertSolution.put("id_problem", problemId);

			connection.insert("solution", insertSolution);

			String getSolId = "select currval('solution_id_solution_seq') as id";
			ResultSet r2 = connection.executeSql(getSolId);
			r2.next();

			insertSolAlt.put("id_solution", r2.getInt("id"));
			connection.insert("solution_alternatives", insertSolAlt);

		} catch (SQLException e) {
			TabsComponentManager.getErrorPaneInstance().setText(e.getMessage());
		}

		connection.disconnect();
	}

	/**
	 * Save on Database a solution alternative for a problem
	 * 
	 * @param problemId
	 * @param name
	 * @param desc
	 */
	/*public void saveAlternative(int problemId, String name, String desc) {
		try {
			List<Object> edgesList = new ArrayList<Object>();
			// graph = (SolutionGraph) UITabbedPaneEditor.getSolutionGraph();

			connection.connect();
			// Getting all the Roots in the Graph(vertex,edges,ports)
			Object[] roots = graph.getRoots();

			// Array that contains all the vertex in the Graph
			ArrayList vertex = new ArrayList();

			for (int i = 0; i < roots.length; i++) {
				Object object = roots[i];
				if (object instanceof DefaultGraphCell) {
					if (graph.getModel().isEdge(object)) {
						edgesList.add(object);
					} else if (graph.getModel().isPort(object)) {
						//
					} else {
						vertex.add(object);
					}
				}
			}

			Hashtable<String, Object> insertGraph = new Hashtable<String, Object>();

			insertGraph.put("description", desc);

			connection.insert("graph", insertGraph);
			String sql = "select currval('graph_id_graph_seq') as id";
			ResultSet r1 = connection.executeSql(sql);

			// Getting the inserted Id
			r1.next();
			int idInsertedGraph = r1.getInt("id");

			Hashtable<String, Object> insertTriple = new Hashtable<String, Object>();
			Hashtable<String, Object> insertAlternative = new Hashtable<String, Object>();

			// Inserting triples
			for (Object object : edgesList) {

				Object sourceVertex, targetVertex;
				GraphModel gm = graph.getModel();

				sourceVertex = gm.getParent(gm.getSource(object));
				targetVertex = gm.getParent(gm.getTarget(object));

				insertTriple.put("id_graph", idInsertedGraph);
				insertTriple.put("subject", sourceVertex);

				if (!(gm.getValue(object) == null))
					insertTriple.put("predicate", gm.getValue(object)
							.toString());
				else
					insertTriple.put("predicate", "null");

				insertTriple.put("object", targetVertex);

				connection.insert("triple_rdf", insertTriple);
			}

			insertAlternative.put("id_graph", idInsertedGraph);
			insertAlternative.put("name", name);

			connection.insert("solution", insertAlternative);

		} catch (SQLException e) {
			TabsComponentManager.getErrorPaneInstance().setText(e.getMessage());
		}
		connection.disconnect();

	}
*/
	/**
	 * Creating the RDF triple 1st Step Create model, running through graph and
	 * for each edge 1. Resource => Source Vertex 2. Property Name => Edge 3.
	 * Property Value => Target Vertex
	 */
/*	public void createRDFTriple() {

		// Navigating the JGraph to make the triple
		try {
			List<Object> edgesList = new ArrayList<Object>();
			for (Object object : edgesList) {

				Object sourceVertex, targetVertex;
				GraphModel gm = graph.getModel();
				Resource resource;

				sourceVertex = gm.getParent(gm.getSource(object));
				targetVertex = gm.getParent(gm.getTarget(object));

				String uriModel = uri + "#" + sourceVertex;

				resource = rdfModel.createResource(uriModel);
				resource.addProperty(rdfModel.createProperty(uri + "#"
						+ gm.getValue(object).toString()),
						(String) targetVertex);

			}
		} catch (Exception e) {
			TabsComponentManager.getErrorPaneInstance().setText(e.getMessage());
		}
	}
*/
	/**
	 * Safe to RDF file system on the specific path
	 * 
	 * @param path
	 */
	/*public void saveModelLikeRdf(String path) {
		String ontUri = "http://www.owl-ontologies.com/SCOR_Class_SubClass.owl";
		// Navigating the JGraph to make the triple
		Object[] roots = graph.getRoots();
		GraphModel gm = graph.getModel();
		List<Object> edgesList = new ArrayList<Object>();
		// Getting the edges
		for (int i = 0; i < roots.length; i++) {
			Object object = roots[i];
			if (object instanceof DefaultGraphCell) {
				if (gm.isEdge(object)) {
					edgesList.add(object);
				}
			}
		}

		for (Object object : edgesList) {

			Object sourceVertex, targetVertex;
			Resource resource;
			Object edgeLabel = ((DefaultEdge) object).getUserObject();
			sourceVertex = gm.getParent(gm.getSource(object));
			targetVertex = gm.getParent(gm.getTarget(object));

			String uriModel = ontUri + "#" + sourceVertex;
			resource = rdfModel.createResource(uriModel);
			if (edgeLabel == null)
				resource.addProperty(rdfModel.createProperty(ontUri + "#"
						+ "null"), ((CustomCell) targetVertex).getUserObject()
						.toString());
			else {
				resource.addProperty(rdfModel.createProperty(ontUri + "#"
						+ edgeLabel.toString()), ((CustomCell) targetVertex)
						.getUserObject().toString());
			}
		}
		try {
			this.rdfModel.write(new PrintWriter(path));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

	}*/

	/**
	 * @return the graph
	 */
	public JGraph getGraph() {
		return graph;
	}

	/**
	 * @param graph
	 *            the graph to set
	 */
	public void setGraph(JGraph graph) {
		this.graph = graph;
	}

	/**
	 * Recorre un modelo de la base de datos
	 * 
	 * @param modelName
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void recorrerModeloBD(String modelName)
	throws InstantiationException, IllegalAccessException, SQLException {
		try {
			// load driver class
			Class.forName(ConnectionToPostgres.DBDRIVER_CLASS).newInstance();
			Connection con = DriverManager.getConnection(
					ConnectionToPostgres.DB_URL, ConnectionToPostgres.DB_USER,
					ConnectionToPostgres.DB_PASSWD);
			DBConnection dbcon = new DBConnection(con, ConnectionToPostgres.DB);
			// Open two existing models
			ModelRDB model1 = ModelRDB.open(dbcon, modelName);
			StmtIterator iter = model1.listStatements();

			while (iter.hasNext()) {
				Statement stmt = iter.nextStatement(); // get next statement
				Resource subject = stmt.getSubject(); // get the subject
				Property predicate = stmt.getPredicate(); // get the predicate
				RDFNode object = stmt.getObject(); // get the object

				/*
				 * System.out.println("Sujeto "+subject.toString());
				 * System.out.println("Predicado " + predicate.toString() +
				 * " "); if (object instanceof Resource) {
				 * System.out.println("Objeto "+object.toString()); } else { //
				 * object is a literal System.out.println("Objeto \"" +
				 * object.toString() + "\""); } System.out.println(" .");
				 */
			}

		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * Calculate the frequency that a triple has according to the total of
	 * triples in Knowledge Base (only matter problems triple)
	 * 
	 * @param sourceVertex
	 * @param targetVertex
	 * 
	 * @return the frequency that have that triple in the Knowledge Base
	 */
	public double calculateTotalFrequencyForProblems(MyProblemCustomCell sourceVertex, MyProblemCustomCell targetVertex) {
		double totalFrecuency = 0;
		// TODO Auto-generated method stub
		try {

			double countingTotal = 0;
			double total = 0;
			String sql = "select count(*) as countingTotal from triple_rdf, problem where " +
			"triple_rdf.subject ='" + sourceVertex.toString() 
			+"' and triple_rdf.object ='" + targetVertex.toString()
			+"'	and triple_rdf.subject_process ='" + GraphEd.tools.concatenateProcess(sourceVertex.getProcess())
			+"'	and triple_rdf.object_process ='" + GraphEd.tools.concatenateProcess(targetVertex.getProcess()) 
			+"' and triple_rdf.id_graph = problem.id_graph";
			// made by leo
			String sqlInferred = "select count(*) as countingTotal from inferred_rdf_triple where " +
			"inferred_rdf_triple.subject ='" + sourceVertex.toString() 
			+"' and inferred_rdf_triple.object ='" + targetVertex.toString()
			+"'	and inferred_rdf_triple.subject_process ='" + GraphEd.tools.concatenateProcess(sourceVertex.getProcess())
			+"'	and inferred_rdf_triple.object_process ='" + GraphEd.tools.concatenateProcess(targetVertex.getProcess()) 
			+"'";
			String sqlTotalInferred = "select count(*) as total from inferred_rdf_triple";
			// end of made by leo
			String sqlTotal = "select count(*) as total from triple_rdf, problem where triple_rdf.id_graph = problem.id_graph ";
			// System.out.println(sqlTotal);

			ConnectionToPostgres connection = PersistentManager
			.getConnectionToPostgreSQL();

			if (connection.tryConnection()) {
				ResultSet r1, r2, r3, r4;

				r1 = connection.executeSql(sql);
				r1.next();
				countingTotal += r1.getInt("countingTotal");
				// System.out.println("countingTotal " + countingTotal);
				// made by leo
				r3 = connection.executeSql(sqlInferred);
				r3.next();
				countingTotal += r3.getInt("countingTotal");
				// end of made by leo
				r2 = connection.executeSql(sqlTotal);
				r2.next();
				total += r2.getInt("total");
				// System.out.println("Total " + total);
				// made by leo
				r4 = connection.executeSql(sqlTotalInferred);
				r4.next();
				total += r4.getInt("total");
				// end of made by leo
				totalFrecuency = countingTotal / total;
				// System.out.println("TotalFre " + totalFrecuency);
			}
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
			// e.printStackTrace();
		}

		// Getting the inserted Id

		return totalFrecuency;

	}
	
	
	/**
	 * 
	 * Calculate the frequency that a triple has according to the total of
	 * triples in Knowledge Base (only matter alternatives triple)
	 * 
	 * @param sourceVertex
	 * @param targetVertex
	 * 
	 * @return the frequency that have that triple in the Knowledge Base
	 */
	public  double calculateTotalFrequencyForAlternatives(MyProblemCustomCell sourceVertex, MyProblemCustomCell targetVertex) {
		double totalFrecuency = 0;		
		try {

			double countingTotal = 0;
			double total = 0;
			String sql = "select count(*) as countingTotal from triple_rdf, solution where " +
			"triple_rdf.subject ='" + sourceVertex.toString() 
			+"' and triple_rdf.object ='" + targetVertex.toString()
			+"'	and triple_rdf.subject_process ='" + GraphEd.tools.concatenateProcess(sourceVertex.getProcess())
			+"'	and triple_rdf.object_process ='" + GraphEd.tools.concatenateProcess(targetVertex.getProcess()) 
			+"' and triple_rdf.id_graph = solution.id_graph";
			
			String sqlTotal = "select count(*) as total from triple_rdf, solution where triple_rdf.id_graph = solution.id_graph ";
			// System.out.println(sqlTotal);

			ConnectionToPostgres connection = PersistentManager
			.getConnectionToPostgreSQL();

			if (connection.tryConnection()) {
				ResultSet r1, r2;

				r1 = connection.executeSql(sql);
				r1.next();
				countingTotal += r1.getInt("countingTotal");
				// System.out.println("countingTotal " + countingTotal);
				
				r2 = connection.executeSql(sqlTotal);
				r2.next();
				total += r2.getInt("total");
				
				totalFrecuency = countingTotal / total;
			}
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}

		// Getting the inserted Id

		return totalFrecuency;

	}
	
	
	
	
	

	/**
	 * Calculate the frequency that a triple has according to the total of
	 * triples by process in Knowledge Base
	 * 
	 * @param sourceVertex
	 * @param targetVertex
	 */
	public static String calculateFrecuencyByProcess(String sourceVertex,
			String targetVertex, String mainProcess) {
		double totalFrecuency;
		String toReturn = "";
		// Total of triples in KB with subject = sourceVertex and object =
		// targetVertex
		String sql = "select count(*) as countingTotal from triple_rdf where "
			+ "triple_rdf.predicate = 'null'" + "and triple_rdf.subject ='"
			+ sourceVertex + "' and triple_rdf.object ='" + targetVertex
			+ "'";

		String sqlTotal = "select count(*) as total from triple_rdf,problem_classification,problem where "
			+ "triple_rdf.id_graph = problem.id_graph and problem_classification.id_problem = problem.id_problem "
			+ "and triple_rdf.subject ='"
			+ sourceVertex
			+ "' and triple_rdf.object ='"
			+ targetVertex
			+ "'"
			+ "and problem_classification.main_process = '"
			+ mainProcess
			+ "'";

		ConnectionToPostgres connection = PersistentManager
		.getConnectionToPostgreSQL();
		if (connection.tryConnection()) {
			ResultSet r1, r2;
			try {
				r1 = connection.executeSql(sql);
				r1.next();
				double countingTotal = r1.getInt("countingTotal");
				r2 = connection.executeSql(sqlTotal);
				r2.next();
				double total = r2.getInt("total");
				totalFrecuency = countingTotal / total;
				// Writing over edges problems
				toReturn = Double.toString(totalFrecuency);
				if (toReturn.equals("0.0")) {
					toReturn = "0.0";
				} else if (toReturn.length() == 3) {

				} else {
					toReturn = toReturn.substring(0, 4);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return toReturn;
	}

	/**
	 * replace the graph of a problem in a dataBase without loose its
	 * alternatives
	 * 
	 * @author Leo
	 * @param graph
	 * @param graphName
	 * @param desc
	 * @return
	 * @throws SQLException
	 */
	public void updateProblem(ProblemGraph graph, String name, String graphDesc)
	throws SQLException {
		try {
			connection.connect();
			Problem problem = new Problem();
			String mainProcess = "";
			int oldIdGraph = connection.getIdGraph(name);
			int idProblem = connection.getIdProblem(name);
			if (graph == null) {
				throw new Exception(
				"The problem can't be saved because the graph is null!");

			}

			List<Object> edgesList = new ArrayList<Object>();

			// Connecting with the database

			// Getting all the Roots in the Graph(vertex,edges,ports)
			Object[] roots = graph.getRoots();
			// Loading Decitional Ontology
			// OntModel model = OntologyLoader.loadDecitionalOntology();

			// Array that contains all the vertex in the Graph
			ArrayList<Object> vertex = new ArrayList<Object>();
			// This Cycle is to get all edges in the Graph that is been saving
			for (int i = 0; i < roots.length; i++) {
				Object object = roots[i];
				if (object instanceof DefaultGraphCell) {
					if (graph.getModel().isEdge(object)) {
						edgesList.add(object);
					} else if (graph.getModel().isPort(object)) {
						//
					} else {
						vertex.add(object);
					}
				}
			}
			// Creating HashTables To Insert
			Hashtable<String, Object> insertGraph = new Hashtable<String, Object>();
			Hashtable<String, Object> insertProblem = new Hashtable<String, Object>();
			Hashtable<String, Object> insertTriple = new Hashtable<String, Object>();
			Hashtable<String, Object> insertInferredTriple = new Hashtable<String, Object>();
			Hashtable<String, Object> insertPoints = new Hashtable<String, Object>();

			insertGraph.put("description", graphDesc);

			connection.insert("graph", insertGraph);

			int idInsertedGraph = connection.getCurrentGraphId();

			// Inserting triples
			for (Object object : edgesList) {

				MyProblemCustomCell sourceVertex, targetVertex;
				int obj_x, obj_y, sub_x, sub_y;

				GraphModel gm = graph.getModel();

				sourceVertex = (MyProblemCustomCell) gm.getParent(gm
						.getSource(object));
				targetVertex = (MyProblemCustomCell) gm.getParent(gm
						.getTarget(object));

				AttributeMap mapSource = ((DefaultGraphCell) sourceVertex)
				.getAttributes();
				AttributeMap mapTarget = ((DefaultGraphCell) targetVertex)
				.getAttributes();

				sub_x = (int) GraphConstants.getBounds(mapSource).getX();
				sub_y = (int) GraphConstants.getBounds(mapSource).getY();

				obj_x = (int) GraphConstants.getBounds(mapTarget).getX();
				obj_y = (int) GraphConstants.getBounds(mapTarget).getY();

				// populating array list for insert triples
				insertTriple.put("id_graph", idInsertedGraph);
				insertTriple.put("subject", sourceVertex);
				insertTriple.put("predicate", "null");
				insertTriple.put("object", targetVertex);
				insertTriple.put("subject_process", GraphEd.tools.concatenateProcess(sourceVertex.getProcess()));
				insertTriple.put("object_process", GraphEd.tools.concatenateProcess(targetVertex.getProcess()));

				// problem.mapElementsToOnt(sourceVertex.toString(), " ",
				// targetVertex.toString());
				// inserting the triples
				connection.insert("triple_rdf", insertTriple);

				int id = connection.getCurrentTripleId();
				// inserting the cell's points
				insertPoints.put("id_triple", id);
				insertPoints.put("subject_x", sub_x);
				insertPoints.put("subject_y", sub_y);

				insertPoints.put("object_x", obj_x);
				insertPoints.put("object_y", obj_y);

				connection.insert("vertex_points", insertPoints);

			}// end inserting triples

			// changing the problem's graph reference in DB
			insertProblem.put("name", name);
			insertProblem.put("id_graph", idInsertedGraph);
			insertProblem.put("problem_classification", graph.classifyProblem());
			Hashtable<String, Object> conditions = new Hashtable<String, Object>();
			conditions.put("name", name);
			connection.update("problem", insertProblem, conditions);

			// After inserted a problem the Inverse Index is created
			// int idInsertedProblem = connection.getCurrentProblemId();
			mainProcess = graph.classifyProblem();

			// insert inferred triples
			// made by Leo
			Object[][] inferred = GraphEd.tools.getInferredTriples();
			for (Object[] objects : inferred) {
				insertInferredTriple.put("subject", objects[0]);
				insertInferredTriple.put("predicate", "isCauseOf");
				insertInferredTriple.put("object", objects[1]);
				insertInferredTriple.put("id_problem", idProblem);
				connection.insert("inferred_rdf_triple", insertInferredTriple);
				insertInferredTriple.clear();
			}

			/*
			 * Get Indicators that belong to a root cause and insert it in table
			 * inverse_index
			 */
			// ArrayList<Object>rootCausesArray = problem.getRootCauses();
			Hashtable<String, Object> inverseIndex = new Hashtable<String, Object>();
			// Inserting the indicator and the problem in Inverse Index
			/*
			 * for (Object indicator : rootCausesArray) {
			 * inverseIndex.put("id_problem",idInsertedProblem);
			 * inverseIndex.put("indicator", indicator);
			 * connection.insert("inverse_index", inverseIndex);
			 * 
			 * // getting class Indicator from Ontology OntClass indicatorClass
			 * = OntologyLoader.modelToScor.getOntClass(OntologyLoader.NS +
			 * "Indicators"); // get all instances of Indicator class
			 * ExtendedIterator iterIndicator = indicatorClass.listInstances();
			 * 
			 * Property belongToKPI =
			 * OntologyLoader.modelToScor.getProperty(OntologyLoader.NS +
			 * "belongToKPI");
			 * 
			 * while (iterIndicator.hasNext()) { Individual object =
			 * (Individual) iterIndicator.next();
			 * if(object.getLocalName().equals(indicator)){ ExtendedIterator
			 * iterator = object.listPropertyValues(belongToKPI); while
			 * (iterator.hasNext()) { Object object2 = (Object) iterator.next();
			 * System.out.println("aqws " + object2); } } } }
			 */
			conditions.clear();
			conditions.put("id_graph", oldIdGraph);
			connection.delete("graph", conditions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		connection.disconnect();
	}
	
	 public static double calculateTotalFrecuencyIndicator(String name) {
	    	// TODO Auto-generated method stub
	    	double totalFrecuency = 0;
	    	String sql = "select count(*) as countingTotal from triple_rdf where " +
	    	"triple_rdf.predicate = 'null'" + 
	    	"and triple_rdf.subject ='" + name+ "'";
	    	String sqlTotal = "select count(*) as total from triple_rdf";
	    	ConnectionToPostgres connection = PersistentManager.getConnectionToPostgreSQL();
	    	if(connection.tryConnection()){
	    	    ResultSet r1,r2;
	    	    try {
	    		r1 = connection.executeSql(sql);
	    		r1.next();
	    		double countingTotal = r1.getInt("countingTotal");
	    		r2 = connection.executeSql(sqlTotal);
	    		r2.next();
	    		double total = r2.getInt("total");
	    		totalFrecuency = countingTotal/total;
	    	    } catch (SQLException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	    }
	    	}
	       	// Getting the inserted Id
	    	return totalFrecuency;
	        }
			


}
