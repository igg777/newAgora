package model.ga;


import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import java.util.Random;


import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;


import model.ga.internal.Couple;

import model.ga.internal.Indicators;

import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Predicate;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFObject;
import system.rdf.dataBase.RDFTriple;
import system.rdf.dataBase.Subject;
import system.rdf.utils.StringUtils;

/**
 * @author Ruben
 * 
 */
public class GenerationMain {
	// Array that contains all the problems
	ArrayList<Problem> problemArray;
	ArrayList<Problem> problemBestGe;
	ArrayList<Problem> problemArrayToSa;
	ArrayList<Problem> problemArrayToSe;
	ArrayList<Couple> problemArrayToSquema;
	ArrayList<Couple> newProblemPopulation;
	ArrayList<Problem> newProblemCrossing;
	ArrayList<Problem> newproblemsMutated;
	ArrayList<Problem> newproblemsValidated;
	ArrayList<Problem> coupleToSelect;
	ArrayList<Couple> problemsAncestor;
	Problem problemFatherA, problemFatherB;
	Problem newProblemByCrossing;
	ArrayList<Problem> childs = new ArrayList<Problem>();
	ArrayList<Problem> population;

	// Array thas contains all the analysed of graph
	ArrayList<String> analysedSpectral;
	// Entered by the user
	int numberOfProblems;
	int iterations = 5;
	int numberOfIndicators = 3;

	// to draw generated problems
	int subjectX;
	int subjectY;

	int objectX;
	int objectY;

	// Point to start drawing generated problem
	int initialX = 12;
	int initialY = 18;
	
	static final double CROSSPROBABILITY = 0.75;
	static final double MUTATIONPROBABILITY = 0.25;
	// Conection to DB
	ConnectionToPostgres connection;
	private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;
	Problem progenitor;
	Problem problem;
	protected ArrayList<RDFTriple> triples;
	protected ArrayList<RDFTriple> triplesToSe;
	public double countvaluefrecuency = 0;


	/**
     * 
     */
	public GenerationMain() {
		super();
		connection = PersistentManager.getConnectionToPostgreSQL();
		try {
			connection.connect();
			problemArray = connection.getProblems();
			connection.disconnect();
		} catch (Exception e) {
			e.getStackTrace(); // TODO: handle exception
		}

	}

	/**
	 * Get an Array with all the instances of Indicators in SCOR Ontology
	 * 
	 * @return
	 */
	public ArrayList<Indicators> loadOntModel() {
	    ArrayList<Indicators> indicatorsArray = new ArrayList<Indicators>();
		/*OntModel modelOnt1 = ModelFactory.createOntologyModel(
				OntModelSpec.OWL_MEM, null);

		modelOnt1.getDocumentManager().addAltEntry(
				"file:system/rdf/resources/",
				"file:system/rdf/resources/SCOR.owl");

		// SCOR Name Space
		String NS = "http://www.owl-ontologies.com/SCOR_Class_SubClass.owl#";

		modelOnt1.read("file:system/rdf/resources/SCORDecisionalContext.owl");
		// This code is to get all the individual(instances) of a given class
		OntClass artefact = modelOnt1.getOntClass(NS + "Indicators");
		ArrayList<Indicators> indicatorsArray = new ArrayList<Indicators>();
		for (Iterator i = artefact.listInstances(); i.hasNext();) {
			Individual c = (Individual) i.next();
			indicatorsArray.add(new Indicators(StringUtils.getAfterUri(c
					.getURI()), null));
		}*/
		return indicatorsArray;
	}

	/**
	 * Generates an array with indicators that have been taken from the SCOR
	 * ontology
	 * 
	 * @param number
	 *            (The number of indicators used to the current problem, entered
	 *            by the user)
	 */
	public ArrayList<Indicators> generateIndicators() {
		ArrayList<Indicators> arrayToReturn = new ArrayList<Indicators>();
		// Number of indicators from the SCOR ontology
		int numberInd = this.loadOntModel().size();
		// Array to get the random numbers
		ArrayList<Integer> randomNumber = new ArrayList<Integer>();
		// Generating random numbers to the number of indicators in the Ontology
		for (int i = 0; i < this.numberOfIndicators; i++) {
			randomNumber.add(new Random().nextInt(numberInd));
		}
		// Adding the random indicators to an array
		/*for (int i = 0; i < randomNumber.size(); i++) {
			int tempNumber = randomNumber.get(i);
			arrayToReturn.add(new Indicators("this.loadOntModel().get(tempNumber).getName(), null"));
		}*/
					
		return arrayToReturn;
	}


