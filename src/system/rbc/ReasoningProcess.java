package system.rbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jgraph.graph.GraphModel;

import system.rdf.dataBase.Predicate;
import system.rdf.dataBase.RDFObject;
import system.rdf.dataBase.RDFTriple;
import system.rdf.dataBase.Solution;
import system.rdf.dataBase.Subject;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.ui.GraphEd;

/**
 * @author Héctor Grabiel Martín Varona
 * 
 */
public class ReasoningProcess {

	/**
	 * @author Héctor Grabiel Martín Varona
	 * 
	 * @description: Method to get adapted the solution of the retrieved problem
	 *               form current modeled problem structure
	 * 
	 * @param retrievedProblem
	 * @param modeledProblem
	 * @return
	 */
	public ArrayList<RDFTriple> GetAdaptedSolution(ProblemGraph retrievedProblem,
			ProblemGraph modeledProblem, Solution retrievedSolution,
			String status) {
		// This array contains all triples must be added or removed from solution
		ArrayList<RDFTriple> triplesToReturn = new ArrayList<RDFTriple>();

		// Lists of edges of modeled and retrieved problems and retrieved
		// solution
		ArrayList<Object> modeledProblemEdgesList = modeledProblem
				.getAllEdges();
		ArrayList<Object> retrievedProblemEdgesList = retrievedProblem
				.getAllEdges();

		SolutionGraph solgraph = new SolutionGraph();
		solgraph.insertSolution(retrievedSolution);

		// Graphs models of modeled and retrieved problems and solution
		GraphModel modeledProblemModel = modeledProblem.getModel();
		GraphModel retrievedProblemModel = retrievedProblem.getModel();

		// This arrays are used to get the difference between two collections of indicators
		ArrayList<String> cellArray1 = new ArrayList<String>();
		ArrayList<String> cellArray2 = new ArrayList<String>();

		// Check if the modeled problem is a subgraph of the retrieved one
		if (status.equals("Subgraph")) {
			// Iterating over all edges of the retrieved problem
			for (int i = 0; i < retrievedProblemEdgesList.size(); i++) {
				MyProblemCustomCell retrievedProblemEdgeSource = (MyProblemCustomCell) retrievedProblemModel
						.getParent(retrievedProblemModel
								.getSource(retrievedProblemEdgesList.get(i)));
				MyProblemCustomCell retrievedProblemEdgeTarget = (MyProblemCustomCell) retrievedProblemModel
						.getParent(retrievedProblemModel
								.getTarget(retrievedProblemEdgesList.get(i)));

				// Creating an string with the source and the target in this format C.C.T.Days-Sales-Outstanding::::EM.Enable-Make
				String cell1 = retrievedProblemEdgeSource + "::::"
						+ retrievedProblemEdgeTarget;
				if (!(cellArray1.contains(cell1)))
					cellArray1.add(cell1);
				;

				// Iterating over all edges of the modeled problem
				for (int j = 0; j < modeledProblemEdgesList.size(); j++) {
					MyProblemCustomCell modeledProblemEdgeSource = (MyProblemCustomCell) modeledProblemModel
							.getParent(modeledProblemModel
									.getSource(modeledProblemEdgesList.get(j)));
					MyProblemCustomCell modeledProblemEdgeTarget = (MyProblemCustomCell) modeledProblemModel
							.getParent(modeledProblemModel
									.getTarget(modeledProblemEdgesList.get(j)));

					// Creating an string with the source and the target in this format C.C.T.Days-Sales-Outstanding::::EM.Enable-Make
					String cell2 = modeledProblemEdgeSource + "::::"
							+ modeledProblemEdgeTarget;
					if (!(cellArray2.contains(cell2)))
						cellArray2.add(cell2);
					;
				}
			}

			// Creating collections for the difference
			Collection<String> col1 = cellArray1;
			Collection<String> col2 = cellArray2;

			// Removing all indicators of the modeled problem from the retrieved one
			col1.removeAll(col2);

			// In col1 there are only the indicators must be deleted from retrieved solution
			for (String string : col1) {
				String[] parts = string.split("::::");

				ArrayList<RDFTriple> tr = retrievedSolution.getTriples();

				// Looking for all triples must be removed
				for (int i = 0; i < tr.size(); i++) {
					if (tr.get(i).getSubject().getLabel().equals(parts[0])
							&& tr.get(i).getObject().getLabel()
									.equals(parts[1])) {
						triplesToReturn.add(retrievedSolution.getTriples().get(i));
						break;
					}
				}
			}
		}

		// Check if the modeled problem is a super graph of the retrieved one
		if (status.equals("Container")) {
			// Iterating over all edges of the retrieved problem
			for (int i = 0; i < retrievedProblemEdgesList.size(); i++) {
				MyProblemCustomCell retrievedProblemEdgeSource = (MyProblemCustomCell) retrievedProblemModel
						.getParent(retrievedProblemModel
								.getSource(retrievedProblemEdgesList.get(i)));
				MyProblemCustomCell retrievedProblemEdgeTarget = (MyProblemCustomCell) retrievedProblemModel
						.getParent(retrievedProblemModel
								.getTarget(retrievedProblemEdgesList.get(i)));

				// Creating an string with the source and the target in this format C.C.T.Days-Sales-Outstanding::::EM.Enable-Make
				String cell1 = retrievedProblemEdgeSource + "::::"
						+ retrievedProblemEdgeTarget;
				if (!(cellArray1.contains(cell1)))
					cellArray1.add(cell1);
				;

				// Iterating over all edges of the modeled problem
				for (int j = 0; j < modeledProblemEdgesList.size(); j++) {
					MyProblemCustomCell modeledProblemEdgeSource = (MyProblemCustomCell) modeledProblemModel
							.getParent(modeledProblemModel
									.getSource(modeledProblemEdgesList.get(j)));
					MyProblemCustomCell modeledProblemEdgeTarget = (MyProblemCustomCell) modeledProblemModel
							.getParent(modeledProblemModel
									.getTarget(modeledProblemEdgesList.get(j)));

					// Creating an string with the source and the target in this format C.C.T.Days-Sales-Outstanding::::EM.Enable-Make
					String cell2 = modeledProblemEdgeSource + "::::"
							+ modeledProblemEdgeTarget;
					if (!(cellArray2.contains(cell2)))
						cellArray2.add(cell2);
					;
				}
			}

			// Creating collections for the difference
			Collection<String> col1 = cellArray1;
			Collection<String> col2 = cellArray2;

			// Removing all indicators of the retrieved problem from the modeled one
			col2.removeAll(col1);

			Solution solut = new Solution();

			// In col2 there are only the indicators must be added to the retrieved solution
			for (String string : col2) {
				String[] parts = string.split("::::");
				
				RDFTriple triple_temp = new RDFTriple();

				ArrayList<RDFTriple> modeledProblemTriples = modeledProblem
						.getProblem_list().get(0).getTriples();

				// Looking for subject part of the triples must be added
				for (int i = 0; i < modeledProblemTriples.size(); i++) {
					if (modeledProblemTriples.get(i).getSubject().getLabel()
							.equals(parts[0])
							&& modeledProblemTriples.get(i).getObject()
									.getLabel().equals(parts[1])) {
						triple_temp.setSubject(modeledProblemTriples.get(i)
								.getSubject());
						triple_temp.setPredicate(modeledProblemTriples.get(i)
								.getPredicate());
						triple_temp.setSubjectProcess(modeledProblemTriples
								.get(i).getSubjetProcess());
						break;
					}
				}

				ArrayList<RDFTriple> retrievedSolutionTriples = retrievedSolution
						.getTriples();

				// Looking for object part of triples must be added
				for (int i = 0; i < retrievedSolutionTriples.size(); i++) {
					if (retrievedSolutionTriples.get(i).getObject().getLabel()
							.equals(parts[1])) {
						triple_temp.setObject(retrievedSolutionTriples.get(i)
								.getObject());
						triple_temp.setObjectProcess(retrievedSolutionTriples
								.get(i).getObjectProcess());
						break;
					}
				}
				triplesToReturn.add(triple_temp);
			}
		}

		// Here there are the triples must be removed from the solution or added to it
		return triplesToReturn;
	}

	public boolean SaveAdaptedProblemAsCase() {
		return true;
	}

}
