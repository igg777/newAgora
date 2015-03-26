/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model.ga.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import model.ga.internal.Indicators;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFObject;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.dataBase.RDFTriple;
import system.rdf.dataBase.Subject;
import system.rdf.graph.MyProblemCustomCell;
import system.rename.Tools;

/**
 * 
 * @author Legolas Class Name Couple.java Loads the decisional problems,Validate
 *         the childs and realize the crossing Date 19/10/2010
 */

public class Couple {
	private Problem problem1;
	private Problem problem2;
	private Problem child;
	private ArrayList<Problem> childs;
	private Random random;
	private ArrayList<RDFTriple> problem1Roots;
	private ArrayList<RDFTriple> problem2Roots;
	private RDFTriple motherTriple;
	private RDFTriple sonTriple;
	private ArrayList<RDFTriple> childTriples;
	/* temporary variable in order to store the triple of the previous level */
	ArrayList<RDFTriple> lastLevel;
	ArrayList<RDFTriple> firstevel;
	private boolean root;
	private boolean effect;
	private RDFSerializator serializator;

	/**
	 * The main variables are initialized
	 * 
	 * @params problemA,problemB
	 */
	public Couple(Problem problemA, Problem problemB) {
		problem1 = problemA;
		problem2 = problemB;
		random = new Random();
		motherTriple = new RDFTriple();
		root = true;
		problem1Roots = new ArrayList<RDFTriple>();
		problem2Roots = new ArrayList<RDFTriple>();
		problem1Roots = problem1.getRootsStored();
		problem2Roots = problem2.getRootsStored();
		childTriples = new ArrayList<RDFTriple>();
		lastLevel = new ArrayList<RDFTriple>();
		childs = new ArrayList<Problem>();
		serializator = new RDFSerializator();
	}

	/**
	 * @return ArrayList<Problem> It returns an arrangement containing the two
	 *         initial problems.
	 */
	public ArrayList<Problem> getProblems() {
		ArrayList<Problem> name = new ArrayList<Problem>();
		name.add(problem1);
		name.add(problem2);
		return name;
	}

	// Made by tauri�an
	public Problem getProblemFatherA() {
		return this.problem1;
	}

	// Made by tauri�an
	public Problem getProblemFatherB() {
		return this.problem2;
	}

	public boolean fitnessFunction() {
		boolean flag = true;
		int cantHijos = childs.size();

		RDFTriple tripleTem = new RDFTriple();
		double frecTem = 0;

		ArrayList<RDFTriple> triplasHijo = childs.get(cantHijos - 1)
				.getTriples();

		int cantHijoAct = 0;
		int cantHijoAnt = 0;

		double maxFrecAct = 0;
		double maxFrecAnt = 0;

		for (int i = 0; i < triplasHijo.size(); i++) {
			tripleTem = triplasHijo.get(i);
			frecTem = serializator.calculateTotalFrequencyForProblems(
					new MyProblemCustomCell(tripleTem.getSubject().getLabel(),
							tripleTem.getSubjetProcess()),
					new MyProblemCustomCell(tripleTem.getObject().getLabel(),
							tripleTem.getObjectProcess()));
			if (frecTem == 0) {
				cantHijoAct++;
			} else {
				if (frecTem > maxFrecAct) {
					maxFrecAct = frecTem;
				}
			}
		}

		triplasHijo = childs.get(cantHijos - 2).getTriples();
		for (int i = 0; i < triplasHijo.size(); i++) {
			tripleTem = triplasHijo.get(i);

			frecTem = serializator.calculateTotalFrequencyForProblems(
					new MyProblemCustomCell(tripleTem.getSubject().getLabel(),
							tripleTem.getSubjetProcess()),
					new MyProblemCustomCell(tripleTem.getObject().getLabel(),
							tripleTem.getObjectProcess()));
			if (frecTem == 0) {
				cantHijoAnt++;
			} else {
				if (frecTem > maxFrecAnt) {
					maxFrecAnt = frecTem;
				}
			}
		}

		if (cantHijoAct != 0 && cantHijoAnt != 0) {
			if (Math.log(cantHijoAct) / Math.log(cantHijoAnt) > 1) {
				flag = false;
			}
		} else {
			if (maxFrecAct < maxFrecAnt) {
				flag = false;
			}
		}

		return flag;

	}

