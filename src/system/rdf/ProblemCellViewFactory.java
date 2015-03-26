package system.rdf;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

import system.rdf.utils.ProblemVertexView;

/**
 * Create a CellWiew for formulated decisional problem
 * @author Armando
 *
 */
public class ProblemCellViewFactory extends DefaultCellViewFactory {
	
	protected VertexView createVertexView(Object v) {
		return new ProblemVertexView(v); 
	
	}
}
