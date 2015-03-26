package model.ga.internal;

/**
 * @author Ruben Tauri�an Osorio
 * Class Name Fitness.java
 * Determining the show of fineness of the formulations
 * Controlling quantity of defective genes and validating 
 * the adaptability of the formulations 
 * Date 06/02/2011  
 */
import java.util.ArrayList;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.dataBase.RDFTriple;
import system.rdf.graph.MyProblemCustomCell;

public class Fitness {
	// Total sum accumulated of the population
	double totaFittness;
	boolean fitnesValid = true;
	boolean validatedFittnes = true;
	int countDefectiveGenes;
	RDFSerializator serializator;

	public Fitness() {
		super();
		serializator = new RDFSerializator();
	}

	/**
	 *This method, construct the fitness of a formulation achieving the sum
	 * accumulated of the weights of his relations and dividing it between the
	 * quantity relational Also he calculate off the quantity of defective genes
	 * of the formulation (those ones than with total relative frequency >= 0,0
	 * � <=0,02586206896551724)
	 * 
	 * @return a double, that it means problem fitness
	 */
	public double fitnesByAccuFreqNorm(ArrayList<RDFTriple> triplesToGetFitnes) {
		double valuefrecuency = 0;
		double accumulafrecuency = 0;
		double alfaDefective = 0.02586206896551724;
		int countRelations = triplesToGetFitnes.size();
		RDFTriple tripleTemp = new RDFTriple();
		for (int i = 0; i < triplesToGetFitnes.size(); i++) {
			tripleTemp = triplesToGetFitnes.get(i);
			valuefrecuency = serializator.calculateTotalFrequencyForProblems(
					new MyProblemCustomCell(tripleTemp.getSubject().getLabel(),
							tripleTemp.getSubjetProcess()),
					new MyProblemCustomCell(tripleTemp.getObject().getLabel(),
							tripleTemp.getObjectProcess()));
			System.out.println("frecuencia de la tripla" + " " + i + " "
					+ valuefrecuency);
			accumulafrecuency += valuefrecuency;

			if (valuefrecuency <= alfaDefective || valuefrecuency == 0.0) {
				countDefectiveGenes++;
			}
		}
		double nomalization = accumulafrecuency /* / countRelations */;
		// UMBRAL
		if (nomalization >= 0.0 && nomalization <= alfaDefective) {
			fitnesValid = false;
		}

		System.out.println("Fitnes del problema Actual");
		System.out.println(nomalization);

		return nomalization;
	}

	public ArrayList<Single> getPopulation(ArrayList<Problem> population) {
		Single p = null;
		ArrayList<Single> result = new ArrayList<Single>();
		double[] fitness = new double[2];
		int countProblem = population.size();
		for (int i = 0; i < countProblem; i++) {
			fitness[0] = population.get(i).getFitnes();
			fitness[1] = population.get(i).countDefectiveGenes;
			p = new Single(population.get(i), fitness);
			result.add(p);
		}
		return result;
	}

	public double calculateTotalFittnes(ArrayList<Problem> population) {
		double accumFreqTotal = 0.0;
		int countProblem = population.size();
		for (int i = 0; i < countProblem; i++) {
			accumFreqTotal += fitnesByAccuFreqNorm(population.get(i)
					.getTriples());
		}
		return accumFreqTotal;
	}

	/**
	 *This method realizes the analysis to Array of problems Obtaining the
	 * problem with triples more halting
	 * 
	 * @param a
	 *            Array of problems
	 * @return the problem with more quantiyt frequency different 0.0 of tiples
	 */
	public Problem getMoreHaltingDF(ArrayList<Problem> sector) {
		Problem poolTemp = null;
		Problem poolWinner = null;
		double maxFrecuency = 0;
		for (int i = 0; i < sector.size(); i++) {
			poolTemp = (Problem) sector.get(i);
			if (getDefectiveGenes(poolTemp) > maxFrecuency)
				maxFrecuency = getDefectiveGenes(poolTemp);
			poolWinner = (Problem) sector.get(i);
		}

		return poolWinner;
	}