	/**
	 * Realize the crossing process
	 * 
	 * @return ArrayList<Problem>
	 */

	public void cross() {
		String name = " Cross Child";
		Tools tools = new Tools();
		// opteniendo el nivel de los problemas en este caso son iguales
		int level = problem1.getHiLevel1();
		System.out.println("Nivel de problema1" + " " + problem1.getName()
				+ " " + level);
		// opteniendo el nivel de los problemas en este caso son iguales
		int level2 = problem2.getHiLevel1();
		System.out.println("Nivel de problema2" + " " + problem2.getName()
				+ " " + level2);
		lastLevel = problem1.getRootsStored();
		System.out.println("Cantidad de causas " + " " + lastLevel.size());
		for (int i = 0; i < lastLevel.size(); i++) {
			System.out.println(lastLevel.get(i).getSubject().getLabel());
			MyProblemCustomCell source = new MyProblemCustomCell(lastLevel.get(
					i).getSubject().getLabel(), lastLevel.get(i)
					.getSubjetProcess());
			System.out.println("Proceso Pricipal de las causas");
			System.out.println(tools.concatenateProcess(source.getProcess()));
		}

	}

	// RESPETANDO EL ESQUEMA(CAUSAS RAICEZ )
	public ArrayList<Problem> crossWithoutRoot() {
		System.out.println("Crusando con causas rices fijas");
		// Generating a random Cross point, according to the number of
		// indicators
		Tools tools = null;
		String name = " Cross Child";
		// opteniendo el nivel de los problemas en este caso son iguales
		int level = problem1.getHiLevel1();
		System.out.println("Nivel" + " " + level);
		// build new triple
		RDFTriple triple = new RDFTriple();
		Subject subject;
		RDFObject object;
		// cantidad de indicadores de un nivel en cuestion;

		ArrayList<Indicators> indicatorsProblem1 = new ArrayList<Indicators>();
		ArrayList<Indicators> indicatorsProblem2 = new ArrayList<Indicators>();
		// Aki es el cliclo de generacion de hijos mientras se cumpla la funcion
		// de parada
		while (childs.size() < 2 || this.fitnessFunction() == true
				&& childs.size() < 10) {
			childTriples = new ArrayList<RDFTriple>();
			child = new Problem();
			// temporal para ir almacenando las triplas que se vallan agregando
			// en ese nivel;
			ArrayList<RDFTriple> currentLevel = new ArrayList<RDFTriple>();
			child.setName(name);

			// aqui obtengo las causas raices de el problema hijo
			lastLevel = problem1.getRootsStored();
			System.out.println("lastLevel" + " " + lastLevel.size());
			// cruzamiento de los indicadores de los que no son causas raices
			// padres.
			for (int i = 1; i < level + 1; i++) {
				ArrayList<Indicators> indOfLevel = new ArrayList<Indicators>();
				// si el nivel no es el primero hago root false para saber que
				// no parto ya de la raiz
				if (i > 1) {
					root = false;
					lastLevel = new ArrayList<RDFTriple>();
					if (currentLevel.size() != 0)
						lastLevel = currentLevel;
					if (problem1.getIndicatorsOfLevel(i).size() != 0)
						System.out.println("Cojio el inicador en nivel" + " "
								+ i);
					indicatorsProblem1 = problem1.getIndicatorsOfLevel(i);

					if (problem2.getIndicatorsOfLevel(i).size() != 0)
						indicatorsProblem2 = problem2.getIndicatorsOfLevel(i);

					// uniendotodos los indicadores de los dos problemas para
					// este nivel
					for (int j = 0; j < indicatorsProblem1.size(); j++) {
						indOfLevel.add(indicatorsProblem1.get(j));
					}
					for (int j = 0; j < indicatorsProblem2.size(); j++) {
						indOfLevel.add(indicatorsProblem2.get(j));
					}

					// agregando al nivel anterior las nuevas triplas y
					// preparandolo para almacenar las nuevas;
					currentLevel = new ArrayList<RDFTriple>();

					// para determinar la cantidad de veces que se cruzará que
					// es el mayor numero entre
					// cant de indicadores en el nivel pasado donde estan los
					// ind que me quede y la cantIndicadores para este nivel
					int cantCruzamientos = lastLevel.size();
					// quedandome con el maximo
					if (indOfLevel.size() > cantCruzamientos) {
						cantCruzamientos = indOfLevel.size();
					}

					// cruzando indicadores de acuerdo con la cantidad max de
					// indicadores del pasado nivel o del actual.
					for (int j = 0; j < cantCruzamientos; j++) {

						subject = new Subject();
						// temporal para almacenar el object de la nueva tripla
						object = new RDFObject();
						// buscando la tripla donde me quede si es el nivel 1 me
						// devuelve una raiz random sino me devuelve la ke tenia
						motherTriple = this.getMotherTriple();

						// asignandole el valor al subject
						subject = new Subject(motherTriple.getObject()
								.getLabel());

						// asignandole el valor al object
						int range = indOfLevel.size();
						int pos = random.nextInt(range);
						object = new RDFObject(indOfLevel.get(pos).getName());

						// creando la nueva tripla
						triple = new RDFTriple(object, null, subject);
						triple.setSubjectProcess(motherTriple
								.getObjectProcess());
						triple.setObjectProcess(indOfLevel.get(pos)
								.getProcess());
						// agregando la nueva tripla si no existe ya
						if (this.existThisTriple(triple, childTriples) == false
								&& !subject.getLabel()
										.equals(object.getLabel())) {
							System.out.println("no raices agregadas"
									+ childTriples.size());
							childTriples.add(triple);
							currentLevel.add(triple);
						}
					}
					System.out.println("tripla" + " " + childTriples.size());
				} // este es el caso del nivel de las causa raices
				else {

					// obteniendo los indicadores de un nivel dado.
					if (problem1.getIndicatorsOfLevel(i).size() != 0)
						System.out.println("Cojio el inicador en nivel" + " "
								+ i);
					indicatorsProblem1 = problem1.getIndicatorsOfLevel(i);
					// indicatorsProblem1 = getRootIndicator(problem1);
					if (problem1.getIndicatorsOfLevel(i).size() != 0)
						indicatorsProblem2 = problem2.getIndicatorsOfLevel(i);
					// indicatorsProblem2 = getRootIndicator(problem2);
					// uniendotodos los indicadores de los dos problemas para
					// este nivel
					for (int j = 0; j < indicatorsProblem1.size(); j++) {
						indOfLevel.add(indicatorsProblem1.get(j));
					}
					for (int j = 0; j < indicatorsProblem2.size(); j++) {
						indOfLevel.add(indicatorsProblem2.get(j));
					}

					// agregando al nivel anterior las nuevas triplas y
					// preparandolo para almacenar las nuevas;
					currentLevel = new ArrayList<RDFTriple>();
					// para determinar la cantidad de veces que se cruzará que
					// es el mayor numero entre
					// cant de indicadores en el nivel pasado donde estan los
					// ind que me quede y la cantIndicadores para este nivel
					int cantCruzamientos = lastLevel.size();
					if (indOfLevel.size() > cantCruzamientos) {
						cantCruzamientos = indOfLevel.size();
					}

					// cruzando indicadores de acuerdo con la cantidad max de
					// indicadores del pasado nivel o del actual.
					for (int j = 0; j < cantCruzamientos; j++) {

						subject = new Subject();
						// temporal para almacenar el object de la nueva tripla
						object = new RDFObject();
						// buscando la tripla donde me quede si es el nivel 1 me
						// devuelve una raiz random sino me devuelve la ke tenia
						motherTriple = this.getMotherTriple();
						System.out.println("Madre"
								+ motherTriple.getObject().getLabel());
						// asignandole el valor al subject
						subject = new Subject(motherTriple.getSubject()
								.getLabel());

						// asignandole el valor al object
						random = new Random();
						int range = indOfLevel.size();
						int pos = random.nextInt(range);
						object = new RDFObject(indOfLevel.get(pos).getName());
						// creando la nueva tripla

						triple = new RDFTriple(object, null, subject);
						triple.setSubjectProcess(motherTriple
								.getObjectProcess());
						triple.setObjectProcess(indOfLevel.get(pos)
								.getProcess());
						// agregando la nueva tripla si no existe ya
						if (this.existThisTriple(triple, childTriples) == false
								&& !subject.getLabel()
										.equals(object.getLabel())) {
							childTriples.add(triple);
							System.out.println("agregadas"
									+ childTriples.size());
							currentLevel.add(triple);
						}
					}
					System.out.println("tripla" + " " + childTriples.size());
				}
			}
			// agregando las triplas formadas al nuevo hijo
			child.setTriples(childTriples);
			// / llenar el arreglo de indicadores con las triplas del hijo antes
			// de devolverlo
			child.setIndicatorsArray(child.getIndicators());
			childs.add(child);
		}
		return childs;
	}

