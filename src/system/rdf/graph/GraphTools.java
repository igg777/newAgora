package system.rdf.graph;

import java.util.ArrayList;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphModel;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;


/**
 * in this class the system check if the graph have any cyclic and if the graph is connected correctly
 * @author Leo
 *
 */
public class GraphTools {

	//this graph allows to know when the graph have a cyclic
	DirectedAcyclicGraph<Object, Object> acyclicGraph = new DirectedAcyclicGraph<Object, Object>(DefaultEdge.class);
	//allows to know when a graph is not complete connected
	ConnectivityInspector<Object, Object> inspector;
	
	
	public GraphTools(ProblemGraph graph)throws CycleFoundException{
		fillAcyclicGraph(graph);
	    inspector = new ConnectivityInspector<Object, Object>(acyclicGraph);
		
	}
	
	public void fillAcyclicGraph(ProblemGraph graph)throws CycleFoundException{
		GraphModel gm = graph.getModel();
		ArrayList<Object> graphEdges = graph.getAllEdges();
		ArrayList<MyProblemCustomCell> graphCell = graph.getCells();
		for (Object object : graphCell) {
			MyProblemCustomCell cell = (MyProblemCustomCell)object;
			acyclicGraph.addVertex(cell.toString()+"--"+cell.getProcess());
		}
		for (Object object : graphEdges) {
			MyProblemCustomCell sourceVertex = (MyProblemCustomCell) gm
						.getParent(gm.getSource((DefaultEdge)object));
			MyProblemCustomCell targetVertex = (MyProblemCustomCell) gm
			.getParent(gm.getTarget((DefaultEdge)object));
			acyclicGraph.addDagEdge(sourceVertex.toString()+"--"+sourceVertex.getProcess(), targetVertex.toString()+"--"+targetVertex.getProcess());
			
		}
		
	}
	
	public boolean isAllConected(){
		return inspector.isGraphConnected();
	}
	}
