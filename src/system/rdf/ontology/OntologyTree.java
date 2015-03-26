package system.rdf.ontology;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import org.jgraph.JGraph;
import org.kswing.JKTree;

import system.rdf.listeners.TreeListener;

/**
 * Customized JTree to visualized the SCOR Ontology
 * @author Armando 
 */
public class OntologyTree extends JKTree{

	public OntologyTree(){
		super();
	}

	public OntologyTree(DefaultTreeModel treeModel) {
		super(treeModel);
	}

	public void addMouseListener(){
		this.addMouseListener(new TreeListener(this));
	}
	
	public void addTreeSelectionListener(){
		this.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent evt) {
				setToolTipText(evt.getPath().getLastPathComponent()
						.toString());
			}
		});
	}
	
	public void activateListeners(){
		addMouseListener();
		addTreeSelectionListener();
	}


}
