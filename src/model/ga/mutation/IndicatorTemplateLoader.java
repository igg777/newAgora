package model.ga.mutation;

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
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import model.ga.internal.Indicators;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;

import system.rdf.graph.MyProblemCustomCell;
import system.rdf.ui.GraphEd;
import system.rdf.utils.StringUtils;
import system.rename.Triplex;

/**
 * the class works whit two different ontology models (one that have all the
 * information and another that is empty) to eliminate undesired implicit
 * triples
 * 
 * @author Leo
 * 
 */
public class IndicatorTemplateLoader {
	public String nameSpace = "http://www.owl-ontologies.com/SCOR_Class_SubClass.owl";
	public OntModel model = null;

	ArrayList<String> assertedStatement;
	ArrayList<IndicatorTemplate> indicatorToMutation;  
	public IndicatorTemplateLoader()  {
		assertedStatement = new ArrayList<String>();

		try {
			createJenaModel();
			indicatorToMutation = getStatement(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * this method initialize all the models(Ontology models and Inferred
	 * models)
	 * @todo fix the loading of the configuration file
	 */
	public void createJenaModel() throws FileNotFoundException {
		/*String location = GraphEd.instalationPath+ "/SCOR.owl";
		model = ModelFactory.createOntologyModel();
		InputStream inSCORDCInstance = FileManager.get().open(
		location);
		model.read(inSCORDCInstance, nameSpace);

		if (model.isEmpty()) {
			throw new FileNotFoundException(
					"An error occur loading the ontologies");
		}*/
	}


	public ArrayList<IndicatorTemplate> getStatement(){
		ArrayList<Triplex> triplex = new ArrayList<Triplex>();
		ArrayList<IndicatorTemplate> indicatorsTemp = new ArrayList<IndicatorTemplate>();
		StmtIterator iter = model.listStatements();
		while (iter.hasNext()) {
			Statement stm = iter.nextStatement();
			String subject = StringUtils.getAfterUri(stm.getSubject().toString());
			String predicate = StringUtils.getAfterUri(stm.getPredicate().toString());
			String object = StringUtils.getAfterUri(stm.getObject().toString());
			if(predicate.equals("type")&& object.equals("Indicators") || predicate.equals("belongToKPI") ||
					predicate.equals("assesTo") || predicate.equals("belongToMainProcess")){
				triplex.add(new Triplex(subject, predicate, object));
			}		
		}

		ArrayList<String> arrToReturn = new ArrayList<String>();
		try{
			//ArrayList<Statement> statements = new ArrayList<Statement>();
			ArrayList<String> links = new ArrayList<String>();

			int cont = 0;

			for (Triplex stm : triplex) {
				String subject = stm.getSubject();
				String predicate = stm.getProperty();
				String object = stm.getObject();
				if(predicate.equals("type")&& object.equals("Indicators") ){
					arrToReturn.add(subject);
				}		
			}
			/**
			 * hasta aqui tienes todos los indicadores de la ontologia en arrToReturn;
			 */
			for (String string : arrToReturn) {
				boolean flag = false;
				String temp = "";

				for (Triplex stm : triplex) {
					String subject = stm.getSubject();
					String predicate = stm.getProperty();
					String object = stm.getObject();
					if(subject.equals(string)&& predicate.equals("belongToKPI") ){
						temp = object;
						flag = true;
					}		
				}
				if(flag == false){
					temp = string;
				}
				links.add(temp);

			}

			/**
			 * hasta aqui se tienen los primeros subprocesos.
			 */

			for (int i = 0; i < links.size(); i++) {
				boolean flag = false;
				IndicatorTemplate  indicator= new IndicatorTemplate(arrToReturn.get(i));
				for (Triplex stm : triplex) {
					String subject = stm.getSubject();
					String predicate = stm.getProperty();
					String object = stm.getObject();

					if(subject.equals(links.get(i))&& predicate.equals("assesTo") ){
						indicator.addSubProses(object);
						flag = true;
						//System.out.println(flag);
					}		
				}
				if(flag == false){
					indicator.addSubProses(links.get(i));
				}
				indicatorsTemp.add(indicator);

			}

			for (IndicatorTemplate indicatorTemplate : indicatorsTemp) {
				for (int i = 0; i< indicatorTemplate.getSubProcess().size(); i++) {
					for (Triplex stm : triplex) {
						String subject = stm.getSubject();
						String predicate = stm.getProperty();
						String object = stm.getObject();
						if(subject.equals(indicatorTemplate.getSubProcess().get(i)) && predicate.equals("belongToMainProcess")){
							indicatorTemplate.addProcess(object);
						}
					}
				}
			}

			/*System.out.println(indicatorsTemp.size());
		for (int i = 0; i < indicatorsTemp.size(); i++ ) {

			for(int j = 0; j < indicatorsTemp.get(i).getProcess().size(); j++){
			System.out.println("el indicador " + indicatorsTemp.get(i).getNombre() + " tiene el proceso" + 
					indicatorsTemp.get(i).getProcess().get(j));	
			}

		}*/
		}

		catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
			e.printStackTrace();
		}

		return indicatorsTemp;
	}

	public void display  (){
		ArrayList<IndicatorTemplate> indicatorsTemp = getStatement();
		for (int i = 0; i < indicatorsTemp.size(); i++ ) {
			for(int j = 0; j < indicatorsTemp.get(i).getProcess().size(); j++){
				System.out.println("el indicador " + indicatorsTemp.get(i).getNombre() + " tiene el proceso" + 
						indicatorsTemp.get(i).getProcess().get(j));		

			}
		}

	}
}