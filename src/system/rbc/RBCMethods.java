package system.rbc;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Problem;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author Héctor Grabiel Martín Varona
 *
 */
public class RBCMethods {

	private ArrayList<ProblemGraph> loadedProblemGraphs = new ArrayList<ProblemGraph>();
//	private ArrayList<ProblemGraph> equalProblemGraphs = new ArrayList<ProblemGraph>();
//	private ArrayList<ProblemGraph> similarProblemGraphs = new ArrayList<ProblemGraph>();
	private ProblemGraph newProblem;

	public RBCMethods(ProblemGraph NewProblem) {
		this.newProblem = NewProblem;
	}
	
	public void RunRbc() throws HeadlessException, NullPointerException, SQLException{
		Retrieve();
		if(loadedProblemGraphs.size() > 0 && newProblem != null){
			StepOne();
		}
	}
	
	private void StepOne(){
		ArrayList<MyProblemCustomCell> rootCauses = newProblem.getRootCauses();
		ArrayList<MyProblemCustomCell> loadedRootCauses = null;
//		System.out.println(rootCauses.size());
//		System.out.println(loadedProblemGraphs.size());
		for (ProblemGraph loadedProbGraph : loadedProblemGraphs) {
			loadedRootCauses = loadedProbGraph.getRootCauses();
			if(loadedRootCauses.size() == rootCauses.size()){
				for (int i = 0; i < rootCauses.size(); i++) {
//					System.out.println(rootCauses.get(i));
					for (int j = 0; j < loadedRootCauses.size(); j++) {
//						System.out.println(loadedRootCauses.get(i));
						if(loadedRootCauses.get(j).toString().equals(rootCauses.get(i).toString())){
							System.out.println("true");						
						}else{
							System.out.println("false");
						}
					}
				}				
			}
		}
	}
	
//	private void FindUtilProblems(){
//		org.jgrapht.experimental.isomorphism.GraphIsomorphismInspector inspector = null;
//		int cont = 0;
//		for (ProblemGraph loadedProblem : loadedProblemGraphs) {
//			inspector = org.jgrapht.experimental.isomorphism.AdaptiveIsomorphismInspectorFactory.createIsomorphismInspector(newProblem.getAcyclicGraph(), loadedProblem.getAcyclicGraph());
//			if(inspector.isIsomorphic()){
//				System.out.println("***Isomorphic " + loadedProblem.getProblem_list().get(0).getName());
//				cont++;
//			}else{
//				System.out.println("No Isomorphyc " + loadedProblem.getProblem_list().get(0).getName());
//			}
//		}
//		System.out.println("------------------------------------------------------");
//		System.out.println(cont + " Isomorphyc problems found");
//	}

	private void Retrieve() throws NullPointerException,
			HeadlessException, SQLException {
		ArrayList<Problem> problems = new ArrayList<Problem>();
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			// try {
			if (PersistentManager.getConnectionToPostgreSQL().getProblems()
					.size() == 0) {
				JOptionPane.showMessageDialog(new JFrame(),
						"No problems found!!!");
			} else {
				// Getting problems from DB
				problems = PersistentManager.getConnectionToPostgreSQL()
						.getProblems();
				ProblemGraph temp = null;
				for (Problem problem : problems) {
					temp = new ProblemGraph();
					temp.insertProblem(problem);
					loadedProblemGraphs.add(temp);
				}
			}
		}
	}

//	/**
//	 * @deprecated
//	 * @author Héctor Grabiel Martín Varona
//	 * @date 18/06/2011 10:34 PM
//	 * @abstract Method to test the isomorphism between two problems (problems
//	 *           are graphs)
//	 * 
//	 */
//	public static boolean isIsomorphic(Model newProblem, Problem loadedProblem) {
//		// Extracting models from graphs
//		Model mloadedProblem = loadedProblem.getModel();
//
//		if (newProblem.isIsomorphicWith(mloadedProblem)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
}
