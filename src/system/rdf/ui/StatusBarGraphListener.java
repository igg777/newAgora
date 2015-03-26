package system.rdf.ui;

import javax.swing.JPanel;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;

public class StatusBarGraphListener extends JPanel implements
GraphModelListener {

/**
* Graph model change event
*/
public void graphChanged(GraphModelEvent e) {
updateStatusBar();
}

protected void updateStatusBar() {

}
}