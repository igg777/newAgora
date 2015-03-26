package system.rdf.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.swingx.JXHeader;

import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Solution;
import system.rdf.graph.SolutionGraph;

import javax.swing.SwingUtilities;



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class UILoadAlternative extends javax.swing.JFrame {
	private JXHeader jXHeader1;
	private JLabel solutionsLabel;
	private JLabel descriptionLabel;
	private JCheckBox associatedProblemCheck;
	private JButton btnCancel;
	private JButton btnLoad;
	private JLabel viewLabel;
	private JScrollPane viewScrollPane;
	private JSeparator separator;
	private JScrollPane descriptionScrollPane;
	private JTextPane descriptionTextPane;
	private JScrollPane solutionsScrollPane;
	private JList solutionsList;

	public SolutionGraph graph;
	public ArrayList<Solution> solutions;
	public ConnectionToPostgres con;
	public static GraphEd graphed;
	public Solution sol;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UILoadAlternative inst = new UILoadAlternative(graphed);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UILoadAlternative(GraphEd principal) {
		super();
		this.graphed = principal;
		con = PersistentManager.getConnectionToPostgreSQL();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			GroupLayout thisLayout = new GroupLayout(
					(JComponent) getContentPane());
			getContentPane().setLayout(thisLayout);
			this.setTitle("Load Solution from Database");
			this.setResizable(false);
			{
				jXHeader1 = new JXHeader();
				jXHeader1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
						new java.awt.Color(192, 192, 192)));
				jXHeader1.setTitle("Load Solution");
				jXHeader1.setDescription("Load a solution saved on database");
			}
			{
				solutionsLabel = new JLabel();
				solutionsLabel.setText("Solutions:");
				solutionsLabel.setFont(new java.awt.Font(
						"Microsoft Sans Serif", 0, 11));
			}
			{
				solutionsScrollPane = new JScrollPane();
				solutionsScrollPane.setBorder(BorderFactory
						.createBevelBorder(BevelBorder.LOWERED));
				{
					con.connect();
					solutions = con.getSolutions();
					con.disconnect();
					ListModel solutionsListModel = new DefaultComboBoxModel(
							solutions.toArray());
					solutionsList = new JList();
					solutionsScrollPane.setViewportView(solutionsList);
					solutionsList.setModel(solutionsListModel);
					solutionsList.setFont(new java.awt.Font(
							"Microsoft Sans Serif", 0, 11));
					solutionsList.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent evt) {
							solutionsListMousePressed(evt);
						}
					});
				}
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
					descriptionTextPane.setFont(new java.awt.Font(
							"Microsoft Sans Serif", 0, 11));
					descriptionTextPane.setEditable(false);
					descriptionTextPane.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent evt) {
							descriptionTextAreaMousePressed(evt);
						}
					});
				}
			}
			{
				separator = new JSeparator();
			}
			{
				viewScrollPane = new JScrollPane();
				viewScrollPane.setBorder(new LineBorder(new java.awt.Color(192,
						192, 192), 1, false));
				viewScrollPane.setSize(153, 143);
			}
			{
				viewLabel = new JLabel();
				viewLabel.setText("view");
				viewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				viewLabel.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
						11));
			}
			{
				btnLoad = new JButton();
				btnLoad.setText("Load");
				btnLoad.setBorder(BorderFactory
						.createBevelBorder(BevelBorder.RAISED));
				btnLoad
				.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
						11));
				btnLoad.setBackground(new java.awt.Color(192, 192, 192));
				btnLoad.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnLoadActionPerformed(evt);
					}
				});
			}
			{
				btnCancel = new JButton();
				btnCancel.setText("Cancel");
				btnCancel.setBorder(BorderFactory
						.createBevelBorder(BevelBorder.RAISED));
				btnCancel.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
						11));
				btnCancel.setBackground(new java.awt.Color(192, 192, 192));
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCancelActionPerformed(evt);
					}
				});
			}
			{
				associatedProblemCheck = new JCheckBox();
				associatedProblemCheck.setText("Load associated problem");
				associatedProblemCheck.setFont(new java.awt.Font(
						"Microsoft Sans Serif", 0, 11));
				associatedProblemCheck.setBackground(new java.awt.Color(210,
						210, 200));
				associatedProblemCheck.setFocusable(false);
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.add(jXHeader1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
				    .add(GroupLayout.BASELINE, viewLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .add(GroupLayout.BASELINE, solutionsLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.add(thisLayout.createParallelGroup()
				    .add(GroupLayout.LEADING, solutionsScrollPane, 0, 142, Short.MAX_VALUE)
				    .add(GroupLayout.LEADING, viewScrollPane, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(descriptionLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.RELATED)
				.add(descriptionScrollPane, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(associatedProblemCheck, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.add(14)
				.add(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
				    .add(GroupLayout.BASELINE, btnLoad, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
				    .add(GroupLayout.BASELINE, btnCancel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
				.addContainerGap());
			thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
				.add(GroupLayout.LEADING, thisLayout.createParallelGroup()
				    .add(GroupLayout.LEADING, jXHeader1, 0, 466, Short.MAX_VALUE)
				    .add(GroupLayout.LEADING, separator, 0, 466, Short.MAX_VALUE))
				.add(thisLayout.createSequentialGroup()
				    .addPreferredGap(jXHeader1, solutionsScrollPane, LayoutStyle.INDENT)
				    .add(thisLayout.createParallelGroup()
				        .add(GroupLayout.LEADING, solutionsScrollPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
				        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				            .add(solutionsLabel, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
				            .add(115))
				        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				            .add(descriptionLabel, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
				            .add(115))
				        .add(GroupLayout.LEADING, descriptionScrollPane, 0, 200, Short.MAX_VALUE)
				        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				            .add(associatedProblemCheck, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
				            .add(54)))
				    .add(10)
				    .add(thisLayout.createParallelGroup()
				        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				            .add(btnLoad, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
				            .add(108)
				            .add(btnCancel, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
				        .add(GroupLayout.LEADING, viewScrollPane, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
				        .add(GroupLayout.LEADING, viewLabel, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE))
				    .addContainerGap(19, 19)));
			pack();
			this.setSize(472, 430);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JLabel getSolutionsLabel() {
		return solutionsLabel;
	}

	public JList getSolutionsList() {
		return solutionsList;
	}

	public JScrollPane getSolutionsScrollPane() {
		return solutionsScrollPane;
	}

	public JLabel getDescriptionLabel() {
		return descriptionLabel;
	}

	public JTextPane getDescriptionTextArea() {
		return descriptionTextPane;
	}

	public JScrollPane getViewScrollPane() {
		return viewScrollPane;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JCheckBox getAssociatedProblemCheck() {
		return associatedProblemCheck;
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void btnLoadActionPerformed(ActionEvent evt) {
		boolean check = getAssociatedProblemCheck().isSelected();
		this.dispose();
		graph.setScale(1.0);
		graph.setEnabled(true);

		UIModeler model;
		if (check) {
			model = graphed.createModel();
			model.getEditorTabbedPane().loadSolutionWithProblem(sol);
			model.addTpeditorChangeListener();
		} else {
			if(GraphEd.modelsss.size() > 0){
			model = ((UIModeler) GraphEd.scenarios.getSelectedComponent());
			model.getEditorTabbedPane().loadSolution(sol);
			}
			else
				JOptionPane.showMessageDialog(null, "Please, create a problem first");
		}		
		this.dispose();

	}

	private void solutionsListMousePressed(MouseEvent evt) {
		sol = (Solution) getSolutionsList().getSelectedValue();
		getDescriptionTextArea().setText(sol.getDescription());

		graph = new SolutionGraph();
		graph.insertSolution(sol);
		graph.setScale(0.20);
		graph.setEnabled(false);
		getViewScrollPane().setViewportView(graph);
		graph.setPreferredSize(new java.awt.Dimension(223, 138));

	}

	private void descriptionTextAreaMousePressed(MouseEvent evt) {

	}

}
