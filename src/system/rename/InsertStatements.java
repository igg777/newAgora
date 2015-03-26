package system.rename;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;

import system.rdf.graph.MyProblemCustomCell;
import system.rdf.ui.GraphEd;
import system.rdf.utils.StringUtils;

/**
 * the class works whit two different ontology models (one that have all the
 * information and another that is empty) to eliminate undesired implicit
 * triples
 * 
 * @author Leo
 * 
 */
public class InsertStatements {
	public String nameSpace = "http://www.owl-ontologies.com/SCORDecisionalContext.owl#";
	public OntModel model = null;
	public OntModel finalModel = null;
	public InfModel inferredModel = null;
	public InfModel emptyInferredModel = null;
	public Model emptyModel = null;
	public Model finalInfModel = null;
	public List<Triplex> triplesToInsert = null;
	public JGraph graph;
	ArrayList<String> inferredStatement;
	ArrayList<String> assertedStatement;
	private Tools tools;

	public InsertStatements(JGraph pgraph) {
		graph = pgraph;
		inferredStatement = new ArrayList<String>();
		assertedStatement = new ArrayList<String>();
		tools = new Tools();
	}

	/**
	 * this method initialize all the models(Ontology models and Inferred
	 * models)
	 */
	public void createJenaModel() throws FileNotFoundException {
//		String location = GraphEd.instalationPath+ "/SCORDecisonalContext.owl";
//		String location = System.getProperty("user.dir")+ "/main/system/rdf/resources/SCORDecisonalContext.owl";
		String location = "C:\\Users\\CHUCHI\\Documents\\THESIS\\AGora_2\\main\\system\\rdf\\resources\\SCORDecisonalContext.owl";
                model = ModelFactory.createOntologyModel();
		emptyModel = ModelFactory.createOntologyModel();
		finalModel = ModelFactory.createOntologyModel();
		InputStream inSCORDCInstance = FileManager.get().open(
		location);
				//"main/system/rdf/resources/SCORDecisonalContext.owl");
		InputStream inSCORInstance1 = FileManager.get().open(
		 location);
				//"main/system/rdf/resources/SCORDecisonalContext.owl");
		InputStream inSCORInstance2 = FileManager.get().open(
		location);
		model.read(inSCORDCInstance, nameSpace);
		emptyModel.read(inSCORInstance1, nameSpace);
		finalModel.read(inSCORInstance2, nameSpace);
		if (model.isEmpty()) {
			throw new FileNotFoundException(
					"An error occur loading the ontologies");
		}
	}

	/**
	 * Insert the triples in the model
	 */
	public void addDataFromTriples() {
		// Create resources
		for (Triplex triplex : triplesToInsert) {
			Resource resource = model.createResource(nameSpace
					+ triplex.getSubject());
			Property prop = model.createProperty(nameSpace
					+ triplex.getProperty());
			Resource obj = model
					.createResource(nameSpace + triplex.getObject());
			model.add(resource, prop, obj);
			finalModel.add(resource, prop, obj);
		}
	}

	
	 
	public void bindReasoner() {
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner = reasoner.bindSchema(model);
		inferredModel = ModelFactory.createInfModel(reasoner, model);
		Reasoner reasoner2 = ReasonerRegistry.getOWLReasoner();
		reasoner2 = reasoner2.bindSchema(emptyModel);
		emptyInferredModel = ModelFactory.createInfModel(reasoner2, emptyModel);
		finalInfModel = ModelFactory.createOntologyModel();
	}