	/**
	 * Main method that execute according the iterations the corresponding
	 * method of Crossing and Mutation
	 * 
	 * @throws SQLException
	 */
	public void executeGA() throws SQLException {
		// executRuleteSelection();
		for (int i = 0; i < 1; i++) {
			double probability = Math.random();
			System.out.println("Probabilidad");
			System.out.println(probability);
			if (probability < MUTATIONPROBABILITY) {

			} else if (probability < CROSSPROBABILITY) {
				//makeCrossOver();
			}
		}
		 insertToDB();
	}

	public void executeGA3() throws SQLException, FileNotFoundException {
		new AG3Algorithm().runAlgorithm(problemArray);
	
	}

	public void executeGA4() throws SQLException, FileNotFoundException {
		new AG4Algorithm().runAlgorithm(problemArray);
	
	}

	/**
	 * Insert to Data Base the generated population, for nine indicators
	 * 
	 * @throws SQLException
	 */
	public void insertToDB() throws SQLException {
		System.out.println("Insetado el base de datos");
		Hashtable<String, Object> insertGraph = new Hashtable<String, Object>();
		Hashtable<String, Object> insertProblem = new Hashtable<String, Object>();
		Hashtable<String, Object> insertTriple = new Hashtable<String, Object>();
		Hashtable<String, Object> insertPoints = new Hashtable<String, Object>();

		ConnectionToPostgres connection = PersistentManager
				.getConnectionToPostgreSQL();

		ArrayList<Indicators> tempArray = null;
		Problem problemToAdd;
		// number of problems that have already entered by the user
		for (int i = 0; i < problemArrayToSe.size(); i++) {
			// Generating relations for the currents problems
			insertGraph.put("description", "Problem Generated by Crossing");
			// Inserting a new Graph on DataBase
			connection.insert("graph", insertGraph);

			int idInsertedGraph = connection.getCurrentGraphId();
			insertProblem.put("name", "Generate Problem by Crossing" + i);
			insertProblem.put("id_graph", idInsertedGraph);
			// In memory Problem
			problemToAdd = new Problem("Problem Generated by Crossing",
					idInsertedGraph, "Generate Problem using Crossing" + i,
					null);
			// Inserting a problem in database
			connection.insert("problem", insertProblem);

			for (int j = 0; j < problemArrayToSe.get(i).getTriples().size(); j++) {

				// Inserting triples
				insertTriple.put("id_graph", idInsertedGraph);
				insertTriple.put("subject", problemArrayToSe.get(i)
						.getTriples().get(j).getSubject().getLabel());
				insertTriple.put("predicate", "null");
				insertTriple.put("object", problemArrayToSe.get(i).getTriples()
						.get(j).getObject().getLabel());

				// inserting the triples
				connection.insert("triple_rdf", insertTriple);

				int id = connection.getCurrentTripleId();
				insertPoints.put("id_triple", id);

				// the bounds of the rectangle are
				// x=12.0,y=18.0,w=163.0,h=49.0
				switch (j) {
				case 0:
					// Locating points for one triple, two indicators
					// x1,y1
					setSubjectX(initialX);
					setSubjectY(initialY);
					// x2,y1 x2 = x1 + size of rectangle(163) + 26
					setObjectX(initialX + 192);
					setObjectY(initialY);
					break;
				case 1:
					// Locating points for two triple, three indicators
					// Repeat the last triple
					setSubjectX(initialX + 192);
					setSubjectY(initialY);
					// x3,y1 x3 = x2 + 186 = x1 + 189 + 189
					setObjectX(initialX + 2 * 192);
					setObjectY(initialY);
					break;
				case 2:
					// Locating points for three triple, four indicators
					// Repeat the last triple x3,y1
					setSubjectX(initialX + 2 * 192);
					setSubjectY(initialY);
					// x1,y2 y2 = y1 + 49 + 30
					setObjectX(initialX);
					setObjectY(initialY + 78);
					break;
				case 3:
					// Locating points for four triple, five indicators
					// x1,y2 y2 = y1 + 49 + 30
					setSubjectX(initialX);
					setSubjectY(initialY + 78);
					// x2, y2
					setObjectX(initialX + 192);
					setObjectY(initialY + 78);
					break;
				case 4:
					// Locating points for five triple, six indicators
					setSubjectX(initialX + 192);
					setSubjectY(initialY + 78);
					// x3,y2
					setObjectX(initialX + 2 * 192);
					setObjectY(initialY + 78);
					break;
				case 5:
					// Locating points for six triple, seven indicators
					setSubjectX(initialX + 2 * 192);
					setSubjectY(initialY + 78);
					// x3,y2
					setObjectX(initialX);
					setObjectY(initialY + 49 * 2 + 29 * 2);
					break;
				case 6:
					// Locating points for seven triple, eight indicators
					setSubjectX(initialX);
					setSubjectY(initialY + 49 * 2 + 29 * 2);
					// x3,y2
					setObjectX(initialX + 192);
					setObjectY(initialY + 49 * 2 + 29 * 2);
					break;
				case 7:
					// Locating points for eight triple, nine indicators
					setSubjectX(initialX + 192);
					setSubjectY(initialY + 49 * 2 + 29 * 2);
					// x3,y2
					setObjectX(initialX + 192 * 2);
					setObjectY(initialY + 49 * 2 + 29 * 2);
					break;

				}

				// inserting the cell's points, this is necessary to Check it
				// later
				insertPoints.put("subject_x", this.getSubjectX());
				insertPoints.put("subject_y", this.getSubjectY());

				insertPoints.put("object_x", this.getObjectX());
				insertPoints.put("object_y", this.getObjectY());

				connection.insert("vertex_points", insertPoints);
			}
		}

	}

