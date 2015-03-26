package model.ga.mutation;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.WindowConstants;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import model.ga.IGeneticAlgorithmStep;
import model.ga.internal.Indicators;

import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFObject;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.dataBase.RDFTriple;
import system.rdf.dataBase.Subject;
import system.rdf.ui.GraphEd;
import system.rdf.utils.StringUtils;

public class MutationWithoutRootCauses implements IGeneticAlgorithmStep {

	static final double MUTATIONPROBABILITY = 0.25;
	Problem childToMutate = null;
	IndicatorTemplateLoader indicatorTemplateLoader = new IndicatorTemplateLoader() ;

	ArrayList<IndicatorTemplate> IncatorToMutation =indicatorTemplateLoader.indicatorToMutation;
	ArrayList<Indicators> OntologyIndicators  = this.loadOntModel();
	int cantOntoIndicators = OntologyIndicators.size();
	/**
	 * Get an Array with all the instances of Indicators in SCOR Ontology
	 * 
	 * @return
	 */
	public ArrayList<Indicators> loadOntModel() {
		System.out.println("Leyendo de la ontologia");
		ArrayList<Indicators> indicatorsArray = new ArrayList<Indicators>();
		Indicators indicatorTemp =null; 
		String []process = null;
		System.out.println("ff"+IncatorToMutation.size());
		for (int i = 0; i < IncatorToMutation.size(); i++) {
			process = new String[IncatorToMutation.get(i).getSubProcess().size()+1];

			process[0] = IncatorToMutation.get(i).getProcess().get(0);
			for (int j = 0; j< IncatorToMutation.get(i).getSubProcess().size(); j++) {
				process[j+1] = IncatorToMutation.get(i).getSubProcess().get(j);
			}
			indicatorTemp = new Indicators(IncatorToMutation.get(i).getNombre(),process);
			indicatorsArray.add(indicatorTemp);
		}
		
		return indicatorsArray;
	}	

	/**
	 * Interchanging elements(Indicators), chosen randomly for each problem
	 * @throws FileNotFoundException 
	 */
	// TENGO QUE MODIFICAR ESTE METODO TRATANDO DE NO DESTRUIR BUENOS BLOQUES DE
	// CONSTRUCCION,SINO EXACTAMENTE LO CONTRARIO
	private Problem makeMutation(Problem childToMutate) throws FileNotFoundException {
		this.childToMutate = childToMutate; 
		System.out.println("Cojio el problema"); 
		// Obteniendo los indicadores de la ontologia...
		Problem problemTemp = new Problem();
		ArrayList<Indicators> indicatorsSelected = new ArrayList<Indicators>();
		// Carga los indicadores de la ontolog�a

		// aqui se copia las triplas dde el problema a mutar para el nuevo
		// problema
		// que se obtebdr� con la mutaci�n

		ArrayList<RDFTriple> tmp = new ArrayList<RDFTriple>();
		ArrayList<RDFTriple> tmpToMutate = new ArrayList<RDFTriple>();
		tmp = childToMutate.getTriples();

		String strObj = new String();
		String strSub = new String();

		for (int i = 0; i < tmp.size(); i++) {
			strObj = tmp.get(i).getObject().getLabel();
			RDFObject ob = new RDFObject(strObj);
			strSub = tmp.get(i).getSubject().getLabel();
			Subject su = new Subject(strSub);
			String[] strSubP = new String[tmp.get(i).getSubjetProcess().length];
			String[] strObjP = new String[tmp.get(i).getObjectProcess().length];

			strSubP =tmp.get(i).getSubjetProcess();
			strObjP =tmp.get(i).getObjectProcess();

			RDFTriple trp = new RDFTriple(ob, null, su);
			trp.setSubjectProcess(strSubP);
			trp.setObjectProcess(strObjP);

			tmpToMutate.add(trp);

		}

		problemTemp.setTriples(tmpToMutate);




		// inicializando indicadores con las triplas copiadas
		problemTemp.setIndicatorsArray(problemTemp.getIndicators());

		// mutando seleccionando la cantidad de indicadores a mutar random
		Random random = new Random();
		// como no se mutan las efectos raices entonces no se pueden tener en
		// cuenta en la cant de ind a mutar
		int aux = childToMutate.getIndicators().size()
		- childToMutate.getRootCauses().size();
		System.out.println("Cantidad de indicadores que no son raices"+aux);

		// buscando los indicadores que se por los que se van asustituir los
		// indicadores del problema
		for (int i = 0; i < aux; i++) {
			System.out.println("Selecionando los inicadores para mutar");
			Indicators indicatorAux = OntologyIndicators.get(random
					.nextInt(cantOntoIndicators));
			if(CanbelongToFormulation(indicatorAux)){
				indicatorsSelected.add(indicatorAux);
				System.out.println("Selecionando"+i);	
			}

		}
		System.out.println("Selecionado los inicadores ");
		// mando a mutar al nuevo problema dandole los indicadores nuevos que lo
		// formaran.

		problemTemp.mutatingByGoodBloqsWRoot(indicatorsSelected);
		problemTemp.setName("Problem Mutated");

		// despues que cambio los indicadores en las triplas vuelvo a generar
		// los indicadores ya con los cambios
		problemTemp.setIndicatorsArray(problemTemp.getIndicators());
		return problemTemp;
	}
	/*
	 *
	 * */
	public boolean CanbelongToFormulation(Indicators indicator) throws FileNotFoundException{
		System.out.println("Bucando los indicadores que puden pertenecer  la formulacion ");
		boolean flag = false;	
		ArrayList<RDFTriple> roots =childToMutate.getRootCauses();
		String process;
		for (int i = 0; i < roots.size(); i++) {
			process = roots.get(i).getSubjetProcess()[0];
			if( getProcess(indicator).contains(process)==true) {
				flag = true;	
			}

		}


		return flag;

	}
	/**
	 * Give all the processes it belongs to an indicator
	 */