	public ArrayList<Indicators> getNotRootIndicator(Problem problem) {
		ArrayList<Indicators> allIndicators = problem.getIndicators();
		ArrayList<Indicators> notRoot = new ArrayList<Indicators>();
		ArrayList<RDFTriple> root = problem.getRootCauses();
		RDFTriple tripleToCompare = new RDFTriple();
		for (int i = 0; i < root.size(); i++) {
			tripleToCompare = root.get(i);
			for (int j = 0; j < allIndicators.size(); j++)
				if (tripleToCompare.getSubject().getLabel()
						.compareToIgnoreCase(allIndicators.get(j).getName()) == 0)
					allIndicators.remove(i);
		}
		notRoot = allIndicators;
		return notRoot;
	}

	public ArrayList<Indicators> getNotEffectIndicator(Problem problem) {
		ArrayList<Indicators> allIndicators = problem.getIndicators();
		ArrayList<Indicators> notEffect = new ArrayList<Indicators>();
		ArrayList<RDFTriple> effect = problem.getUnwantedEffects();
		RDFTriple tripleToCompare = new RDFTriple();
		for (int i = 0; i < effect.size(); i++) {
			tripleToCompare = effect.get(i);
			for (int j = 0; j < allIndicators.size(); j++)
				if (tripleToCompare.getObject().getLabel().compareToIgnoreCase(
						allIndicators.get(j).getName()) == 0)
					allIndicators.remove(i);
		}
		notEffect = allIndicators;
		return notEffect;
	}

