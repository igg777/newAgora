package system.rdf.utils;

import java.util.ArrayList;

import model.ga.internal.Indicators;

/**
 * This Class is built with the purpose of provide some needful String functions
 * @author Irlan
 *
 */
public class StringUtils {
    
    
    /**
     * This method is used to get the string after the symbol # in the ontology uri
     * for example 
     * http://www.owl-ontologies.com/
     * SCOR_Class_SubClass.owl#O.F.C.T.Stage-Finished-Product-Cycle-Time
     * Should return O.F.C.T.Stage-Finished-Product-Cycle-Time
     * @param complete
     * @return
     */
    public static String getAfterUri(String complete){
	return complete.substring(complete.indexOf("#") + 1, complete.length());
    }
    
    /**
     * Eliminate duplicate elements in an String Array
     * @param toEliminate
     * @return
     */
    public static ArrayList<Indicators> eliminateDuplicate(ArrayList<Indicators>toEliminate){
	ArrayList<Indicators>toReturn = new ArrayList<Indicators>();

	for (int i = 0; i < toEliminate.size(); i++) {
	    if(!toReturn.contains(toEliminate.get(i)))
		toReturn.add(toEliminate.get(i));
	}
	return toReturn;
    }
  
    /**
     * Method to change the elements of an array indicators array for example an 
     * array with the elements "one","two","three" should return "two","three","one"
     * @param a 
     * @return ArrayList<Indicators>
     */
    public static ArrayList<Indicators>changeIndElements(ArrayList<Indicators> a){
	String temp1 = a.get(0).getName();
	for (int i = 0; i < a.size(); i++) {
	    if(i + 1 < a.size()){
		a.get(i).setName(a.get(i + 1).getName());
	    }else a.get(a.size() - 1).setName(temp1);
	}
	return a;
    }
}
