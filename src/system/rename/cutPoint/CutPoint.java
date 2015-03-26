package system.rename.cutPoint;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.ui.GraphEd;
import system.rename.Tools;

/**
 * this class implements the cut points for the suggested Nodes,
 *  we get all the possible nodes and suggest only the most
 * significant. the possibles nodes are the entire population, then is
 * calculated, for each node, the frequency of apparition in this array, then
 * the more significants indicators are suggested. the cut point by default is 0.51
 * 
 * @author Leo
 * 
 */
public class CutPoint {

	private Tools tools = new Tools();

	// this is the cut point
	private double cutPoint = 0.51;

	// this is the entire population, all the possible nodes that can be
	// suggested, can be repeated nodes
	private ArrayList<MyProblemCustomCell> population;

	// all the different element that are in the population
	private ArrayList<ElementOfThePopulation> elements = new ArrayList<ElementOfThePopulation>();

	private MyProblemCustomCell subject;

	/**
	 * Constructor
	 * 
	 * @param subject
	 */
	public CutPoint(MyProblemCustomCell subject) {
		super();
		this.subject = subject;
		population = new ArrayList<MyProblemCustomCell>();
	}
	
	/**
	 * this method fill the population, make an exhaustive search in the DB and
	 * add to the population ArrayList all the possibles indicators to suggest, that
	 * is to say, all the indicators in DB that are object to the subject.
	 */
	public void getPopulationForCell() {
		String subjectProcessArr = tools.concatenateProcess(subject
				.getProcess());
		try {
			ConnectionToPostgres connect =PersistentManager.getConnectionToPostgreSQL();
			connect.tryConnection();
			ResultSet result = connect
					.executeSql("select object, object_process from triple_rdf,problem where subject = '"
							+ subject.toString()
							+ "'and subject_process = '"
							+ subjectProcessArr
							+ "' and triple_rdf.id_graph = problem.id_graph");
			while (result.next()) {
				String object = result.getString("object");
				String object_process = result.getString("object_process");
				population.add(new MyProblemCustomCell(object, object_process
						.split("--")));
			}
			result = PersistentManager.getConnectionToPostgreSQL()
					.executeSql("select object, object_process from inferred_rdf_triple where subject = '"
							+ subject
							+ "' and subject_process = '"
							+ subjectProcessArr + "'");
			while (result.next()) {
				String object = result.getString("object");
				String process = result.getString("object_process");
				population.add(new MyProblemCustomCell(object, process
						.split("--")));
			}
		connect.disconnect();
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

	/**
	 * get all the elements without repetitions and store them in the elements{@link ArrayList} 
	 */
	public void fillElements() {
		boolean flag = false;
		for (MyProblemCustomCell cell : population) {
			flag = false;
			for (ElementOfThePopulation element : elements) {
				if (element.compare(cell)) {
					flag = true;
				}                
			}
			if(flag == false){
				elements.add(new ElementOfThePopulation(subject,cell));
			}
		}
	}

	/**
	 * seek for each element how many times occur in
	 * population
	 */
	public void seekOccurrences() {
    for (ElementOfThePopulation element : elements) {
		for (MyProblemCustomCell cell : population) {
			if(element.compare(cell))
				element.increaseAppear();
		}
	}
	}

	/**
	 * assign the frequency to each element that belong to the population, relation frequency
	 */
	public void getFrequency() {
      for (ElementOfThePopulation element : elements) {
		element.calculateFrequency(population.size());
	}
	}
    
	/**
	 * arrange the elements by the frequency of apparition in the population whit the {@link Collections}
	 */
	public void arrange(){
		Collections.sort(elements);
	}
	/**
	 * this method calls all the necessary methods that are needed to suggest the more significant indicators 	
	 * @return
	 */
	public ArrayList<ElementOfThePopulation> getSuggestedIndicators(){
		getPopulationForCell();
		fillElements();
		seekOccurrences();
		getFrequency();
		arrange();
		ArrayList<ElementOfThePopulation> toReturn = new ArrayList<ElementOfThePopulation>();
		double cont = 0.0;
		for (int i = 0; i < elements.size(); i++) {
			if(cont < cutPoint){
				toReturn.add(elements.get(i));
				cont += elements.get(i).getFrequencyForThisConnection();
				}
			else
				break;
		}
		return toReturn;
	}

	
	
	/**
	 * 
	 * @return the current cut point for the class
	 */
	public double getCutPoint() {
		return cutPoint;
	}

	/**
	 * set the actually cut point
	 * 
	 * @param cutPoint
	 */
	public void setCutPoint(double cutPoint) {
		this.cutPoint = cutPoint;
	}

	/**
	 * 
	 * @return all the possibles nodes to suggest, can be repeated nodes
	 */
	public ArrayList<MyProblemCustomCell> getPopulation() {
		return population;
	}

	/**
	 * set the population
	 * 
	 * @param population
	 */
	public void setPopulation(ArrayList<MyProblemCustomCell> population) {
		this.population = population;
	}

}
