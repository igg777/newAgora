package system.rdf.graph;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;

import system.rdf.CustomCell;
import system.rdf.EditorGraphUI;
import system.rdf.SolutionCellViewFactory;
import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.dataBase.RDFTriple;
import system.rdf.dataBase.Solution;
import system.rdf.handlers.SolutionMarqueeHandler;
import system.rdf.listeners.GraphListener;
import system.rdf.ui.GraphEd;
import system.rename.Tools;

/*Class that represent the place where the solution is modeled*/
public class SolutionGraph extends JGraph {
	/**
     * 
     */
	private static final long serialVersionUID = -4858341142061570762L;
	/**
	 * Selected Cell
	 */
	protected Object selectedCell = null;
	protected int id = 0;
	protected ArrayList insertedCells = new ArrayList();
	private MyProblemCustomCell vertex;
	private GraphUndoManager undoManager = new GraphUndoManager();
	// This flag indicate if the problem was saved after the last change
	private boolean savedAsAlternative = false;
	// the name which the problem has been saved
	private String savedName = "New Alternative";
	private RDFSerializator serializator = new RDFSerializator();
	private Tools tools = new Tools();

	/**
	 * Constructor
	 * 
	 * @param modelToScor
	 */
	public SolutionGraph() {
		this(new CustomGraphModel(), null);
		setGraphFactory(); // Change the graph factory with a new cell view
		ToolTipManager.sharedInstance().registerComponent(this);
	}

	/**
	 * Construct the Graph using the model as its Data Source
	 * 
	 * @param model
	 * @param cache
	 */
	public SolutionGraph(GraphModel model, GraphLayoutCache cache) {
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
		// add mouseListener
		this.addMouseListener(new GraphListener(this));

		setMarqueeHandler(new SolutionMarqueeHandler(this));

		this.getModel().addUndoableEditListener(undoManager);
	}

