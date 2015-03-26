package system.rdf.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jgraph.JGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.ui.EditorScrollPane;
import system.rdf.ui.ErrorPane;
import system.rdf.ui.GraphEd;
import system.rdf.ui.UIModeler;
import system.rdf.ui.UISaveAlternative;
import system.rdf.ui.UISaveDecisionProblem;
import system.rdf.ui.UITabbedPaneEditor;
import system.rename.Tools;

/**
 * Personalized listener to PopUp menu
 * 
 * @author Armando Carracedo Vel�zquez
 */
public class PopupListener extends MouseAdapter {

	// The popup menu to custom tabbed pane
	JPopupMenu popup;
	// Custom tabbed pane for the graph editor
	UITabbedPaneEditor tpeditor;
	// To create a solution model
	JMenuItem createAlternative;
	// To save solution
	JMenuItem saveSolution;
	// To save problem
	JMenuItem saveProblem;

	// To close Tab
	JMenuItem closeTab;

	JMenuItem clearPane;

	JMenuItem saveAsSolution;

	ErrorPane pane;

	/**
	 * Constructor that initialize the custom tabbed pane and adds the menu
	 * items to the popup menu
	 * 
	 * @param jtp_editor
	 */
	public PopupListener(UITabbedPaneEditor jtp_editor) {
		this.tpeditor = jtp_editor;
	}

	public PopupListener(ErrorPane err) {
		this.pane = err;
	}

