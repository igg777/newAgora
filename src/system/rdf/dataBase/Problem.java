package system.rdf.dataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jgraph.graph.DefaultEdge;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import system.rdf.CustomCell;
import model.ga.internal.Indicators;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.utils.StringUtils;
import system.rename.Tools;

import java.util.Random;

/**
 * This Class is used to describe a problem. A problem is a connection of many
 * RDF triples(A Graph)
 * 
 * @author Ing. Irlán Grangel González
 * 
 */
public class Problem {
	protected int id;
	protected int level = 0;
	protected String name;
	protected String description;
	protected ArrayList<RDFTriple> triples = new ArrayList<RDFTriple>();
	protected ArrayList<Indicators> indicatorsArray = new ArrayList<Indicators>();
	protected ArrayList<RDFTriple> rootCauses = new ArrayList<RDFTriple>();
	protected ArrayList<RDFTriple> unwantedEffects = new ArrayList<RDFTriple>();
	protected double fitness = 0;
	protected boolean fitnesValid = false;
	public double countDefectiveGenes = 0;
	private RDFSerializator serializator;

	ArrayList<Integer> levels = new ArrayList<Integer>();
	// After being modeled the problem is classify into five process of SC
	String mainProcess;

	// create an empty graph
	Model model;
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		boolean triples_bool = false, indicators_bool = true;
		Problem pro = (Problem) obj;
		if (this.triples.size() == pro.getTriples().size()) {
			triples_bool = true;
		}
		
		for (Indicators indicator_one : getIndicators()) {
			if(!(pro.getIndicators().contains(indicator_one))) indicators_bool = false;
		}
		
		if (triples_bool && indicators_bool)
			return true;
		return false;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	/**
     *
     */
	public Problem() {
		model = ModelFactory.createDefaultModel();
		serializator = new RDFSerializator();
	}

	public void addTripleToModel(String subject, String predicate,
			String objectTo) {
		Resource resource = model.createResource(subject);
		Property prop = model.createProperty(predicate);
		Resource obj = model.createResource(objectTo);
		model.add(resource, prop, obj);
	}

	public Problem(String description, String name, ArrayList<RDFTriple> triples) {
		this.description = description;
		this.name = name;
		this.triples = triples;
		serializator = new RDFSerializator();
	}

