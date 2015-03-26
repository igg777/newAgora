package model.ga.validation;

import java.util.ArrayList;

import model.ga.IGeneticAlgorithmStep;
import system.rdf.dataBase.Problem;

public class ValidationType1 implements IGeneticAlgorithmStep {

	private ValidationData data = null;
	
	public ArrayList<Problem> run(ArrayList<Problem> problems) {
		data = new ValidationData();		
		
		ArrayList<Problem> newproblemsValidated = new ArrayList<Problem>();
		data.setGeneratedChildCount(problems.size());
		System.out.println(problems.size()
				+ " cantidad de hijos antes de comprobar validez");
		// eliminando los hijos obtenidos que posean ciclos y tengan subgrafos
		for (int i = 0; i < problems.size(); i++) {
			if (problems.get(i).haveCicles() == true
				|| problems.get(i).checkSubGraph() == true) {
				problems.remove(i);
			}else{
			newproblemsValidated.add(problems.get(i));		
			}
		}
		
		
		data.setValidChildCount(newproblemsValidated.size());
		
		System.out.println(newproblemsValidated.size()
				+ " hijos validos generados ");
		return newproblemsValidated;
	}

	public Object getAssociatedData() {
		return data;
	}
		
}
