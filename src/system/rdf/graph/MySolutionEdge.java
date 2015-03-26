package system.rdf.graph;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphModel;

import system.rdf.dataBase.RDFSerializator;
import system.rename.Tools;

/**
 * this class allow insert a corrective action and show another text
 * 
 * @author Leo
 * 
 */
public class MySolutionEdge extends DefaultEdge {
	private String correctiveAction;
	Tools tools;
	JGraph graph;
	RDFSerializator serializator;

	public MySolutionEdge(JGraph graph, DefaultEdge edge) {
		super(edge);
		tools = new Tools();
		serializator = new RDFSerializator();
		this.graph = graph;
		correctiveAction = "";
	}

	public MySolutionEdge(JGraph graph) {
		super();
		tools = new Tools();
		serializator = new RDFSerializator();
		this.graph = graph;
		correctiveAction = "";

	}

	public void assingCorrectiveAction(String correctiveAtion) {
		this.correctiveAction = correctiveAtion;
		MyProblemCustomCell vertexSource = (MyProblemCustomCell) DefaultGraphModel
				.getSourceVertex(graph.getModel(), this);
		MyProblemCustomCell vertexTarget = (MyProblemCustomCell) DefaultGraphModel
				.getTargetVertex(graph.getModel(), this);
		String totalFrequency = tools.round(serializator
				.calculateTotalFrequencyForAlternatives(vertexSource,
						vertexTarget), 3);
		if (this.correctiveAction.equals("")) {
			this.setUserObject(totalFrequency);
		} else {
			this.setUserObject(totalFrequency + "<<Corrective action added>>");
		}
	}

	public String getCorrectiveAction() {
		return correctiveAction;
	}

	public void setCorrectiveAction(String correctiveAction) {
		this.correctiveAction = correctiveAction;
	}
	

}