	public ArrayList<Indicators> getRootIndicator(Problem problem) {

		ArrayList<Indicators> Root = new ArrayList<Indicators>();
		ArrayList<RDFTriple> root = problem.getRootCauses();
		RDFTriple triple = new RDFTriple();
		for (int i = 0; i < root.size(); i++) {
			triple = root.get(i);
			Root.add(new Indicators(triple.getObject().getLabel(), triple
					.getObjectProcess()));

		}

		return Root;
	}

	public ArrayList<Indicators> getEffectIndicator(Problem problem) {

		ArrayList<Indicators> effectsIndicator = new ArrayList<Indicators>();
		ArrayList<RDFTriple> effects = problem.getUnwantedEffects();
		RDFTriple triple = new RDFTriple();
		for (int i = 0; i < effects.size(); i++) {
			triple = effects.get(i);
			effectsIndicator.add(new Indicators(triple.getSubject().getLabel(),
					triple.getObjectProcess()));

		}

		return effectsIndicator;
	}

	/**
	 * This method generates a random cross point, then get the indicator that
	 * correspond to that cross point in the problem, and put that indicator in
	 * the next problem
	 */
	// RESPETANDO EL ESQUEMA( EFECTOS NO DESEADOS FIJOS)
	public ArrayList<Problem> crossWithoutEffects() {

		System.out.println("Ejecutando Crusamiento");
		String name = " Cross Child";
		// Crossing indicators
		System.out.println("Crusando con efectos  fijos");
		int level = problem1.getHiLevel();
		// build new triple
		RDFTriple triple = new RDFTriple();
		Subject subject;
		RDFObject object;
		// cantidad de indicadores de un nivel en cuestion;

		ArrayList<Indicators> indicatorsProblem1 = new ArrayList<Indicators>();
		ArrayList<Indicators> indicatorsProblem2 = new ArrayList<Indicators>();

		// Aki es el cliclo de generacion de hijos mientras se cumpla la funcion
		// de parada

		while (childs.size() < 2 || this.fitnessFunction() == true
				&& childs.size() < 10) {
			childTriples = new ArrayList<RDFTriple>();
			child = new Problem();
			ArrayList<RDFTriple> currentLevel = new ArrayList<RDFTriple>();
			child.setName(name);
			System.out.println(child.getName());
			// obteniendo los efectos
			lastLevel = problem1.getUnwantedEffects();
			System.out.println("Efectos size" + lastLevel.size());
			// tengo que cruzar los indicadores no son raiz ni efectos

			for (int i = 1; i < level + 1; i++) {
				ArrayList<Indicators> indOfLevel = new ArrayList<Indicators>();
				if (i == 1 || i != level - 1) {
					System.out.println("Entrooo" + i);
					effect = false;
					if (currentLevel.size() != 0)
						lastLevel = currentLevel;
					if (problem1.getIndicatorsOfLevel2(i).size() != 0)
						System.out.println("Cojio el inicador en nivel" + " "
								+ i);
					indicatorsProblem1 = problem1.getIndicatorsOfLevel2(i);

					if (problem2.getIndicatorsOfLevel2(i).size() != 0)
						indicatorsProblem2 = problem2.getIndicatorsOfLevel2(i);

					// uniendotodos los indicadores de los dos problemas para
					// este nivel
					for (int j = 0; j < indicatorsProblem1.size(); j++) {
						indOfLevel.add(indicatorsProblem1.get(j));
					}
					for (int j = 0; j < indicatorsProblem2.size(); j++) {
						indOfLevel.add(indicatorsProblem2.get(j));
					}

					// agregando al nivel anterior las nuevas triplas y
					// preparandolo para almacenar las nuevas;
					currentLevel = new ArrayList<RDFTriple>();

					// para determinar la cantidad de veces que se cruzar� que
					// es el mayor numero entre
					// cant de indicadores en el nivel pasado donde estan los
					// ind que me quede y la cantIndicadores para este nivel
					int cantCruzamientos = lastLevel.size();
					System.out.println("Cantidad de crusamientos"
							+ cantCruzamientos);
					// quedandome con el maximo
					if (indOfLevel.size() > cantCruzamientos) {
						cantCruzamientos = indOfLevel.size();
					}

					// cruzando indicadores de acuerdo con la cantidad max de
					// indicadores del pasado nivel o del actual.
					for (int j = 0; j < cantCruzamientos; j++) {

						subject = new Subject();
						// temporal para almacenar el object de la nueva tripla
						object = new RDFObject();
						// buscando la tripla donde me quede si es el nivel 1 me
						// devuelve una raiz random sino me devuelve la ke tenia
						System.out.println("Tripla madre");
						motherTriple = this.getMotherTriple();
						System.out
								.println(motherTriple.getSubject().getLabel());
						// asignandole el valor al subject
						subject = new Subject(motherTriple.getSubject()
								.getLabel());

						// asignandole el valor al object
						int range = indOfLevel.size();
						int pos = random.nextInt(range);
						object = new RDFObject(indOfLevel.get(pos).getName());
						// creando la nueva tripla
						triple = new RDFTriple(object, null, subject);
						triple.setSubjectProcess(motherTriple
								.getObjectProcess());
						triple.setObjectProcess(indOfLevel.get(pos)
								.getProcess());
						// agregando la nueva tripla si no existe ya
						if (this.existThisTriple(triple, childTriples) == false
								&& !subject.getLabel()
										.equals(object.getLabel())) {
							childTriples.add(triple);
							System.out.println("agregadas"
									+ childTriples.size());
							currentLevel.add(triple);
						}
					}

				} else {

					System.out.print("Los efectos");

					// obteniendo los indicadores de un nivel dado.
					if (problem1.getIndicatorsOfLevel2(i).size() != 0)
						System.out.println("Cojio el inicador en nivel" + " "
								+ i);
					indicatorsProblem1 = problem1.getIndicatorsOfLevel(i);

					if (problem2.getIndicatorsOfLevel2(i).size() != 0)
						indicatorsProblem2 = problem2.getIndicatorsOfLevel(i);

					// uniendotodos los indicadores de los dos problemas para
					// este nivel
					for (int j = 0; j < indicatorsProblem1.size(); j++) {
						indOfLevel.add(indicatorsProblem1.get(j));
					}
					for (int j = 0; j < indicatorsProblem2.size(); j++) {
						indOfLevel.add(indicatorsProblem2.get(j));
					}

					// agregando al nivel anterior las nuevas triplas y
					// preparandolo para almacenar las nuevas;
					currentLevel = new ArrayList<RDFTriple>();
					// para determinar la cantidad de veces que se cruzar� que
					// es el mayor numero entre
					// cant de indicadores en el nivel pasado donde estan los
					// ind que me quede y la cantIndicadores para este nivel
					int cantCruzamientos = lastLevel.size();
					System.out.println("Cantidad de cursamientos"
							+ cantCruzamientos);
					if (indOfLevel.size() > cantCruzamientos) {
						cantCruzamientos = indOfLevel.size();
					}

					// cruzando indicadores de acuerdo con la cantidad max de
					// indicadores del pasado nivel o del actual.
					for (int j = 0; j < cantCruzamientos; j++) {

						subject = new Subject();
						// temporal para almacenar el object de la nueva tripla
						object = new RDFObject();
						// buscando la tripla donde me quede si es el nivel 1 me
						// devuelve una raiz random sino me devuelve la ke tenia
						motherTriple = this.getMotherTriple();

						// asignandole el valor al subject
						subject = new Subject(motherTriple.getSubject()
								.getLabel());

						// asignandole el valor al object
						random = new Random();
						int range = indOfLevel.size();
						int pos = random.nextInt(range);
						object = new RDFObject(indOfLevel.get(pos).getName());
						// creando la nueva tripla
						triple = new RDFTriple(object, null, subject);
						triple.setSubjectProcess(motherTriple
								.getObjectProcess());
						triple.setObjectProcess(indOfLevel.get(pos)
								.getProcess());
						// agregando la nueva tripla si no existe ya
						System.out.println("Existe" + " "
								+ this.existThisTriple(triple, childTriples));
						if (this.existThisTriple(triple, childTriples) == false
								&& !subject.getLabel()
										.equals(object.getLabel())) {
							System.out.println("agregadas"
									+ childTriples.size());
							childTriples.add(triple);
							currentLevel.add(triple);
						}
					}
				}
			}
			child.setTriples(childTriples);
			// / llenar el arreglo de indicadores con las triplas del hijo antes
			// de devolverlo
			child.setIndicatorsArray(child.getIndicators());
			childs.add(child);
		}
		return childs;
	}

