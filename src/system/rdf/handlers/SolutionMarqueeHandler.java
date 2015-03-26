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
import java.util.Hashtable;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;

import system.rdf.CustomCell;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.EdgeManager;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.MySolutionEdge;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.listeners.ProblemKeyListener;
import system.rdf.ui.GraphEd;
import system.rdf.utils.TabsComponentManager;
import system.rename.Tools;

public class SolutionMarqueeHandler extends BasicMarqueeHandler {
	// Holds the Start and the Current Point
	protected Point2D start, current;

	// Holds the First and the Current Port
	protected PortView port, firstPort;
	protected JGraph graph;
	private RDFSerializator serializator = new RDFSerializator();
	public SolutionMarqueeHandler(JGraph graph) {
		this.graph = graph;
		this.graph.addKeyListener(new ProblemKeyListener(graph));
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
		// If remembered Start Point is Valid
		if (start != null) {
			// Fetch Graphics from Graph
			Graphics g = graph.getGraphics();
			// Reset Remembered Port
			PortView newPort = getTargetPortAt(e.getPoint());
			// Do not flicker (repaint only on real changes)
			if (newPort == null || newPort != port) {
				// Xor-Paint the old Connector (Hide old Connector)
				// paintConnector(Color.black, graph.getBackground(), g);
				// If Port was found then Point to Port Location
				port = newPort;
				if (port != null)
					current = graph.toScreen(port.getLocation());
				// Else If no Port was found then Point to Mouse Location
				else
					current = graph.snap(e.getPoint());
				// Xor-Paint the new Connector
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
	 * Create a Popup Menu to the edge
	 * 
	 * @param pt
	 * @param cell
	 * @return
	 */
	public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
		JPopupMenu menu = new JPopupMenu();
		if (cell != null) {
			// Edit
			if (cell instanceof CustomCell) {
				menu.add(new AbstractAction("Edit") {
					public void actionPerformed(ActionEvent e) {
						graph.startEditingAtCell(cell);
					}
				});
			}
		}
		// Remove
		if (!graph.isSelectionEmpty()) {
			if (cell instanceof CustomCell) {
				menu.addSeparator();
			}
			menu.add(new AbstractAction("Delete") {
				public void actionPerformed(ActionEvent e) {
					if (graph instanceof SolutionGraph) {
						((SolutionGraph) graph).removeCell();
					}
				}
			});
		}
		return menu;
	}

	public void connect(Port source, Port target) {
		((SolutionGraph)graph).setSavedAsAlternative(false);
		Tools tools = new Tools();
		// Construct Edge with no label
		MySolutionEdge edge = new MySolutionEdge(graph);
		// Verify that source and target name are different.
		// Graph could not allow cycle
		// Port belong to cell and to obtain cell we need to navigate throw port
		if (graph.getModel().acceptsSource(edge, source)
				&& graph.getModel().acceptsTarget(edge, target)) {
			// Create a Map that holds the attributes for the edge
			edge.getAttributes().applyMap(EdgeManager.problemEdgeAttributes());

			graph.getGraphLayoutCache().insertEdge(edge, source, target);

			edge.setSource(source);
			edge.setTarget(target);

			MyProblemCustomCell vertexSource = (MyProblemCustomCell) DefaultGraphModel
					.getSourceVertex(graph.getModel(), edge);
			MyProblemCustomCell vertexTarget = (MyProblemCustomCell) DefaultGraphModel
					.getTargetVertex(graph.getModel(), edge);

			String totalFrequency = tools.round(serializator.calculateTotalFrequencyForAlternatives(vertexSource, vertexTarget), 3);

			Map attr = edge.getAttributes().applyMap(
					EdgeManager.problemEdgeAttributes());
			
			// Showing frequency over edge
			edge.setUserObject(totalFrequency);
			if (Double.parseDouble(totalFrequency) < 0.25) {
				// ESTO HAY QUE CAMBIARLO LUEGO Y PONERLO EN LA CLASE
				// EdgeManager
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
				// ESTO HAY QUE CAMBIARLO LUEGO Y PONERLO EN LA CLASE
				// EdgeManager
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
}