	/**
	 *This method realizes the analysis to a graph Obtaining quantity of
	 * triples with frequency different 0.0
	 * 
	 * @param a
	 *            Problem
	 * @return the quantity of genes not defective like a integer
	 */
	public int getDefectiveGenes(Problem problem) {
		double valuefrecuency = 0;
		int countvaluefrecuency = 0;
		ArrayList<RDFTriple> triples = problem.getTriples();
		RDFTriple tripleTemp = new RDFTriple();
		for (int j = 0; j < triples.size(); j++) {
			tripleTemp = triples.get(j);
			valuefrecuency = serializator.calculateTotalFrequencyForProblems(
					new MyProblemCustomCell(tripleTemp.getSubject().getLabel(),
							tripleTemp.getSubjetProcess()),
					new MyProblemCustomCell(tripleTemp.getObject().getLabel(),
							tripleTemp.getObjectProcess()));
			System.out.println("Frecuencia de la tripla" + j);
			System.out.println(valuefrecuency);
			if (valuefrecuency != 0) {
				countvaluefrecuency++;
			}
		}
		System.out.println("Cantida de triplas con frecuencia =" + 0);
		System.out.println(countvaluefrecuency);
		return countvaluefrecuency;

	}

	/**
	 *This method realizes the analysis to Array of problems Obtaining the
	 * problem with triples more halting
	 * 
	 * @param a
	 *            Array of problems
	 * @return the problem with more accumulated frecuency
	 */
	public Problem getMoreHaltingAF(ArrayList<Problem> sector) {

		Problem poolWinner = null;
		double maxFrecuency = 0;
		ArrayList<RDFTriple> triplesTemp = new ArrayList<RDFTriple>();
		for (int i = 0; i < sector.size(); i++) {
			triplesTemp = sector.get(i).getTriples();
			System.out.println(sector.get(i));
			if (accumulatedFrequency(triplesTemp) > maxFrecuency) {
				maxFrecuency = accumulatedFrequency(triplesTemp);
				poolWinner = (Problem) sector.get(i);
			}
		}
		return poolWinner;
	}

	public double accumulatedFrequency(ArrayList<RDFTriple> triplesToSe) {
		double valuefrecuency = 0;
		double accumulatedfrecuency = 0;
		int countProblemCurr = 0;
		RDFTriple tripleTemp = new RDFTriple();
		for (int i = 0; i < triplesToSe.size(); i++) {
			tripleTemp = triplesToSe.get(i);
			valuefrecuency = serializator.calculateTotalFrequencyForProblems(
					new MyProblemCustomCell(tripleTemp.getSubject().getLabel(),
							tripleTemp.getSubjetProcess()),
					new MyProblemCustomCell(tripleTemp.getObject().getLabel(),
							tripleTemp.getObjectProcess()));
			if (valuefrecuency == 0) {
				countProblemCurr++;
			} else {
				accumulatedfrecuency += valuefrecuency;
			}
		}
		if (countProblemCurr != 0) {
			if (Math.log(countProblemCurr) > 1) {
				validatedFittnes = false;
			}
		}

		return valuefrecuency;
	}

	public Problem getMoreHaltingGenes(ArrayList<Problem> sector) {
		Problem poolWinner = null;
		double maxFrecuency = 0;
		ArrayList<RDFTriple> triplesTemp = new ArrayList<RDFTriple>();
		for (int i = 0; i < sector.size(); i++) {
			triplesTemp = sector.get(i).getTriples();
			System.out.println(sector.get(i));
			if (fitnesByaccumulatedFrequency(triplesTemp) > maxFrecuency) {
				maxFrecuency = fitnesByaccumulatedFrequency(triplesTemp);
				poolWinner = (Problem) sector.get(i);
			}
		}
		return poolWinner;
	}

