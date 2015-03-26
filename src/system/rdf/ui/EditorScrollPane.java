package system.rdf.ui;

import java.awt.Component;

import javax.swing.JScrollPane;

import org.jgraph.JGraph;

public class EditorScrollPane extends JScrollPane {

    public JGraph component = null;

    public EditorScrollPane(Component view) {
	super(view);
	if(view instanceof JGraph){
	    component = (JGraph) view;
	}

    }

    public EditorScrollPane() {
	super();

    }

    public JGraph getComponent() {
	return component;
    }
}
