
package system.rdf.utils;

import org.jgraph.JGraph;

import system.rdf.ui.GraphEd;
import system.rdf.ui.UIModeler;

/**
 * @author armando
 *
 */
public abstract class ProblemFactory implements ProblemFactoryInterface {

	/**
	 * Create a new problem model 
	 * @see system.rdf.utils.ProblemFactoryInterface#createProblem(system.rdf.ui.GraphEd, org.jgraph.JGraph)
	 */
	public void createProblem(GraphEd graphed, JGraph graph) {
		
		/*graphed.model = new InternalFrameModel(graph);
		graphed.jp.add(graphed.model);*/
	
	}

}
