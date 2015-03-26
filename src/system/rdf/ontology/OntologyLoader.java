
// Package
///////////////
package system.rdf.ontology;


// Imports
///////////////
import java.util.Iterator;
import java.util.List;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owl.util.OWLManager;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.Filter;


/**
 * Class used to load the ontology in a JTree component
 * @author Irlan
 *
 */
public class OntologyLoader {

	static OntologyTree owlTree;
	public static OntModel modelToScor;
	public static OntModel modelToDecitional;

	static OntologyTreeManager ontTreeHandler;

	private static String urlForScor = "file:D:/Deutch/development/NewAgora/SCOR.owl";

	public static String urlDecitionalContext = "file:system/rdf/resources/SCORDecisionalContext.owl";
	//SCOR Name Space
	public static final String NS = "http://www.owl-ontologies.com/SCOR_Class_SubClass.owl#";

	public static final String NSDECITIONALCONTEXT = "http://www.owl-ontologies.com/SCORDecisionalContext.owl#";

	private static OntDocumentManager ontManager = OntDocumentManager.getInstance();

	private static OntDocumentManager ontDecitionalManager = OntDocumentManager.getInstance();

	public OntologyLoader() {
		Iterator modelIter;
		loadOntology();

		if (modelToScor != null) {
			ontTreeHandler = new OntologyTreeManager();
			Resource r;
			OntClass mainProcess = modelToScor.getOntClass(NS + "Main_Process");
			modelIter = mainProcess.listInstances().filterDrop(
					new Filter() {
						public boolean accept(Object o) {
							return ((Resource) o).isAnon();
						}
					}); 
			// Create all rootnode and subnode
			List rootNodes = ontTreeHandler.createTreeNodes(modelIter, "");
			owlTree = ontTreeHandler.createTree(rootNodes);
		}
	}

	/**
	 * Constructor overflow
	 * @param url
	 * @param uri
	 */
	public OntologyLoader(String url,String uri) {

		//loadOntology();

		if (modelToScor != null) {
			Iterator modelIter = modelToScor.listIndividuals().filterDrop(
					new Filter() {
						public boolean accept(Object o) {
							return ((Resource) o).isAnon();
						}
					});
			// Create all rootnode and subnode
			List rootNodes = ontTreeHandler.createTreeNodes(modelIter, "");
			owlTree = ontTreeHandler.createTree(rootNodes);
		}
	}

	/**
	 * Load the SCOR Ontology file.
	 * @return OntModel 
	 */
	public static OntModel loadOntology(){
		ontManager.addAltEntry("SCOR",urlForScor);
		modelToScor = ontManager.getOntology("SCOR", OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		return modelToScor;
	}

	/**
	 * Load the SCORDecitionalContext Ontology file.
	 * @return OntModel 
	 */
	public static OntModel loadDecitionalOntology(){
		ontDecitionalManager.addAltEntry("DecitionalSCOR",urlDecitionalContext);
		modelToDecitional = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );
		modelToDecitional.read(urlDecitionalContext);
		//modelToDecitional = ontDecitionalManager.getOntology("DecitionalSCOR", OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		return modelToDecitional;
	}

	public void assignIndividual(String individualName){
		//OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//OWLOntology ontology = manager.loadOntology(URI.create(urlDecitionalContext));
	}

	/**
	 * @return JTree
	 */
	public OntologyTree getTree(){
		if(ontTreeHandler != null)
			return owlTree;
		else
			return null;
	}

	public OntModel getOntModel(){
		return modelToScor; 
	}

	public static OntModel getOntDecisionalModel(){
		return modelToDecitional; 
	}

	public boolean isInstances(String literal){
		return false;
	}

}

