package system.rdf.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXHeader;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;

import system.rbc.ReasoningProcess;
import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.dataBase.RDFTriple;
import system.rdf.dataBase.Solution;
import system.rdf.graph.EdgeManager;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.MySolutionEdge;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.ontology.OntologyTree;
import system.rename.Tools;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class UIRetrieveProblem extends javax.swing.JFrame {
	private JXHeader jXHeader1;
	private JLabel problemLabel;
	private JList ProblemList;
	private JScrollPane scrollProblemList;
	private JLabel viewLabel;
	private JButton btnCancel;
	private JButton btnLoad;
	private JSeparator jSeparator;
	private JScrollPane descriptionScrollPane;
	private JTextPane descriptionTextPane;
	private JLabel descriptionLabel;
	private JScrollPane graphScrollPane;
	private static GraphEd graphed = null;

	public ProblemGraph graph;
	public ArrayList<ProblemGraph> problems;
	public ConnectionToPostgres con;
	private JLabel JlblView;
	public Problem pro;
	public Solution sol;
	private static Hashtable<ProblemGraph, String> retrievedProblems;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIRetrieveProblem inst = new UIRetrieveProblem(graphed,
						retrievedProblems);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIRetrieveProblem(GraphEd graphed,
			Hashtable<ProblemGraph, String> retrievedProblems) {
		super();
		this.retrievedProblems = retrievedProblems;
		this.problems = new ArrayList<ProblemGraph>();
		setResizable(false);
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			this.setTitle("Retrieve Problem from Database");
			GroupLayout thisLayout = new GroupLayout(
					(JComponent) getContentPane());
			getContentPane().setLayout(thisLayout);
			this.setResizable(false);
			this.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10));
			{
				jXHeader1 = new JXHeader();
				jXHeader1.setTitle("Retrieve Problem");
				jXHeader1
				.setDescription("Load retrieved problem from database");
				jXHeader1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
						new java.awt.Color(192, 192, 192)));
				jXHeader1.setIcon(new ImageIcon(getClass().getClassLoader()
						.getResource("system/rdf/resources/database.png")));
			}
			{
				descriptionLabel = new JLabel();
				descriptionLabel.setText("Description:");
				descriptionLabel.setFont(new java.awt.Font(
						"Microsoft Sans Serif", 0, 11));
			}
			{
				descriptionScrollPane = new JScrollPane();
				descriptionScrollPane.setBorder(BorderFactory
						.createBevelBorder(BevelBorder.LOWERED));
				{
					descriptionTextPane = new JTextPane();
					descriptionScrollPane.setViewportView(descriptionTextPane);
					descriptionTextPane.setEditable(false);
					descriptionTextPane.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent evt) {
							descriptionTextAreaMousePressed(evt);
						}
					});

				}
			}
			{
				graphScrollPane = new JScrollPane();
				graphScrollPane.setBounds(0, 0, 153, 143);
				{
					graph = new ProblemGraph();
					graphScrollPane.setViewportView(graph);
					graph.insertProblem(pro);
					graph.setScale(0.20);
					graph.setEnabled(false);
				}
			}
			{
				jSeparator = new JSeparator();
				jSeparator.setBorder(BorderFactory
						.createEmptyBorder(1, 0, 0, 0));
			}
			{
				JlblView = new JLabel();
				JlblView.setText("View");
			}
			{
				viewLabel = new JLabel();
				viewLabel.setText("view");
				viewLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
						11));
				viewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				btnLoad = new JButton();
				btnLoad.setText("Load");
				btnLoad.setBackground(new java.awt.Color(192, 192, 192));
				btnLoad.setBorder(BorderFactory
						.createBevelBorder(BevelBorder.RAISED));
				btnLoad.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
				btnLoad.setFocusable(false);
				btnLoad.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnLoadActionPerformed(evt);
					}
				});
			}
			{
				btnCancel = new JButton();
				btnCancel.setText("Cancel");
				btnCancel.setBackground(new java.awt.Color(192, 192, 192));
				btnCancel.setBorder(BorderFactory
						.createBevelBorder(BevelBorder.RAISED));
				btnCancel.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
						11));
				btnCancel.setFocusable(false);
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCancelActionPerformed(evt);
					}
				});
			}
			{
				problemLabel = new JLabel();
				problemLabel.setText("Problems:");
				problemLabel.setFont(new java.awt.Font("Microsoft Sans Serif",
						0, 11));
			}
			{
				scrollProblemList = new JScrollPane();
				scrollProblemList.setBorder(BorderFactory
						.createBevelBorder(BevelBorder.LOWERED));
				{

					for (ProblemGraph problemGraph : retrievedProblems.keySet()) {
						problems.add(problemGraph);
					}

					// con.connect();
					// problems = con.getProblems();
					// con.disconnect();
					ListModel ProblemListModel = new DefaultComboBoxModel(
							problems.toArray());
					ProblemList = new JList();
					scrollProblemList.setViewportView(ProblemList);
					ProblemList.setModel(ProblemListModel);
					ProblemList.setFont(new java.awt.Font(
							"Microsoft Sans Serif", 0, 11));
					ProblemList.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent evt) {
							ProblemListMousePressed(evt);
						}
					});
					ProblemList
					.addVetoableChangeListener(new VetoableChangeListener() {
						public void vetoableChange(
								PropertyChangeEvent evt) {
							ProblemListVetoableChange(evt);
						}
					});
					ProblemList
					.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							ProblemListValueChanged(evt);
						}
					});
				}
			}
			thisLayout.setVerticalGroup(thisLayout
					.createSequentialGroup()
					.addComponent(jXHeader1, GroupLayout.PREFERRED_SIZE, 50,
							GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(
									thisLayout
									.createParallelGroup(
											GroupLayout.Alignment.BASELINE)
											.addComponent(problemLabel,
													GroupLayout.Alignment.BASELINE,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.PREFERRED_SIZE)
													.addComponent(viewLabel,
															GroupLayout.Alignment.BASELINE,
															GroupLayout.PREFERRED_SIZE,
															GroupLayout.PREFERRED_SIZE,
															GroupLayout.PREFERRED_SIZE)
															.addComponent(JlblView,
																	GroupLayout.Alignment.BASELINE,
																	GroupLayout.PREFERRED_SIZE,
																	GroupLayout.PREFERRED_SIZE,
																	GroupLayout.PREFERRED_SIZE))
																	.addGroup(
																			thisLayout
																			.createParallelGroup()
																			.addComponent(scrollProblemList,
																					GroupLayout.Alignment.LEADING,
																					GroupLayout.PREFERRED_SIZE, 159,
																					GroupLayout.PREFERRED_SIZE)
																					.addComponent(graphScrollPane,
																							GroupLayout.Alignment.LEADING,
																							GroupLayout.PREFERRED_SIZE, 159,
																							GroupLayout.PREFERRED_SIZE))
																							.addGap(17)
																							.addComponent(descriptionLabel, GroupLayout.PREFERRED_SIZE,
																									GroupLayout.PREFERRED_SIZE,
																									GroupLayout.PREFERRED_SIZE)
																									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																									.addComponent(descriptionScrollPane,
																											GroupLayout.PREFERRED_SIZE, 62,
																											GroupLayout.PREFERRED_SIZE)
																											.addGap(22)
																											.addComponent(jSeparator, GroupLayout.PREFERRED_SIZE,
																													GroupLayout.PREFERRED_SIZE,
																													GroupLayout.PREFERRED_SIZE)
																													.addGap(34)
																													.addGroup(
																															thisLayout
																															.createParallelGroup(
																																	GroupLayout.Alignment.BASELINE)
																																	.addComponent(btnLoad,
																																			GroupLayout.Alignment.BASELINE,
																																			GroupLayout.PREFERRED_SIZE, 24,
																																			GroupLayout.PREFERRED_SIZE)
																																			.addComponent(btnCancel,
																																					GroupLayout.Alignment.BASELINE,
																																					GroupLayout.PREFERRED_SIZE, 24,
																																					GroupLayout.PREFERRED_SIZE))
																																					.addContainerGap(43, 43));
			thisLayout
			.setHorizontalGroup(thisLayout
					.createSequentialGroup()
					.addGroup(
							thisLayout
							.createParallelGroup()
							.addGroup(
									GroupLayout.Alignment.LEADING,
									thisLayout
									.createParallelGroup()
									.addComponent(
											jXHeader1,
											GroupLayout.Alignment.LEADING,
											GroupLayout.PREFERRED_SIZE,
											447,
											GroupLayout.PREFERRED_SIZE)
											.addComponent(
													jSeparator,
													GroupLayout.Alignment.LEADING,
													GroupLayout.PREFERRED_SIZE,
													447,
													GroupLayout.PREFERRED_SIZE))
													.addGroup(
															thisLayout
															.createSequentialGroup()
															.addPreferredGap(
																	jXHeader1,
																	scrollProblemList,
																	LayoutStyle.ComponentPlacement.INDENT)
																	.addGroup(
																			thisLayout
																			.createParallelGroup()
																			.addComponent(
																					scrollProblemList,
																					GroupLayout.Alignment.LEADING,
																					GroupLayout.PREFERRED_SIZE,
																					235,
																					GroupLayout.PREFERRED_SIZE)
																					.addGroup(
																							GroupLayout.Alignment.LEADING,
																							thisLayout
																							.createSequentialGroup()
																							.addComponent(
																									problemLabel,
																									GroupLayout.PREFERRED_SIZE,
																									85,
																									GroupLayout.PREFERRED_SIZE)
																									.addGap(150))
																									.addGroup(
																											GroupLayout.Alignment.LEADING,
																											thisLayout
																											.createSequentialGroup()
																											.addComponent(
																													descriptionLabel,
																													GroupLayout.PREFERRED_SIZE,
																													85,
																													GroupLayout.PREFERRED_SIZE)
																													.addGap(150))
																													.addComponent(
																															descriptionScrollPane,
																															GroupLayout.Alignment.LEADING,
																															GroupLayout.PREFERRED_SIZE,
																															235,
																															GroupLayout.PREFERRED_SIZE))
																															.addGap(21)
																															.addGroup(
																																	thisLayout
																																	.createParallelGroup()
																																	.addGroup(
																																			GroupLayout.Alignment.LEADING,
																																			thisLayout
																																			.createSequentialGroup()
																																			.addGroup(
																																					thisLayout
																																					.createParallelGroup()
																																					.addGroup(
																																							thisLayout
																																							.createSequentialGroup()
																																							.addGap(0,
																																									0,
																																									Short.MAX_VALUE)
																																									.addComponent(
																																											btnLoad,
																																											GroupLayout.PREFERRED_SIZE,
																																											62,
																																											GroupLayout.PREFERRED_SIZE))
																																											.addGroup(
																																													GroupLayout.Alignment.LEADING,
																																													thisLayout
																																													.createSequentialGroup()
																																													.addGap(7)
																																													.addComponent(
																																															JlblView,
																																															GroupLayout.PREFERRED_SIZE,
																																															34,
																																															GroupLayout.PREFERRED_SIZE)
																																															.addGap(21)))
																																															.addGap(26)
																																															.addComponent(
																																																	btnCancel,
																																																	GroupLayout.PREFERRED_SIZE,
																																																	60,
																																																	GroupLayout.PREFERRED_SIZE)
																																																	.addPreferredGap(
																																																			LayoutStyle.ComponentPlacement.RELATED))
																																																			.addGroup(
																																																					thisLayout
																																																					.createSequentialGroup()
																																																					.addGap(0,
																																																							0,
																																																							Short.MAX_VALUE)
																																																							.addComponent(
																																																									graphScrollPane,
																																																									GroupLayout.PREFERRED_SIZE,
																																																									153,
																																																									GroupLayout.PREFERRED_SIZE)))
																																																									.addGap(26)))
																																																									.addGap(229)
																																																									.addComponent(viewLabel,
																																																											GroupLayout.PREFERRED_SIZE, 165,
																																																											GroupLayout.PREFERRED_SIZE));
			pack();
			this.setSize(458, 485);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JLabel getProblemLabel() {
		return problemLabel;
	}

	public JList getProblemList() {
		return ProblemList;
	}

	public JTextPane getDescriptionTextPane() {
		return descriptionTextPane;
	}

	public JScrollPane getDescriptionScrollPane() {
		return descriptionScrollPane;
	}

	public JButton getBtnLoad() {
		return btnLoad;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JLabel getViewLabel() {
		return viewLabel;
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void btnLoadActionPerformed(ActionEvent evt) {
		this.dispose();
		String statusText = retrievedProblems.get(graph);

		// If the retrieved problem is equals with the modeled one, then load
		// its solution
		if (statusText.equals("Equals")) {
			if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
				try {
					int solution_id = PersistentManager
							.getConnectionToPostgreSQL().getSolutionForProblem(
									graph.getProblem_list().get(0).getId());
					sol = PersistentManager.getConnectionToPostgreSQL()
							.getSolution(solution_id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			UIModeler modeler = ((UIModeler) graphed.scenarios
					.getSelectedComponent());
			modeler.getEditorTabbedPane().loadSolution(sol);
			
			// If the modeled problem is a subgraph of the retrieved one
		} else if (statusText.equals("Subgraph")) {
			if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
				try {
					int solution_id = PersistentManager
							.getConnectionToPostgreSQL().getSolutionForProblem(
									graph.getProblem_list().get(0).getId());
					sol = PersistentManager.getConnectionToPostgreSQL()
							.getSolution(solution_id);

					UIModeler modeler = ((UIModeler) graphed.scenarios
							.getSelectedComponent());
					if (modeler != null) {
						int selectedIndex = graphed.scenarios
								.getSelectedIndex();

						JGraph GraphTemp = ((UIModeler) graphed.scenarios
								.getSelectedComponent()).getEditorTabbedPane()
								.getTabGraph();

						ProblemGraph problemgraph = (ProblemGraph) GraphTemp;						
						// All code until here is to get the modeled problem, the retrieved problem and the retrieved solution

						// Creating the reasoning instance to get the adapted solution
						ReasoningProcess reasoner = new ReasoningProcess();

						// Getting all triples must be removed from the solution
						ArrayList<RDFTriple> triples = reasoner
								.GetAdaptedSolution(graph, problemgraph, sol,
										"Subgraph");
						ArrayList<RDFTriple> solutionTriples = sol.getTriples();

						// Removing triples from the retrieved solution
						for (int i = 0; i < triples.size(); i++) {
							for (int j = 0; j < solutionTriples.size(); j++) {
								if (solutionTriples
										.get(j)
										.getSubject()
										.getLabel()
										.equals(triples.get(i).getSubject()
												.getLabel())
												&& solutionTriples
												.get(j)
												.getObject()
												.getLabel()
												.equals(triples.get(i)
														.getObject().getLabel())) {
									sol.getTriples().remove(j);
								}
							}
						}

						// Show the adapted solution in the editor
						UIModeler modeler1 = ((UIModeler) graphed.scenarios
								.getSelectedComponent());
						modeler1.getEditorTabbedPane().loadSolution(sol);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// If the modeled problem is a super graph of the retrieved one
		} else if (statusText.equals("Container")) {
			if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
				try {
					int solution_id = PersistentManager
							.getConnectionToPostgreSQL().getSolutionForProblem(
									graph.getProblem_list().get(0).getId());
					sol = PersistentManager.getConnectionToPostgreSQL()
							.getSolution(solution_id);

					UIModeler modeler = ((UIModeler) graphed.scenarios
							.getSelectedComponent());
					if (modeler != null) {
						int selectedIndex = graphed.scenarios
								.getSelectedIndex();

						JGraph GraphTemp = ((UIModeler) graphed.scenarios
								.getSelectedComponent()).getEditorTabbedPane()
								.getTabGraph();

						ProblemGraph problemgraph = (ProblemGraph) GraphTemp;
						// All code until here is to get the modeled problem, the retrieved problem and the retrieved solution

						// Creating the reasoning instance to get the adapted solution
						ReasoningProcess reasoner = new ReasoningProcess();

						// Loading the retrieved solution to then adding triples to it
						UIModeler modeler1 = ((UIModeler) graphed.scenarios
								.getSelectedComponent());
						modeler1.getEditorTabbedPane().loadSolution(sol);

						// Getting all triples must be added to the showed solution
						ArrayList<RDFTriple> triple = reasoner
								.GetAdaptedSolution(graph, problemgraph, sol,
										"Container");

						// Getting the graph of the modeled solution
						SolutionGraph solGraph = (SolutionGraph) modeler1
								.getEditorTabbedPane().getGraphs().get(1);

						// Iterating over all triples must be added to the solution
						for (int i = 0; i < triple.size(); i++) {
							// Inserting new indicators to the graph
							solGraph.insertCell(triple.get(i).getSubject()
									.getPoint(), triple.get(i).getSubject()
									.getLabel(), triple.get(i)
									.getSubjetProcess());

							// Creating ports for new indicators in order to after connect them
							Object sourcePort = solGraph.getPortForLocation(
									triple.get(i).getSubject().getPoint()
									.getX(), triple.get(i).getSubject()
									.getPoint().getY());
							Object targetPort = solGraph
									.getPortForLocation(triple.get(i)
											.getObject().getPoint().getX(),
											triple.get(i).getObject()
											.getPoint().getY());

							// Preparing the graph to connect new indicators
							solGraph.setSavedAsAlternative(false);
							// Creating the edge and tools to connect indicators
							Tools tools = new Tools();
							MySolutionEdge edge = new MySolutionEdge(solGraph);
							
							// If the graph accepts this source and this target then connect them
							if (solGraph.getModel().acceptsSource(edge,
									sourcePort)
									&& solGraph.getModel().acceptsTarget(edge,
											targetPort)) {
								
								// Insert source and target ports
								edge.getAttributes().applyMap(
										EdgeManager.problemEdgeAttributes());
								solGraph.getGraphLayoutCache().insertEdge(edge,
										sourcePort, targetPort);

								edge.setSource(sourcePort);
								edge.setTarget(targetPort);

								MyProblemCustomCell vertexSource = (MyProblemCustomCell) DefaultGraphModel
										.getSourceVertex(solGraph.getModel(),
												edge);
								MyProblemCustomCell vertexTarget = (MyProblemCustomCell) DefaultGraphModel
										.getTargetVertex(solGraph.getModel(),
												edge);

								RDFSerializator serializator = new RDFSerializator();

								// Calculate the frequency of the connection and set it to each connection between source and target
								String totalFrequency = tools
										.round(serializator
												.calculateTotalFrequencyForAlternatives(
														vertexSource,
														vertexTarget), 3);

								Map attr = edge.getAttributes().applyMap(
										EdgeManager.problemEdgeAttributes());

								edge.setUserObject(totalFrequency);

								if (Double.parseDouble(totalFrequency) < 0.25) {
									Map map = new Hashtable();
									// Add a Line End Attribute
									GraphConstants.setLineEnd(map,
											GraphConstants.ARROW_TECHNICAL);
									// Fill end line
									GraphConstants.setEndFill(map, true);
									// Add a label along edge attribute
									GraphConstants.setLabelAlongEdge(map, true);
									// Cannot edit the edge
									GraphConstants.setEditable(map, false);
									// Changing the color for the frecuency
									GraphConstants.setLineColor(map, Color.RED);
									solGraph.getGraphLayoutCache().editCell(
											edge, map);
								} else {
									Map map = new Hashtable();
									// Add a Line End Attribute
									GraphConstants.setLineEnd(map,
											GraphConstants.ARROW_TECHNICAL);
									// Fill end line
									GraphConstants.setEndFill(map, true);
									// Add a label along edge attribute
									GraphConstants.setLabelAlongEdge(map, true);
									// Cannot edit the edge
									GraphConstants.setEditable(map, false);

									solGraph.getGraphLayoutCache().editCell(
											edge, map);

								}
							}
						}
					}
					
//					UIModeler modeler1 = ((UIModeler) graphed.scenarios.getSelectedComponent());
//					int selectedTab = ((UIModeler) graphed.scenarios.getSelectedComponent())
//							.getEditorTabbedPane().getSelectedIndex();
//					
//					System.out.println(((ProblemGraph) ((UIModeler) graphed.scenarios
//							.getSelectedComponent()).getEditorTabbedPane()
//							.getGraphs().get(0)).isDropEnabled() + "drop");
//					
//					System.out.println(((ProblemGraph) ((UIModeler) graphed.scenarios
//							.getSelectedComponent()).getEditorTabbedPane()
//							.getGraphs().get(0)).isDragEnabled() + "drag");
//					
//					System.out.println(((ProblemGraph) ((UIModeler) graphed.scenarios
//							.getSelectedComponent()).getEditorTabbedPane()
//							.getGraphs().get(0)).isBendable() + "bendable");
//					
//					System.out.println(((ProblemGraph) ((UIModeler) graphed.scenarios
//							.getSelectedComponent()).getEditorTabbedPane()
//							.getGraphs().get(0)).isEditable() + "editable");
//					
//					System.out.println(((ProblemGraph) ((UIModeler) graphed.scenarios
//							.getSelectedComponent()).getEditorTabbedPane()
//							.getGraphs().get(0)).isSaved() + "saved");
//					
//					System.out.println(((ProblemGraph) ((UIModeler) graphed.scenarios
//							.getSelectedComponent()).getEditorTabbedPane()
//							.getGraphs().get(0)).isValid() + "valid");
					
					((ProblemGraph) ((UIModeler) graphed.scenarios
							.getSelectedComponent()).getEditorTabbedPane()
							.getGraphs().get(0)).setDropEnabled(true);
					
					((ProblemGraph) ((UIModeler) graphed.scenarios
							.getSelectedComponent()).getEditorTabbedPane()
							.getGraphs().get(0)).setDragEnabled(true);
					
					System.out.println(((ProblemGraph) ((UIModeler) graphed.scenarios
							.getSelectedComponent()).getEditorTabbedPane()
							.getGraphs().get(0)).getDropTarget().getComponent().getAccessibleContext());
					
//					System.out.println(selectedTab);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void ProblemListValueChanged(ListSelectionEvent evt) {
		// System.out.println("ProblemList.valueChanged, event="+evt);

	}

	private void ProblemListVetoableChange(PropertyChangeEvent evt) {
		// System.out.println("ProblemList.vetoableChange, event="+evt);
	}

	private void ProblemListMousePressed(MouseEvent evt) {
		// pro = (Problem) getProblemList().getSelectedValue();

		graph = new ProblemGraph();
		// graph.insertProblem(pro);
		graph = (ProblemGraph) getProblemList().getSelectedValue();
		graph.setScale(0.20);
		graph.setEnabled(false);
		getGraphScrollPane().setViewportView(graph);
		graph.setPreferredSize(new java.awt.Dimension(223, 138));

		String statusText = retrievedProblems.get(graph);
		String status = "";

		if (statusText.equals("Equals")) {
			status = "This problem is equals to the modeled one";
		} else if (statusText.equals("Subgraph")) {
			status = "The modeled problem is a subgraph of this one";
		} else if (statusText.equals("Container")) {
			status = "This problem is a subgraph of the modeled one";
		}

		getDescriptionTextPane().setText(
				graph.getProblem_list().get(0).getDescription()
				+ "\nStatus: \n" + status);
		// GraphManager manager = new GraphManager();
		// manager.getloadedProblemGraph.add(graph);
	}

	public JScrollPane getGraphScrollPane() {
		return graphScrollPane;
	}

	private void descriptionTextAreaMousePressed(MouseEvent evt) {
	}

}
