package system.rdf.handlers;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import system.rdf.CustomCell;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.listeners.ProblemKeyListener;
import system.rdf.ui.GraphEd;
import system.rdf.utils.TabsComponentManager;
import system.rename.SubProblems;
import system.rename.Tools;
import system.rename.cutPoint.CutPoint;
import system.rename.cutPoint.ElementOfThePopulation;
import system.rename.cutPoint.SuggestedController;
import view.suggest.UISuggested;

/**
 * Custom MarqueeHandler that Connects Vertices and Displays PopupMenus
 * 
 * @author Armando
 */
public class ProblemMarqueeHandler extends BasicMarqueeHandler {
	// Holds the Start and the Current Point
	protected Point2D start, current;

	// Holds the First and the Current Port
	protected PortView port, firstPort;
	protected JGraph graph;
	private RDFSerializator serializator = new RDFSerializator();
	private Hashtable<String, String> commonProcessInterface;
	public static boolean commonInterface = false;

	public ProblemMarqueeHandler(JGraph graph) {
		this.graph = graph;
		this.graph.addKeyListener(new ProblemKeyListener(graph));
		commonProcessInterface = new Hashtable<String, String>();
		commonProcessInterface.put("P.Plan", "S.Source");
		commonProcessInterface.put("S.Source", "M.Make");
		commonProcessInterface.put("M.Make", "D.Deliver");
		commonProcessInterface.put("D.Deliver", "R.Return");
		commonProcessInterface.put("R.Return", "P.Plan");
	}

	// Override to Gain Control (for PopupMenu and ConnectMode)
	public boolean isForceMarqueeEvent(MouseEvent e) {
		if (e.isShiftDown())
			return false;
		// If Right Mouse Button we want to Display the PopupMenu
		if (SwingUtilities.isRightMouseButton(e))
			// Return Immediately
			return true;
		// Find and Remember Port
		port = getSourcePortAt(e.getPoint());
		// If Port Found and in ConnectMode (=Ports Visible)
		if (port != null && graph.isPortsVisible())
			return true;
		// Else Call Superclass
		return super.isForceMarqueeEvent(e);
	}

	// Display PopupMenu or Remember Start Location and First Port
	public void mousePressed(final MouseEvent e) {
		// If Right Mouse Button
		if (SwingUtilities.isRightMouseButton(e)) {
			// Find Cell in model Coordinates
			Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
			// Create PopupMenu for the Cell
			JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
			// Display PopupMenu
			menu.show(graph, e.getX(), e.getY());
			// Else if in ConnectMode and Remembered Port is Valid
		} else if (port != null && graph.isPortsVisible()) {
			// Remember Start Location
			start = graph.toScreen(port.getLocation());

			// Remember First Port
			firstPort = port;

		} else {
			// Call Superclass
			super.mousePressed(e);
		}
	}

	// Find Port under Mouse and Repaint Connector
	public void mouseDragged(MouseEvent e) {
		// Fetch Graphics from Graph
		Graphics g = graph.getGraphics();
		// If remembered Start Point is Valid
		if (start != null) {
			// Reset Remembered Port
			PortView newPort = getTargetPortAt(e.getPoint());
			// Do not flicker (repaint only on real changes)
			if (newPort == null || newPort != port) {
				// Xor-Paint the old Connector (Hide old Connector)
				paintConnector(Color.BLACK, graph.getBackground(), g);
				// If Port was found then Point to Port Location
				port = newPort;
				if (port != null)
					current = graph.toScreen(port.getLocation());
				// Else If no Port was found then Point to Mouse Location
				else
					current = graph.snap(e.getPoint());
				// Xor-Paint the new Connector
				paintConnector(graph.getBackground(), Color.black, g);
			}
		}
		// Call Superclass
		super.mouseDragged(e);
	}

	public PortView getSourcePortAt(Point2D point) {
		// Disable jumping
		graph.setJumpToDefaultPort(false);
		PortView result;
		try {
			// Find a Port view in model Coordinates and Remember
			result = graph.getPortViewAt(point.getX(), point.getY());
		} finally {
			graph.setJumpToDefaultPort(true);
		}
		return result;
	}

	// Find a Cell at point and Return its first Port as a PortView
	protected PortView getTargetPortAt(Point2D point) {
		// Find a Port view in model Coordinates and Remember
		return graph.getPortViewAt(point.getX(), point.getY());
	}

	// Connect the First Port and the Current Port in the Graph or Repaint
	public void mouseReleased(MouseEvent e) {

		// If Valid Event, Current and First Port
		if (e != null && port != null && firstPort != null && firstPort != port) {
			// Then Establish Connection
			connect((Port) firstPort.getCell(), (Port) port.getCell());
			e.consume();
			// Else Repaint the Graph
		} else
			graph.repaint();
		// Reset Global Vars
		firstPort = port = null;
		start = current = null;

		graph.repaint();
		// Call Superclass
		super.mouseReleased(e);
	}

