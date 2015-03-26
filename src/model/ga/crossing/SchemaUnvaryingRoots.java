package model.ga.crossing;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import model.ga.internal.Couple;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFTriple;

/**
 * Class to process the schema fixed root causes
 * @author Ruben
 *
 */
public class SchemaUnvaryingRoots implements ISchemaSelectable {
	/**
	 * Method to determine if two problems have root causes similar 
	 * , implementing three comparisons them that should happen to successful:
	 * 1 The root causes of both problems should have the same levels,
	 * 2 both problems should have same the number of roots causes, 
	 * 3 At the very least an indicator should have both formulations
	 * in common forming part it of roots cause. 
	 * @param tow problems to analyze.
	 * 
	 * @return True if three comparisons happen to successful else false.
	 * 
	 */

	public boolean rootSimilar(Problem problem1, Problem problem2) {
		System.out.println("Procesando esquema de "+" "+problem1.getName());
		System.out.println(problem2.getName());
		// flag para saber si las condiciones se cumplen para ser similares
		// entre si
		boolean flag = true;
		// contador para contar la cantidad de raices iguales entre los dos
		// problemas.
		int cont = 0;
		ArrayList<RDFTriple> roots1 = new ArrayList<RDFTriple>();
		ArrayList<RDFTriple> roots2 = new ArrayList<RDFTriple>();
		// aqui mando a buscar las casusas raices de cada problema para
		// gurdarlas
		roots1 = problem1.getRootCauses();
		roots2 = problem2.getRootCauses();
		// guardadando las causas raices para no volver a hallarlas
		problem1.setRoots(roots1);
		problem2.setRoots(roots2);
		// si las causas raices difieren en cantidad o los niveles no son
		// iguales entonces marco la vandera falsa y no sigo
		if (roots1.size() != roots2.size()
				|| problem1.getHiLevel1() != problem2.getHiLevel1()) {
			flag = false;
		}
		// si las causas raices son iguales y los niveles tambien entonces
		// compruebo que las raices sean las mismas
		else {
			// recorro las causas raices comprobando los subjects si son iguales
			// cuento.
			for (int i = 0; i < roots1.size(); i++) {
				for (int j = 0; j < roots2.size(); j++) {
					if (roots1.get(i).getSubject().getLabel().equals(
							roots2.get(j).getSubject().getLabel())) {
						cont++;
					}
				}
			}
			// despues de recorridas las causas raices compruebo si la cantidad
			// de raices iguales concuerda con la cantidad de raices totales
			if (cont < 1) {
				System.out.println("Contador de onject"+" "+cont);	
				flag = false;
			}
			else flag = true;
		}

		// devuelvo true si los niveles y las causas raies son iguales
		return flag;
	}

	/**
	 * Method that is assigned of classification of problems with similar
	 * root causes 
	 * @param problems Array list of problems to be analyze
	 * @return Array list of couple of problems with the same root causes .
	 * 
	 */

	public ArrayList<Couple> selectSchema(ArrayList<Problem> problems) {
		Couple couple;
		boolean selected = false;
		ArrayList<Couple> problemArrayToSquema = new ArrayList<Couple>();
		for (int i = 0; i < problems.size() - 1; i++) {
			selected = false;
			for (int j = i + 1; j < problems.size(); j++) {
				if ((this.rootSimilar(problems.get(i), problems
						.get(j)) == true)
						&& (rootSimilar(problems.get(i), problems.get(j)) == true)
						&& problems.size() < 500 && commonProcessInterface(problems.get(i),problems.get(j))) {
					couple = new Couple(problems.get(i), problems.get(j));
					problemArrayToSquema.add(couple);
					selected = true;
				}

			}

			if (selected == false) {
				problems.remove(i);
			}
		}
		System.out.println("Cantidad de problemas couples con un esquema similar(causas raices):");
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