	/**
	 * 
	 * @param queryRequest
	 * @param model
	 * @return
	 */
	public ArrayList<String> runQuery(String queryRequest, Model model) {
		ArrayList<String> list = new ArrayList<String>();
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

				if (object != null && prop != null) {
					if (!(StringUtils.getAfterUri(prop.toString())
							.equals("sameAs")))
						if (!(StringUtils.getAfterUri(object.toString())
								.equals("Thing")))
							if (!(StringUtils.getAfterUri(object.toString())
									.equals("Resource"))) {
								list.add(StringUtils.getAfterUri(prop
										.toString())
										+ "    "
										+ StringUtils.getAfterUri(object
												.toString()));
							}
				} else
					list.add("Nothing was inferred");
			}
		} finally {
			qexec.close();
		}
		return list;
	}

	/**
	 * Fill the finalModel without the the undesired triples
	 */
	public void addModel() {
		finalInfModel = inferredModel.difference(emptyInferredModel);
		finalModel.add(finalInfModel);
	}

	/**
	 * this method returns the implicit triples in the current graph
	 * 
	 * @return
	 */
	public List<Triplex> getAssertedTriples() {
		List<Object> edgesList = new ArrayList<Object>();
		// Getting all the Roots in the Graph(vertex and edges)
		Object[] roots = graph.getRoots();
		// This Cycle is to get all edges in the Graph that will be saved
		for (int i = 0; i < roots.length; i++) {
			Object object = roots[i];
			if (object instanceof DefaultGraphCell) {
				if (graph.getModel().isEdge(object)) {
					edgesList.add(object);
				}
			}
		}

		// Inserting triples in the List
		triplesToInsert = new ArrayList<Triplex>();
		for (Object object : edgesList) {
			MyProblemCustomCell sourceVertex, targetVertex;
			GraphModel gm = graph.getModel();
			sourceVertex = (MyProblemCustomCell)gm.getParent(gm.getSource(object));
			targetVertex = (MyProblemCustomCell)gm.getParent(gm.getTarget(object));
			String source = sourceVertex.toString()+"--"+tools.concatenateProcess(sourceVertex.getProcess());
			String target = targetVertex.toString() + "--" + tools.concatenateProcess(targetVertex.getProcess());
			Triplex toInsert = new Triplex(source,"isCauseOf",target);
			triplesToInsert.add(toInsert);
		}
		return triplesToInsert;
	}

	public Model getModel() {
		return finalModel;
	}

	/**
	 * Fill one List whit the assertedStatement in the current graph to fill the
	 * table later
	 */
	public void assertedStatement() {
		for (Triplex triple : triplesToInsert) {
			String temp = triple.getSubject() + "  " + triple.getProperty()
					+ "  " + triple.getObject();
			assertedStatement.add(temp);
		}
	}

	// este metodo no pincha bien cuando hay mas de un nodo que es solamente
	// objeto de otro
	/**
	 * Fill one List whit the inferredStatement in the current graph to fill the
	 * table later
	 */
	public void inferredStatement() {
		ArrayList<String> nodos = nodes();
		ArrayList<String> list;
		for (String nods : nodos) {
			list = runQuery(" select DISTINCT ?object ?prop where{  scor:"
					+ nods + " ?prop ?object} ", onlyInferredStatement());
			for (String toInsert : list) {
				inferredStatement.add(nods + "    " + toInsert);
			}
		}
	}

	/**
	 * @return a model which only have the inferredStatements in the current
	 *         graph
	 */
	public Model onlyInferredStatement() {
		Model tempModel = ModelFactory.createOntologyModel();
		// Create resources
		for (Triplex triplex : triplesToInsert) {
			Resource resource = model.createResource(nameSpace
					+ triplex.getSubject());
			Property prop = model.createProperty(nameSpace
					+ triplex.getProperty());
			Resource obj = model
					.createResource(nameSpace + triplex.getObject());
			tempModel.add(resource, prop, obj);
		}
		Model onlyInferred = finalInfModel.difference(tempModel);
		return onlyInferred;
	}

	/**
	 * Fill the table GraphEd.tableInsertedStatement
	 */
	public void fillTable() {
		int leng = (int) inferredStatement.size();
		Object rows[][] = new Object[leng][2];
		for (int i = 0; i < leng; i++) {
			if (i >= assertedStatement.size()) {
				rows[i][0] = " ";
				rows[i][1] = inferredStatement.get(i);
			} else {
				rows[i][0] = assertedStatement.get(i);
				rows[i][1] = inferredStatement.get(i);
			}
		}
		String columns[] = { "Asserted Statements", "Inferred Statements" };
		DefaultTableModel tableModel = new DefaultTableModel(rows, columns);
		GraphEd.tableInsertedStatement.setModel(tableModel);
	}

	/**
	 * 
	 * @return all nods in the current graph
	 */
	public ArrayList<String> nodes() {
		ArrayList<String> nodes = new ArrayList<String>();
		for (Triplex triple : triplesToInsert) {
			String subject = triple.getSubject();
			String object = triple.getObject();
			if (!(nodes.contains(subject)))
				nodes.add(subject);
			if (!(nodes.contains(object)))
				nodes.add(object);
		}
		return nodes;
	}

}