	public String getCount() {
		return "Cont: " + String.valueOf(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	/**
	 * Return the tool tip text
	 */
	public String getToolTipText(MouseEvent e) {
		if (e != null) {
			// Fetch Cell under Mouse pointer
			Object c = getFirstCellForLocation(e.getX(), e.getY());
			if (c != null
					&& getCellBounds(c).getBounds().getCenterY() < e.getY()
					|| c instanceof DefaultEdge)
				// Convert Cell to String and Return
				return convertValueToString(c);
			else if (c != null
					&& getCellBounds(c).getBounds().getCenterY() > e.getY()) {
				String accion = ((CustomCell) c).getTextToUp();
				if (accion.equals("..."))
					return "Accion: null";
				else
					return accion;
			}
		}
		return null;
	}

	/**
	 * Change the graph factory
	 */
	public void setGraphFactory() {
		this.getGraphLayoutCache().setFactory(new SolutionCellViewFactory());
	}

	/**
	 * Change the GraphUI
	 */
	public void setUI() {
		this.setUI(new EditorGraphUI());

	}

	/**
	 * Use a Custom MarqueeHandler
	 */
	public void setMarqueeHandler() {
		setMarqueeHandler(new SolutionMarqueeHandler(this));
	}

	// este metodo ta por gusto.
	/**
	 * Insert into GraphModel a group of cells
	 * 
	 * @param Object
	 *            [] cells
	 */
	public void insert(Object[] cells) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] instanceof DefaultGraphCell)
				((DefaultGraphCell) cells[i]).getAttributes().applyMap(
						VertexManager.solutionCellAttributes());
			getGraphLayoutCache().insert(cells[i]);
		}
	}

	/**
	 * Insert into GraphModel a cell
	 * 
	 * @param Object
	 *            cell
	 */
	public void insert(Object cell) {
		if (cell != null)
			this.getGraphLayoutCache().insert(cell);
	}

	/**
	 * Insert a cell on the point with the specific label and insert the main
	 * process at which that cell belong
	 * 
	 * @param Point2D
	 * @param String
	 */
	public void insertCell(Point2D point, String insertAs, String[] process) {
		savedAsAlternative = false;
		GraphEd.errorPane.clear();
		if (isPainted(new MyProblemCustomCell(insertAs, process)))
			GraphEd.errorPane.printMessage(new Exception(
					"Error! The indicator " + insertAs
							+ " is already in the current formulation"));
		else {
			DefaultPort port = new DefaultPort("Floating");
			vertex = new MyProblemCustomCell(insertAs, process);
			vertex.getAttributes().applyMap(
					VertexManager.solutionCellAttributes((snap((Point2D) point
							.clone()))));

			getGraphLayoutCache().insert(vertex);
		}
	}

	/**
	 * Insert into GraphModel an edge
	 * 
	 * @param edge
	 * @param source
	 * @param target
	 */
	public void insertEdge(Object edge, Object source, Object target) {
		this.getGraphLayoutCache().insertEdge(edge, source, target);
	}

	/**
	 * Insert into GraphModel a triple(two nodes with edge)
	 * 
	 * @param edge
	 * @param sourceVertex
	 * @param targetVertex
	 * @param sourceVertexProcess
	 * @param targetVertexProcess
	 */
	public void insertTriple(Object sourceVertex, Object targetVertex,
			String[] sourceVertexProcess, String[] targetVertexProcess) {

		Object source = ((DefaultGraphCell) sourceVertex).getChildAt(0);
		Object target = ((DefaultGraphCell) targetVertex).getChildAt(0);

		((MyProblemCustomCell) sourceVertex).getAttributes().applyMap(
				VertexManager.solutionCellAttributes());
		((MyProblemCustomCell) targetVertex).getAttributes().applyMap(
				VertexManager.solutionCellAttributes());
		// the code bellow is to get the correct frequency
		MySolutionEdge edgessss = new MySolutionEdge(this);
		Tools tools = new Tools();
		String frequency = tools.round(serializator
				.calculateTotalFrequencyForAlternatives(
						new MyProblemCustomCell(sourceVertex.toString(),
								sourceVertexProcess), new MyProblemCustomCell(
								targetVertex.toString(), targetVertexProcess)),
				3);
		if (Float.parseFloat(frequency) >= 0.25) {
			edgessss.getAttributes().applyMap(
					EdgeManager.problemEdgeAttributes());
		} else
			edgessss.getAttributes().applyMap(
					EdgeManager.problemEdgeAttributesLF());
		edgessss.setUserObject(frequency);
		// end off calculating frequency
		insert(sourceVertex);
		insert(targetVertex);

		insertEdge(edgessss, source, target);
	}

	/**
	 * Insert into the graph a RDF Triple
	 * 
	 * @param RDFTriple
	 *            triple
	 */
	public void insertTriple(RDFTriple triple) {
		ConnectionToPostgres connection = PersistentManager
				.getConnectionToPostgreSQL();
		if (connection.tryConnection()) {
			try {
				String correctiveAction = "";
				String userObject = "";
				String sql = "SELECT * FROM triple_rdf,solution WHERE solution.id_graph = triple_rdf.id_graph and "
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
				correctiveAction = r1.getString("predicate");
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
							VertexManager.solutionCellAttributes(sourcePoint));
					getGraphLayoutCache().insert(source);
				}

				// Getting the Object
				MyProblemCustomCell target = new MyProblemCustomCell(triple
						.getObject().getLabel(), triple.getObjectProcess());
				if (isPainted(target)) {
					target = getCell(target);
				} else {
					target.getAttributes().applyMap(
							VertexManager.solutionCellAttributes(targetPoint));
					getGraphLayoutCache().insert(target);
				}

				MySolutionEdge edge = new MySolutionEdge(this);
				Tools tools = new Tools();
				String frequency = tools.round(
						serializator.calculateTotalFrequencyForAlternatives(
								source, target), 3);
				if (correctiveAction.equals("No corrective action assigned")) {
					userObject = frequency;
				} else{
					userObject = frequency + "<<Corrective action added>>";
					edge.setCorrectiveAction(correctiveAction);				
				}
				if (Float.parseFloat(frequency) >= 0.25) {
					edge.getAttributes().applyMap(
							EdgeManager.problemEdgeAttributes());
				} else
					edge.getAttributes().applyMap(
							EdgeManager.problemEdgeAttributesLF());
				Port sourcePort = this.getDefaultPort(source);
				Port targetPort = this.getDefaultPort(target);
				edge.setUserObject(userObject);
				getGraphLayoutCache().insertEdge(edge, sourcePort, targetPort);
				connection.disconnect();
			} catch (Exception e) {
				GraphEd.errorPane.printMessage(e);
				
			}
		}
	}

	/*
	 * } else { // If exists the subject if
	 * (cellPortMap.containsKey(source.toString()) == true) {
	 * 
	 * target.addPort(); Port targetPort = this.getDefaultPort(target, this
	 * .getModel()); getGraphLayoutCache().insert(target);
	 * cellPortMap.put(target.toString(), (DefaultPort) targetPort);
	 * getGraphLayoutCache().insertEdge(edge,
	 * cellPortMap.get(source.toString()), targetPort); } else if
	 * (cellPortMap.containsKey(target.toString())) { source.addPort(); Port
	 * sourcePort = this.getDefaultPort(source, this .getModel());
	 * getGraphLayoutCache().insert(source); cellPortMap.put(source.toString(),
	 * (DefaultPort) sourcePort); getGraphLayoutCache().insertEdge(edge,
	 * sourcePort, cellPortMap.get(target.toString())); } }
	 * 
	 * // Showing frecuency over edge double totalFrecuency = RDFSerializator
	 * .calculateTotalFrequency(source.toString(), target .toString(),
	 * source.getProcess(), target .getProcess()); // Writing over edges
	 * problems String showFrecuency = Double.toString(totalFrecuency); if
	 * (showFrecuency.equals("0.0")) { showFrecuency = "0.0"; } else if
	 * (showFrecuency.length() == 3) {
	 * 
	 * } else { showFrecuency = showFrecuency.substring(0, 4); }
	 * edge.setUserObject(showFrecuency); Map attr = ((DefaultEdge)
	 * edge).getAttributes(); getGraphLayoutCache().editCell(edge, attr);
	 * 
	 * } catch (SQLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * 
	 * 
	 * / //this cont is to know when a node is in the graph previously
	 * 
	 * // if the cont = 0 no node is already painted, if is 1 node 1 was found,
	 * if is 2 node 2 //and if is 3 the two nodes was painted before
	 * ArrayList<Object> graphTriple = triple.getGraphTriple(); double
	 * totalfrequency = 0.0; MyProblemCustomCell source = (MyProblemCustomCell)
	 * graphTriple.get(0); MyProblemCustomCell target = (MyProblemCustomCell)
	 * graphTriple.get(1); DefaultEdge edge = (DefaultEdge) graphTriple.get(2);
	 * if(isPainted(source.toString(), source.getProcess())){ source =
	 * getCell(source.toString(), source.getProcess()); } else{ Point2D point =
	 * triple.getSubject().getPoint(); System.out.println("este es el pto " +
	 * point.toString()); insertCell(point, source.toString(),
	 * source.getProcess()); source = getCell(source.toString(),
	 * source.getProcess()); }
	 * 
	 * if(isPainted(target.toString(), target.getProcess())){ target =
	 * getCell(target.toString(), target.getProcess()); } else{ Point2D point =
	 * triple.getObject().getPoint(); insertCell(point, target.toString(),
	 * target.getProcess()); target = getCell(target.toString(),
	 * target.getProcess()); }
	 * 
	 * Port sourcePort = getDefaultPort(source); Port targetPort =
	 * getDefaultPort(target); //Port sourcePort = (Port) source.getChildAt(0);
	 * //Port targetPort = (Port) target.getChildAt(0);
	 * 
	 * Point2D sourcePoint = triple.getSubject().getPoint(); Point2D targetPoint
	 * = triple.getObject().getPoint();
	 * 
	 * source.getAttributes().applyMap(
	 * VertexManager.solutionCellAttributes(sourcePoint));
	 * target.getAttributes().applyMap(
	 * VertexManager.solutionCellAttributes(targetPoint));
	 * edge.getAttributes().applyMap(EdgeManager.solutionEdgeAttributes());
	 * String correctiveAction = edge.getUserObject().toString(); totalfrequency
	 * = RDFSerializator.calculateTotalFrequency(source.toString(),
	 * target.toString(), source.getProcess(), target.getProcess()); String
	 * userObject = totalfrequency+"<<"+correctiveAction+">>";
	 * edge.setUserObject(userObject); getGraphLayoutCache().insertEdge(edge,
	 * sourcePort, targetPort); / int j = insertedCells.size(); if (j > 0) { for
	 * (int i = 0; i < j; i++) { MyProblemCustomCell cell =
	 * (MyProblemCustomCell) insertedCells .get(i); Map map =
	 * cell.getAttributes(); Rectangle2D rect = GraphConstants.getBounds(map);
	 * String userObect = cell.getUserObject().toString(); Point2D pt = new
	 * Point((int) rect.getX(), (int) rect.getY());
	 * 
	 * if (userObect.equalsIgnoreCase(source.getUserObject() .toString()) &&
	 * pt.equals(sourcePoint)) {
	 * 
	 * Port port = (Port) cell.getChildAt(0); edge.setSource(port);
	 * getGraphLayoutCache().insert(target); totalfrequency =
	 * RDFSerializator.calculateTotalFrequency(cell.toString(),
	 * target.toString(), cell.getProcess(), target.getProcess());
	 * getGraphLayoutCache().insertEdge(edge, port, targetPort); sdsdsds
	 * insertedCells.add(target); } if
	 * (userObect.equalsIgnoreCase(target.getUserObject() .toString()) &&
	 * pt.equals(targetPoint)) { Port port = (Port) cell.getChildAt(0);
	 * edge.setSource(port); getGraphLayoutCache().insert(source);
	 * getGraphLayoutCache().insertEdge(edge, sourcePort, port);
	 * insertedCells.add(source); } }
	 * 
	 * } else if (insertedCells.size() == 0) {
	 * 
	 * getGraphLayoutCache().insert(source);
	 * getGraphLayoutCache().insert(target);
	 * getGraphLayoutCache().insertEdge(edge, sourcePort, targetPort);
	 * insertedCells.add(source); insertedCells.add(target); }
	 */

	/**
	 * Insert a solution into the graph
	 * 
	 * @param Solution
	 *            solution
	 */
	public void insertSolution(Solution solution) {
		if (solution != null) {
			for (int i = 0; i < solution.getTriples().size(); i++) {
				insertTriple(solution.getTriples().get(i));
			}
		}
	}

	/**
	 * Remove the selected cell
	 */
	public void removeCell() {
		savedAsAlternative = false;
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
				if (cell.equals((MyProblemCustomCell)object))
					flag = true;
			}
		}

		return flag;
	}

	public GraphUndoManager getUndoManager() {
		return undoManager;
	}

	public void setUndoManager(GraphUndoManager graphUndoManager) {
		this.undoManager = graphUndoManager;
	}

	/**
	 * @author Leo
	 * @return true if the solution was saved as alternative after the last
	 *         change
	 */
	public boolean isSavedAsAlternative() {
		return savedAsAlternative;
	}

	public void setSavedAsAlternative(boolean savedAsAlternative) {
		this.savedAsAlternative = savedAsAlternative;
	}

	public String getSavedName() {
		return savedName;
	}

	public void setSavedName(String savedName) {
		this.savedName = savedName;
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
			if (cell.equals(myProblemCustomCell))
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
	 * @param sourceCellName
	 * @param targetCellName
	 * @return the edge that connect two cells
	 */
	public Object getEdge(String sourceCell, String targetCell,
			String sourceCellProcess, String targetCellProcces) {
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
	
	/**
	 * @author Hector
	 * @param sourceCellName
	 * @param targetCellName
	 */
	public void removeEdge(String sourceCell, String targetCell) {
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
				gm.remove(new Object[]{sourceVertex,targetVertex});
			}
		}
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

}
