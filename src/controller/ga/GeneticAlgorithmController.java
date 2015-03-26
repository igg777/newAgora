package controller.ga;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import model.ga.*;
public class GeneticAlgorithmController {

	private static GeneticAlgorithmController singleton = null;
	
	private ArrayList<GeneticAlgorithmTemplate> algorithms = null;
	private ArrayList<String> algorithmsNames = null;
	
	private GeneticAlgorithmController() {
		algorithms = new ArrayList<GeneticAlgorithmTemplate>();
		
		algorithms.add(new AG3Algorithm());
		algorithms.add(new AG4Algorithm());
		
		algorithmsNames = new ArrayList<String>();
		for (GeneticAlgorithmTemplate alg : algorithms) {
			algorithmsNames.add(alg.toString());
		}
	}
	
	public static GeneticAlgorithmController instance() {
		if (singleton == null)
			singleton = new GeneticAlgorithmController();
		return singleton;
	}
	
	public GeneticAlgorithmTemplate getAlgorithm(String algorithmName) {
		for (GeneticAlgorithmTemplate alg : algorithms) {
			if (alg.toString().equals(algorithmName))
				return alg;
		}
		return null;
	}
	
	public ArrayList<GeneticAlgorithmTemplate> getAlgorithms() {
		return algorithms;
	}
	
	public ArrayList<String> getAlgorithmsNames() {
		return algorithmsNames;
	}
	
	public GeneticAlgorithmTemplate runAlgorithm(String algorithmName)throws SQLException, FileNotFoundException {
		GeneticAlgorithmTemplate alg = getAlgorithm(algorithmName);
		alg.runAlgorithm(ProblemManagerController.instance().getProblems());
		return alg;
	}
}