	public HashMap<String, Point2D.Double> getPointByIndicator(int j) {
		Point2D.Double toReturnSubject = new Point2D.Double();
		Point2D.Double toReturnObject = new Point2D.Double();
		// the bounds of the rectangle are
		// x=12.0,y=18.0,w=163.0,h=49.0
		switch (j) {
		case 0:
			// Locating points for one triple, two indicators
			// x1,y1
			toReturnSubject.x = initialX;
			toReturnSubject.y = initialY;
			// x2,y1 x2 = x1 + size of rectangle(163) + 26
			toReturnObject.x = initialX + 192;
			toReturnObject.y = initialY;
			break;
		case 1:
			// Locating points for two triple, three indicators
			// Repeat the last triple
			toReturnSubject.x = initialX + 192;
			toReturnSubject.y = initialY;
			// x3,y1 x3 = x2 + 186 = x1 + 189 + 189
			toReturnObject.x = initialX + 2 * 192;
			toReturnObject.y = initialY;
			break;
		case 2:
			// Locating points for three triple, four indicators
			// Repeat the last triple x3,y1
			toReturnSubject.x = initialX + 2 * 192;
			toReturnSubject.y = initialY;
			// x1,y2 y2 = y1 + 49 + 30
			toReturnObject.x = initialX;
			toReturnObject.y = initialY + 78;
			break;
		case 3:
			// Locating points for four triple, five indicators
			// x1,y2 y2 = y1 + 49 + 30
			toReturnSubject.x = initialX;
			toReturnSubject.y = initialY + 78;
			// x2, y2
			toReturnObject.x = initialX + 192;
			toReturnObject.y = initialY + 78;
			break;
		case 4:
			// Locating points for five triple, six indicators
			toReturnSubject.x = initialX + 192;
			toReturnSubject.y = initialY + 78;
			// x3,y2
			toReturnObject.x = initialX + 2 * 192;
			toReturnObject.y = initialY + 78;
			break;
		case 5:
			// Locating points for six triple, seven indicators
			toReturnSubject.x = initialX + 2 * 192;
			toReturnSubject.y = initialY + 78;
			// x3,y2
			toReturnObject.x = initialX;
			toReturnObject.y = initialY + 49 * 2 + 29 * 2;
			break;
		case 6:
			// Locating points for seven triple, eight indicators
			toReturnSubject.x = initialX;
			toReturnSubject.y = initialY + 49 * 2 + 29 * 2;
			// x3,y2
			toReturnObject.x = initialX + 192;
			toReturnObject.y = initialY + 49 * 2 + 29 * 2;
			break;
		case 7:
			// Locating points for eight triple, nine indicators
			toReturnSubject.x = initialX + 192;
			toReturnSubject.y = initialY + 49 * 2 + 29 * 2;
			// x3,y2
			toReturnObject.x = initialX + 192 * 2;
			toReturnObject.y = initialY + 49 * 2 + 29 * 2;
			break;
		}

		HashMap<String, Point2D.Double> toReturn = new HashMap<String, Point2D.Double>();
		toReturn.put("subject", toReturnSubject);
		toReturn.put("object", toReturnObject);
		return toReturn;
	}

