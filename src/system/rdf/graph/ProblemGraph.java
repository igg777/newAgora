package system.rdf.graph;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.ToolTipManager;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import system.rdf.CustomCell;
import system.rdf.ProblemCellViewFactory;
import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.dataBase.RDFTriple;
import system.rdf.handlers.ProblemMarqueeHandler;
import system.rdf.listeners.GraphListener;
import system.rdf.ui.GraphEd;
import system.rdf.utils.TabsComponentManager;
import system.rename.Tools;

/**
 * 
 * Defines a Custom Graph that uses the Shift-Button (Instead of the Right Mouse
 * Button, which is Default) to add/remove point to/from an edge.
 * 
 * @author Armando
 */
/**
 * @author Hector
 *
 */
public class ProblemGraph extends JGraph {
	private DirectedAcyclicGraph<Object, Object> acyclicGraph = new DirectedAcyclicGraph<Object, Object>(
			DefaultEdge.class);
	private static final long serialVersionUID = 9099565591715729814L;
	private GraphUndoManager undoManager = new GraphUndoManager();
	// private DefaultGraphCell vertex;
	private Object selectedCell;
	// This flag indicate if the problem was saved after the last change
	private boolean saved = false;
	// the name which the problem has been saved
	private String savedName = "";
	private Tools tools = new Tools();
	private RDFSerializator serializator = new RDFSerializator();
	private ArrayList<Problem> problem_list = new ArrayList<Problem>();

	// Takes the indicator that is being used in formulation and related with
	// Main Process
	// private Hashtable<String, String> indicatorProcess = new
	// Hashtable<String, String>();

	// Map<String, DefaultPort> cellPortMap = new Hashtable<String,
	// DefaultPort>();

	public ArrayList<Problem> getProblem_list() {
		return problem_list;
	}

	public void setProblem_list(ArrayList<Problem> problem_list) {
		this.problem_list = problem_list;
	}

	/**
	 * Construct the Graph using the model as its Data Source
	 */
	public ProblemGraph() {
		this(new CustomGraphModel(), null);
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.problem_list.get(0).getName();
	}

	/**
	 * Construct the Graph using the model as its Data Source
	 * 
	 * @param GraphModel
	 * @param GraphLayoutCache
	 */
	public ProblemGraph(GraphModel model, GraphLayoutCache cache) {
		super(model, cache);
		// Make Ports Visible by Default
		setPortsVisible(true);
		// Use the Grid (but don't make it Visible)
		setGridEnabled(true);
		// Set the Grid Size to 10 Pixel
		setGridSize(6);
		// Set the Tolerance to 2 Pixel
		setTolerance(2);
		// Accept edits if click on background
		setInvokesStopCellEditing(true);
		// Allows control-drag
		setCloneable(true);
		// Jump to default port on connect
		setJumpToDefaultPort(true);
		// Use a Custom MarqueeHandler
		setMarqueeHandler(new ProblemMarqueeHandler(this));
		// Use a ProblemCellViewFactory
		setGraphFactory();
		this.getModel().addUndoableEditListener(undoManager);
	}

	/**
	 * Change the graph factory
	 */
	public void setGraphFactory() {
		this.getGraphLayoutCache().setFactory(new ProblemCellViewFactory());
	}

	/**
	 * Insert a cell on the point with the specific label. Used to maintain the
	 * indicators related with the main process. This method is called from
	 * TreeListener Class(double click over the tree) and from DragDropHandler
	 * class
	 * 
	 * @param point
	 * @param cellToInsert
	 */
	public void insertCell(Point2D point, MyProblemCustomCell cellToInsert)
			throws Exception {
		GraphEd.errorPane.clear();
		saved = false;
		if (isPainted(cellToInsert))
			throw new Exception("Error! The indicator "
					+ cellToInsert.toString() + " that belongs to process "
					+ tools.concatenateProcess(cellToInsert.getProcess())
					+ " is already in the current formulation");
		else {
			cellToInsert.getAttributes().applyMap(
					VertexManager.problemCellAttributes(snap((Point2D) point
							.clone())));
			this.getGraphLayoutCache().insert(cellToInsert);
			GraphEd.tableInsertedStatement = TabsComponentManager
					.getTableInsertedStatement();
			GraphEd.errorPane.clear();
		}
	}

