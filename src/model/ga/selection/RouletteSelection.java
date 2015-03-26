package model.ga.selection;

import java.util.ArrayList;
import java.util.Iterator;


import model.ga.IGeneticAlgorithmStep;
import model.ga.internal.Fitness;
import model.ga.internal.Single;
import system.rdf.dataBase.Problem;

public class RouletteSelection implements IGeneticAlgorithmStep {

	private ArrayList<Problem> problemArrayToSe = null;
	
	/**
	 *This method make the selection by Roulette Accumulating the sum of the
	 * fitness of every problem Generating a random number between 0 and the sum
	 * accumulate of them fitness of the population The population accumulating
	 * the fitness again goes over if the accumulated sum is major than or equal
	 * the number generated is selected the problem than this at the position
	 * @param problemArray Array list of problems to make a selection
	 * @return Array the problem with best fitness
	 */	
	/*public ArrayList<Problem> run(ArrayList<Problem> problemArray) {
		Fitness fitness = new Fitness();
		problemArrayToSe = new ArrayList<Problem>();
		Problem SelectedProblem = new Problem();
		double valueFitness = 0;
		double accumulatedFitness = 0;
		double totalFitness = fitness.calculateTotalFittnes(problemArray);
		System.out.println("Fitness total"+totalFitness);
		double ConfineTitness = Math.random() % totalFitness;
		System.out.println("Numero generado"+ConfineTitness);
		for (int i = 0; i < problemArray.size(); ++i) {
			valueFitness = fitness.fitnesByAccuFreqNorm(problemArray.get(i).getTriples());
			accumulatedFitness += valueFitness;		
			System.out.println("Fitness Acumulada");
			System.out.println(accumulatedFitness);
			System.out.println("Numero "+ConfineTitness);
			if (accumulatedFitness >ConfineTitness ||accumulatedFitness ==ConfineTitness ) {
				System.out.println("Entro...");
				SelectedProblem = problemArray.get(i);
				problemArrayToSe.add(SelectedProblem);
			}
		}
		System.out.println("Fitness Acumulada");
		System.out.println(accumulatedFitness);
		System.out.println("Contidad de problemas selecionados:"
				+ problemArrayToSe.size());
		return problemArrayToSe;
	}*/
	public ArrayList<Problem> run(ArrayList<Problem> problemArray) {
		Fitness fitness = new Fitness();
		problemArrayToSe = new ArrayList<Problem>();
		Problem SelectedProblem = new Problem();
		
		double accumulatedFitness = 0;
		double totalFitness = 0;
		ArrayList<Single> problemArra= fitness.getPopulation(problemArray) ;
		for (Iterator iterator = problemArra.iterator(); iterator.hasNext();) {
			Single Single = (Single) iterator.next();
			totalFitness+= Single.getFitnes()[0];
		}
	
		System.out.println("Fitness total"+" "+totalFitness);
		double ConfineTitness = Math.random() % totalFitness;
		System.out.println("Numero generado"+ConfineTitness);
		for (int i = 0; i < problemArra.size(); ++i) {
			accumulatedFitness += problemArra.get(i).getFitnes()[0];		
			System.out.println("Fitness Acumulada");
			System.out.println(accumulatedFitness);
			System.out.println("Numero "+ConfineTitness);
			if (accumulatedFitness >ConfineTitness ||accumulatedFitness ==ConfineTitness ) {
				SelectedProblem = problemArra.get(i).getP();
				problemArrayToSe.add(SelectedProblem);
			}
		}
	
		System.out.println("Contidad de problemas selecionados:"
				+ problemArrayToSe.size());
		return problemArrayToSe;
	}	
	public Object getAssociatedData() {
		return null;
	}
	
}
