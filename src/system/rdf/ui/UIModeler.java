package system.rdf.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import system.rdf.graph.ProblemGraph;
import system.rdf.utils.TabsComponentManager;
import system.rename.Tools;

/**
 * This class is used to create and editing new models to formulate problem and
 * its solutions
 */

public class UIModeler extends javax.swing.JInternalFrame {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1140445997595826439L;

	public String title = "New model";

	private UITabbedPaneEditor editorTabbedPane;
	private ArrayList zoom;

	/**
	 * Auto-generated main method to display this JInternalFrame inside a new
	 * JFrame.
	 */

	public UIModeler() {
		super();
		initGUI();

	}

	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			setVisible(true);
			setTitle(title);
			this.setClosable(true);
			this.setResizable(true);
			this.setFrameIcon(new ImageIcon("system/rdf/resources/iframe.png"));
			this.setMaximizable(true);
			this.setBounds(0, 0, 400, 300);
			this.addInternalFrameListener(new InternalFrameAdapter() {

				public void internalFrameOpened(InternalFrameEvent evt) {
					thisInternalFrameOpened(evt);
				}

				public void internalFrameClosing(InternalFrameEvent evt) {
					thisInternalFrameClosing(evt);
				}

				public void internalFrameActivated(InternalFrameEvent evt) {
					thisInternalFrameActivated(evt);
				}

				public void internalFrameClosed(InternalFrameEvent evt) {
					thisInternalFrameClosed(evt);
				}
			});
			this.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent evt) {
					thisComponentResized(evt);
				}

				public void componentHidden(ComponentEvent evt) {
					thisComponentHidden(evt);
				}
			});
			{
				editorTabbedPane = new UITabbedPaneEditor();
				getContentPane().add(editorTabbedPane);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Change the {@link JInternalFrame} title
	 * 
	 * @param title
	 */
	public void setOwnTitle(String title) {
		this.setTitle(title);
		try {
			this.setIcon(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

	}

	public UITabbedPaneEditor getEditorTabbedPane() {
		return editorTabbedPane;
	}

	/**
	 * Modeled area is closed. A few validation has to be done in this method.
	 * The map(indicatorProcess of RDFSerializator class) has to be cleared.
	 * 
	 * @param evt
	 */
	private void thisInternalFrameClosed(InternalFrameEvent evt) {
		GraphEd.zoom.setEnabled(false);
		GraphEd.zoomin.setEnabled(false);
		GraphEd.zoomout.setEnabled(false);
		GraphEd.remove.setEnabled(false);
		GraphEd.group.setEnabled(false);
	}

	private void thisInternalFrameActivated(InternalFrameEvent evt) {

	}

	/**
	 * Method use for ask the user if he want to save the modeled graph or no
	 * 
	 * @param evt
	 */
	private void thisInternalFrameClosing(InternalFrameEvent evt) {
		try {
			// This is the way to access the graph that is been modeled
			ProblemGraph graph = ((ProblemGraph) editorTabbedPane.getGraphs()
					.get(0));
			if (graph.getModel().getRootCount() >= 1 && !graph.isSaved()) {
				int option = JOptionPane.showConfirmDialog(this,
						"Do you want to save it");
				// means that user has chosen yes option
				if (option == 0) {
					if (graph.connectivitiInspector()) {
						Tools tools = new Tools();
						tools.fillTable(graph);
						ArrayList<String> cyclic = tools.isCyclic();
						// checking if the graph haven't any cyclic
						// if (cyclic.size() == 0) {
						UISaveDecisionProblem save = new UISaveDecisionProblem(
								(ProblemGraph) graph);
						save.setModel((UIModeler) GraphEd.scenarios
								.getSelectedComponent());
						save.setLocationRelativeTo(null);
						/*
						 * } else { String toShow =
						 * "The problem can't be saved because there is a cyclic connection between this nodes \n"
						 * ; for (String string : cyclic) { toShow = toShow +
						 * string + "\n"; } throw new Exception(toShow); }
						 */
					} else
						throw new Exception(
								"Please, check the graph because is not connected correctly");

				} else if (option == 1) {
					GraphEd.modelsss.remove(this);
					GraphEd.tableInsertedStatement = TabsComponentManager
							.getTableInsertedStatement();
					GraphEd.refreshScenarios();
					this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					// means that user has chosen cancel option
				} else if (option == 2) {
					this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}
			} else {
				GraphEd.modelsss.remove(this);
				GraphEd.refreshScenarios();
				GraphEd.tableInsertedStatement = TabsComponentManager
						.getTableInsertedStatement();
				GraphEd.errorPane.clear();
			}
		} catch (CycleFoundException e1) {
			GraphEd.errorPane.printMessage(new Exception(
					"Error, exist a cyclic connection in the graph"));
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

	private void thisInternalFrameOpened(InternalFrameEvent evt) {
		// System.out.println("this.internalFrameOpened, event="+evt);
	}

	private void thisComponentHidden(ComponentEvent evt) {
		// System.out.println("this.componentHidden, event="+evt);
	}

	private void thisComponentResized(ComponentEvent evt) {
		// System.out.println("this.componentResized, event="+evt);
	}

	/**
	 * add a ChangeListener to the UITabbedPaneEdtor
	 * 
	 * @author Leo
	 * 
	 */
	public void addTpeditorChangeListener() {
		editorTabbedPane.addChangeListener();
	}

}