	// made by tauri�an
	public boolean defectiveSimilar(Problem problem1, Problem problem2) {
		ArrayList<RDFTriple> defective1 = new ArrayList<RDFTriple>();
		ArrayList<RDFTriple> defective2 = new ArrayList<RDFTriple>();
		if (getDefectiveGenes(problem1) == getDefectiveGenes(problem2)
				&& getDefectiveGenes(problem1) <= 4) {
			defective1 = problem1.getTriples();
			defective2 = problem2.getTriples();
			problem1.setTriples(defective1);
			problem2.setTriples(defective2);
		}
		// Always return true
		return true;
	}

	/**
	 *This method realizes the analysis to Array of problems Obtaining the
	 * problem with triples more halting
	 * 
	 * @param a
	 *            Array of problems
	 * @return the problem with more quantity of triples different 0.0
	 */
	/*
	 * public Problem getMoreHalting(ArrayList<Problem> sector){ Problem
	 * poolTemp=null; Problem poolWinner=null; double maxFrecuency=0; for (int i
	 * = 0; i < sector.size(); i++){ poolTemp = (Problem)sector.get(i);
	 * System.out.println(poolTemp);
	 * if(getDefectiveGenes(poolTemp)>maxFrecuency){ maxFrecuency
	 * =getDefectiveGenes(poolTemp); poolWinner = (Problem)sector.get(i); } }
	 * return poolWinner; }
	 */
	/**
	 *This method realizes the analysis to a graph Obtaining quantity of
	 * triples with frequency different 0.0
	 * 
	 * @param a
	 *            Problem
	 * @return the quantity of genes not defective like a integer
	 */
	/*
	 * public ArrayList<Problem> getProbBestGenes(ArrayList<Problem> problem){
	 * double valuefrecuency = 0; int maxDefective = 0; int minAceptedDefective
	 * = 5; double alfaDefective = 0.02586206896551724; problemBestGe = new
	 * ArrayList<Problem>(); RDFTriple tripleTemp = new RDFTriple() ; for (int j
	 * = 0; j <problem.size(); j++) { ArrayList<RDFTriple>triples =
	 * problem.get(j).getTriples(); for (int k = 0; k <triples.size(); k++) {
	 * tripleTemp = triples.get(k); valuefrecuency =
	 * RDFSerializator.calculateTotalFrecuency(
	 * tripleTemp.getSubject().getLabel(), tripleTemp.getObject().getLabel());
	 * System.out.println("Frecuencia de la tripla"+" "+j);
	 * System.out.println(valuefrecuency); if(valuefrecuency<=alfaDefective){
	 * maxDefective++; } } if(maxDefective<=minAceptedDefective)
	 * problemBestGe.add(problem.get(j)); }
	 * System.out.println("Cantida de genes defecuosos <=0.02586206896551724");
	 * System.out.println(maxDefective); return problemBestGe;
	 * 
	 * }
	 */

	public double fitnesByaccumulatedFrequency(ArrayList<RDFTriple> triplesToSe) {
		double valuefrecuency = 0;
		int countProblemCurr = 0;
		RDFTriple tripleTemp = new RDFTriple();
		for (int i = 0; i < triplesToSe.size(); i++) {
			tripleTemp = triplesToSe.get(i);
			valuefrecuency += serializator.calculateTotalFrequencyForProblems(
					new MyProblemCustomCell(tripleTemp.getSubject().getLabel(),
							tripleTemp.getSubjetProcess()),
					new MyProblemCustomCell(tripleTemp.getObject().getLabel(),
							tripleTemp.getObjectProcess()));
			if (valuefrecuency == 0) {
				countProblemCurr++;
			}
		}
		if (countProblemCurr != 0) {
			if (Math.log(countProblemCurr) > 1) {
				validatedFittnes = false;
			}
		}
		return valuefrecuency;
	}

}