	/**
	 * Insert a RDFTriple on Database
	 * 
	 * @param RDFTriple
	 */
	public void insertTriple(RDFTriple triple) {

		ConnectionToPostgres connection = PersistentManager
				.getConnectionToPostgreSQL();
		if (connection.tryConnection()) {
			try {
				String sql = "SELECT * FROM triple_rdf,problem WHERE problem.id_graph = triple_rdf.id_graph and "
						+ "subject ='"
						+ triple.getSubject().getLabel()
						+ "'"
						+ "and object = "
						+ "'"
						+ triple.getObject().getLabel()
						+ "'"
						+ "and subject_process = "
						+ "'"
						+ tools.concatenateProcess(triple.getSubjetProcess())
						+ "'"
						+ "and object_process = "
						+ "'"
						+ tools.concatenateProcess(triple.getObjectProcess())
						+ "'";
				java.sql.ResultSet r1 = connection.executeSql(sql);
				r1.next();
				int idTriple = r1.getInt("id_triple");
				java.sql.ResultSet r2 = connection.select("vertex_points",
						"id_triple =" + idTriple);
				r2.next();
				int subjectX = r2.getInt("subject_x");
				int subjectY = r2.getInt("subject_y");

				int objectX = r2.getInt("object_x");
				int objectY = r2.getInt("object_y");

				Point sourcePoint = new Point((int) subjectX, (int) subjectY);
				Point targetPoint = new Point((int) objectX, (int) objectY);

				// Getting the subject
				MyProblemCustomCell source = new MyProblemCustomCell(triple
						.getSubject().getLabel(), triple.getSubjetProcess());
				if (isPainted(source)) {
					source = getCell(source);
				} else {
					source.getAttributes().applyMap(
							VertexManager.problemCellAttributes(sourcePoint));
					getGraphLayoutCache().insert(source);
				}

				// Getting the Object
				MyProblemCustomCell target = new MyProblemCustomCell(triple
						.getObject().getLabel(), triple.getObjectProcess());
				if (isPainted(target)) {
					target = getCell(target);
				} else {
					target.getAttributes().applyMap(
							VertexManager.problemCellAttributes(targetPoint));
					getGraphLayoutCache().insert(target);
				}

				DefaultEdge edge = new DefaultEdge();
				Tools tools = new Tools();
				String frequency = tools.round(serializator
						.calculateTotalFrequencyForProblems(source, target), 3);
				if (Float.parseFloat(frequency) >= 0.25) {
					edge.getAttributes().applyMap(
							EdgeManager.problemEdgeAttributes());
				} else
					edge.getAttributes().applyMap(
							EdgeManager.problemEdgeAttributesLF());
				Port sourcePort = this.getDefaultPort(source);
				Port targetPort = this.getDefaultPort(target);
				edge.setUserObject(frequency);
				getGraphLayoutCache().insertEdge(edge, sourcePort, targetPort);

				String edgeSource = triple.getSubject().getLabel();
				String edgeTarget = triple.getObject().getLabel();

				acyclicGraph.addVertex(edgeSource);
				acyclicGraph.addVertex(edgeTarget);
				acyclicGraph.addEdge(edgeSource, edgeTarget, frequency);

				connection.disconnect();
			} catch (Exception e) {
				GraphEd.errorPane.printMessage(e);
			}
		}
	}

	/**
	 * 
	 */
	public void insertProblem(Problem problem) {
		this.problem_list.add(0, problem);
		if (problem != null) {
			for (int i = 0; i < problem.getTriples().size(); i++) {
				insertTriple(problem.getTriples().get(i));
			}
		}
	}

	// este metodo esta por gusto
	/**
	 * Insert a array of edges
	 * 
	 * @param ArrayList
	 *            edges
	 */
	public void insertGraph(ArrayList edges) {
		for (int i = 0; i < edges.size(); i++) {
			DefaultEdge edge = (DefaultEdge) edges.get(i);
			CustomCell source = (CustomCell) edge.getSource();
			CustomCell target = (CustomCell) edge.getTarget();

			graphLayoutCache.insert(source);
			graphLayoutCache.insert(target);
			graphLayoutCache.insertEdge(edge, source.getChildAt(0),
					target.getChildAt(0));
		}
	}

	/**
	 * Remove the selected cell
	 */
	public void removeCell() {
		Object[] cells = this.getSelectionCells();
		Object[] roots = this.getRoots();
		ArrayList<Object> edges = new ArrayList<Object>();

		if (cells != null) {
			this.getModel().remove(this.getDescendants(cells));
		}

		for (int i = 0; i < roots.length; i++) {
			if (roots[i] instanceof DefaultEdge) {
				if (((DefaultEdge) roots[i]).getSource() == null
						|| ((DefaultEdge) roots[i]).getTarget() == null) {
					edges.add(roots[i]);
				}
			}
		}
		this.getModel().remove(edges.toArray());
	}

