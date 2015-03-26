
package system.rdf.graph;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import org.jgraph.graph.GraphConstants;

import system.rdf.ui.GraphEd;

/**
 * @author Armando Carracedo
 *
 */
public class EdgeManager {
    
    static Map mapForProblem = new Hashtable();
    /**
     * Create the edge attributes for solution
     * @return Map
     */
    public static Map solutionEdgeAttributes(){

	Map map = new Hashtable();

	// Add a Line End Attribute
	GraphConstants.setLineEnd(map, GraphConstants.ARROW_TECHNICAL);
	// Fill end line
	GraphConstants.setEndFill(map, true);
	// Add a label along edge attribute
	GraphConstants.setLabelAlongEdge(map, true);

	GraphConstants.setDisconnectable(map, false);

	// Cannot edit the edge
	GraphConstants.setEditable(map, false);

	return map;

    } 
    /**
     * Create the edge attributes for problem
     * @return Map
     */
    public static Map problemEdgeAttributes(){
/*
	// Add a Line End Attribute
	GraphConstants.setLineEnd(mapForProblem, GraphConstants.ARROW_TECHNICAL);
	// Fill end line
	GraphConstants.setEndFill(mapForProblem, true);
	// Add a label along edge attribute
	GraphConstants.setLabelAlongEdge(mapForProblem, true);
	// Cannot edit the edge
	GraphConstants.setEditable(mapForProblem, false);
	GraphConstants.setMoveable(mapForProblem, true);
	GraphConstants.setRouting(mapForProblem, GraphConstants.ROUTING_DEFAULT);
	*/
    	Map map = new Hashtable();
		// Add a Line End Attribute
		GraphConstants.setLineEnd(map,
				GraphConstants.ARROW_TECHNICAL);
		// Fill end line
		GraphConstants.setEndFill(map, true);
		// Add a label along edge attribute
		GraphConstants.setLabelAlongEdge(map, true);
		// Cannot edit the edge
		GraphConstants.setEditable(map, false);
	return map;
    } 

    
    /**
     * Create the edge attributes for problem when the frecuency is low
     * @return Map
     */
    public static Map problemEdgeAttributesLF(){
    /*	
	// Add a Line End Attribute
	GraphConstants.setLineEnd(mapForProblem, GraphConstants.ARROW_TECHNICAL);
	// Fill end line
	GraphConstants.setEndFill(mapForProblem, true);
	// Add a label along edge attribute
	GraphConstants.setLabelAlongEdge(mapForProblem, true);
	// Cannot edit the edge
	GraphConstants.setEditable(mapForProblem, false);
	//Changing the color for the frecuency
	GraphConstants.setLineColor(mapForProblem, Color.RED);
	*/
    	Map map = new Hashtable();
		// Add a Line End Attribute
		GraphConstants.setLineEnd(map,
				GraphConstants.ARROW_TECHNICAL);
		// Fill end line
		GraphConstants.setEndFill(map, true);
		// Add a label along edge attribute
		GraphConstants.setLabelAlongEdge(map, true);
		// Cannot edit the edge
		GraphConstants.setEditable(map, false);
		// Changing the color for the frecuency
		GraphConstants.setLineColor(map, Color.RED);
	return map;
	
    } 

}
