package system.rdf.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphModel;

/**
 * A Custom model that does not allow Self-References
 * @author Armando Carracedo Vel�zquez
 */
public class CustomGraphModel extends DefaultGraphModel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7450990061430956249L;

	public CustomGraphModel(){
		addGraphModelListener();
	}

	// Override Superclass Method
	public boolean acceptsSource(Object edge, Object port) {
		// Source only Valid if not Equal Target
		return (((Edge) edge).getTarget() != port);
	}

	// Override Superclass Method
	public boolean acceptsTarget(Object edge, Object port) {
		// Target only Valid if not Equal Source
		return (((Edge) edge).getSource() != port);
	}

	public void cellsChanged(Object[] cells){
		//System.out.println();
	}


	protected CustomGraphModel clone() throws CloneNotSupportedException {
		CustomGraphModel model = new CustomGraphModel();

		model.asksAllowsChildren = this.asksAllowsChildren;
		model.attributes = this.attributes;
		model.compoundEdit = this.compoundEdit;
		model.emptyIterator = this.emptyIterator;
		model.listenerList = this.listenerList;
		model.listeners = this.listeners;
		model.realSource = this.realSource;
		model.removeEmptyGroups = this.removeEmptyGroups;
		model.roots = this.roots;
		model.updateLevel = this.updateLevel;

		return model;


	}

	public void addGraphModelListener(){
		this.addGraphModelListener(new GraphModelListener() {

			public void graphChanged(GraphModelEvent e) {
				GraphModelEvent.GraphModelChange c = e.getChange();
				if (c.getRemoved() == null && c.getInserted() == null) {
					Map previousAttributes = c.getPreviousAttributes();
					Set keySet = previousAttributes.keySet();
					Iterator iter = keySet.iterator();
					while (iter.hasNext()) {
						Object attribute = iter.next();
						//System.out.println("Prev Key "								+ String.valueOf(attribute));
						Object value = c.getPreviousAttributes().get(attribute);
						//System.out.println("\t" + String.valueOf(value));
					}
					Map attributes = c.getAttributes();
					keySet = attributes.keySet();
					iter = keySet.iterator();
					while (iter.hasNext()) {
						Object attribute = iter.next();
						//System.out.println("Curr Key "+ String.valueOf(attribute));
						Object value = c.getAttributes().get(attribute);
						//System.out.println("\t" + String.valueOf(value));
					}
				}
			}
		});
	}

	/**
	 * Returns a deep clone of the specified cells, including all children.
	 */
	public static Object[] cloneCell(GraphModel model, Object[] cells) {

		Map clones = model.cloneCells(getDescendants(model, cells).toArray());

		Object []clonedCells = new Object[clones.size()];
		for (int i = 0; i < cells.length; i++) {
			clonedCells [i] = clones.get(cells[i]);   
		}
		return clonedCells;
	}

}