	public ArrayList<String > getProcess (Indicators indicator) throws FileNotFoundException{

		ArrayList<String >process = new ArrayList<String>(); 
		for (int i = 0; i < IncatorToMutation.size(); i++ ) {
			if( indicator.getName().compareTo(IncatorToMutation.get(i).getNombre())==0)
				process = IncatorToMutation.get(i).getProcess();

		}	
		return  process ;	
	}



	public ArrayList<Problem> run(ArrayList<Problem> problems) throws FileNotFoundException {

		ArrayList<Problem> newproblemsMutated = new ArrayList<Problem>();
		Problem newProblem = null;

		for (int i = 0; i < problems.size(); i++) {
			double probability = Math.random() % 1;
			if (probability <= MUTATIONPROBABILITY) {
				newProblem = makeMutation(problems.get(i));
				newproblemsMutated.add(newProblem);
			}

		}

		return newproblemsMutated;
	}

	public Object getAssociatedData() {
		return null;
	}	
	public ArrayList<IndicatorTemplate> getProcessTemplate() {
		return IncatorToMutation ;
	}	
	/*public static void main(String[] args) throws FileNotFoundException {
		MutationWithoutRootCauses m = new MutationWithoutRootCauses(); 
		ArrayList<String> process = new ArrayList<String>();

		IndicatorProcessLoader indicatorProcessLoader = new IndicatorProcessLoader();
		m.IncatorToMutation=indicatorProcessLoader.indicatorToMutation;
		String Ind ="C.C.T.Days-Playable-Outstanding";
		for (int i = 0; i < m.IncatorToMutation.size(); i++ ) 
		if( "U.S.C.F.Current-Invetory-on-Hand-WIP-FG".compareToIgnoreCase(m.IncatorToMutation.get(i).nombreIndicator.toString())==0)
		 	process = m.IncatorToMutation.get(i).getProcess();



		 for(int j = 0; j < process.size(); j++) 	
		System.out.println(process.get(j));


	}	
	 */
}