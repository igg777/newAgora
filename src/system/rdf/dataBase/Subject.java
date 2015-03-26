package system.rdf.dataBase;

import java.awt.geom.Point2D;
/**
 * Class to represent the label of a RDF Subject and it's location as a 
 * Graph represented drawn by JGraph and 
 * @author Irlan
 *
 */
public class Subject {

    protected String label;
    protected Point2D point;
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((label == null) ? 0 : label.hashCode());
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof Subject))
	    return false;
	Subject other = (Subject) obj;
	if (label == null) {
	    if (other.label != null)
		return false;
	} else if (!label.equals(other.label))
	    return false;
	return true;
    }

    /**
     * 
     */
    public Subject() {
	super();
    }

    /**
     * @param label
     */
    public Subject(String label) {
	super();
	this.label = label;
    }

    public Subject(String label, Point2D point) {
	this.label = label;
	this.point = point;
    }

    public String getLabel() {
	return label;
    }
    public void setLabel(String label) {
	this.label = label;
    }
    public Point2D getPoint() {
	return point;
    }
    public void setPoint(Point2D point) {
	this.point = point;
    }



}