	public void getLocationToTriple(int j) {
		switch (j) {
		case 0:
			// Locating points for one triple, two indicators
			// x1,y1
			setSubjectX(initialX);
			setSubjectY(initialY);
			// x2,y1 x2 = x1 + size of rectangle(163) + 26
			setObjectX(initialX + 192);
			setObjectY(initialY);
			break;
		case 1:
			// Locating points for two triple, three indicators
			// Repeat the last triple
			setSubjectX(initialX + 192);
			setSubjectY(initialY);
			// x3,y1 x3 = x2 + 186 = x1 + 189 + 189
			setObjectX(initialX + 2 * 192);
			setObjectY(initialY);
			break;
		case 2:
			// Locating points for three triple, four indicators
			// Repeat the last triple x3,y1
			setSubjectX(initialX + 2 * 192);
			setSubjectY(initialY);
			// x1,y2 y2 = y1 + 49 + 30
			setObjectX(initialX);
			setObjectY(initialY + 78);
			break;
		case 3:
			// Locating points for four triple, five indicators
			// x1,y2 y2 = y1 + 49 + 30
			setSubjectX(initialX);
			setSubjectY(initialY + 78);
			// x2, y2
			setObjectX(initialX + 192);
			setObjectY(initialY + 78);
			break;
		case 4:
			// Locating points for five triple, six indicators
			setSubjectX(initialX + 192);
			setSubjectY(initialY + 78);
			// x3,y2
			setObjectX(initialX + 2 * 192);
			setObjectY(initialY + 78);
			break;
		case 5:
			// Locating points for six triple, seven indicators
			setSubjectX(initialX + 2 * 192);
			setSubjectY(initialY + 78);
			// x3,y2
			setObjectX(initialX);
			setObjectY(initialY + 49 * 2 + 29 * 2);
			break;
		case 6:
			// Locating points for seven triple, eight indicators
			setSubjectX(initialX);
			setSubjectY(initialY + 49 * 2 + 29 * 2);
			// x3,y2
			setObjectX(initialX + 192);
			setObjectY(initialY + 49 * 2 + 29 * 2);
			break;
		case 7:
			// Locating points for eight triple, nine indicators
			setSubjectX(initialX + 192);
			setSubjectY(initialY + 49 * 2 + 29 * 2);
			// x3,y2
			setObjectX(initialX + 192 * 2);
			setObjectY(initialY + 49 * 2 + 29 * 2);
			break;
		}
	}

	/**
	 * @return the numberOfProblems
	 */
	protected int getNumberOfProblems() {
		return numberOfProblems;
	}

	/**
	 * @param numberOfProblems
	 *            the numberOfProblems to set
	 */
	public void setNumberOfProblems(int numberOfProblems) {
		this.numberOfProblems = numberOfProblems;
	}

	/**
	 * @return the iterations
	 */
	protected int getIterations() {
		return iterations;
	}

	/**
	 * @param iterations
	 *            the iterations to set
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * @return the numberOfIndicators
	 */
	public int getNumberOfIndicators() {
		return numberOfIndicators;
	}

	/**
	 * @param numberOfIndicators
	 *            the numberOfIndicators to set
	 */
	public void setNumberOfIndicators(int numberOfIndicators) {
		this.numberOfIndicators = numberOfIndicators;
	}

	/**
	 * @param initialX
	 *            the initialX to set
	 */
	protected void setInitialX(int initialX) {
		this.initialX = initialX;
	}

	/**
	 * @param initialY
	 *            the initialY to set
	 */
	protected void setInitialY(int initialY) {
		this.initialY = initialY;
	}

	/**
	 * @return the subjectX
	 */
	protected int getSubjectX() {
		return subjectX;
	}

	/**
	 * @param subjectX
	 *            the subjectX to set
	 */
	protected void setSubjectX(int subjectX) {
		this.subjectX = subjectX;
	}

	/**
	 * @return the subjectY
	 */
	protected int getSubjectY() {
		return subjectY;
	}

	/**
	 * @param subjectY
	 *            the subjectY to set
	 */
	protected void setSubjectY(int subjectY) {
		this.subjectY = subjectY;
	}

	/**
	 * @return the objectX
	 */
	protected int getObjectX() {
		return objectX;
	}

	/**
	 * @param objectX
	 *            the objectX to set
	 */
	protected void setObjectX(int objectX) {
		this.objectX = objectX;
	}

	/**
	 * @return the objectY
	 */
	protected int getObjectY() {
		return objectY;
	}

	/**
	 * @param objectY
	 *            the objectY to set
	 */
	protected void setObjectY(int objectY) {
		this.objectY = objectY;

	}

	public int rulette(int max) {
		return (int) (Math.random() * 1000000) % max;
	}
	
    public static void main(String[] args) throws SQLException {
		GenerationMain gm = new GenerationMain(); 
		               Problem p= gm.problemArray.get(0);
		               System.out.println("Probema"+ p.getName());
		               
		 
     
    } 
	   	
}
