package model.ga;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import system.rdf.dataBase.Problem;

/**Interface to take steps toward the 
 * interrelationships of the algorithm.
 * @author Ruben
 */
public interface IGeneticAlgorithmStep {

    public ArrayList<Problem> run(ArrayList<Problem> problems)throws SQLException, FileNotFoundException ;
    public Object getAssociatedData();

}
