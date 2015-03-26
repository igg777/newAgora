package system.rename.cutPoint;

import java.util.Collection;

import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.MyProblemCustomCell;
import system.rename.Tools;

/**
 * this class handle the elements of the population, store one element with
 * its frequencyForThisConnection and the times that it appear in the population
 * 
 * @author Leo
 * 
 */
public class ElementOfThePopulation implements Comparable<ElementOfThePopulation>{
	
	//the possible node to suggest
	private MyProblemCustomCell possibleNode;
    
	//Quantity of apparitions of this relation in the entire population
	private int appearInPopulation;
	
	//for this only matters the triples in DB that have this subject
	private double frequencyForThisConnection;
	
	private Tools tools;
	//for this frequency matters all the triples in DB
	private double globalFrequency;
	private RDFSerializator serializator = new RDFSerializator();
	
    /**
     * Constructor
     * @param subject
     * @param possibleNode
     */
	public ElementOfThePopulation(MyProblemCustomCell subject, MyProblemCustomCell possibleNode) {
		super();
		this.possibleNode = possibleNode;
		appearInPopulation = 0;
		frequencyForThisConnection = 0;
		tools = new Tools();
		globalFrequency = serializator.calculateTotalFrequencyForProblems(subject, possibleNode);
	}

	/**
	 * get the possible node to suggest
	 * @return
	 */
	public MyProblemCustomCell getPossibleNode() {
		return possibleNode;
	}

	/**
	 * set the possible node to suggest
	 * @param possibleNode
	 */
	public void setPossibleNode(MyProblemCustomCell possibleNode) {
		this.possibleNode = possibleNode;
	}

	/**
	 * 
	 * @return how many times appear this relation in the population
	 */
	public int getAppearInPopulation() {
		return appearInPopulation;
	}

	/**
	 * increase the times that this relation appear in the population
	 */
	public void increaseAppear() {
		appearInPopulation = appearInPopulation += 1;
	}
	

	/**
	 * 
	 * @return the frequency for this relation in the population
	 */
	public double getFrequencyForThisConnection() {
		return frequencyForThisConnection;
	}

	/**
	 * set the the frequency for this relation in the population
	 * @param frequencyForThisConnection
	 */
	public void setFrequencyForThisConnection(double frequencyForThisConnection) {
		this.frequencyForThisConnection = frequencyForThisConnection;
	}
	
	

	/**
	 * 
	 * @return the frequency for this relation in the DB's triples
	 */
	public double getGlovalFrequency() {
		return globalFrequency;
	}

	/**
	 * set the frequency for this relation in the DB's triples
	 * @param glovalFrequency
	 */
	public void setGlovalFrequency(double glovalFrequency) {
		this.globalFrequency = glovalFrequency;
	}

	/**
	 * compare the possibleNode that belong to this class whit other
	 * 
	 * @param toCompare
	 * @return true if the cells are the same one
	 * 
	 */
	public boolean compare(MyProblemCustomCell toCompare) {
		if (toCompare.toString().equals(this.possibleNode.toString())
				&& tools.concatenateProcess(possibleNode.getProcess()).equals(
						tools.concatenateProcess(toCompare.getProcess())))
			return true;
		return false;
	}

	/**
	 * calculate the frequency of the relation in the population
	 * @param sizeOfPopulation
	 */
	public void calculateFrequency(float sizeOfPopulation) {
		frequencyForThisConnection = appearInPopulation / sizeOfPopulation;
	}
	
	/**
	 * this method is to arrange the elements with the {@link Collection} 
	 * @param forCompare
	 * @return 
	 */
	public int compareTo(ElementOfThePopulation forCompare){
		if(forCompare.getFrequencyForThisConnection() > frequencyForThisConnection)
			return 1;
		else if(forCompare.getFrequencyForThisConnection() < frequencyForThisConnection)
			return -1;
		return 0;
	}

}