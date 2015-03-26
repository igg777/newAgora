package system.rdf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Map;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;


/**
 * @author Armnado Carracedo Vel�zquez
 */
public class SolutionVertexView extends VertexView {

    /**
     */
    public static transient JGraphCustomVertexRenderer renderer = new JGraphCustomVertexRenderer();

    /**
     */
    public SolutionVertexView() {
	super();
    }

    /**
     */
    public SolutionVertexView(Object cell) {
	super(cell);
    }

    /**
     */
    public CellViewRenderer getRenderer() {
	return renderer;
    }

    /**
     */
    public static class JGraphCustomVertexRenderer extends VertexRenderer {

	/**
	 * The up label of cell
	 */
	public String label="";

	/**
	 * Return a slightly larger preferred size than for a rectangle.
	 */
	public Dimension getPreferredSize() {
	    Dimension d = super.getPreferredSize();
	    d.width = d.width;
	    d.height = 60;
	    return d;
	}


	@Override
	public void setPreferredSize(Dimension preferredSize) {

	    super.setPreferredSize(preferredSize);
	}


	/**
	 * Paint the divisor line and up label on cell
	 */
	public void paint(Graphics g) {

	    Graphics2D  g2 = (Graphics2D) g;
	    Line2D      ln = null;
	    FontMetrics fm = g.getFontMetrics();
	    boolean   tmp = selected;

	    try {
		selected = false;
		super.paint(g);
	    } finally {
		selected = tmp;
	    }
	    if (bordercolor != null) {
		ln =   new Line2D.Double(0,getHeight()/2,getWidth(), getHeight()/2);
		g2.draw(ln);
		if(this.getText() != null){ // If user object is not null

		    int d  = fm.stringWidth(label)/2;//half label width
		    int h  = fm.getHeight();         //cell Height  
		    int w  = getWidth();             //cell Width

		    //painting up the label in the cell center
		    g.drawString(label,w/2-d , h);

		    if(2*d > w){
			//setPreferredSize(new Dimension(2*d ,dmn.height));
		    }
		}
	    }

	}//End of mathod paint...

	public Component getRendererComponent(JGraph graph, CellView view,boolean sel, boolean focus, boolean preview) {

	    Object cell = view.getCell();
	    label = ((CustomCell)cell).getTextToUp();
	    Dimension dimension = getPreferredSize();

	    if(graph.getEditingCell() == cell){

		double b = view.getBounds().getWidth();
		String lb = ((CustomCell)cell).getTextToUp();
		int d = graph.getGraphics().getFontMetrics().stringWidth(lb);
		     Map map = new Hashtable();
		     GraphConstants.setSize(map, new Dimension(30,60));
		    ((CustomCell)  cell).getAttributes().applyMap(map);
		/*if(d > b){
		     Map map = new Hashtable();
		     GraphConstants.setSize(map, new Dimension(30,60));
		    ((CustomCell)  cell).getAttributes().applyMap(map);
		}*/
	    }
	    return super.getRendererComponent(graph, view, sel, focus, preview);

	}


    }
}