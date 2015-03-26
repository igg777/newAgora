package system.rdf.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

import system.rdf.handlers.DragDropHandler;
import system.rdf.ontology.OntologyTreeManager;
import system.rdf.ui.GraphEd;
import system.rdf.ui.UITabbedPaneEditor;
import system.rdf.utils.TabsComponentManager;

public class GraphManager {

	private ProblemGraph problemGraph;
	private SolutionGraph solutionGraph;

	private ArrayList<SolutionGraph> solutionGraphs = new ArrayList<SolutionGraph>();
	private ArrayList<ProblemGraph> problemGraphs = new ArrayList<ProblemGraph>();

	private ArrayList<JGraph> solution = new ArrayList<JGraph>();
	private ArrayList<JGraph> currentGraph = new ArrayList<JGraph>();
	/**
	 * On this array list the loaded problem graph will be saved
	 */
	private ArrayList<JGraph> loadedProblemGraph = new ArrayList<JGraph>();

	private int cont = 0;

	/**
	 * Empty constructor
	 */
	public GraphManager() {

	}

	/**
	 * Create a JGraph object to formulated a solution
	 * 
	 * @return JGraph object for solution
	 */
	public JGraph createSolutionGraph(ProblemGraph problemGraph) {
		int id = cont++;
		// Solution Graph
		solutionGraph = new SolutionGraph();
		solutionGraph.setId(id);
		solutionGraphs.add(id, solutionGraph);

		// This is the problem graph
		JGraph graph = problemGraph;
		Object[] roots = graph.getRoots().clone();

		// Clone of edges without saving dandling edge
		Map clonedMap = graph.cloneCells(roots);
		Set<Object> set = clonedMap.keySet();
		Object[] keySet2 = new Object[set.size()];
		Object[] valueSet2 = new Object[set.size()];
		// ArrayList<Object> valueToInsert = new ArrayList<Object>(set.size());
		// ArrayList celdas = new ArrayList ();
		set.toArray(keySet2);
		java.util.List keyList = Arrays.asList(keySet2);

		for (int i = 0; i < keyList.size(); i++) {
			valueSet2[i] = clonedMap.get(keyList.get(i));
		}

		for (int i = 0; i < keyList.size(); i++) {

			if (keyList.get(i) instanceof Edge) {

				MyProblemCustomCell source = (MyProblemCustomCell) DefaultGraphModel
						.getSourceVertex(graph.getModel(), keyList.get(i));
				MyProblemCustomCell target = (MyProblemCustomCell) DefaultGraphModel
						.getTargetVertex(graph.getModel(), keyList.get(i));

				int posSource = -1;
				int posTarget = -1;
				for (int j = 0; j < keyList.size(); j++) {
					if (keyList.get(j) == source)
						posSource = j;
					if (keyList.get(j) == target)
						posTarget = j;
				}

				if (posSource != -1 && posTarget != -1) {

					MyProblemCustomCell sourceVertex = (MyProblemCustomCell) valueSet2[posSource];
					MyProblemCustomCell targetVertex = (MyProblemCustomCell) valueSet2[posTarget];
					// Adding ports to the vertex cloned
					sourceVertex.addPort();
					targetVertex.addPort();

					((DefaultEdge) valueSet2[i]).setSource(sourceVertex
							.getChildAt(0));
					((DefaultEdge) valueSet2[i]).setTarget(targetVertex
							.getChildAt(0));
					((DefaultEdge) valueSet2[i]).getAttributes().applyMap(
							EdgeManager.solutionEdgeAttributes());
					solutionGraph.insertTriple(sourceVertex, targetVertex,
							sourceVertex.getProcess(), targetVertex
									.getProcess());
				}
			}
		}

		//solutionGraph.addMouseListener(TabsComponentManager.getPropertiesTable
		// ());
		// System.out.println("EToys");
		// Create a Drag Drop Handler to draw cell
		new DragDropHandler(solutionGraph, OntologyTreeManager
				.getOntologyTree());

		return solutionGraph;
	}

	/**
	 * Create a Graph to formulated a decisional problem
	 * 
	 * @param GraphEd
	 * @return JGraph
	 */
	public JGraph createProblemGraph() {
		// Create new instance of problem graph
		problemGraph = new ProblemGraph();
		// Add listener with the property table for cell
		problemGraph.addMouseListener();
		// Install the listener for tool bar buttons
		// graphed.installListeners(problemGraph);
		// graphed.invalidate();
		// Create a Drag Drop Handler to draw cell
		new DragDropHandler(problemGraph, OntologyTreeManager.getOntologyTree());
		currentGraph.add(0, problemGraph);
		return problemGraph;
	}

	public ArrayList<SolutionGraph> getSolutionGraphs() {
		return solutionGraphs;
	}

	/**
	 * This method is to install the listener to graphs
	 * 
	 * @param JGraph
	 */
	public void installListeners(JGraph graph) {

		/*
		 * // Register UndoManager with the model
		 * graph.getModel().addUndoableEditListener(undoManager); // Update
		 * ToolBar based on Selection Changes
		 * graph.getSelectionModel().addGraphSelectionListener(this); // Listen
		 * for Delete Keystroke when the Graph has Focus
		 * graph.addKeyListener(this);
		 * graph.getModel().addGraphModelListener(statusBar);
		 */
	}

	public Map createEdgeAttributes() {
		Map map = new Hashtable();
		// Add a Line End Attribute
		GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
		// Fill end line
		GraphConstants.setEndFill(map, true);
		// Add a label along edge attribute
		GraphConstants.setLabelAlongEdge(map, true);
		// Cannot edit the edge
		GraphConstants.setEditable(map, false);

		return map;
	}

	public ProblemGraph getProblemGraph() {
		return problemGraph;
	}

	/**
	 * @return ProblemGraph
	 */
	public SolutionGraph getSolutionGraph() {
		return (SolutionGraph) solution.get(0);
	}

	public ArrayList<ProblemGraph> getProblemGraphs() {
		return problemGraphs;
	}

	public void setProblemGraphs(ArrayList<ProblemGraph> problemGraphs) {
		this.problemGraphs = problemGraphs;
	}

	public ArrayList<JGraph> getSolution() {
		return solution;
	}

	public void setSolution(ArrayList<JGraph> solution) {
		this.solution = solution;
	}

	public ArrayList<JGraph> getCurrentGraph() {
		return currentGraph;
	}

	public void setCurrentGraph(ArrayList<JGraph> currentGraph) {
		this.currentGraph = currentGraph;
	}

	public int getCont() {
		return cont;
	}

	public void setCont(int cont) {
		this.cont = cont;
	}

	public void setProblemGraph(ProblemGraph problemGraph) {
		this.problemGraph = problemGraph;
	}

	public void setSolutionGraph(SolutionGraph solutionGraph) {
		this.solutionGraph = solutionGraph;
	}

	public void setSolutionGraphs(ArrayList<SolutionGraph> solutionGraphs) {
		this.solutionGraphs = solutionGraphs;
	}

	public ArrayList<JGraph> getLoadedProblemGraph() {
		return loadedProblemGraph;
	}

	public void setLoadedProblemGraph(ArrayList<JGraph> loadedProblemGraph) {
		this.loadedProblemGraph = loadedProblemGraph;
	}

}