	// este metodo esta por gusto
	/**
	 * @see org.jgraph.JGraph#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent e) {
		if (e != null) {
			// Fetch Cell under Mousepointer
			selectedCell = getFirstCellForLocation(e.getX(), e.getY());
			if (selectedCell != null
					&& getCellBounds(selectedCell).getBounds().getCenterY() < e
							.getY() || selectedCell instanceof DefaultEdge)
				// Convert Cell to String and Return
				return convertValueToString(selectedCell);

		}
		return null;
	}

	/**
	 * Add to graph a mouse listener
	 */
	public void addMouseListener() {
		this.addMouseListener(new GraphListener(this));

	}

	// este metodo esta por gusto
	/**
	 * 
	 * If something is true it returns an array of cells corresponding the true
	 * values
	 */
	public Object[] getCells(boolean groups, boolean vertices, boolean ports,
			boolean edges) {
		return this.getGraphLayoutCache().getCells(groups, vertices, ports,
				edges);
	}

	/**
	 * Return all the cells in the current graph
	 * 
	 * @author Leo
	 */
	public ArrayList<MyProblemCustomCell> getCells() {
		Object[] roots = this.getRoots();
		ArrayList<MyProblemCustomCell> cells = new ArrayList<MyProblemCustomCell>();
		for (Object object : roots) {
			if (object instanceof MyProblemCustomCell)
				cells.add((MyProblemCustomCell) object);
		}
		return cells;
	}

	/**
	 * 
	 * @param cellName
	 * @param cellProcess
	 * @return the cell that has that name and process
	 */
	public MyProblemCustomCell getCell(MyProblemCustomCell cell) {
		ArrayList<MyProblemCustomCell> cells = getCells();
		for (MyProblemCustomCell myProblemCustomCell : cells) {
			if (myProblemCustomCell.equals(cell))
				return myProblemCustomCell;
		}
		return null;
	}

	/**
	 * Return the edges in the current graph
	 * 
	 * @author Leo
	 */
	public ArrayList<Object> getAllEdges() {
		ArrayList<Object> edgesList = new ArrayList<Object>();
		Object[] roots = this.getRoots();
		for (int i = 0; i < roots.length; i++) {
			Object object = roots[i];
			if (object instanceof DefaultGraphCell) {
				if (this.getModel().isEdge(object)) {
					edgesList.add(object);
				}
			}
		}
		return edgesList;
	}

	/**
	 * @author Leo
	 * @param sourceCell
	 * @param targetCell
	 * @return the edge that connect two cells
	 */
	public Object getEdge(MyProblemCustomCell sourceCell,
			MyProblemCustomCell targetCell) {
		Object ToReturn = null;
		ArrayList<Object> edgesList = getAllEdges();

		for (Object object : edgesList) {
			MyProblemCustomCell sourceVertex, targetVertex;
			GraphModel gm = this.getModel();
			sourceVertex = (MyProblemCustomCell) gm.getParent(gm
					.getSource(object));
			targetVertex = (MyProblemCustomCell) gm.getParent(gm
					.getTarget(object));
			if (sourceVertex.equals(sourceCell)
					&& targetVertex.equals(targetCell)) {
				ToReturn = object;
			}

		}

		return ToReturn;

	}

	public Port getDefaultPort(Object vertex) {
		// Iterate over all Children
		for (int i = 0; i < this.getModel().getChildCount(vertex); i++) {
			// Fetch the Child of Vertex at Index i
			Object child = this.getModel().getChild(vertex, i);
			// Check if Child is a Port
			if (child instanceof Port) {
				return (Port) child;
			}
		}
		Port toReturn = (Port) ((MyProblemCustomCell) vertex).addPort();
		return toReturn;
	}

