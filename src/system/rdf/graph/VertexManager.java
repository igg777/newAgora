package system.rdf.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.SwingConstants;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphConstants;

/**
 * Manage the graph vertex attributes
 * @author Armando Carracedo
 */
public class VertexManager {

    public static Map solutionCellAttributes(){

	Map map = new Hashtable();

	// Snap the Point to the Grid
	GraphConstants.setSize(map, new Dimension(30,60));
	// Make sure the cell is resized on insert
	GraphConstants.setResize(map, true);
	// GraphConstants.setAutoSize(map, true);
	// Add a nice looking gradient background
	//GraphConstants.setGradientColor(map, Color.white);
	// Add a Border Color Attribute to the Map
	GraphConstants.setBorderColor(map, Color.black);
	// Add a White Background
	GraphConstants.setBackground(map, new java.awt.Color(255,255,164));
	// Make Vertex Opaque
	GraphConstants.setOpaque(map, true);
	
	// Setting the text to BOTTOM
	GraphConstants.setVerticalAlignment(map, SwingConstants.CENTER);
	GraphConstants.setFont(map,new java.awt.Font("Microsoft Sans Serif",1,11));
	// to Change the Editing
	GraphConstants.setConnectable(map, true);

	GraphConstants.setEditable(map, false);
	return map;
    }

    /*
     * Create the cell attributes
     * @param Point2D
     * @return Map
     */
    public static Map problemCellAttributes(Point2D point){
    	
 	Map map = new Hashtable();
 	// Add a Bounds Attribute to the Map
 	GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(),
 		point.getY(),point.getX()+ 100,point.getY() + 100));
 	GraphConstants.setSize(map, new Dimension(30,60));
 	// Make sure the cell is resized on insert
 	GraphConstants.setResize(map, true);
 	// GraphConstants.setAutoSize(map, true);
 	// Add a nice looking gradient background
 	//GraphConstants.setGradientColor(map, Color.white);
 	// Add a Border Color Attribute to the Map
 	GraphConstants.setBorderColor(map, Color.black);
 	// Add a Background
 	GraphConstants.setBackground(map, new Color(208, 236, 255));
 	// Make Vertex Opaque
 	GraphConstants.setOpaque(map, true);
 	//cannot edit the cell
 	GraphConstants.setEditable(map, false);
 	// Setting the text to BOTTOM
 	GraphConstants.setVerticalAlignment(map, SwingConstants.CENTER);
 	// 
 	GraphConstants.setConnectable(map, true);
 	// 
 	GraphConstants.setFont(map,new java.awt.Font("Microsoft Sans Serif",1,11));
 	//The edge cannot disconnect on cell
 	GraphConstants.setDisconnectable(map, false);
    	
 	return map;
    }    
    
    public static Map solutionCellAttributes(Point2D point){

	Map map = new Hashtable();

	GraphConstants.setBounds(map, new Rectangle2D.Double(point.getX(),
		point.getY(),0, 0));
	// Snap the Point to the Grid
	GraphConstants.setSize(map, new Dimension(30,60));
	// Make sure the cell is resized on insert
	GraphConstants.setResize(map, true);
	// GraphConstants.setAutoSize(map, true);
	// Add a nice looking gradient background
	//GraphConstants.setGradientColor(map, Color.white);
	// Add a Border Color Attribute to the Map
	GraphConstants.setBorderColor(map, Color.black);
	// Add a White Background
	GraphConstants.setBackground(map, new java.awt.Color(255,255,164));
	// Make Vertex Opaque
	GraphConstants.setOpaque(map, true);
	// Setting the text to BOTTOM
	GraphConstants.setVerticalAlignment(map, SwingConstants.CENTER);
	GraphConstants.setFont(map,new java.awt.Font("Microsoft Sans Serif",1,11));
	// to Change the Editing
	GraphConstants.setConnectable(map, true);
    //cannot edit the cell
	GraphConstants.setEditable(map, false);
	return map;
    }
    
   
}
