package model.ga.crossing;

import java.util.ArrayList;
import java.util.Random;

import model.ga.IGeneticAlgorithmStep;
import model.ga.internal.Couple;
import system.rdf.dataBase.Problem;

/**
 * Class that implement the interface of step of the algorithms
 * to accomplish the cross over among two couples  
 * with similar unwanted effects
 * @author Ruben
 */
public class CrossingWithotUnwantedEffects implements IGeneticAlgorithmStep {

	//probability to cross over
	static final double CROSSPROBABILITY = 0.75;

	/**
	 * This method accomplishes the cross over among two couples
	 * with similar unwanted effects 
	 * if the random generated number is bigger or equal to probability to cross over 
	 * define like 0.75 
	 * @param problems Array list of problems 
	 * @return Array the new problem with same unwanted effects to the parents 
	 */
	public ArrayList<Problem> run(ArrayList<Problem> problems) {

		ArrayList<Couple> problemsAncestor = new SchemaUnvaryingEffects().selectSchema(problems);
		int cantCouple = problemsAncestor.size();
		ArrayList<Problem> childsTemp = new ArrayList<Problem>();
		ArrayList<Problem> childs = new ArrayList<Problem>();
		double probability = Math.random() % 1;

		for (int i = 0; i < cantCouple; i++) {
			//if(probability <= CROSSPROBABILITY)
			childsTemp = problemsAncestor.get(i).crossWithoutEffects();
			System.out.println("Pareja " + i);
			for (int j = 0; j < childsTemp.size(); j++) {
				childs.add(childsTemp.get(j));
			}
		}
		System.out.println(childs.size() + " cantidad de hijos");
		return childs;
	}

	public Object getAssociatedData() {
		return null;
	}	

}