	/**
	 * return true if the node already exist in the graph
	 * 
	 * @author Leo
	 * 
	 * @param node
	 *            Name
	 */
	public boolean isPainted(MyProblemCustomCell cell) {
		boolean flag = false;
		Object[] roots = this.getRoots();
		for (Object object : roots) {
			if (object instanceof CustomCell) {
				if (((MyProblemCustomCell) object).equals(cell))
					flag = true;
			}
		}

		return flag;
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 *
	 * @description: Method to look for the classification of a problem's level
	 *
	 * @param causes
	 * @return
	 * @throws SQLException
	 */
	private Hashtable<String, MyProblemCustomCell[]> classifyProblem(MyProblemCustomCell[] causes)
			throws SQLException {
		String mainProcess = "";
		//Here there are stored all process with it's occurrences count
		Hashtable<String, Integer> processList = new Hashtable<>();
		
		if (causes.length == 1) {
			//If there is only one root cause the it's process is the level classification
			mainProcess = causes[0].getProcess()[0];
		} else {
			//Iterating over the level to fill the process hash
			for (int i = 0; i < causes.length; i++) {
				String key = causes[i].getProcess()[0];
				if (!(processList.containsKey(key))) {
					processList.put(key, 1);
				} else {
					int value = processList.get(key);
					value++;
					processList.put(key, value);
				}
			}
			
			//Counting process occurrences
			int cont = 0;
			Set<String> keys = processList.keySet();
			for (String key : keys) {
				if(processList.get(key) > cont){
					cont = processList.get(key);
					mainProcess = key;
				}else if(processList.get(key) == cont){
					mainProcess = "No Level";
				}
			}
		}
		
		Hashtable<String, MyProblemCustomCell[]> result = new Hashtable<>();
		result.put(mainProcess, causes);
		return result;
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 *
	 * @description: Method to load levels, for example 0 is rootcauses leve, 1 is first level, etc.
	 *
	 * @param level
	 * @return
	 */
	public MyProblemCustomCell[] loadLevel(Integer level, MyProblemCustomCell[] parents){
		MyProblemCustomCell[] causes = null;
		if(level == 0){
			causes = new MyProblemCustomCell[getRootCauses().size()];
			getRootCauses().toArray(causes);
		}else{
			ArrayList<MyProblemCustomCell> levelCauses = getLevelCauses(parents);
			causes = new MyProblemCustomCell[levelCauses.size()];
			levelCauses.toArray(causes);
		}
		return causes;
	}

	/**
	 * Return the root cause of the problem, only one, the most significant
	 * 
	 * @param idProblem
	 * @return
	 * @throws SQLException
	 */
	public String classifyProblem() throws SQLException {	
		String process = "";
		int cont = 0;
		MyProblemCustomCell[] causes = null;
		do {			
			//Getting levels to verify classification
			if(cont == 0)causes = loadLevel(cont ++, null);
			Hashtable<String, MyProblemCustomCell[]> mainProcess = classifyProblem(causes);
			process = mainProcess.keys().nextElement();
			if(cont > 0 && process.equals("No Level"))causes = loadLevel(cont ++, mainProcess.get(process));
//			System.out.println(process);
		} while (process.equals("No Level"));
		return process;
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 *
	 * @description: Method to get all nodes into different levels
	 *
	 * @param parents
	 * @return
	 */
	public ArrayList<MyProblemCustomCell> getLevelCauses(MyProblemCustomCell[] parents) {
		ArrayList<MyProblemCustomCell> levelCauses = new ArrayList<MyProblemCustomCell>();
		ArrayList<Object> edgesList = getAllEdges();
		GraphModel gm = this.getModel();
		// Iterating over the parents list
		for (int i = 0; i < parents.length; i++) {
			MyProblemCustomCell parent = parents[i];
			for (int j = 0; j < edgesList.size(); j++) {
				// Getting the source nodes from edgeList
				MyProblemCustomCell candidateNodeSource = (MyProblemCustomCell) gm.getParent(gm.getSource(edgesList.get(j)));
				if(parent.equals(candidateNodeSource)){
					// If the the source node in the edgeList is in parents[] then analyze the target nodes
					MyProblemCustomCell candidateNodeTarget = (MyProblemCustomCell) gm.getParent(gm.getTarget(edgesList.get(j)));
					if(!(levelCauses.contains(candidateNodeTarget))){
						// If the target node isn't stored then store it
						levelCauses.add(candidateNodeTarget);
					}
				}
			}
		}
		return levelCauses;
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 *
	 * @description: Method to get all nodes that are not object of nobody else
	 *
	 * @return
	 */
	public ArrayList<MyProblemCustomCell> getRootCauses() {
		ArrayList<MyProblemCustomCell> rootCauses = new ArrayList<MyProblemCustomCell>();
		ArrayList<Object> edgesList = getAllEdges();
		GraphModel gm = this.getModel();
		//This is when there is only one root cause
		if (edgesList.size() == 1) {
			MyProblemCustomCell sourceVertex = (MyProblemCustomCell) gm
					.getParent(gm.getSource(edgesList.get(0)));
			rootCauses.add(sourceVertex);
		} else {
			//Iterating over the edges list
			for (int i = 0; i < edgesList.size(); i++) {
				boolean flag = true;
				//Getting the source node for this edge
				MyProblemCustomCell candidateNodeSource = (MyProblemCustomCell) gm.getParent(gm.getSource(edgesList.get(i)));
				//Iterating over the edge list 
				for (int j = 0; j < edgesList.size(); j++) {
					//Getting the target edge
					MyProblemCustomCell candidateNodeTarget = (MyProblemCustomCell) gm.getParent(gm.getTarget(edgesList.get(j)));
					//If the node appear like a target and a source node, then it can be a root cause
					if(candidateNodeSource.equals(candidateNodeTarget)){
						flag = false;
					}
				}
				
				//If the node only appear like a source node, then it is a root node
				if(flag){
					//Adding the candidate node if it isn't in the root causes list
					if(!(rootCauses.contains(candidateNodeSource))){
						rootCauses.add(candidateNodeSource);
					}else{
						int index = rootCauses.indexOf(candidateNodeSource);
						String process = rootCauses.get(index).getProcess()[0];
						//Adding the candidate node if it is in the root causes list but in a different process
						if(!(candidateNodeSource.getProcess()[0].equals(process))){
							rootCauses.add(candidateNodeSource);
						}
					}
				}
			}
		}
		return rootCauses;
	}
	
	/**
	 * @author Héctor Grabiel Martín Varona
	 *
	 * @description: Method to get all nodes that doesn't have childs
	 *
	 * @return
	 */
	public ArrayList<MyProblemCustomCell> getUnwantedEffects() {
		ArrayList<MyProblemCustomCell> undeseateEffects = new ArrayList<MyProblemCustomCell>();
		ArrayList<Object> edgesList = getAllEdges();
		GraphModel gm = this.getModel();
		//This is when there is only one unwanted effect
		if (edgesList.size() == 1) {
			MyProblemCustomCell sourceVertex = (MyProblemCustomCell) gm
					.getParent(gm.getTarget(edgesList.get(0)));
			undeseateEffects.add(sourceVertex);
		} else {
			//Iterating over the edges list
			for (int i = 0; i < edgesList.size(); i++) {
				boolean flag = true;
				//Getting the target node for this edge
				MyProblemCustomCell candidateNodeTarget = (MyProblemCustomCell) gm.getParent(gm.getTarget(edgesList.get(i)));
				//Iterating over the edge list 
				for (int j = 0; j < edgesList.size(); j++) {
					//Getting the source edge
					MyProblemCustomCell candidateNodeSource = (MyProblemCustomCell) gm.getParent(gm.getSource(edgesList.get(j)));
					//If the node appear like a target and a source node, then it can be a unwanted effect
					if(candidateNodeTarget.equals(candidateNodeSource)){
						flag = false;
					}
				}
				
				//If the node only appear like a target node, then it is an unwanted effect
				if(flag){
					//Adding the candidate node if it isn't in the unwanted effect list
					if(!(undeseateEffects.contains(candidateNodeTarget))){
						undeseateEffects.add(candidateNodeTarget);
					}else{
						int index = undeseateEffects.indexOf(candidateNodeTarget);
						String process = undeseateEffects.get(index).getProcess()[0];
						//Adding the candidate node if it is in the unwanted effect list list but in a different process
						if(!(candidateNodeTarget.getProcess()[0].equals(process))){
							undeseateEffects.add(candidateNodeTarget);
						}
					}
				}
			}
		}
//		System.out.println(undeseateEffects);
		return undeseateEffects;
	}

	public GraphUndoManager getUndoManager() {
		return undoManager;
	}

	public void setUndoManager(GraphUndoManager undoManager) {
		this.undoManager = undoManager;
	}

	/**
	 * @author Leo
	 * @return true if the problem was saved after the last change
	 */
	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public String getSavedName() {
		return savedName;
	}

	public void setSavedName(String savedName) {
		this.savedName = savedName;
	}

	public DirectedAcyclicGraph<Object, Object> getAcyclicGraph() {
		return acyclicGraph;
	}

	public void setAcyclicGraph(
			DirectedAcyclicGraph<Object, Object> acyclicGraph) {
		this.acyclicGraph = acyclicGraph;
	}

	/**
	 * <P>
	 * Check the connections between all the vertex
	 * </p>
	 * 
	 * @return true if the graph has all this vertex connected
	 * @throws CycleFoundException
	 *             if exist a cyclic connection in the graph
	 */
	public boolean connectivitiInspector() throws CycleFoundException {
		GraphTools graphTools = new GraphTools(this);
		return graphTools.isAllConected();
	}

}
