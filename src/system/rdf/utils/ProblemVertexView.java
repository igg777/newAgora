package system.rdf.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;


public class ProblemVertexView extends VertexView {
    /**
     */
    public static transient JGraphCustomVertexRenderer renderer = new JGraphCustomVertexRenderer();

    /**
     */
    public ProblemVertexView() {
	super();
    }

    /**
     */
    public ProblemVertexView(Object cell) {
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
	 * Return a slightly larger preferred size than for a rectangle.
	 */
	public Dimension getPreferredSize() {
	    Dimension d = super.getPreferredSize();
	    d.width = 160;
	    d.height = 50;
	    return d;
	}

	public void setPreferredSize(Dimension preferredSize) {
	    super.setPreferredSize(preferredSize);
	}


	/**
	 * Paint the divisor line and up label on cell
	 */
	public void paint(Graphics g) {

	    Dimension dmn = getPreferredSize();
	    boolean   tmp = selected;

	    try {
		selected = false;
		super.paint(g);
	    } finally {
		selected = tmp;
	    }
	    if (bordercolor != null) {
		setPreferredSize(dmn);
	    }

	}//End of mathod paint...

	public Component getRendererComponent(JGraph graph, CellView view,boolean sel, boolean focus, boolean preview) {
	    return super.getRendererComponent(graph, view, sel, focus, preview);
	}


    }
}
