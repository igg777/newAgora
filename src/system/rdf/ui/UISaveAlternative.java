package system.rdf.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.swingx.JXHeader;
import org.jgraph.JGraph;

import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.ExtractData;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;


public class UISaveAlternative extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8640492396434446371L;
	private JLabel jLabel1;
	private JLabel solutionNameLabel;
	private JScrollPane descriptionScrollPane;
	private JTextPane descriptionPane;
	private JLabel descriptionLabel;
	private JXHeader jXHeader1;
	private JComboBox associatedProblemComboBox;
	private JButton btnCancel;
	private JButton btnSave;
	private JTextField txtSolutionName;
	private SolutionGraph graph = null;
	private boolean flag = false;
	UITabbedPaneEditor tpeditor = null;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public UITabbedPaneEditor getTpeditor() {
		return tpeditor;
	}

	public void setTpeditor(UITabbedPaneEditor tpeditor) {
		this.tpeditor = tpeditor;
	}

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UISaveAlternative inst = new UISaveAlternative();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UISaveAlternative() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Save Alternative");
			GroupLayout thisLayout = new GroupLayout(
					(JComponent) getContentPane());
			getContentPane().setLayout(thisLayout);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			pack();
			this.setSize(300, 320);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			{
				jLabel1 = new JLabel();
				jLabel1.setText("Associated problem:");
			}
			{
				solutionNameLabel = new JLabel();
				solutionNameLabel.setText("Alternative name:");
			}
			{
				txtSolutionName = new JTextField();
			}
			{
				descriptionScrollPane = new JScrollPane();
				{
					descriptionPane = new JTextPane();
					descriptionScrollPane.setViewportView(descriptionPane);
				}
			}
			{
				descriptionLabel = new JLabel();
				descriptionLabel.setText("Description:");
			}
			{
				btnSave = new JButton();
				btnSave.setText("Save");
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnSaveActionPerformed(evt);
					}
				});
			}
			{
				btnCancel = new JButton();
				btnCancel.setText("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCancelActionPerformed(evt);
					}
				});
			}
			{
				PersistentManager p = new PersistentManager();

				associatedProblemComboBox = new JComboBox();
				//associatedProblemComboBox.setModel(p.getProblem());
				String problemName  = (((ProblemGraph) ((UIModeler) GraphEd.scenarios
						.getSelectedComponent()).getEditorTabbedPane()
						.getGraphs().get(0)).getSavedName());
				int problemId = p.getConnectionToPostgreSQL().getIdProblem(problemName);
				ExtractData activeProblem = new ExtractData(problemId, problemName);
				associatedProblemComboBox.addItem(activeProblem);				
				associatedProblemComboBox.setEnabled(false);
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().add(
					getJXHeader1(), 0, 50, Short.MAX_VALUE).addPreferredGap(
					LayoutStyle.UNRELATED).add(jLabel1,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE).add(associatedProblemComboBox,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE).addPreferredGap(
					LayoutStyle.UNRELATED).add(solutionNameLabel,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE).add(txtSolutionName,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE).addPreferredGap(
					LayoutStyle.RELATED).add(descriptionLabel,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE).add(descriptionScrollPane,
					GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED).add(
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
					.addContainerGap());
			thisLayout
					.setHorizontalGroup(thisLayout
							.createParallelGroup()
							.add(GroupLayout.LEADING, getJXHeader1(), 0, 292,
									Short.MAX_VALUE)
							.add(
									thisLayout
											.createSequentialGroup()
											.addPreferredGap(getJXHeader1(),
													solutionNameLabel,
													LayoutStyle.INDENT)
											.add(
													thisLayout
															.createParallelGroup()
															.add(
																	GroupLayout.LEADING,
																	thisLayout
																			.createSequentialGroup()
																			.add(
																					thisLayout
																							.createParallelGroup()
																							.add(
																									GroupLayout.LEADING,
																									solutionNameLabel,
																									GroupLayout.PREFERRED_SIZE,
																									GroupLayout.PREFERRED_SIZE,
																									GroupLayout.PREFERRED_SIZE)
																							.add(
																									GroupLayout.LEADING,
																									thisLayout
																											.createSequentialGroup()
																											.add(
																													descriptionLabel,
																													GroupLayout.PREFERRED_SIZE,
																													GroupLayout.PREFERRED_SIZE,
																													GroupLayout.PREFERRED_SIZE)
																											.add(
																													29)))
																			.add(
																					btnSave,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.PREFERRED_SIZE)
																			.add(
																					45)
																			.add(
																					btnCancel,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.PREFERRED_SIZE)
																			.add(
																					18))
															.add(
																	GroupLayout.LEADING,
																	jLabel1,
																	GroupLayout.PREFERRED_SIZE,
																	231,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	txtSolutionName,
																	GroupLayout.PREFERRED_SIZE,
																	231,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	descriptionScrollPane,
																	GroupLayout.PREFERRED_SIZE,
																	233,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	associatedProblemComboBox,
																	GroupLayout.PREFERRED_SIZE,
																	231,
																	GroupLayout.PREFERRED_SIZE))
											.addContainerGap(47, 47)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JLabel getSolutionNameLabel() {
		return solutionNameLabel;
	}

	public JScrollPane getDescriptionScrollPane() {
		return descriptionScrollPane;
	}

	public JTextPane getDescriptionPane() {
		return descriptionPane;
	}

	public JLabel getDescriptionLabel() {
		return descriptionLabel;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JTextField getTxtSolutionName() {
		return txtSolutionName;
	}

	private void btnSaveActionPerformed(ActionEvent evt) {
		try {
			ConnectionToPostgres connect = PersistentManager
					.getConnectionToPostgreSQL();
			ExtractData objectSelectedId = (ExtractData) getAssociatedProblemComboBox()
					.getSelectedItem();
			int selectedId = objectSelectedId.getId();

			String name = getTxtSolutionName().getText();
			if (!(connect.existAlternativeInDB(name, selectedId))) {
				executeSaveAction(name, selectedId);				
			} else {
				// ask to the user if wish replace the alternative 
				int option = JOptionPane.showConfirmDialog(this, name
						+ " already exist, Do you want to replaze it?");
				// means that user has chosen yes option
				if (option == 0) {
					//deleting previous alternative
                     Hashtable<String, Object> conditions = new Hashtable<String, Object>();
                     conditions.put("name", name);
                     conditions.put("id_problem", selectedId);
                     connect.delete("solution", conditions);
                     //inserting the new one
                     executeSaveAction(name, selectedId);
					// means that user has chosen no option
				} else if (option == 1) {
					if (flag) {
						tpeditor.remove(tpeditor.getSelectedIndex());
						this.dispose();
					}
					// means that user has chosen cancel option
				} else if (option == 2) {

				}
			}
			connect.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			GraphEd.errorPane.printMessage(e);
		}
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();

	}

	protected void clear() {
		getTxtSolutionName().setText("");
		getDescriptionPane().setText("");
	}

	public JComboBox getAssociatedProblemComboBox() {
		return associatedProblemComboBox;
	}

	private JXHeader getJXHeader1() {
		if (jXHeader1 == null) {
			jXHeader1 = new JXHeader();
			jXHeader1.setTitle("Alternative to problem");
			jXHeader1.setDescription("Save on database a alternative");
		}
		return jXHeader1;
	}

	public SolutionGraph getGraph() {
		return graph;
	}

	public void setGraph(SolutionGraph graph) {
		this.graph = graph;
	}
	
	/**
	 * 
	 * @param alternative's name
	 * @param associated problem's id
	 */
	public void executeSaveAction(String alternativeName, int idPro){
		graph.setSavedName(alternativeName);
		String desc = getDescriptionPane().getText();
		RDFSerializator serializator = new RDFSerializator(graph);
		serializator.saveSolutionOnDB(idPro, alternativeName, desc);
		graph.setSavedAsAlternative(true);
		clear();
		JOptionPane.showMessageDialog(null,
				"The Alternative "+ alternativeName +"has been saved");
		((UIModeler) GraphEd.scenarios.getSelectedComponent())
				.setTitle(alternativeName);
		GraphEd.refreshScenarios();
		if (flag) {
			tpeditor.remove(tpeditor.getSelectedIndex());
		}
		this.dispose();
	}

}