	// Show Special Cursor if Over Port
	public void mouseMoved(MouseEvent e) {
		// Check Mode and Find Port
		if (e != null && getSourcePortAt(e.getPoint()) != null
				&& graph.isPortsVisible()) {
			// Set Cusor on Graph (Automatically Reset)
			graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
			// Consume Event
			// Note: This is to signal the BasicGraphUI's
			// MouseHandle to stop further event processing.
			e.consume();
		} else
			// Call Superclass
			super.mouseMoved(e);
	}

	// Use Xor-Mode on Graphics to Paint Connector
	protected void paintConnector(Color fg, Color bg, Graphics g) {
		// Set Foreground
		g.setColor(fg);
		// Set Xor-Mode Color
		g.setXORMode(bg);
		// Highlight the Current Port
		paintPort(graph.getGraphics());
		// If Valid First Port, Start and Current Point
		if (firstPort != null && start != null && current != null)
			// Then Draw A Line From Start to Current Point
			g.drawLine((int) start.getX(), (int) start.getY(), (int) current
					.getX(), (int) current.getY());
	}

	// Use the Preview Flag to Draw a Highlighted Port
	protected void paintPort(Graphics g) {
		// If Current Port is Valid
		if (port != null) {
			// If Not Floating Port...
			boolean o = (GraphConstants.getOffset(port.getAllAttributes()) != null);
			// ...Then use Parent's Bounds
			Rectangle2D r = (o) ? port.getBounds() : port.getParentView()
					.getBounds();
			// Scale from model to Screen
			r = graph.toScreen((Rectangle2D) r.clone());
			// Add Space For the Highlight Border
			r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r
					.getHeight() + 6);
			// Paint Port in Preview (=Highlight) Mode
			graph.getUI().paintCell(g, port, r, true);
		}
	}

	/**
	 * XXX AQUI ES DONDE HAY QUE IMPLEMENTAR QUE EL GRAFO SEA DIRIGIDO Insert a
	 * new edge between source and target
	 * 
	 * @param Port
	 *            source
	 * @param Port
	 *            target
	 */
	public void connect(Port source, Port target) {
		((ProblemGraph) graph).setSaved(false);
		Tools tools = new Tools();
		// erasing the table tableInsertedStatement
		GraphEd.tableInsertedStatement = TabsComponentManager
				.getTableInsertedStatement();
		GraphEd.errorPane.clear();
		// Construct Edge with no label
		DefaultEdge edge = new DefaultEdge();
		// Verify that source and target name are different.
		// Graph could not allow cycle
		// Port belong to cell and to obtain cell we need to navigate throw port

		if (graph.getModel().acceptsSource(edge, source)
				&& graph.getModel().acceptsTarget(edge, target)) {
			// Insert the Edge and its Attributes
			/*
			 * if(!(source.edges().hasNext()&& target.edges().hasNext())){
			 * DefaultEdge edgeSource = (DefaultEdge) source.edges().next();
			 * DefaultEdge edgeTarget = (DefaultEdge) target.edges().next();
			 * if(!edgeSource.equals(edgeTarget)){
			 * graph.getGraphLayoutCache().insertEdge(edge,source,target); }else
			 * graph.setCursor(DragSource.DefaultMoveNoDrop); }
			 */
			graph.getGraphLayoutCache().insertEdge(edge, source, target);

			edge.setSource(source);
			edge.setTarget(target);

			MyProblemCustomCell vertexSource = (MyProblemCustomCell) DefaultGraphModel
					.getSourceVertex(graph.getModel(), edge);
			MyProblemCustomCell vertexTarget = (MyProblemCustomCell) DefaultGraphModel
					.getTargetVertex(graph.getModel(), edge);

			// Process to compare if they have a common interface in Supply
			// Chain
			String sourceProcess = vertexSource.getProcess()[0];
			String targetProcess = vertexTarget.getProcess()[0];

			if (sourceProcess.equals(targetProcess)) {
				commonInterface = true;
			} else {
				Enumeration enum1 = commonProcessInterface.keys();
				while (enum1.hasMoreElements()) {
					String key = (String) enum1.nextElement();
					// System.out.println("KEY " + key);
					// System.out.println("Value " +
					// commonProcessInterface.get(key));
					if (key.equals(sourceProcess)
							&& commonProcessInterface.get(key).equals(
									targetProcess)) {
						commonInterface = true;
					}
				}
			}
			// Saying visual messages
			if (commonInterface == false)
				GraphEd.errorPane.setText("The Indicator "
						+ vertexSource.toString() + " belongs to a Process "
						+ sourceProcess + " that has a different common "
						+ " interface that indicator "
						+ vertexTarget.toString() + " which belong to "
						+ "process " + targetProcess);

			// Calculating total frecuency
			String totalFrequency = tools.round(serializator
					.calculateTotalFrequencyForProblems(vertexSource,
							vertexTarget), 3);
			// If the frecuency is lower that a given value the edge will be
			// red

			// Writing over edges problems

			// comment by leo
			/*
			 * //ESTO HAY QUE CAMBIARLO LUEGO if(showFrecuency.contains("E")){
			 * showFrecuency = "0.01"; } //este if creo que no tiene logica(leo)
			 * if(showFrecuency.equals("0.0")){ showFrecuency = "0.0"; } else if
			 * (showFrecuency.length()==3){
			 * 
			 * }else { showFrecuency = showFrecuency.substring(0,4); }
			 */
			// Showing frecuency over edge
			edge.setUserObject(totalFrequency);
			if (Double.parseDouble(totalFrequency) < 0.25) {
				/*
				 * ESTO HAY QUE CAMBIARLO LUEGO Y PONERLO EN LA CLASE
				 * EdgeManager
				 */
				Map map = new Hashtable();
				// Add a Line End Attribute
				GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
				// Fill end line
				GraphConstants.setEndFill(map, true);
				// Add a label along edge attribute
				GraphConstants.setLabelAlongEdge(map, true);
				// Cannot edit the edge
				GraphConstants.setEditable(map, false);
				// Changing the color for the frecuency
				GraphConstants.setLineColor(map, Color.RED);
				graph.getGraphLayoutCache().editCell(edge, map);
			} else {
				/*
				 * ESTO HAY QUE CAMBIARLO LUEGO Y PONERLO EN LA CLASE
				 * EdgeManager
				 */
				Map map = new Hashtable();
				// Add a Line End Attribute
				GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
				// Fill end line
				GraphConstants.setEndFill(map, true);
				// Add a label along edge attribute
				GraphConstants.setLabelAlongEdge(map, true);
				// Cannot edit the edge
				GraphConstants.setEditable(map, false);

				graph.getGraphLayoutCache().editCell(edge, map);
			}

		}
	}

	/**
	 * Create a Popup Menu to the edge
	 * 
	 * @param pt
	 * @param cell
	 * @return JPopupMenu
	 */
	public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
		JPopupMenu menu = new JPopupMenu();
		if (cell != null) {
			// Edit
			if (!(cell instanceof DefaultEdge)) {
				menu.add(new AbstractAction("Edit") {
					public void actionPerformed(ActionEvent e) {
						graph.startEditingAtCell(cell);
					}
				});
			}

		}

		// Remove
		if (!graph.isSelectionEmpty()) {
			if (!(cell instanceof DefaultEdge)) {
				menu.addSeparator();
			}
			menu.add(new AbstractAction("Delete") {
				public void actionPerformed(ActionEvent e) {
					if (graph instanceof ProblemGraph) {
						((ProblemGraph) graph).removeCell();
						GraphEd.tableInsertedStatement = TabsComponentManager
								.getTableInsertedStatement();
					}
				}
			});
		}
		// Suggested nodes
		if (!graph.isSelectionEmpty()) {
			if ((cell instanceof MyProblemCustomCell)) {
				menu.addSeparator();
				CutPoint point = new CutPoint((MyProblemCustomCell) cell);
				menu.add(getSuggestedNodesJMItem(
						point.getSuggestedIndicators(),
						(MyProblemCustomCell) cell));
			}
		}

		// Find Intermediates
		if (!graph.isSelectionEmpty()) {
			if ((cell instanceof MyProblemCustomCell)) {
				menu.addSeparator();
				SubProblems obj = new SubProblems(graph);
				menu.add(obj.getIntermediateNods(cell.toString(),
						((MyProblemCustomCell) cell).getProcess(), graph));
			}
		}

		return menu;
	}

	private JMenuItem getSuggestedNodesJMItem(
			ArrayList<ElementOfThePopulation> elements,
			MyProblemCustomCell subject) {
		final ArrayList<ElementOfThePopulation> newElements = elements;
		final MyProblemCustomCell subjectCell = subject;
		JMenuItem toReturn = new JMenuItem();
		toReturn.setText("Suggested nodes");
		toReturn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								try {
									SuggestedController controller = new SuggestedController(
											newElements);
									UISuggested suggested = new UISuggested();
									suggested.setTableModel(controller
											.getTableModel());
									suggested.init(subjectCell,
											(ProblemGraph) graph);
									suggested.setVisible(true);

								} catch (Exception e2) {
									GraphEd.errorPane.printMessage(e2);
								}
							}
						});
					}
				}).start();
			}
		});

		return toReturn;
	}
}