	/**
	 * 
	 * @return the menuItem2
	 */
	protected JMenuItem getSaveModel() {

		saveSolution = new JMenuItem("Save");
		if ((((SolutionGraph) tpeditor.getTabGraph()).isSavedAsAlternative()))
			saveSolution.setEnabled(false);
		saveSolution.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				SolutionGraph graph = (SolutionGraph) tpeditor.getTabGraph();
				UISaveAlternative save = new UISaveAlternative();
				save.setGraph(graph);
			}
		});
		return saveSolution;
	}

	/**
	 * @return JMenuItem
	 */
	public JMenuItem getSaveProblem() {
		saveProblem = new JMenuItem("Save");
		if ((((ProblemGraph) tpeditor.getTabGraph()).isSaved())) {
			saveProblem.setEnabled(false);
		}
		saveProblem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					if((((ProblemGraph) tpeditor.getTabGraph()).connectivitiInspector())){
					Tools tools = new Tools();
					tools.fillTable(tpeditor.getGraphs().get(0));
					ArrayList<String> cyclic = tools.isCyclic();
					if (cyclic.size() == 0) {
						executeSaveProblemModel(e);
					} else {
						String toShow = "The problem can't be saved because there is a cyclic connection between this nodes \n";
						for (String string : cyclic) {
							toShow = toShow + string + "\n";
						}
						throw new Exception(toShow);
					}
				}
					else
						throw new Exception("The problem can't be saved because is not connected correctly");
				}
				catch (CycleFoundException e1) {
					GraphEd.errorPane.printMessage(new Exception("The problem can't be saved because there is a cyclic connection"));
				}
				catch (Exception e1) {
				
					GraphEd.errorPane.printMessage(e1);
				}
			}
		});
		return saveProblem;
	}

	/**
	 * Save solution graph on DB
	 * 
	 * @return
	 */
	public JMenuItem getSaveAsSolution() {
		saveAsSolution = new JMenuItem("Save as Solution");
		if (!(((SolutionGraph) tpeditor.getTabGraph()).isSavedAsAlternative())) {
			saveAsSolution.setEnabled(false);
		}
		saveAsSolution.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				executeSaveSolutionToProblem(e);
			}
		});
		return saveAsSolution;
	}

	/**
	 * @return the menuItem3
	 */
	protected JMenuItem getCloseTab() {
		closeTab = new JMenuItem("Close");
		closeTab.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (tpeditor != null) {

					// This is the way to access the graph that is been modeled
					if (tpeditor.getTabGraph().getModel().getRootCount() >= 1
							&& !((SolutionGraph) tpeditor.getTabGraph())
							.isSavedAsAlternative()) {
						int option = JOptionPane.showConfirmDialog(null,
								"Do you want to save it");
						// means that user has chosen yes option
						if (option == 0) {
							UISaveAlternative save = new UISaveAlternative();
							save.setTpeditor(tpeditor);
							save.setFlag(true);
							save.setLocationRelativeTo(null);
							save.setGraph((SolutionGraph) tpeditor
									.getTabGraph());
							// means that user has chosen no option
						} else if (option == 1) {
							tpeditor.closeTab();
							/*
							 * PersistentManager p = new PersistentManager();
							 * p.getProblemWithAlternative();
							 */
							// means that user has chosen cancel option
						} else if (option == 2) {

						}
					} else {
						tpeditor.closeTab();
					}
					// ///////

				}

			}
		});
		return closeTab;
	}

	/**
	 * @return the menuItem
	 */
	protected JMenuItem getCreateSolution() {
		createAlternative = new JMenuItem("Create Alternative");
		if (!((ProblemGraph) tpeditor.getTabGraph()).isSaved()) {
			createAlternative.setEnabled(false);
		}
		createAlternative
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (tpeditor != null) {
					tpeditor.createSolutionTab();
				}
			}
		});
		return createAlternative;
	}

	public JMenuItem getClearPane() {
		clearPane = new JMenuItem();
		clearPane.setText("Clear");
		clearPane.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				pane.clear();

			}
		});
		return clearPane;
	}

	
	public void mousePressed(MouseEvent e) {
		if (e.getComponent() instanceof ErrorPane)
			showClearErrorPane(e);
		else if (e.getComponent() instanceof UITabbedPaneEditor) {
			maybeShowPopup(e);
			int index = tpeditor.getSelectedIndex();
			Object editor = tpeditor.getComponentAt(index);
			if (editor instanceof EditorScrollPane) {
				JGraph graph = ((EditorScrollPane) editor).getComponent();
				if (graph instanceof SolutionGraph) {
					((UITabbedPaneEditor) e.getComponent()).getGraphHandler()
					.getSolution().add(0, graph);
					((UITabbedPaneEditor) e.getComponent()).getGraphHandler()
					.getCurrentGraph().add(0, graph);
				}
				((UITabbedPaneEditor) e.getComponent()).getGraphHandler()
				.getCurrentGraph().add(0, graph);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getComponent() instanceof ErrorPane)
			showClearErrorPane(e);
		else if (e.getComponent() instanceof UITabbedPaneEditor) {
			maybeShowPopup(e);
		}
	}

	/**
	 * Show the popup menu
	 * 
	 * @param MouseEvent
	 *            e
	 */
	private void maybeShowPopup(MouseEvent e) {
		// If right click
		if (e.isPopupTrigger()) {

			popup = new JPopupMenu();
			if (tpeditor.getSelectedIndex() == 0) {
				// Adds the menu items to popup menu
				popup.add(getSaveProblem());
				popup.add(getCreateSolution());

			} else {
				popup.add(getSaveModel());
				popup.add(getCloseTab());
				popup.add(getSaveAsSolution());
			}
			// Show the popup menu on the specific point
			popup.show(e.getComponent(), e.getX(), e.getY());
		}

	}

	/**
	 * Show the popup menu "Clear" to clear the error pane. This method is
	 * called when user do right click on error pane
	 * 
	 * @param MouseEvent
	 */
	private void showClearErrorPane(MouseEvent e) {

		if (e.isPopupTrigger()) {
			popup = new JPopupMenu();
			popup.add(getClearPane());
			popup.show(e.getComponent(), e.getX(), e.getY());
		}

	}

	//
	/**
	 * Execute save a solution modeled
	 */
	public void executeSaveSolutionModel(ActionEvent e) {

		JGraph graph = tpeditor.getTabGraph();
		RDFSerializator save = new RDFSerializator(graph);
		// save.saveSolutionOnDB();
		// TabsComponentManager.getErrorPaneInstance().setText(
		// "La solucion fue salvada");

	}

	/**
	 * modify by leo
	 * 
	 * @param e
	 */
	public void executeSaveProblemModel(ActionEvent e) {

		try {
			ProblemGraph graph = (ProblemGraph) tpeditor.getTabGraph();
			if (graph.getModel().getRootCount() >= 1) {
				// Iterate throw all node in graph
				// RDFSerializator rdfStorage = new RDFSerializator(graph);
				
					if(graph.connectivitiInspector()){
					UISaveDecisionProblem save = new UISaveDecisionProblem(
							graph);
					save.setModel((UIModeler) GraphEd.scenarios
							.getSelectedComponent());
					save.setLocationRelativeTo(null);
					}
					else
						throw new Exception("Please, check the graph because is not connected correctly");
				
			}
		}
				catch (CycleFoundException e1) {
					GraphEd.errorPane.printMessage(new Exception("Error, exist a cyclic connection in the graph"));
				}catch (Exception e2) {
					GraphEd.errorPane.printMessage(e2);
				}
			}
		
	
		


	

	public void executeSaveSolutionToProblem(ActionEvent e) {
		try {
			SolutionGraph solGraph = (SolutionGraph) tpeditor.getTabGraph();
			ProblemGraph proGraph = (ProblemGraph) tpeditor.getGraphs().get(0);
			if(proGraph.isSaved()){
				String solName = solGraph.getSavedName();
				String proName = proGraph.getSavedName();
				if(PersistentManager.getConnectionToPostgreSQL().tryConnection()){
							int solId = PersistentManager.getConnectionToPostgreSQL().getIdSolution(solName);
							int proId = PersistentManager.getConnectionToPostgreSQL().getIdProblem(proName);
							if (!(PersistentManager.getConnectionToPostgreSQL().existSolutonInDB(proId))) {
								PersistentManager.getConnectionToPostgreSQL().assingSolutionToProblem(proId, solId);
								JOptionPane.showMessageDialog(null, "The alternative " + solName + " has been saved as solution to the problem "+ proName);
							} else {
								int option = JOptionPane.showConfirmDialog(null,proName	+ " already have a solution, Do you want to remplaze it");
								// means that user has chosen yes option
								if (option == 0) {
									Hashtable<String, Object> values = new Hashtable<String, Object>();
									values.put("id_problem", proId);
									values.put("id_solution", solId);
									Hashtable<String, Object> conditions = new Hashtable<String, Object>();
									conditions.put("id_problem", proId);
									PersistentManager.getConnectionToPostgreSQL().update("problem_has__solution", values, conditions);
									JOptionPane.showMessageDialog(null, "The alternative " + solName + " has been saved as solution to the problem "+ proName);
									// means that user has chosen no option
								} else if (option == 1) {
									// means that user has chosen cancel option
								} else if (option == 2) {

								}
							}
				}
			}else{
				JOptionPane.showMessageDialog(new JFrame(), "Save the problem first");
			}
			
		} catch (Exception e1) {
			GraphEd.errorPane.printMessage(e1);
		}
	}

}
