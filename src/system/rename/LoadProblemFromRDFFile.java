package system.rename;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Port;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.EdgeManager;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.VertexManager;
import system.rdf.handlers.ProblemMarqueeHandler;
import system.rdf.ui.EditorScrollPane;
import system.rdf.ui.GraphEd;
import system.rdf.ui.GraphEd;
import system.rdf.ui.UIModeler;
import system.rdf.utils.StringUtils;

public class LoadProblemFromRDFFile {
	private ProblemGraph graph = new ProblemGraph();
	private String location;
	private String nameSpace = "http://www.owl-ontologies.com/SCORDecisionalContext.owl#";
	private OntModel model;
	GraphEd graphed;
	private Tools tools;
	private RDFSerializator serializator = new RDFSerializator();

	public LoadProblemFromRDFFile(String location, GraphEd graphed2) {
		super();
		this.location = location;
		this.graphed = graphed2;
		this.tools = new Tools();
	}

	public void createJenaModel() throws FileNotFoundException, Exception {
		try {
			model = ModelFactory.createOntologyModel();
			InputStream inSCORDCInstance = FileManager.get().open(location);
			model.read(inSCORDCInstance, nameSpace);
			if (model.isEmpty()) {
				throw new FileNotFoundException(
						"An error occur loading the ontologies");
			}
		} catch (Exception e) {
			throw new Exception("An error occur creating the model");
		}
	}

	public ArrayList<String> runQuery(String queryRequest) {
		ArrayList<String> toReturn = new ArrayList<String>();
		StringBuffer queryStr = new StringBuffer();
		// Establish Prefixes
		queryStr.append("PREFIX scor" + ": <" + nameSpace + "> ");
		queryStr.append("PREFIX rdfs" + ": <"
				+ "http://www.w3.org/2000/01/rdf-schema#" + "> ");
		queryStr.append("PREFIX rdf" + ": <"
				+ "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
		// Now add query
		queryStr.append(queryRequest);
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet response = qexec.execSelect();
			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode object = soln.get("?object");
				RDFNode prop = soln.get("?prop");
				RDFNode subject = soln.get("?subject");

				if (object != null && prop != null && subject != null) {
					toReturn.add(StringUtils.getAfterUri(subject.toString())
							+ "    " + StringUtils.getAfterUri(prop.toString())
							+ "    "
							+ StringUtils.getAfterUri(object.toString()));

				} else
					toReturn.add("Nothing was inferred");
			}
		} finally {
			qexec.close();
		}
		return toReturn;
	}

	public void createGraph() {
		ProblemMarqueeHandler handler = new ProblemMarqueeHandler(graph);
		ArrayList<String> tripleList = runQuery(" select DISTINCT ?subject ?prop ?object  where{?subject ?prop ?object} ");
		for (String string : tripleList) {
			String[] arr = string.split("    ");
			if (arr[1].equals("isCauseOf")) {
				
				String subjectPoint = getNodePoint(tripleList, arr[0]);
				int subjectX = Integer.parseInt(subjectPoint.split(",")[0]);
				int subjectY = Integer.parseInt(subjectPoint.split(",")[1]);

				String objectPoint = getNodePoint(tripleList, arr[2]);
				int objectX = Integer.parseInt(objectPoint.split(",")[0]);
				int objectY = Integer.parseInt(objectPoint.split(",")[1]);
				
				Point sourcePoint = new Point((int) subjectX, (int) subjectY);
				Point targetPoint = new Point((int) objectX, (int) objectY);
				ArrayList<Object> cell = tools.desconcatenateNameAndProcess(arr[0]);
				MyProblemCustomCell subject = new MyProblemCustomCell(cell.get(0).toString(), (String[])cell.get(1));
		        if (graph.isPainted(subject)) {
					subject = graph.getCell(subject);
				}
		        else {
		        	subject.getAttributes().applyMap(
		    				VertexManager.problemCellAttributes(sourcePoint));
		        	graph.getGraphLayoutCache().insert(subject);
		        }
		        cell = tools.desconcatenateNameAndProcess(arr[2]);
		        MyProblemCustomCell object = new MyProblemCustomCell(cell.get(0).toString(), (String[])cell.get(1));
				if (graph.isPainted(object)) {
					object = graph.getCell(object);
				}
				else{
					object.getAttributes().applyMap(
							VertexManager.problemCellAttributes(targetPoint));
					graph.getGraphLayoutCache().insert(object);
				}
				DefaultEdge edge = new DefaultEdge();
				Tools tools = new Tools();
				String  frequency = tools.round(serializator.calculateTotalFrequencyForProblems(subject, object),3);
				edge.getAttributes().applyMap(
						EdgeManager.problemEdgeAttributes());
					Port sourcePort = graph.getDefaultPort(subject);
					Port targetPort = graph.getDefaultPort(object);
					edge.setUserObject(frequency);
					graph.getGraphLayoutCache().insertEdge(edge, sourcePort,
							targetPort);
			}
			else if(arr[1].equals("hasName") && arr.length>2){
				
				graph.setSavedName(arr[2]);
			}
		}
		
		//them create the graph in a new model
		UIModeler model = graphed.createModel();
		model.setTitle(graph.getSavedName());
		EditorScrollPane editor = new EditorScrollPane(graph);
	    model.getEditorTabbedPane().removeAll();
	    model.getEditorTabbedPane().getGraphs().set(0, graph);
	    String tips = "Right click to show options";
	    model.getEditorTabbedPane().addTab("Problem Formulation",null,editor,tips);	
		model.addTpeditorChangeListener();
		GraphEd.refreshScenarios();
	}

	public String getNodePoint(ArrayList<String> list, String nodeName) {
		for (String string : list) {
			String[] arr = string.split("    ");
			if (arr[0].equals(nodeName) && arr[1].equals("hasPosition")) {
				return arr[2];
			}
		}
		return "0,0";
	}
}
