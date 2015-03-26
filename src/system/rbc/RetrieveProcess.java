package system.rbc;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jgraph.graph.GraphModel;

import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Problem;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;

/**
 * @author Héctor Grabiel Martín Varona
 * 
 */
public class RetrieveProcess {
	ArrayList<ProblemGraph> problemsInDB;
	Hashtable<ProblemGraph, String> retrievedProblems;

	public RetrieveProcess() {
		this.problemsInDB = new ArrayList<ProblemGraph>();
		this.retrievedProblems = new Hashtable<ProblemGraph, String>();
	}

	/**
	 * @author Héctor Grabiel Martín Varona
	 * 
	 * @description: Method to load all existing problems from data base
	 * 
	 * @throws HeadlessException
	 * @throws SQLException
	 */
	public int LoadDBProblems(ProblemGraph modeledProblem)
			throws HeadlessException, SQLException {
		ArrayList<Problem> problems = new ArrayList<Problem>();
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			if (PersistentManager
					.getConnectionToPostgreSQL()
					.getProblemsByClassification(
							modeledProblem.classifyProblem()).size() == 0) {
				return -1;
//				JOptionPane.showMessageDialog(new JFrame(),
//						"No problems found!!!");
			} else {
				// Getting problems from DB (problems with the same
				// classification of the modeled problem)
				problems = PersistentManager.getConnectionToPostgreSQL()
						.getProblemsByClassification(
								modeledProblem.classifyProblem());
				ProblemGraph temp = null;
				// Storing all retrieved problems into an array
				for (Problem problem : problems) {
					temp = new ProblemGraph();
					temp.insertProblem(problem);
					problemsInDB.add(temp);
				}
			}
		}
		return 0;
//		retrieveProblems(modeledProblem);
	}
	
	public Hashtable<ProblemGraph, String> Retrieve(ProblemGraph modeledProblem){
		if(problemsInDB.size() > 0){
			for (int i = 0; i < problemsInDB.size(); i++) {
				if(IsEquals(modeledProblem, problemsInDB.get(i))){
					retrievedProblems.put(problemsInDB.get(i), "Equals");
				}else if(IsSubgraph(modeledProblem, problemsInDB.get(i))){
					retrievedProblems.put(problemsInDB.get(i), "Subgraph");
				}else if(IsContainer(modeledProblem, problemsInDB.get(i))){
					retrievedProblems.put(problemsInDB.get(i), "Container");
				}
			}
			return retrievedProblems;
		}
		return null;
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 *
	 * @description: Method to look if there are any problems which are equals with the modeled problem
	 *
	 * @param modeledProblem
	 * @param targetProblem
	 * @return
	 */
	public boolean IsEquals(ProblemGraph modeledProblem, ProblemGraph targetProblem){
		// Lists of edges of modeled and target problems
		ArrayList<Object> modeledProblemEdgesList = modeledProblem.getAllEdges();
		ArrayList<Object> targetProblemEdgesList = targetProblem.getAllEdges();
		
		// Graphs models of modeled and target problems
		GraphModel modeledProblemModel = modeledProblem.getModel();
		GraphModel targetProblemModel = targetProblem.getModel();
		
		
		// If the modeled problem and the target problems has both the same size then they could be the same problem
		if(modeledProblemEdgesList.size() == targetProblemEdgesList.size()){
			for (int i = 0; i < modeledProblemEdgesList.size(); i++) {
				boolean flag = false;
				MyProblemCustomCell modeledProblemEdgeSource = (MyProblemCustomCell) modeledProblemModel.getParent(modeledProblemModel.getSource(modeledProblemEdgesList.get(i)));
				MyProblemCustomCell modeledProblemEdgeTarget = (MyProblemCustomCell) modeledProblemModel.getParent(modeledProblemModel.getTarget(modeledProblemEdgesList.get(i)));
				
				for (int j = 0; j < targetProblemEdgesList.size(); j++) {
					MyProblemCustomCell targetProblemEdgeSource = (MyProblemCustomCell) targetProblemModel.getParent(targetProblemModel.getSource(targetProblemEdgesList.get(j)));
					MyProblemCustomCell targetProblemEdgeTarget = (MyProblemCustomCell) targetProblemModel.getParent(targetProblemModel.getTarget(targetProblemEdgesList.get(j)));

					if(String.valueOf(modeledProblemEdgeSource).equals(String.valueOf(targetProblemEdgeSource))) if(String.valueOf(modeledProblemEdgeTarget).equals(String.valueOf(targetProblemEdgeTarget))) flag = true;;
				}
				
				// If the edge doesn't exist then the target problem isn't equals with the modeled problem
				if(!flag) return false;
			}
		}else{
			return false;
		}
		return true;												
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 *
	 * @description: Method to look if the modeled problem is a subgraph of any other problem
	 *
	 * @param modeledProblem
	 * @param targetProblem
	 * @return
	 */
	public boolean IsSubgraph(ProblemGraph modeledProblem, ProblemGraph targetProblem){
		// Lists of edges of modeled and target problems
		ArrayList<Object> modeledProblemEdgesList = modeledProblem.getAllEdges();
		ArrayList<Object> targetProblemEdgesList = targetProblem.getAllEdges();
		
		// Graphs models of modeled and target problems
		GraphModel modeledProblemModel = modeledProblem.getModel();
		GraphModel targetProblemModel = targetProblem.getModel();
		
		
		// If the modeled problem is smallest than the target problem then the modeled problem could be a subgraph of the target problem
		if(modeledProblemEdgesList.size() < targetProblemEdgesList.size()){
			for (int i = 0; i < modeledProblemEdgesList.size(); i++) {
				boolean flag = false;
				MyProblemCustomCell modeledProblemEdgeSource = (MyProblemCustomCell) modeledProblemModel.getParent(modeledProblemModel.getSource(modeledProblemEdgesList.get(i)));
				MyProblemCustomCell modeledProblemEdgeTarget = (MyProblemCustomCell) modeledProblemModel.getParent(modeledProblemModel.getTarget(modeledProblemEdgesList.get(i)));
				
				for (int j = 0; j < targetProblemEdgesList.size(); j++) {
					MyProblemCustomCell targetProblemEdgeSource = (MyProblemCustomCell) targetProblemModel.getParent(targetProblemModel.getSource(targetProblemEdgesList.get(j)));
					MyProblemCustomCell targetProblemEdgeTarget = (MyProblemCustomCell) targetProblemModel.getParent(targetProblemModel.getTarget(targetProblemEdgesList.get(j)));
					
					if(String.valueOf(modeledProblemEdgeSource).equals(String.valueOf(targetProblemEdgeSource))) if(String.valueOf(modeledProblemEdgeTarget).equals(String.valueOf(targetProblemEdgeTarget))) flag = true;;
				}
				
				// If the edge doesn't exist then the modeled problem isn't a subgraph of the target problem
				if(!flag) return false;
			}
		}else{
			return false;
		}
		return true;
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 *
	 * @description: Method to look if there are any problems which are subgraph of the modeled problem
	 *
	 * @param modeledProblem
	 * @param targetProblem
	 * @return
	 */
	public boolean IsContainer(ProblemGraph modeledProblem, ProblemGraph targetProblem){
		// Lists of edges of modeled and target problems
		ArrayList<Object> modeledProblemEdgesList = modeledProblem.getAllEdges();
		ArrayList<Object> targetProblemEdgesList = targetProblem.getAllEdges();
		
		// Graphs models of modeled and target problems
		GraphModel modeledProblemModel = modeledProblem.getModel();
		GraphModel targetProblemModel = targetProblem.getModel();
		
		
		// If the modeled problem is bigger than the target problem then the target problem could be a subgraph of the modeled problem
		if(modeledProblemEdgesList.size() > targetProblemEdgesList.size()){
			for (int i = 0; i < targetProblemEdgesList.size(); i++) {
				boolean flag = false;
				MyProblemCustomCell targetProblemEdgeSource = (MyProblemCustomCell) targetProblemModel.getParent(targetProblemModel.getSource(targetProblemEdgesList.get(i)));
				MyProblemCustomCell targetProblemEdgeTarget = (MyProblemCustomCell) targetProblemModel.getParent(targetProblemModel.getTarget(targetProblemEdgesList.get(i)));
				
				for (int j = 0; j < modeledProblemEdgesList.size(); j++) {
					MyProblemCustomCell modeledProblemEdgeSource = (MyProblemCustomCell) modeledProblemModel.getParent(modeledProblemModel.getSource(modeledProblemEdgesList.get(j)));
					MyProblemCustomCell modeledProblemEdgeTarget = (MyProblemCustomCell) modeledProblemModel.getParent(modeledProblemModel.getTarget(modeledProblemEdgesList.get(j)));
					
					if(String.valueOf(targetProblemEdgeSource).equals(String.valueOf(modeledProblemEdgeSource))) if(String.valueOf(targetProblemEdgeTarget).equals(String.valueOf(modeledProblemEdgeTarget))) flag = true;;
				}
				
				// If the edge doesn't exist then the target problem isn't a subgraph of the modeled problem
				if(!flag) return false;
			}
		}else{
			return false;
		}
		return true;
	}

//	/**
//	 * @author Héctor Grabiel Martín Varona
//	 * 
//	 * @description: Method to retrieve problems equals or similar with the
//	 *               modeled problem
//	 * 
//	 * @param modeledProblem
//	 */
//	private void retrieveProblems(ProblemGraph modeledProblem) {
//		// Looking for equals problems
//		// Getting all roots from modeled problems
//		Object[] modeledProblemRootsTemp = modeledProblem.getRoots();
//		String[] modeledProblemRoots = new String[modeledProblemRootsTemp.length];
//		for (int i = 0; i < modeledProblemRootsTemp.length; i++) {
//			modeledProblemRoots[i] = modeledProblemRootsTemp[i].toString();
//		}
//
//		for (int i = 0; i < problemsInDB.size(); i++) {
//			// This flag indicates if the retrieved problem is equals with the
//			// modeled problem or not
//			boolean flag = true;
//			Object[] retrievedProblemRootsTemp = problemsInDB.get(i).getRoots();
//			String[] retrievedProblemRoots = new String[retrievedProblemRootsTemp.length];
//			for (int j = 0; j < retrievedProblemRootsTemp.length; j++) {
//				retrievedProblemRoots[j] = retrievedProblemRootsTemp[j]
//						.toString();
//			}
//			// Checking if the retrieved problem has the same roots that the
//			// modeled problem
//			if (retrievedProblemRoots.length == modeledProblemRoots.length) {
//				// Checking if the retrieved problem has the same root causes
//				// that the modeled problem
//				if (problemsInDB.get(i).getRootCauses().size() == modeledProblem
//						.getRootCauses().size()) {
//					Arrays.sort(modeledProblemRoots);
//					Arrays.sort(retrievedProblemRoots);
//					
//					// Iterating over arrays to check if are the same
//					for (int j = 0; j < modeledProblemRoots.length; j++) {
//						if (!(modeledProblemRoots[j]
//								.equals(retrievedProblemRoots[j]))) {
//							flag = false;
//							break;
//						}
//					}
//					if (flag) {
//						retrievedProblems.add(modeledProblem);
//					}
//				}
//			}
//		}
//		// Here ends code for equals problems
//		System.out.println(retrievedProblems.size());
//	}
}
