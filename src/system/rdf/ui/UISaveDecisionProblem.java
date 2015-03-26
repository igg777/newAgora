package system.rdf.ui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXImagePanel;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;

import system.rename.InsertStatements;
import system.rename.Tools;
import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.dataBase.RDFTriple;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.handlers.ExceptionHandler;
import system.rdf.utils.TabsComponentManager;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;

public class UISaveDecisionProblem extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lbDescription;
	private JLabel lbProblemName;
	private JTextField txtProblemName;
	private JEditorPane descriptionPanel;
	private JButton btnSave;
	private JScrollPane descriptionScroll;
	private JButton btnCancel;
	private UIModeler model;
	// this flag is to know if the model is closing or not
	private boolean flag = false;
	private RDFSerializator serializator = new RDFSerializator();
	private ProblemGraph graph;
	private JXHeader bannerHeader;

	public UISaveDecisionProblem() {
		super();
		initGUI();
	}

	public UISaveDecisionProblem(ProblemGraph graph) {
		super();
		this.graph = graph;
		initGUI();
	}

	public static void main(String[] args) {
		new UISaveDecisionProblem();
	}

	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout(
					(JComponent) getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Save Decisional Problem");
			this.setVisible(true);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			this.setResizable(false);
			{
				descriptionScroll = new JScrollPane();
				descriptionScroll.getHorizontalScrollBar().setPreferredSize(
						new java.awt.Dimension(179, 15));
				{
					descriptionPanel = new JTextPane();
					descriptionScroll.setViewportView(descriptionPanel);
					descriptionPanel.setFont(new java.awt.Font("Arial", 0, 12));
				}
			}

			{
				txtProblemName = new JTextField();
				txtProblemName.setFont(new java.awt.Font("Arial", 0, 12));
				txtProblemName.setBorder(BorderFactory
						.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				lbProblemName = new JLabel();
				lbProblemName.setText("Name:");
				lbProblemName.setFont(new java.awt.Font("Microsoft Sans Serif",
						0, 11));
			}
			{
				btnSave = new JButton();
				btnSave.setText("Save");
				btnSave
						.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
								11));
				btnSave.setBackground(new java.awt.Color(210, 210, 200));
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnSaveActionPerformed(evt);
					}
				});
			}
			{
				lbDescription = new JLabel();
				lbDescription.setText("Description:");
				lbDescription.setFont(new java.awt.Font("Microsoft Sans Serif",
						0, 11));
			}
			{
				btnCancel = new JButton();
				btnCancel.setText("Cancel");
				btnCancel.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
						11));
				btnCancel.setBackground(new java.awt.Color(210, 210, 200));
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCancelActionPerformed(evt);
					}
				});
			}
			{
				bannerHeader = new JXHeader();
				bannerHeader.setTitle("Desicion Problem");
				bannerHeader.setDescription("Save a desicion problem");
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().add(
					bannerHeader, GroupLayout.PREFERRED_SIZE, 50,
					GroupLayout.PREFERRED_SIZE).add(22).add(lbProblemName,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE).add(txtProblemName,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE).addPreferredGap(
					LayoutStyle.UNRELATED).add(lbDescription,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE).add(descriptionScroll,
					GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
					.add(27).add(
							thisLayout
									.createParallelGroup(GroupLayout.BASELINE)
									.add(GroupLayout.BASELINE, btnSave,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE).add(
											GroupLayout.BASELINE, btnCancel,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE))
					.addContainerGap(25, 25));
			thisLayout
					.setHorizontalGroup(thisLayout
							.createParallelGroup()
							.add(GroupLayout.LEADING, bannerHeader,
									GroupLayout.PREFERRED_SIZE, 294,
									GroupLayout.PREFERRED_SIZE)
							.add(
									thisLayout
											.createSequentialGroup()
											.addPreferredGap(bannerHeader,
													descriptionScroll,
													LayoutStyle.INDENT)
											.add(
													thisLayout
															.createParallelGroup()
															.add(
																	GroupLayout.LEADING,
																	descriptionScroll,
																	GroupLayout.PREFERRED_SIZE,
																	234,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	txtProblemName,
																	GroupLayout.PREFERRED_SIZE,
																	234,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	thisLayout
																			.createSequentialGroup()
																			.add(
																					thisLayout
																							.createParallelGroup()
																							.add(
																									GroupLayout.LEADING,
																									lbDescription,
																									GroupLayout.PREFERRED_SIZE,
																									GroupLayout.PREFERRED_SIZE,
																									GroupLayout.PREFERRED_SIZE)
																							.add(
																									GroupLayout.LEADING,
																									thisLayout
																											.createSequentialGroup()
																											.add(
																													lbProblemName,
																													GroupLayout.PREFERRED_SIZE,
																													GroupLayout.PREFERRED_SIZE,
																													GroupLayout.PREFERRED_SIZE)
																											.add(
																													25)))
																			.add(
																					77)
																			.add(
																					btnSave,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.PREFERRED_SIZE)
																			.add(
																					15)
																			.add(
																					btnCancel,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.PREFERRED_SIZE)))
											.addContainerGap(48, 48)));
			pack();
			this.setSize(300, 320);
		} catch (Exception e) {
			System.err.println("ERROR:");
			e.printStackTrace();
		}
	}

	public JTextField getTxtProblemName() {
		return txtProblemName;
	}

	public JLabel getLbProblemName() {
		return lbProblemName;
	}

	public JEditorPane getDescriptionPanel() {
		return descriptionPanel;
	}

	public JLabel getLbDescription() {
		return lbDescription;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	/**
	 * Execute the button save
	 * 
	 * @param ActionEvent
	 */
	private void btnSaveActionPerformed(ActionEvent evt) {
		try {
			String name = getTxtProblemName().getText();
			if(PersistentManager.getConnectionToPostgreSQL().tryConnection()){
				
				if (!(PersistentManager.getConnectionToPostgreSQL().existProblemInDB(name))) {
					executeSaveAction(name, true);
					if (flag) {
						GraphEd.modelsss.remove(model);
						GraphEd.tableInsertedStatement = TabsComponentManager
						.getTableInsertedStatement();
						GraphEd.errorPane.clear();
					}

				} else if (graph.getSavedName().equals(name) == false) {
					// aqui mplementar lo del borrar y insertar de nuevo si el
					// usuario desea
					int option = JOptionPane
							.showConfirmDialog(
									this,
									name
											+ " already exist, Do you want to replaze it and erase all its alternatives?");
					// means that user has chosen yes option
					if (option == 0) {
						Hashtable<String, Object> conditions = new Hashtable<String, Object>();
						int idGraph = PersistentManager.getConnectionToPostgreSQL().getIdGraph(name);
						conditions.put("id_graph", idGraph);
						PersistentManager.getConnectionToPostgreSQL().delete("graph", conditions);
						executeSaveAction(name, true);
						if (flag) {
							GraphEd.modelsss.remove(model);
						}
						// means that user has chosen no option
					} else if (option == 1) {
						if (flag) {
							GraphEd.modelsss.remove(model);
						}

						// means that user has chosen cancel option
					} else if (option == 2) {
						// do nothing
					}
				} else {
					//aqui es para implementar lo del update
					int option = JOptionPane
							.showConfirmDialog(
									this,
									name
											+ " already exist, Do you want to update it?");
					// means that user has chosen yes option
					if (option == 0) {
						executeSaveAction(name, false);
						if (flag) {
							GraphEd.modelsss.remove(model);
						}
						// means that user has chosen no option
					} else if (option == 1) {
						if (flag) {
							GraphEd.modelsss.remove(model);
						}

						// means that user has chosen cancel option
					} else if (option == 2) {
						// do nothing
					}
					//
				}
			}
			
			GraphEd.modelsss.remove(model);
			
		} catch (Exception e) {
			TabsComponentManager.getErrorPaneInstance().printMessage(e);
			e.printStackTrace();
		}
		GraphEd.refreshScenarios();
		this.dispose();
	}

	/**
	 * Execute the save Button
	 */
	
	public JScrollPane getDescriptionScroll() {
		return descriptionScroll;
	}

	public UIModeler getModel() {
		return model;
	}

	public void setModel(UIModeler model) {
		this.model = model;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * this method is to eliminate the repetition of the same code many times if
	 * the problem was previously saved <code>save</code> = true, if its an update
	 * <code>save</code> is false
	 * 
	 * @author Leo
	 * @execute the save problem action
	 * 
	 * 
	 * @param proName
	 * @param save
	 */
	public void executeSaveAction(String proName, boolean save) {
		try {
			graph.setSaved(true);
			graph.setSavedName(proName);
			String description = getDescriptionPanel().getText();
			if (save) {
				PersistentManager.getSerializator().saveProblem(graph, proName,
						description);
				int position = GraphEd.scenarios.getSelectedIndex();
				GraphEd.modelsss.get(position).getEditorTabbedPane().getGraphs().set(0, graph);
				GraphEd.ReloadProblems(proName);
			} else {
				PersistentManager.getSerializator().updateProblem(graph,
						proName, description);
				JOptionPane.showMessageDialog(null, "The Problem "+ proName+" has been updated");
			}
			model.setTitle(proName);
			
			Object[] roots = graph.getRoots();
			for (int i = 0; i < roots.length; i++) {
				Object object = roots[i];
				if (object instanceof DefaultGraphCell) {
					if (graph.getModel().isEdge(object)) {
						GraphModel gm = graph.getModel();
						DefaultEdge edge = (DefaultEdge) object;
						MyProblemCustomCell sourceVertex = (MyProblemCustomCell)gm
						.getParent(gm.getSource(object));
						MyProblemCustomCell targetVertex = (MyProblemCustomCell)gm
						.getParent(gm.getTarget(object));
						Tools tools = new Tools();
						  String totalFrequency = tools.round(serializator.calculateTotalFrequencyForProblems(sourceVertex, targetVertex), 3);
							edge.setUserObject(totalFrequency);
						Map attr = ((DefaultEdge) edge).getAttributes();
						graph.getGraphLayoutCache().editCell(edge, attr);
					}
				}
			}

		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

}
