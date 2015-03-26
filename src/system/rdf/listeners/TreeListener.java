package system.rdf.listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jgraph.JGraph;

import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.ontology.OntologyTree;
import system.rdf.ui.GraphEd;
import system.rdf.ui.UIModeler;
import system.rdf.ui.UITabbedPaneEditor;

public class TreeListener extends MouseAdapter {
	
	private OntologyTree tree;

	public TreeListener(OntologyTree tree) {
		
		this.tree = tree;
	}

	public void mousePressed(MouseEvent e) {
		try{
		DefaultMutableTreeNode node;
		// Add elements to editors if can
		if (e.getClickCount() == 2) {
			node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if(node != null)
				if (node.isLeaf()) {
					int selectedComponent = GraphEd.scenarios.getSelectedIndex();
					int selectedTab = ((UITabbedPaneEditor)((UIModeler)GraphEd.scenarios.getSelectedComponent())
							.getEditorTabbedPane()).getSelectedIndex();
					String[] process = new String[node.getPath().length-2];
					for (int i = 1; i < process.length+1; i++) {
						process[i-1] = node.getPath()[i].toString();
					}
					if (selectedTab == 0) {
						((ProblemGraph)GraphEd.modelsss.get(selectedComponent).getEditorTabbedPane().getGraphs().get(0))
						.insertCell(new Point(10, 10), 
								new MyProblemCustomCell(tree.getLastSelectedPathComponent().toString(), 
								process));
						GraphEd.refreshScenarios();
							
					} else {
						((SolutionGraph)GraphEd.modelsss.get(selectedComponent).getEditorTabbedPane().getGraphs().get(selectedTab))
						.insertCell(new Point(10, 10), 
								tree.getLastSelectedPathComponent().toString(), 
								process);
						GraphEd.refreshScenarios();
						
					}
					
					
					
					/*if (((UIModeler)GraphEd.scenarios.getComponentAt(selectedComponent)).getEditorTabbedPane().graphs.get(0) != null)
						GraphEd.graph = ((ProblemGraph)((UIModeler)GraphEd.scenarios.getSelectedComponent()).getEditorTabbedPane().
								graphs.get(0));
						((ProblemGraph)((UIModeler)GraphEd.scenarios.getSelectedComponent()).getEditorTabbedPane().
								graphs.get(0)).insertCell(new Point(10, 10), 
						tree.getLastSelectedPathComponent().toString(), 
						node.getPath()[1].toString());
					int contForClass = ((UIModeler)GraphEd.scenarios.getComponentAt(selectedComponent)).getCont();
					System.out.println("el contador de la clase es " + contForClass);
					*/
				}
		} else {
			tree.setSelectionPath(tree.getPathForLocation(e.getX(),e.getY()));
		}
	}
		catch (ArrayIndexOutOfBoundsException ex) {
			GraphEd.errorPane.printMessage(new Exception("Please, create a model first"));
		}
		catch (Exception e2) {
			GraphEd.errorPane.printMessage(e2);
		}
  
}
}
