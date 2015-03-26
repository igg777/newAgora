package system.rdf.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jgraph.JGraph;

import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.ui.GraphEd;
import system.rdf.utils.TabsComponentManager;

public class ProblemKeyListener implements KeyListener {
	private JGraph graph = null;



	public ProblemKeyListener(JGraph graph) {
		super();
		this.graph = graph;
	}

	//
	// KeyListener for Delete KeyStroke
	//
	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		// Listen for Delete Key Press
		if (e.getKeyCode() == KeyEvent.VK_DELETE) // Execute Remove Action on Delete Key Press
		{
			if(graph instanceof ProblemGraph){
				((ProblemGraph) graph).removeCell();
				GraphEd.tableInsertedStatement = TabsComponentManager.getTableInsertedStatement();
			}
			else if (graph instanceof SolutionGraph){
				((SolutionGraph) graph).removeCell();
			}
		}
	}

}
