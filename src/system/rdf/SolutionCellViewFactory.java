package system.rdf;


import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

import system.rdf.utils.ProblemVertexView;

/**
 * A default view factory for a JGraph. This simple factory associate a given
 * cell class to a cell view. This is a javabean, just parameter it correctly in
 * order it meets your requirements (else subclass it or subclass
 * DefaultCellViewFactory). You can also recover the gpConfiguration of that
 * javabean via an XML file via XMLEncoder/XMLDecoder.
 * 
 * @author Armando Carracedo Velázquez
 */
public class SolutionCellViewFactory extends DefaultCellViewFactory {
	
	protected VertexView createVertexView(Object v) {
		return new ProblemVertexView(v); //super.createVertexView(v);
	}

}