	/**
	 * Detects if one exists triple
	 * 
	 * @param RDFTriple
	 *            () Triple to check if it exists
	 * @param trpToCheck
	 *            () Triples's arrray
	 * @return boolean Is true if finds the triple one been contained in the
	 *         given arrangement.
	 */
	public boolean existThisTriple(RDFTriple triple,
			ArrayList<RDFTriple> trpToCheck) {
		boolean flag = false;
		int size = trpToCheck.size();

		if (size == 0) {
			flag = false;
		} else {
			for (int i = 0; i < size; i++) {

				/*
				 * If the triple one for parameter is similar to some other one
				 * triple of the son returns true and to check the object and
				 * the subject of the triple one.
				 */
				if (trpToCheck.get(i).getSubject().getLabel().equals(
						triple.getSubject().getLabel())
						&& trpToCheck.get(i).getObject().getLabel().equals(
								triple.getObject().getLabel())) {
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * Return the triple to the previous level, triple happiness becomes the
	 * triple mother
	 * 
	 * @return RDFTriple To return a triple one anyone of the previous level.
	 */
	public RDFTriple getMotherTriple() {
		random = new Random();
		try {
			int cant = lastLevel.size();
			System.out.println("Cant" + cant);
			if (cant == 0 || cant == 1) {
				motherTriple = lastLevel.get(0);
			} else {
				System.out.println("Cant mayor que 1");
				int pos = random.nextInt(cant);
				motherTriple = lastLevel.get(pos);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return motherTriple;
	}

	/**
	 * Return the triple to the previous level, triple happiness becomes the
	 * triple mother
	 * 
	 * @return RDFTriple To return a triple one anyone of the previous level.
	 */
	public RDFTriple getSonTriple() {
		random = new Random();
		try {
			int cant = firstevel.size();
			System.out.println("Cant" + cant);
			if (cant == 0 || cant == 1) {
				sonTriple = firstevel.get(0);
			} else {
				System.out.println("Cant mayor que 1");
				int pos = random.nextInt(cant);
				sonTriple = firstevel.get(pos);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return sonTriple;
	}
}