package system.rename;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.ui.GraphEd;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

public class SaveProblemToRDFFile {
	private OntModel model = null;
	private String nameSpace = "http://www.owl-ontologies.com/SCORDecisionalContext.owl#";
	private List<Triplex> triplesToInsert = new ArrayList<Triplex>();
	private JGraph graph;
    private String URL;
    
	public SaveProblemToRDFFile(JGraph graph, String URL) {
		super();
		this.graph = graph;
		this.URL = URL;
	}

	/**
	 * this method initialize the model
	 */
	public void createJenaModel() throws FileNotFoundException, Exception {
		try{
		String location = GraphEd.instalationPath + "/OntologieForSaveToRDF.owl";
		model = ModelFactory.createOntologyModel();
		InputStream inSCORDCInstance = FileManager.get().open(location);
		model.read(inSCORDCInstance, nameSpace);
		if (model.isEmpty()) {
			throw new FileNotFoundException(
					"An error occur loading the ontologies");
		}
		}
		catch (Exception e) {
			throw new Exception("An error occur creating the model");
		}
	}

	/**
	 * get all triples in the graph and store them in a ArrayList for save them
	 * lather in a RDF file. before calls this method the graph was to be
	 * analyzed for check if it have a cyclic connection or an invalid
	 * connection between all the nodes and it have to be saved previously in
	 * the dataBase
	 */
	public void getTriplesFromGraph() throws Exception{
	   // try{
		if (graph instanceof ProblemGraph) {
			// getting the problem's name
			String name = ((ProblemGraph) graph).getSavedName();
			triplesToInsert.add(new Triplex("problem", "hasName",
					((ProblemGraph) graph).getSavedName()));
			ArrayList<Object> edges = ((ProblemGraph) graph).getAllEdges();
			for (Object object : edges) {
				GraphModel gm = graph.getModel();
				MyProblemCustomCell subjectCell = (MyProblemCustomCell) gm
				.getParent(gm.getSource((DefaultEdge)object));
	            MyProblemCustomCell objectCell = (MyProblemCustomCell) gm
	            .getParent(gm.getTarget((DefaultEdge)object));
				String subjectt = subjectCell.toString() + "--"
						+ subjectCell.getProcess();
				String objectt = objectCell.toString() + "--"
						+ objectCell.getProcess();
				// getting the points of each node
				int obj_x, obj_y, sub_x, sub_y;
				AttributeMap mapSource = ((DefaultGraphCell) subjectCell)
						.getAttributes();
				AttributeMap mapTarget = ((DefaultGraphCell) objectCell)
						.getAttributes();

				sub_x = (int) GraphConstants.getBounds(mapSource).getX();
				sub_y = (int) GraphConstants.getBounds(mapSource).getY();

				obj_x = (int) GraphConstants.getBounds(mapTarget).getX();
				obj_y = (int) GraphConstants.getBounds(mapTarget).getY();

				// adding triples
				triplesToInsert
						.add(new Triplex(subjectt, "isCauseOf", objectt));
				triplesToInsert.add(new Triplex(subjectt, "hasPosition", sub_x
						+ "," + sub_y));
				triplesToInsert.add(new Triplex(objectt, "hasPosition", obj_x
						+ "," + obj_y));
			}
		}
		// if is not a problemGraph them is a SolutionGraph
		else {

		}
		/*}
		catch (Exception e) {
			throw new Exception("An error occur getting the triples from graph");
		}*/
	}

	/**
	 * Insert the triples in the model
	 */
	public void addDataFromTriples() throws Exception {
		try{
		// Create resources
		for (Triplex triplex : triplesToInsert) {
			Resource resource = model.createResource(nameSpace
					+ triplex.getSubject());
			Property prop = model.createProperty(nameSpace
					+ triplex.getProperty());
			Resource obj = model
					.createResource(nameSpace + triplex.getObject());
			model.add(resource, prop, obj);
		}
		}
		catch (Exception e) {
			throw new Exception("An error occur introducing the triples in the model");
		}
	}

	/**
	 * Export the model in a file in the URL specified, the most used format are
	 * "RDF/XML", "Turtle" and "N-Triples"
	 * 
	 * @param format
	 * @throws Exception if the URL is't a correct path
	 */
	public void writeData(String format)throws Exception {
		try{
		FileOutputStream scor = new FileOutputStream(URL);
		model.write(scor, format);
		scor.close();
		}
		catch (Exception e) {
			throw new Exception("An error occur exporting the model");
		}
	}

}
