package system.rdf.ui;


import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jgraph.JGraph;

import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFLoader;
import system.rdf.dataBase.Solution;
import system.rdf.graph.GraphManager;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.listeners.PopupListener;

/**
 * This class is a custom TabbedPane to formulate the decision problem
 * @author Armando Carracedo
 */
public class UITabbedPaneEditor extends JTabbedPane {
    private static final long serialVersionUID = 4314472905479224749L;
    private static String tips = "Right click to show options";
    private GraphManager graphHandler;
    private ArrayList<JGraph> graphs = new ArrayList<JGraph>();
    private ArrayList<Problem> problemList = new ArrayList<Problem>();
    public ProblemGraph problemGraph;
    
    public ArrayList<Problem> getProblemList() {
		return problemList;
	}

	public void setProblemList(ArrayList<Problem> problemList) {
		this.problemList = problemList;
	}

	//private RDFLoader loader = new RDFLoader();
    /**
     * Constructor
     * @param JGraph g
     */
    public UITabbedPaneEditor(){
	//this.problemGraph = g.graph; 
	this.setFocusable(false);
	this.graphHandler = new GraphManager();
	this.setTabPlacement(JTabbedPane.BOTTOM);
	// Add GraphListener instance
	this.addMouseListener();
	this.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
	// Create a tab for edit decision problems
	createProblemTab();
    }

    /**
     * Create a tab for modeling a decision problem
     * @param JGraph
     */
    public void createProblemTab(){
	 problemGraph = (ProblemGraph)graphHandler.
	createProblemGraph();
	graphs.add(this.getTabCount(), problemGraph);
	EditorScrollPane ed = new EditorScrollPane(problemGraph);
	this.addTab("Problem Formulation",null,ed,tips);
    }

    /**
     * From the decision problem that was modeled create a model for solution
     */
    public void createSolutionTab(){
	//index represent the number of solution tab  
	String index  = String.valueOf(getTabCount());
	SolutionGraph solutionGraph = (SolutionGraph)graphHandler.
	createSolutionGraph((ProblemGraph)graphs.get(0));

	EditorScrollPane editor = new EditorScrollPane(solutionGraph);
	graphs.add(this.getTabCount(),solutionGraph);

	this.addTab("Alternative " + index,null,editor,tips);
    }

    /**
     * Adds a tab with the selected problem to load
     * @param JGraph graph
     */
    public void loadProblem(Problem pro){
	if(pro != null){
		problemList.add(0,pro);
	    ProblemGraph graph = new ProblemGraph();
	    graph.insertProblem(pro);
	    graph.setSavedName(pro.getName());
        graph.setSaved(true);
	    EditorScrollPane editor = new EditorScrollPane(graph);
	    this.removeAll();
	    graphs.set(0, graph);
	    this.addTab("Problem Formulation",null,editor,tips);
	}
    }

    /**
     * Adds a tab with the selected solution to load
     * @param JGraph graph
     */
    public void loadSolution(Solution sol){
	if(sol != null){
	    SolutionGraph graph = new SolutionGraph();
	    graph.insertSolution(sol);
	    graph.setSavedName(sol.getName());
	    graph.setSavedAsAlternative(true);
	    EditorScrollPane editor = new EditorScrollPane(graph);
	    this.addTab(sol.getName(), null, editor, tips);
	    graphs.add(graph);
	}
    }


    /**
     * Insert a solution with his associated problem
     * @param Solution sol
     */
    public void loadSolutionWithProblem(Solution sol){
    	this.removeAll();
        
    	Problem pro = sol.getAssociatedProblem();

	ProblemGraph problemGraph = new ProblemGraph();
	problemGraph.insertProblem(pro);
	problemGraph.setSavedName(pro.getName());
    problemGraph.setSaved(true);
	EditorScrollPane problemEditor = new EditorScrollPane(problemGraph);
	this.addTab(pro.getName(),null,problemEditor,tips);
	graphs.add(0,problemGraph);
	///
	SolutionGraph solutionGraph = new SolutionGraph();
    solutionGraph.insertSolution(sol);
    solutionGraph.setSavedName(sol.getName());
    solutionGraph.setSavedAsAlternative(true);
    graphs.add(this.getTabCount(), solutionGraph);
    EditorScrollPane solutionEditor = new EditorScrollPane(solutionGraph);
	this.addTab(sol.getName(), null, solutionEditor,tips);
	
    
    
	
    }

    /**
     * Close a selected tab 
     */
    public void closeTab(){
	int index = this.getSelectedIndex();
	graphs.remove(index);
	problemList.remove(index);
	this.remove(index);
    }

    /**
     * Gets the JGraph object on the selected tab
     * @return Object on the selected components
     */
    public JGraph getTabGraph(){
	int index = getSelectedIndex();
	return graphs.get(index);
	
    }

    /**
     * Gets the JGraph object on the selected tab
     * @param int index
     * @return JGraph object
     */
    public  JGraph getTabGraph(int index){
	return graphs.get(index);
    }

    /**
     * Add a tab with the specified title and component
     * @see javax.swing.JTabbedPane#addTab(java.lang.String, java.awt.Component)
     */
    public void addTab(String title, Component component) {
	super.addTab(title, component);
    } 
    /**
     * Add the the PopupListener instance to save as solution
     */
    public void addMouseListener(){
	this.addMouseListener(new PopupListener(this));
    }

	public GraphManager getGraphHandler() {
		return graphHandler;
	}

	public void setGraphHandler(GraphManager graphHandler) {
		this.graphHandler = graphHandler;
	}

	public ArrayList<JGraph> getGraphs() {
		return graphs;
	}

	public void setGraphs(ArrayList<JGraph> graphs) {
		this.graphs = graphs;
	}
	
	 private void tabbedPaneEditosStateChanged(javax.swing.event.ChangeEvent evt) {
		 if(GraphEd.modelsss.size() > 0){
		    JGraph graph = getTabGraph();
		    String name = "";
		    if(graph instanceof ProblemGraph)
		    	name = ((ProblemGraph)graph).getSavedName();
		    else
		    	name = ((SolutionGraph)graph).getSavedName();
		    GraphEd.modelsss.get(GraphEd.scenarios.getSelectedIndex()).setTitle(name);
		    GraphEd.refreshScenarios();
		 }
	    }
	 
	 public void addChangeListener(){
		//adding a chage listener
	    	this.addChangeListener(new javax.swing.event.ChangeListener() {
	            public void stateChanged(javax.swing.event.ChangeEvent evt) {
	                tabbedPaneEditosStateChanged(evt);
	            }
	        });
	 }
    
}
