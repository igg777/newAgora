package system.rdf.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;

import system.rdf.graph.GraphManager;
import system.rdf.graph.SolutionGraph;
import system.rdf.ui.GraphEd;
import system.rdf.ui.UIAssignCorrectiveAction;
import system.rdf.utils.TabsComponentManager;
/**
 * Personalized listener to JGraph object
 * @author Armando Carracedo Vel�zquez 
 */
public class GraphListener extends MouseAdapter {

    public JGraph graph = null;

    /**
     * Constructor  
     */
    public GraphListener() {
    }

    /**
     * 
     * @param table
     */
    public GraphListener(JGraph g) {
	this.graph = g;
    }

    public void mousePressed(MouseEvent e) {
	if(e.getClickCount()== 2){
	    if (graph instanceof SolutionGraph) {
		Object cell = graph.getSelectionCell();
		if (graph.getModel().isEdge(cell)) {
		    UIAssignCorrectiveAction ass = new UIAssignCorrectiveAction((DefaultEdge) cell);
		    ass.setVisible(true);
		    ass.setLocationRelativeTo(graph);
		}
	    }
	}

    }
  
}