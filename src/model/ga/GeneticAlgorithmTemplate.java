package model.ga;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFTriple;
    /**
     *Building a scale prototype of genetic algorithm 
     *athrough the IGeneticAlgorithmStep interface 
     * @author Ruben
     */
public abstract class GeneticAlgorithmTemplate {

	public abstract IGeneticAlgorithmStep getSelection();
	public abstract IGeneticAlgorithmStep getCrossing();
	public abstract IGeneticAlgorithmStep getMutation();
	public abstract IGeneticAlgorithmStep getValidation();
	public abstract IGeneticAlgorithmStep getSeveChiles();
		
	public ArrayList<Problem> runAlgorithm(ArrayList<Problem> problems) throws SQLException, FileNotFoundException{
		
		ArrayList<Problem> result;
		ArrayList<Problem> resultMutation;
		
		result = getSelection().run(problems);
		result = getCrossing().run(result);
		resultMutation= getMutation().run(result);

		for (int i = 0; i < resultMutation.size(); i++) {
		  result.add(resultMutation.get(i));	
		  }
		result = getValidation().run(result);
		result = getSeveChiles().run(result);
	  
		return result;
	}
	
}
