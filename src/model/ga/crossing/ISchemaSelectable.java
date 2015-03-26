package model.ga.crossing;

import java.util.ArrayList;

import model.ga.internal.Couple;
import system.rdf.dataBase.Problem;

/**
 * Interface to implement for the processing of the defined schema
 * @author Ruben
 */
public interface ISchemaSelectable {

	public ArrayList<Couple> selectSchema(ArrayList<Problem> problems);
	
}
