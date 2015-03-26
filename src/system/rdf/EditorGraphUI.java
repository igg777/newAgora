package system.rdf;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.SwingUtilities;

import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.plaf.basic.BasicGraphUI;

import system.rdf.ui.GraphEd;

/**
 * Definition of the custom GraphUI.
 * This class is used to implement the top label of the cell
 */
/**
 * @author Irl�n
 *
 */
public class EditorGraphUI extends BasicGraphUI {
   
	public static int flag = 0;
   
    protected void completeEditing(boolean messageStop, boolean messageCancel,
	    boolean messageGraph) {

	if (stopEditingInCompleteEditing && editingComponent != null) {
	    Component oldComponent = editingComponent;
	    Object oldCell = editingCell;
	    GraphCellEditor oldEditor = cellEditor;
	    Object newValue = oldEditor.getCellEditorValue();
	    boolean requestFocus = (graph != null && (graph.hasFocus() || SwingUtilities
		    .findFocusOwner(editingComponent) != null));
	    editingCell = null;
	    editingComponent = null;
	    if (messageStop)
		oldEditor.stopCellEditing();
	    else if (messageCancel)
		oldEditor.cancelCellEditing();
	    graph.remove(oldComponent);
	    if (requestFocus)
		graph.requestFocus();
	    if (messageGraph) {

		// if double click up
		if (flag == 1) {

		    if (newValue.toString().equals(""))
			((CustomCell) oldCell).setTextToUp("...");
		    else {
			((CustomCell) oldCell).setTextToUp(newValue.toString());
			//This code is to refresh the change otherwise is refreshed
			//when the user clicks
			Object oldValue = ((CustomCell) oldCell)
				          .getUserObject();
			graphLayoutCache.valueForCellChanged(oldCell, oldValue);
			//End the code to refresh
		    }
		    // if double click down
		} else if (flag == 0)
		    graphLayoutCache.valueForCellChanged(oldCell, newValue);
	    }
	    updateSize();
	    // Remove Editor Listener
	    if (oldEditor != null && cellEditorListener != null)
		oldEditor.removeCellEditorListener(cellEditorListener);
	    cellEditor = null;
	}
    }
    
    protected boolean startEditing(Object cell, MouseEvent event) {

	completeEditing();
	// This code is to know if event double click was on the top of the cell
	if (event != null)
	    if (event.getClickCount() == 2) { // if double click on cell
		// the cell y center value
		double yCenter = graph.getCellBounds(cell).getBounds()
			.getCenterY();
		// if double click on cell
		if (cell instanceof CustomCell) {
		    // if double click down
		    if (yCenter < event.getY()) {
			flag = 0;
			// if double click up
		    } else if (yCenter > event.getY())
			flag = 1;
		    // if double click on Edge
		} else if (cell instanceof DefaultEdge)
		    flag = 0;
	    }
	// End of the code is to know if event double click was on the top of the cell
	if (graph.isCellEditable(cell)) {
	    CellView tmp = graphLayoutCache.getMapping(cell, false);
	    cellEditor = tmp.getEditor();

	    if (flag == 1) {
		Object uo = ((CustomCell) cell).getUserObject();
		((CustomCell) cell).setUserObject(((CustomCell) cell)
			.getTextToUp());
		editingComponent = cellEditor.getGraphCellEditorComponent(
			graph, cell, graph.isCellSelected(cell));
		((CustomCell) cell).setUserObject(uo);
	    } else
		editingComponent = cellEditor.getGraphCellEditorComponent(
			graph, cell, graph.isCellSelected(cell));

	    if (cellEditor.isCellEditable(event)) {
		Rectangle2D cellBounds = graph.getCellBounds(cell);

		editingCell = cell;

		Dimension2D editorSize = editingComponent.getPreferredSize();

		graph.add(editingComponent);
		Point2D p = getEditorLocation(cell, editorSize, graph
			.toScreen(new Point2D.Double(cellBounds.getX(),
				cellBounds.getY())));

		editingComponent.setBounds((int) p.getX(), (int) p.getY(),
			(int) editorSize.getWidth(), (int) editorSize
				.getHeight());
		editingCell = cell;
		editingComponent.validate();

		// Add Editor Listener
		if (cellEditorListener == null)
		    cellEditorListener = createCellEditorListener();
		if (cellEditor != null && cellEditorListener != null)
		    cellEditor.addCellEditorListener(cellEditorListener);
		Rectangle2D visRect = graph.getVisibleRect();
		graph.paintImmediately((int) p.getX(), (int) p.getY(),
			(int) (visRect.getWidth() + visRect.getX() - cellBounds
				.getX()), (int) editorSize.getHeight());
		if (cellEditor.shouldSelectCell(event)
			&& graph.isSelectionEnabled()) {
		    stopEditingInCompleteEditing = false;
		    try {
			graph.setSelectionCell(cell);
		    } catch (Exception e) {
		    	GraphEd.errorPane.printMessage(new Exception("Editing exception: " + e));
		    }
		    stopEditingInCompleteEditing = true;
		}

		if (event instanceof MouseEvent) {
		    /*
		     * Find the component that will get forwarded all the mouse
		     * events until mouseReleased.
		     */
		    Point componentPoint = SwingUtilities.convertPoint(graph,
			    new Point(event.getX(), event.getY()),
			    editingComponent);

		    /*
		     * Create an instance of BasicTreeMouseListener to handle
		     * passing the mouse/motion events to the necessary
		     * component.
		     */
		    // We really want similiar behavior to getMouseEventTarget,
		    // but it is package private.
		    Component activeComponent = SwingUtilities
			    .getDeepestComponentAt(editingComponent,
				    componentPoint.x, componentPoint.y);
		    if (activeComponent != null) {
			new MouseInputHandler(graph, activeComponent, event);
		    }
		}
		return true;
	    } else
		editingComponent = null;
	}
	return false;
    }

}