	/**
	 * @param description
	 * @param id
	 * @param name
	 * @param triples
	 */
	public Problem(String description, int id, String name,
			ArrayList<RDFTriple> triples) {
		super();
		this.description = description;
		this.id = id;
		this.name = name;
		this.triples = triples;
		serializator = new RDFSerializator();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<RDFTriple> getTriples() {
		return triples;
	}

	public void setTriples(ArrayList<RDFTriple> triples) {
		this.triples = triples;
	}

	public double getFitnes() {
		if (fitness == 0) {
			double valuefrecuency = 0;
			double accumulafrecuency = 0;
			double alfaDefective = 0.02586206896551724;
			int countRelations = triples.size();
			RDFTriple tripleTemp = new RDFTriple();
			for (int i = 0; i < countRelations; i++) {
				tripleTemp = triples.get(i);
				valuefrecuency = serializator
						.calculateTotalFrequencyForProblems(
								new MyProblemCustomCell(tripleTemp.getSubject()
										.getLabel(), tripleTemp
										.getSubjetProcess()),
								new MyProblemCustomCell(tripleTemp.getObject()
										.getLabel(), tripleTemp
										.getObjectProcess()));
				accumulafrecuency += valuefrecuency;

				if (valuefrecuency <= alfaDefective || valuefrecuency == 0.0) {
					countDefectiveGenes++;
				}
			}
			fitness = accumulafrecuency /* / countRelations */;
			// UMBRAL
			if (fitness >= 0.0 && fitness <= alfaDefective) {
				fitnesValid = false;
			}
		}

		return fitness;

	}

	public String classifyProblem() {

		int max = 0;
		int MaxPos = 0;
		ArrayList<Indicators> rootCauses = this.getIndicatorsRootCauses();
		if (rootCauses.size() == 1) {
			mainProcess = rootCauses.get(0).getProcess()[0];
		} else {
			Tools tools = new Tools();
			Object[][] inferredTriples = tools.getInferredTriples();
			Object[][] assertedTriples = tools.getAssertedTriples();
			for (int i = 0; i < rootCauses.size(); i++) {
				int contToCompare = 0;
				String toCompare = rootCauses.get(i).toString() + "--"
						+ rootCauses.get(i).getProcess()[0];
				for (int j = 0; j < assertedTriples.length; j++) {
					String subjectToCompare = assertedTriples[j][0].toString();
					if (toCompare.equals(subjectToCompare)) {
						contToCompare++;
					}

				}
				for (int j = 0; j < inferredTriples.length; j++) {
					String subjectToCompare = inferredTriples[j][0].toString();
					if (toCompare.equals(subjectToCompare)) {
						contToCompare++;
					}

				}
				if (contToCompare > max) {
					max = contToCompare;
					MaxPos = i;
				}

			}

			mainProcess = rootCauses.get(MaxPos).getProcess()[0];

		}
		return mainProcess;
	}

	public ArrayList<Indicators> getIndicatorsRootCauses() {

		ArrayList<Indicators> rootCauses = new ArrayList<Indicators>();

		if (triples.size() == 1) {
			Indicators indicator = new Indicators(triples.get(0).getSubject()
					.getLabel(), triples.get(0).getObjectProcess());

			rootCauses.add(indicator);
		} else {
			for (int i = 0; i < triples.size(); i++) {
				boolean flag = true;
				Indicators Candidateindicator = new Indicators(triples.get(i)
						.getSubject().getLabel(), triples.get(i)
						.getObjectProcess());
//				System.out.println("indicator" + "sub "
//						+ triples.get(i).getObjectProcess());
				for (int j = 0; j < rootCauses.size(); j++) {
					Indicators indicatorToCompare = new Indicators(triples.get(
							i).getObject().getLabel(), triples.get(i)
							.getObjectProcess());
					System.out
							.println("indicator" + "ob " + indicatorToCompare);
					if (Candidateindicator.toString().equals(
							indicatorToCompare.toString())
							&& Candidateindicator.getProcess().equals(
									indicatorToCompare.getProcess())) {
						flag = false;
					}
				}
				if (flag) {
					if (rootCauses.contains(Candidateindicator) == false)
						rootCauses.add(Candidateindicator);
				}
			}
		}
		return rootCauses;
	}

	/**
	 * The Object(Indicator) is subject and it is not an object of other RDF
	 * triple
	 */
	public ArrayList<RDFTriple> getRootCauses() {
		ArrayList<RDFTriple> rootCauses = new ArrayList<RDFTriple>();
		boolean flag = true;
		if (triples.size() == 1) {
			rootCauses.add(triples.get(0));
		} else {
			for (int i = 0; i < triples.size(); i++) {
				RDFTriple tripleCandidate = new RDFTriple();
				tripleCandidate = triples.get(i);
				for (int j = 0; j < triples.size(); j++) {
					RDFTriple tripleToCompare = new RDFTriple();
					tripleToCompare = triples.get(j);
					if (tripleCandidate.getSubject().getLabel().equals(
							tripleToCompare.getObject().getLabel()))
						flag = false;
				}
				if (flag == true) {
					rootCauses.add(tripleCandidate);
				}
			}
		}
		return rootCauses;
	}

	/**
	 * @author Tauriñan The(Indicator) is a object is not an subject of other
	 *         RDF triple
	 * @return arrayList of RDFTriple where the triples are the unwanted effects
	 *         of the problem
	 */
	// debo preguntar por las triplas que son implicadad por otras(Objeto) y
	// ella no implican
	// nadiemas(NO Sujeto)
	// ¿que pasa si
	// los efecto no deseados(indicadores del ultimo nivel)
	// se relaciona con alguien más ?
	// falla lo anterior
	public ArrayList<RDFTriple> getUnwantedEffects() {
		// arreglo para almacenar las triplas que su subject sea causa raiz del
		// problema
		ArrayList<RDFTriple> UnwantedEffects = new ArrayList<RDFTriple>();
		boolean flag = true;
		// si solo hay una tripla ya esta forma parte de las causas raices y
		// ecefecto no deseados;
		if (triples.size() == 1) {
			UnwantedEffects.add(triples.get(0));

		}
		// si hay mas de una recorro el arreglo y si encuentro alguna que su
		// subject(object) no sea object de ninguna otra la agrego.
		else {
			for (int i = 0; i < triples.size(); i++) {
				flag = false;
				// tripla candidata a ser parte de las causas raices.
				RDFTriple tripleCandidate = new RDFTriple();
				tripleCandidate = triples.get(i);
				for (int j = 0; j < triples.size(); j++) {
					// tripla a comparar con la candidata.
					RDFTriple tripleToCompare = new RDFTriple();
					tripleToCompare = triples.get(j);
					// Aclarar con irlán
					// si el subject de la tripla candidata es igual al subject
					// de la tripla a comparar entonces la bandera se pone en
					// false
					if (tripleCandidate.getSubject().getLabel().equals(
							tripleToCompare.getObject().getLabel()))
						// Cuando se relaciona con orta aunque este en el ultimo
						// nivel falla
						flag = true;
				}
				// cuando termino de comparar la tripla con todas las demas si
				// la bandera se mantiene en true la agrego al arreglo.
				if (flag == true) {
					UnwantedEffects.add(tripleCandidate);
				}
			}
		}
		return UnwantedEffects;
	}

	/**
	 * @author Legolas
	 * @return an integer whith the numbers of indicators roots that have the
	 *         problem
	 * 
	 */
	public int getCantRoots() {
		int cant = 0;
		for (int i = 0; i < indicatorsArray.size(); i++) {
			if (this.isRoot(indicatorsArray.get(i))) {
				cant++;
			}
		}
		return cant;
	}

	/**
	 * @author Ruben
	 * @return an integer whith the numbers of indicators roots that have the
	 *         problem
	 * 
	 */
	public int getCantEffects() {
		int cant = 0;
		for (int i = 0; i < indicatorsArray.size(); i++) {
			if (this.isEffect(indicatorsArray.get(i))) {
				cant++;
			}
		}
		return cant;
	}

	public ArrayList<RDFTriple> getSimilarTripeDefective() {
		ArrayList<RDFTriple> tripeDefectives = new ArrayList<RDFTriple>();
		boolean flag = true;
		if (triples.size() > 1) {
			for (int i = 0; i < triples.size(); i++)
				tripeDefectives.add(triples.get(i));
		} else {
			for (int i = 0; i < triples.size(); i++) {
				RDFTriple tripleCandidate = new RDFTriple();
				tripleCandidate = triples.get(i);
				for (int j = 0; j < triples.size(); j++) {
					RDFTriple tripleToCompare = new RDFTriple();
					tripleToCompare = triples.get(j);
					if ((serializator.calculateTotalFrequencyForProblems(
							new MyProblemCustomCell(tripleCandidate
									.getSubject().getLabel(), tripleCandidate
									.getSubjetProcess()),
							new MyProblemCustomCell(tripleCandidate.getObject()
									.getLabel(), tripleCandidate
									.getObjectProcess())) <= 0.02586206896551724)
							|| (serializator
									.calculateTotalFrequencyForProblems(
											new MyProblemCustomCell(
													tripleToCompare
															.getSubject()
															.getLabel(),
													tripleToCompare
															.getSubjetProcess()),
											new MyProblemCustomCell(
													tripleToCompare.getObject()
															.getLabel(),
													tripleToCompare
															.getObjectProcess())) <= 0.02586206896551724))

						flag = false;
				}
				if (flag == true) {
					tripeDefectives.add(tripleCandidate);
				}
			}
		}
		return tripeDefectives;
	}

	public int getDefectiveGenes(ArrayList<RDFTriple> triples) {
		double valuefrecuency = 0;
		int maxDefective = 0;
		double alfaDefective = 0.02586206896551724;
		triples = new ArrayList<RDFTriple>();
		RDFTriple tripleTemp = new RDFTriple();
		for (int j = 0; j < triples.size(); j++) {
			tripleTemp = triples.get(j);
			valuefrecuency = serializator.calculateTotalFrequencyForProblems(
					new MyProblemCustomCell(tripleTemp.getSubject().getLabel(),
							tripleTemp.getSubjetProcess()),
					new MyProblemCustomCell(tripleTemp.getObject().getLabel(),
							tripleTemp.getObjectProcess()));
			System.out.println("Frecuencia de la tripla" + " " + j);
			System.out.println(valuefrecuency);
			if (valuefrecuency <= alfaDefective) {
				maxDefective++;
			}
		}
		System.out.println("Cantida de genes defecuosos <=0.02586206896551724");
		System.out.println(maxDefective);
		return maxDefective;

	}

	/**
	 * Setting the indicator ind2 to the current triple,where was the indicator
	 * ind1
	 * 
	 * @param ind1
	 * @param ind2
	 */
	public void crossingIndicators(Indicators ind1, Indicators ind2) {
		Subject subToAdd = new Subject();
		RDFObject obToAdd = new RDFObject();

		for (int i = 0; i < triples.size(); i++) {

			if (triples.get(i).getSubject().equals(
					new Subject(ind1.getName(), null))) {
				if (triples.get(i).getSubject() != null)
					System.out.println("Entro");
				subToAdd.setLabel(ind2.getName());
				triples.get(i).setSubject(subToAdd);
			}

			if (triples.get(i).getObject().equals(
					new RDFObject(ind1.getName(), null))) {
				if (triples.get(i).getObject() != null)
					obToAdd.setLabel(ind2.getName());
				triples.get(i).setObject(obToAdd);
			}
		}
	}

	/**
	 * CHANGE ind1
	 * 
	 * @param ind1
	 * @param ind2
	 */
	// Este es que tengo que modificar teniendo en cuenta los bloques de
	// contrucción
	// Aun no se vajo que criterio(El peso sigue siendo importante)
	public void mutatingIndicators(Indicators ind1, Indicators ind2) {
		ArrayList<RDFTriple> rdfTripletmp = new ArrayList<RDFTriple>();
		Subject subToAdd1 = new Subject();
		RDFObject obToAdd1 = new RDFObject();
		// COMO COPIAR ESTO ELEGANTEMENTE PREGUNTAR, el clone no funcionï¿½
		for (int i = 0; i < triples.size(); i++) {
			subToAdd1 = new Subject(triples.get(i).getSubject().getLabel(),
					triples.get(i).getSubject().getPoint());
			obToAdd1 = new RDFObject(triples.get(i).getObject().getLabel(),
					triples.get(i).getObject().getPoint());
			rdfTripletmp.add(new RDFTriple(obToAdd1, null, subToAdd1));
		}
		// //////////////////////////////////////////////

		for (int j = 0; j < rdfTripletmp.size(); j++) {

			if (rdfTripletmp.get(j).getSubject().equals(
					new Subject(ind1.getName(), null))) {
				Subject subToAdd = new Subject();
				subToAdd.setLabel(ind2.getName());
				triples.get(j).setSubject(subToAdd);
			}

			if (rdfTripletmp.get(j).getObject().equals(
					new RDFObject(ind1.getName(), null))) {
				RDFObject obToAdd = new RDFObject();
				obToAdd.setLabel(ind2.getName());
				triples.get(j).setObject(obToAdd);
			}

			if (rdfTripletmp.get(j).getSubject().equals(
					new Subject(ind2.getName(), null))) {
				Subject subToAdd = new Subject();
				subToAdd.setLabel(ind1.getName());
				triples.get(j).setSubject(subToAdd);
			}

			if (rdfTripletmp.get(j).getObject().equals(
					new RDFObject(ind2.getName(), null))) {
				RDFObject obToAdd = new RDFObject();
				obToAdd.setLabel(ind1.getName());
				triples.get(j).setObject(obToAdd);
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		String problem = name;

		return problem;
	}

	/**
	 * @return the indicatorsArray
	 */
	public ArrayList<Indicators> getIndicatorsArray() {
		return indicatorsArray;
	}

	/**
	 * @param indicatorsArray
	 *            the indicatorsArray to set
	 */
	public void setIndicatorsArray(ArrayList<Indicators> indicatorsArray) {
		this.indicatorsArray = indicatorsArray;
	}

	public ArrayList getProblemCells() {
		ArrayList cells = new ArrayList();
		for (int i = 0; i < triples.size(); i++) {
			ArrayList rdfTriple = triples.get(i).getGraphTriple();
			cells.add((CustomCell) rdfTriple.get(0));
			cells.add((CustomCell) rdfTriple.get(1));
		}
		return cells;
	}

	public ArrayList getProblemEdges() {
		ArrayList edges = new ArrayList();

		for (int i = 0; i < triples.size(); i++) {
			ArrayList rdfTriple = triples.get(i).getGraphTriple();
			edges.add((DefaultEdge) rdfTriple.get(2));

		}
		return edges;
	}

	/**
	 * Filling an array with all the indicators contained in triples, it
	 * contains duplicate elements, but it is fixed with the method
	 * eliminateDuplicate
	 * 
	 * @return ArrayList<Indicators>
	 */
	public ArrayList<Indicators> getIndicators() {
		ArrayList<Indicators> arrayToReturn = new ArrayList<Indicators>();
		for (int i = 0; i < triples.size(); i++) {
			// Is an Object or a Subject
			arrayToReturn.add(new Indicators(triples.get(i).getSubject()
					.getLabel(), triples.get(i).getSubjetProcess()));
			arrayToReturn.add(new Indicators(triples.get(i).getObject()
					.getLabel(), triples.get(i).getObjectProcess()));
		}

		return StringUtils.eliminateDuplicate(arrayToReturn);
	}

	/**
	 * Method used to get the RDF triples where an indicator participate in RDF
	 * triples
	 * 
	 * @param ind
	 * @return ArrayList<RDFTriple>
	 */
	public ArrayList<RDFTriple> getSubjectsOfInd1(Indicators ind) {
		ArrayList<RDFTriple> arrayToReturn = new ArrayList<RDFTriple>();
		RDFTriple toAdd = new RDFTriple();
		Subject subToAdd = new Subject();
		RDFObject obToAdd = new RDFObject();

		for (int i = 0; i < triples.size(); i++) {
			if (triples.get(i).getSubject().equals(
					new Subject(ind.getName(), null))) {
				if (triples.get(i).getObject() != null)
					obToAdd.setLabel(triples.get(i).getObject().getLabel());
			}

			if (triples.get(i).getObject().equals(
					new RDFObject(ind.getName(), null))) {
				if (triples.get(i).getSubject() != null)
					subToAdd.setLabel(triples.get(i).getSubject().getLabel());
			}
			toAdd.setSubject(subToAdd);
			toAdd.setObject(obToAdd);
			arrayToReturn.add(toAdd);
		}
		return arrayToReturn;
	}

	// return triples that have the subject equal to ind
	public ArrayList<RDFTriple> getTriplesOfInd1(RDFObject ind) {
		ArrayList<RDFTriple> arrayToReturn = new ArrayList<RDFTriple>();
		RDFTriple toAdd = new RDFTriple();
		Subject subToAdd = new Subject();
		RDFObject obToAdd = new RDFObject();

		for (int i = 0; i < triples.size(); i++) {
			if (triples.get(i).getSubject().equals(
					new Subject(ind.getLabel(), null))) {
				if (triples.get(i).getObject() != null)
					obToAdd.setLabel(triples.get(i).getObject().getLabel());
			}

			toAdd.setSubject(subToAdd);
			toAdd.setObject(obToAdd);
			arrayToReturn.add(toAdd);
		}
		return arrayToReturn;
	}

	// TENGO QUE VALIDAR QUE PUEDA PERTENECER LAS EFECTOS NO DESEADOS .
	public void mutatingByGoodBloqsWEffects(
			java.util.ArrayList<Indicators> indicators) {
		System.out.println(" Mutando");
		Random random = new Random();
		Indicators ind = new Indicators(null, null);
		int cont = 0;
		double frecuencyOfIndicator = 0.001841620626151013;
		ArrayList<Indicators> indicatorsToMutate = new ArrayList<Indicators>();
		int cantIndicators = indicators.size();
		// llenando un arreglo con un random de indicadores a mutar que no sean
		// causas raices
		while (cont < cantIndicators) {
			ind = indicatorsArray.get(random.nextInt(cantIndicators));
			if (this.isEffect(ind) == false) {
				System.out.println("Encontro una causa");
				indicatorsToMutate.add(ind);
				cont++;
			}
		}
		// cambiando los indicadores
		for (int i = 0; i < cantIndicators; i++) {
			System.out.println("Tiene buena frecucia absoluta");
			if (frecuencyOfIndicator <= RDFSerializator
					.calculateTotalFrecuencyIndicator(indicatorsToMutate.get(i)
							.getName()))
				this.crossingIndicators(indicators.get(i), indicatorsToMutate
						.get(i));
		}

	}

	// TENGO QUE VALIDAR QUE PUEDA PERTENECER LAS CAUSAS RAICES .
	public void mutatingByGoodBloqsWRoot(ArrayList<Indicators> indicators) {
		System.out.println(" Mutando....");
		Random random = new Random();
		Indicators ind = new Indicators(null, null);
		int cont = 0;
		double frecuencyOfIndicator = 0.001841620626151013;
		ArrayList<Indicators> indicatorsToMutate = new ArrayList<Indicators>();
		int cantIndicators = indicators.size();
		// llenando un arreglo con un random de indicadores a mutar que no sean
		// causas raices
		while (cont < cantIndicators) {
			ind = indicatorsArray.get(random.nextInt(cantIndicators));
			if (this.isRoot(ind) == false) {
				System.out.println("Encontro un  Efecto");
				indicatorsToMutate.add(ind);
				cont++;
			}
		}
		// cambiando los indicadores
		for (int i = 0; i < cantIndicators; i++) {
			if (frecuencyOfIndicator <= RDFSerializator
					.calculateTotalFrecuencyIndicator(indicatorsToMutate.get(i)
							.getName()))
				this.crossingIndicators(indicators.get(i), indicatorsToMutate
						.get(i));
			System.out.println("Mutó el indicador" + " " + i);
		}

	}

	/***
     * 
     */
	public boolean isRoot(Indicators ind) {
		boolean flag = false;
		for (int i = 0; i < rootCauses.size(); i++) {
			if (rootCauses.get(i).getSubject().getLabel().equals(ind.getName())) {
				flag = true;
			}
		}
		return flag;
	}

	public boolean isEffect(Indicators ind) {
		boolean flag = false;
		for (int i = 0; i < unwantedEffects.size(); i++) {
			if (unwantedEffects.get(i).getObject().getLabel().equals(
					ind.getName())) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * Method used to get an ArrayList of indicators where it the indicator ind,
	 * For example in a triple like this: A-->B, subject A, Object B, pass A as
	 * parameter and return B
	 * 
	 * @param ind
	 * @return
	 */
	public ArrayList<RDFObject> getObjectsOfInd(Indicators ind,
			ArrayList<RDFTriple> triple) {
		ArrayList<RDFObject> arrayToReturn = new ArrayList<RDFObject>();
		for (int i = 0; i < triple.size(); i++) {
			if (triple.get(i).getSubject().equals(
					new Subject(ind.getName(), null))) {
				arrayToReturn.add(new RDFObject(triple.get(i).getObject()
						.getLabel(), null));
			}
		}
		return arrayToReturn;
	}

	/**
	 * Method used to get an ArrayList of indicators where it the indicator ind,
	 * For example in a triple like this: A-->B, subject A, Object B, pass A as
	 * parameter and return null
	 * 
	 * @param ind
	 * @return
	 */
	public ArrayList<Subject> getSubjectsOfInd(Indicators ind,
			ArrayList<RDFTriple> triple) {
		ArrayList<Subject> arrayToReturn = new ArrayList<Subject>();
		for (int i = 0; i < triple.size(); i++) {
			if (triple.get(i).getObject().equals(
					new RDFObject(ind.getName(), null))) {
				arrayToReturn.add(new Subject(triple.get(i).getSubject()
						.getLabel(), null));
			}
		}
		return arrayToReturn;
	}

	/**
	 * 
	 * @param ind
	 * @param subjectArray
	 */
	public void setSubjectsToTriple(int crossPoint, Indicators ind,
			ArrayList<Subject> subjectArray) {

		// System.out.println(ind.getName() + "Indicador");

		for (int i = 0; i < triples.size(); i++) {
			System.out.println(triples.get(i).getSubject().getLabel()
					+ "subject");
			System.out
					.println(triples.get(i).getObject().getLabel() + "object");

			if (triples.get(i).getSubject().equals(
					new Subject(ind.getName(), null))) {
				for (int j = 0; j < subjectArray.size(); j++) {
					triples.get(i).setSubject(subjectArray.get(j));
				}
			}
		}
	}

	/**
	 * @author Legolas this return a list with the levels of the all the posible
	 *         ways of the problem
	 * @param triple
	 *            the first triple to begun find ways
	 * @param level
	 *            this is a counter to count the level of the one way
	 * @param levels
	 *            here are going to store the diferent levels of the all ways
	 *            possible
	 * @return
	 */
	public void fillLevels(RDFTriple triple, int lvl) {
		int aux = lvl;
		if (this.isLeaf(triple)) {
			levels.add(aux);
		} else {
			aux++;
			for (int i = 0; i < triples.size(); i++) {
				if (triples.get(i).subject.label.equals(triple.object.label)) {
					this.fillLevels(triples.get(i), aux);
				}
			}
		}

	}

	public void fillLevels1(RDFTriple triple, int lvl) {
		int aux = lvl;
		if (this.isR(triple)) {
			levels.add(aux);
		} else {
			aux++;
			for (int i = 0; i < triples.size(); i++) {
				if (triples.get(i).object.label.equals(triple.subject.label)) {
					this.fillLevels1(triples.get(i), aux);
				}
			}
		}

	}

	/**
	 * @author Legolas this determinate the most hi level of the problem
	 *         starting from the given a root
	 * @param root
	 *            root of the problem
	 * @return
	 */
	public int getHiLevel() {
		int hiLevel = 0;
		// llenando un array con los mayores levels de cada camino
		unwantedEffects = this.getUnwantedEffects();
		for (int j = 0; j < unwantedEffects.size(); j++) {
			levels = new ArrayList<Integer>();
			fillLevels1(unwantedEffects.get(j), 1);
			for (int k = 0; k < levels.size(); k++) {
				if (levels.get(k) >= hiLevel) {
					hiLevel = levels.get(k);
				}
			}

		}
		// seleccionando el nivel mas grande de todos los caminos
		level = hiLevel;
		return hiLevel;
	}

	public int getHiLevel1() {
		int hiLevel = 0;

		// llenando un array con los mayores levels de cada camino
		rootCauses = this.getRootCauses();
		for (int i = 0; i < rootCauses.size(); i++) {
			levels = new ArrayList<Integer>();
			fillLevels(rootCauses.get(i), 1);
			for (int j = 0; j < levels.size(); j++) {
				if (levels.get(j) >= hiLevel) {
					hiLevel = levels.get(j);
				}
			}
		}
		// seleccionando el nivel mas grande de todos los caminos
		level = hiLevel;
		return hiLevel;
	}

	/**
	 * @author Legolas This is for determinate if a triple is the last in the
	 *         problem
	 * @param triple
	 * @return
	 */
	public boolean isLeaf(RDFTriple triple) {
		boolean flag = true;
		for (int i = 0; i < triples.size(); i++) {
			if (triples.get(i).getSubject().label.equals(triple.object.label)) {
				flag = false;
			}
		}
		return flag;
	}

	public boolean isR(RDFTriple triple) {
		boolean flag = true;
		for (int i = 0; i < triples.size(); i++) {
			if (triples.get(i).getObject().label.equals(triple.subject.label)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 *@author Legolas this is for store the already calculated roots causas to
	 *         calculate it any more in yhe subProces
	 * @param roots
	 */
	public void setRoots(ArrayList<RDFTriple> roots) {
		rootCauses = roots;
	}

	/**
	 *@author tauriñan this is for store the already calculated effects causas
	 *         to calculate it any more in yhe subProces
	 * @param effects
	 */
	public void setEffects(ArrayList<RDFTriple> effects) {
		unwantedEffects = effects;
	}

	/**
	 * @author Legolas this is fof obtain the roots alrady finded and stores by
	 *         getRootsCauses() ;
	 * @return
	 */
	public ArrayList<RDFTriple> getRootsStored() {
		return rootCauses;
	}

	public ArrayList<RDFTriple> getEffectStored() {
		return unwantedEffects;
	}

	/**
	 * * @author Legolas this is for return the indicators of a given level
	 * 
	 * @param level
	 * @return
	 */
	/*
	 * public ArrayList<RDFObject> getIndicatorsOfLevel(int level){
	 * ArrayList<RDFObject> indicatorsToFill = new ArrayList<RDFObject>();
	 * ArrayList<RDFObject> indicatorOfRoot = new ArrayList<RDFObject>();
	 * ArrayList<RDFObject> indicators = new ArrayList<RDFObject>(); RDFTriple
	 * triple = new RDFTriple(); for(int i=0; i<rootCauses.size(); i++){
	 * indicatorOfRoot = this.fillIndicatorsOfLevel(rootCauses.get(i),
	 * indicatorsToFill, 0, level); for(int j=0; j<indicatorOfRoot.size();j++){
	 * indicators.add(indicatorOfRoot.get(j)); } }
	 * 
	 * return indicators; }
	 */
	public ArrayList<Indicators> getIndicatorsOfLevel(int level) {

		ArrayList<Indicators> indicatorOfRoot = new ArrayList<Indicators>();
		ArrayList<Indicators> indicators = new ArrayList<Indicators>();
		for (int i = 0; i < rootCauses.size(); i++) {
			indicatorOfRoot = new ArrayList<Indicators>();
			indicatorOfRoot = this.fillIndicatorsOfLevel(rootCauses.get(i), 0,
					level);
			for (int j = 0; j < indicatorOfRoot.size(); j++) {
				indicators.add(indicatorOfRoot.get(j));
			}
		}

		return StringUtils.eliminateDuplicate(indicators);
	}

	public ArrayList<Indicators> getIndicatorsOfLevel2(int level) {

		ArrayList<Indicators> indicatorOfEfect = new ArrayList<Indicators>();
		ArrayList<Indicators> indicators = new ArrayList<Indicators>();
		for (int i = 0; i < unwantedEffects.size(); i++) {
			indicatorOfEfect = new ArrayList<Indicators>();
			indicatorOfEfect = this.fillIndicatorsOfLevel2(unwantedEffects
					.get(i), 0, level);
			for (int j = 0; j < indicatorOfEfect.size(); j++) {
				indicators.add(indicatorOfEfect.get(j));
			}
		}

		return StringUtils.eliminateDuplicate(indicators);
	}

	/**
	 * @author Legolas this is for return the indicators of a theterminated
	 *         level
	 * @param root
	 * @param indicators
	 * @param levelActual
	 * @param levelDeseado
	 * @return
	 */
	public ArrayList<Indicators> fillIndicatorsOfLevel(RDFTriple root,
			int levelActual, int levelDeseado) {
		// si el nivel en que estoy es uno menor que el deseado quiere desir que
		// esta tripla el objeto pertenece a l nivel requerido porque en el
		// proximo
		// nivel la otra tripla tiene como sujeto al indicador que es objeto de
		// esta tripla asi que lo qgrego aqui para no tener que volverlo a
		// llamar
		Indicators ind = new Indicators(root.getObject().label, root
				.getObjectProcess());
		ArrayList<Indicators> indicators = new ArrayList<Indicators>();
		if (levelActual + 1 == levelDeseado) {
			indicators.add(ind);
		} else {
			ArrayList<RDFTriple> triplesOfIndi = new ArrayList<RDFTriple>();
			triplesOfIndi = this.getTriplesOfInd1(ind);
			for (int i = 0; i < triplesOfIndi.size(); i++) {
				indicators = this.fillIndicatorsOfLevel(triplesOfIndi.get(i),
						levelActual + 1, levelDeseado);
			}
		}
		return indicators;
	}

	/**
	 * @author tauriñan this is for return the indicators of a theterminated
	 *         level
	 * @param root
	 * @param indicators
	 * @param levelActual
	 * @param levelDeseado
	 * @return
	 */
	public ArrayList<Indicators> fillIndicatorsOfLevel2(RDFTriple effects,
			int levelActual, int levelDeseado) {
		// si el nivel en que estoy es uno menor que el deseado quiere desir que
		// esta tripla el objeto pertenece a l nivel requerido porque en el
		// proximo
		// nivel la otra tripla tiene como sujeto al indicador que es objeto de
		// esta tripla asi que lo qgrego aqui para no tener que volverlo a
		// llamar
		Indicators ind = new Indicators(effects.getObject().label, effects
				.getObjectProcess());
		ArrayList<Indicators> indicators = new ArrayList<Indicators>();
		if (levelActual + 1 == levelDeseado) {
			indicators.add(ind);
		} else {
			ArrayList<RDFTriple> triplesOfIndi = new ArrayList<RDFTriple>();
			triplesOfIndi = this.getTriplesOfInd1(ind);
			for (int i = 0; i < triplesOfIndi.size(); i++) {
				indicators = this.fillIndicatorsOfLevel2(triplesOfIndi.get(i),
						levelActual + 1, levelDeseado);
			}
		}
		return indicators;
	}

	// return triples that have the subject equal to ind
	public ArrayList<RDFTriple> getTriplesOfInd1(Indicators ind) {
		ArrayList<RDFTriple> arrayToReturn = new ArrayList<RDFTriple>();
		RDFTriple toAdd = new RDFTriple();
		Subject subToAdd = new Subject();
		RDFObject obToAdd = new RDFObject();

		for (int i = 0; i < triples.size(); i++) {
			if (triples.get(i).getSubject().equals(
					new Subject(ind.getName(), null))) {
				if (triples.get(i).getObject() != null)
					obToAdd.setLabel(triples.get(i).getObject().getLabel());
				subToAdd.setLabel(triples.get(i).getSubject().getLabel());
				toAdd.setSubject(subToAdd);
				toAdd.setObject(obToAdd);
				arrayToReturn.add(toAdd);
			}

		}
		return arrayToReturn;
	}

	/**
	 * @author Legolas this is for return the indicators of a theterminated
	 *         level
	 * @param root
	 * @param indicators
	 * @param levelActual
	 * @param levelDeseado
	 * @return
	 */
	public ArrayList<RDFObject> fillIndicatorsOfLevel(RDFTriple root,
			ArrayList<RDFObject> indicators, int levelActual, int levelDeseado) {
		// si el nivel en que estoy es uno menor que el deseado quiere desir que
		// esta tripla el objeto pertenece a l nivel requerido porque en el
		// proximo
		// nivel la otra tripla tiene como sujeto al indicador que es objeto de
		// esta tripla asi que lo qgrego aqui para no tener que volverlo a
		// llamar
		RDFObject object = new RDFObject();
		object = root.getObject();
		if (levelActual + 1 == levelDeseado) {
			indicators.add(object);
		} else {
			ArrayList<RDFTriple> triplesOfIndi = new ArrayList<RDFTriple>();
			triplesOfIndi = this.getTriplesOfInd1(object);
			for (int i = 0; i < triplesOfIndi.size(); i++) {
				this.fillIndicatorsOfLevel(triplesOfIndi.get(i), indicators,
						levelActual + 1, levelDeseado);
			}
		}
		return indicators;
	}

	/***
	 * @author Legolas
	 * @param ind
	 * @param triple
	 * @return a array list of triples where the Ind guiven is Subject only
	 */
	public ArrayList<RDFTriple> getTriplesPointedBy(RDFObject obj) {
		ArrayList<RDFTriple> arrayToReturn = new ArrayList<RDFTriple>();
		for (int i = 0; i < triples.size(); i++) {
			if (triples.get(i).getSubject().getLabel().equals(obj.getLabel())) {
				arrayToReturn.add(triples.get(i));
			}
		}
		return arrayToReturn;
	}

	/**
	 * @author Legolas
	 * @param ind
	 *            is the indicator to find in the triples of the problems
	 * @return array list whith all the triples that the subjects is the same
	 *         indicator in the parameter and the triple visited is false;
	 */
	public ArrayList<RDFTriple> getTriplesIsSubject(Indicators ind) {
		ArrayList<RDFTriple> arrayToReturn = new ArrayList<RDFTriple>();

		for (int i = 0; i < triples.size(); i++) {
			if (triples.get(i).getSubject().getLabel().equals(ind.getName())
					&& triples.get(i).visited == false) {
				if (triples.get(i).getObject() != null)

					arrayToReturn.add(triples.get(i));
			}

		}
		return arrayToReturn;
	}

	/**
	 * @author Legolas
	 * @param ind
	 *            is the indicator to find in the triples of the problems
	 * @return array list whith all the triples that the Object is the same
	 *         indicator in the parameter
	 */
	public ArrayList<RDFTriple> getTriplesIsObject(Indicators ind) {
		ArrayList<RDFTriple> arrayToReturn = new ArrayList<RDFTriple>();

		for (int i = 0; i < triples.size(); i++) {
			if (triples.get(i).getObject().getLabel().equals(ind.getName())
					&& triples.get(i).visited == false) {
				if (triples.get(i).getSubject() != null)

					arrayToReturn.add(triples.get(i));
			}

		}
		return arrayToReturn;
	}

	/**
	 * @author Legolas
	 * @return return true if this problem have cicles
	 * 
	 */
	public boolean haveCicles() {
		boolean flag = false;
		ArrayList<RDFTriple> tripleRoots = new ArrayList<RDFTriple>();
		// cojo las causas raices para empesar a buscar los ciclos partiendo de
		// ellas.
		tripleRoots = this.getRootCauses();
		RDFTriple tripTemp = new RDFTriple();
		for (int i = 0; i < tripleRoots.size(); i++) {
			tripTemp = tripleRoots.get(i);
			// si hay algun ciclo partiendo de esta tripla devolvera true al
			// primero ya salgo
			if (this.findCicles(tripTemp) == true) {
				return true;
			}
			// aqui reseteo los visitados para continuar con la proxima causa
			// raiz
			for (int j = 0; j < triples.size(); j++) {
				triples.get(j).visited = false;
			}
		}

		return flag;

	}

	/**
	 * @author Legolas
	 * @param tripToCompare
	 *            is the triple where the algorithm start to find
	 * @return true if find a cicles starting to find by the triple guiven
	 */
	public boolean findCicles(RDFTriple tripToCompare) {
		boolean flag1 = false;
		// si la tripla ya fue visitada
		if (tripToCompare.visited == true) {
			flag1 = true;
		}
		// en caso contrario continuo buscadon con las proximas triplas que ella
		// apunta
		// y la marco visitada a ella
		else {
			tripToCompare.visited = true;
			ArrayList<RDFTriple> triplesNext = new ArrayList<RDFTriple>();
			// aqui busco las triplas que son apuntadas por la tripla en
			// cuestion para chekearlas en la proxima iteracion
			triplesNext = this.getTriplesPointedBy(tripToCompare.object);
			// si el arreglo esta vacio quiere desir que la tripla en cuestion
			// es hoja por lo tanto termino.
			if (triplesNext.size() == 0) {
				return flag1;
			}
			// en caso contrario le paso a este metodo a verificar todas las
			// triplas
			else {
				for (int i = 0; i < triplesNext.size(); i++) {
					flag1 = this.findCicles(triplesNext.get(i));
					// aqui compruebo porque al primer ciclo que encuentre ya
					// salgo
					if (flag1 == true) {
						return flag1;
					}
				}
			}

		}

		return flag1;
	}

	public boolean checkSubGraph() {
		// bandera para comparar la cantidad de indicadores relacionados con los
		// indicadores
		// totales.
		boolean flag = false;
		// contador para la cantidad de indicadores relacionados entre si
		int cont = 0;
		// aui creo un indicador con el subject de la primera tripla del arreglo
		// para
		// empesar a correo el algoritmo desde ese indicador y recorriendo las
		// demas triplas
		// conectadas.
		Indicators indicatorToStart = new Indicators(triples.get(0)
				.getSubject().getLabel(), triples.get(0).getSubjetProcess());

		// aqui comienzo a buscar la cantidad de indicadores relacionados
		cont = this.findSubGraph(indicatorToStart, cont);

		// aqui compruebo la cantidad encontrada con la cantidad de indicadores
		// totales.
		if (cont != indicatorsArray.size()) {
			/*
			 * si la cantidad de ind relacionados empesando por indicatorToStart
			 * no es igual a la cantidad de indicadores totales entonces quiere
			 * desir que existen subgrafos o triplas que no estan relacionadas
			 * en ningun camino con el indicatorToStart
			 */
			flag = true;
		}
		// retorna verdadero si existen subgrafos de no haber retorn falso
		return flag;
	}

	/**
	 * @author Legolas recrusive method to find is the problem caotains any
	 *         subgraphs in another words if all the triples are conected in any
	 *         way.
	 * @param cont
	 *            is to count the indicators conected finded.
	 * @param indToCheck
	 *            is the indicator to check if is conected in any way.
	 * @return an intiger with the number of indicators related.
	 */
	public int findSubGraph(Indicators indToCheck, int cont) {
		// para guardar las triplas que tienen al indicador como subject o
		// object y no han sido visitadas
		ArrayList<RDFTriple> subjects = new ArrayList<RDFTriple>();
		ArrayList<RDFTriple> objects = new ArrayList<RDFTriple>();
		int aux = cont;
		// aqui voy a almacenar las triplas que tienen el subject igual al
		// indicador pasado por parametros
		subjects = this.getTriplesIsSubject(indToCheck);
		objects = this.getTriplesIsObject(indToCheck);
		if (subjects.size() == 0 && objects.size() == 0) {
			// contando indicador conectado
			aux++;
			// saliendo
			return aux;
		} else {
			// contando indicador conectado
			aux++;
			Indicators ind = new Indicators(null, null);
			// comprobando los indicadores object de las triplas que tienen a
			// este indicador como subject
			for (int i = 0; i < subjects.size(); i++) {
				// marcando la tripla visitada
				subjects.get(i).visited = true;
				// verificando los object de las triplas almacenadas en subjects
				ind.setName(subjects.get(i).getObject().getLabel());

				aux = this.findSubGraph(ind, aux);
			}
			// comprobando los indicadores subject de las triplas que tienen a
			// este indicador como object
			for (int i = 0; i < objects.size(); i++) {
				// marcando la tripla visitada
				objects.get(i).visited = true;

				// verificando los subject de las triplas almacenadas en objects
				ind.setName(objects.get(i).getSubject().getLabel());

				aux = this.findSubGraph(ind, aux);
			}
		}
		return aux;
	}

	public int getCantidadInditators() {
		return getIndicators().size();
	}

	public void changeIndicators(Indicators indToChange,
			Indicators indChangeable) {
		int cantTriples = triples.size();
		Subject sub = new Subject();
		RDFObject obj = new RDFObject();

		for (int i = 0; i < cantTriples; i++) {
			if (triples.get(i).getSubject().getLabel().equals(
					indChangeable.getName())) {
				sub.setLabel(indToChange.getName());
				triples.get(i).setSubject(sub);
			}
			if (triples.get(i).getObject().getLabel().equals(
					indChangeable.getName())) {
				obj.setLabel(indToChange.getName());
			}
		}
	}

	/**
	 * @return the mainProcess
	 */
	public String getMainProcess() {
		return mainProcess;
	}

	/**
	 * @param mainProcess
	 *            the mainProcess to set
	 */
	protected void setMainProcess(String mainProcess) {
		this.mainProcess = mainProcess;
	}
}
