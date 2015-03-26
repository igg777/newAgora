package model.ga.crossing;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import model.ga.internal.Couple;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFTriple;

    /**
     * Class to process the schema fixed unwanted effects
     * @author Ruben
     */
public class SchemaUnvaryingEffects implements ISchemaSelectable {
	/**
	 * Method to determine if two problems have unwanted effects similar 
	 * , implementing three comparisons them that should happen to successful:
	 * 1 The unwanted effects of both problems should have the same levels,
	 * 2 both problems should have same the number of unwanted effects, 
	 * 3 At the very least an indicator should have both formulations 
	 *  in common forming part it of unwanted effects. 
	 * @param tow problems to analyze.
	 * 
	 * @return True if three comparisons happen to successful else false.
	 * 
	 */

	private boolean effectsSimilar(Problem problem1, Problem problem2) {
		System.out.println("Procesando esquema de "+" "+problem1.getName());
		System.out.println(problem2.getName());
		// flag para saber si las condiciones se cumplen para ser similares
		// entre si
		boolean flag = true;
		// contador para contar la cantidad de efectos no deseados iguales entre
		// los dos problemas.
		int cont = 0;
		ArrayList<RDFTriple> effects1 = new ArrayList<RDFTriple>();
		ArrayList<RDFTriple> effects2 = new ArrayList<RDFTriple>();
		// aqui mando a buscar los efectos no deseados de cada problema para
		// gurdarlas
		effects1 = problem1.getUnwantedEffects();
		effects2 = problem2.getUnwantedEffects();
		// guardadando los efectos no deseados
		problem1.setEffects(effects1);
		problem2.setEffects(effects2);
		// si los efectos no deseados difieren en cantidad o los niveles no son
		// iguales entonces marco la vandera falsa y no sigo
		if (effects1.size() != effects2.size()
				|| problem1.getHiLevel() != problem2.getHiLevel()) {
			flag = false;
		}
		// si los efectos no deseados son iguales y los niveles tambien entonces
		// compruebo que los efectos no deseados sean los mismos
		else {
			// recorro los efectos no deseados comprobando los object si son
			// iguales cuento.
			for (int i = 0; i < effects1.size(); i++) {
				for (int j = 0; j < effects2.size(); j++) {
					if (effects1.get(i).getObject().getLabel().equals(
							effects2.get(j).getObject().getLabel())) {
						cont++;
					}
				}
			}
			// despues de recorridas los efectos no deseados compruebo si la
			// cantidad de efectos no deseados iguales concuerda con la cantidad
			// de los efectos no deseados totales
			if (cont < 1) {
			System.out.println("Contador de onject"+" "+cont);	
				flag = false;
			}
		else flag = true;
		}
        
		// devuelvo true si los niveles y los efectos no deseados son iguales
		return flag;
	}
	/**
	 * Method that is assigned of classification of problems with similar
	 * unwanted effect
	 * @param problems Array list of problems to be analyze
	 * @return Array list of couple of problems with the same unwanted effects.
	 * 
	 */
	public ArrayList<Couple> selectSchema(ArrayList<Problem> problems) {
		Couple couple;
		boolean selected = false;
		ArrayList<Couple> problemArrayToSquema = new ArrayList<Couple>();
		for (int i = 0; i < problems.size() - 1; i++) {
			selected = false;
			for (int j = i + 1; j < problems.size(); j++) {
				if ((effectsSimilar(problems.get(i), problems.get(j)) == true)
						&& problems.size() < 500 && commonProcessInterface(problems.get(i), problems.get(j))) {
					couple = new Couple(problems.get(i), problems.get(j));
					problemArrayToSquema.add(couple);
					selected = true;
				}

			}
			// si el problema problemArray(i) no fue seleccionado en ninguna
			// pareja guardada lo elimino
			if (selected == false) {
				problems.remove(i);
			}
		}
	
			
		
		System.out
				.println("Cantidad de problemas couples con un esquema similar(efectos no deseados):");
		System.out.println(problemArrayToSquema.size());
		return problemArrayToSquema;
	}
	public boolean commonProcessInterface(Problem A,Problem B){
		Hashtable<String, String> commonProcessInterface = new Hashtable<String, String>();
		commonProcessInterface.put("P.Plan","S.Source");
		commonProcessInterface.put("S.Source","M.Make");
		commonProcessInterface.put("M.Make","D.Deliver");
		commonProcessInterface.put("D.Deliver","R.Return");
		commonProcessInterface.put("R.Return","P.Plan");
		String interfaceA = A.classifyProblem();
		String interfaceB = B.classifyProblem();
		boolean commonInterface= false;
		if(interfaceB.equals(interfaceA)){
			commonInterface = true;	
		}else{
			Enumeration enum1 = commonProcessInterface.keys();
			while (enum1.hasMoreElements()) {
				String key = (String) enum1.nextElement();
				if (key.equals(interfaceA)
						&& commonProcessInterface.get(key).equals(
								interfaceB)) {
					commonInterface = true;
				}
			}
		}


		return commonInterface;



	}
}
