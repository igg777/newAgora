package system.rdf;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

/**
 * Custom cell with the up string text
 * @author Armando Carracedo Vel�zquez
 */
public class CustomCell extends DefaultGraphCell{

    protected String textToUp = new AccionText().getACCION_NULL();

    public CustomCell() {
    }

    public CustomCell(String userobject) {
	super(userobject);
    }

    public String getTextToUp() {
	return textToUp;
    }

    public void setTextToUp(String textToUp) {
	this.textToUp = textToUp;
    }

    /**
     * Return the position point
     * @return Point2D
     */
    public Point2D getPositionPoint(){
	Rectangle2D bound = GraphConstants.getBounds(this.getAttributes());
	Point2D point = new Point((int)bound.getX(),(int)bound.getY());
	return point;
    }

    
    public void addPort(DefaultPort sourcePortTo) {
	// TODO Auto-generated method stub
	DefaultPort port = new DefaultPort("Floating");
	this.add(port);
	//port.setParent(this);
    }
    
    

}
