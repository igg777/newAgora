package system.rdf.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.sql.ResultSet;
import java.util.ArrayList;

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
import javax.swing.JTextArea;
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

import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.Solution;
import system.rdf.graph.GraphManager;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;


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
public class UILoadProblem extends javax.swing.JFrame {
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
	public ArrayList<Problem> problems;
	public ConnectionToPostgres con;
	private JLabel JlblView;
	public Problem pro;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UILoadProblem inst = new UILoadProblem(graphed);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UILoadProblem(GraphEd graphed) {
		super();
		con = PersistentManager.getConnectionToPostgreSQL();
		initGUI();
		UILoadProblem.graphed = graphed;
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			this.setTitle("Load Problem from Database");
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			this.setResizable(false);
			this.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10));
			{
				jXHeader1 = new JXHeader();
				jXHeader1.setTitle("Load Problem");
				jXHeader1.setDescription("Load a saved problem from database");
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
				btnLoad
						.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
								11));
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
					con.connect();
					problems = con.getProblems();
					con.disconnect();
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
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addComponent(jXHeader1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(problemLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(viewLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .addComponent(JlblView, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(scrollProblemList, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
				    .addComponent(graphScrollPane, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
				.addGap(17)
				.addComponent(descriptionLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(descriptionScrollPane, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
				.addGap(22)
				.addComponent(jSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(34)
				.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				    .addComponent(btnLoad, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
				    .addComponent(btnCancel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(43, 43));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addGroup(thisLayout.createParallelGroup()
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createParallelGroup()
				        .addComponent(jXHeader1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 447, GroupLayout.PREFERRED_SIZE)
				        .addComponent(jSeparator, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 447, GroupLayout.PREFERRED_SIZE))
				    .addGroup(thisLayout.createSequentialGroup()
				        .addPreferredGap(jXHeader1, scrollProblemList, LayoutStyle.ComponentPlacement.INDENT)
				        .addGroup(thisLayout.createParallelGroup()
				            .addComponent(scrollProblemList, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(problemLabel, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
				                .addGap(150))
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addComponent(descriptionLabel, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
				                .addGap(150))
				            .addComponent(descriptionScrollPane, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE))
				        .addGap(21)
				        .addGroup(thisLayout.createParallelGroup()
				            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                .addGroup(thisLayout.createParallelGroup()
				                    .addGroup(thisLayout.createSequentialGroup()
				                        .addGap(0, 0, Short.MAX_VALUE)
				                        .addComponent(btnLoad, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
				                    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				                        .addGap(7)
				                        .addComponent(JlblView, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
				                        .addGap(21)))
				                .addGap(26)
				                .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
				                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
				            .addGroup(thisLayout.createSequentialGroup()
				                .addGap(0, 0, Short.MAX_VALUE)
				                .addComponent(graphScrollPane, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)))
				        .addGap(26)))
				.addGap(229)
				.addComponent(viewLabel, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE));
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
		UIModeler model = graphed.createModel();
		model.setTitle(pro.getName());
		model.getEditorTabbedPane().loadProblem(pro);
		model.addTpeditorChangeListener();
		
		if(PersistentManager.getConnectionToPostgreSQL().tryConnection()){
			try {
				if(PersistentManager.getConnectionToPostgreSQL().existSolutonInDB(pro.getId())){
					int solution_id = PersistentManager.getConnectionToPostgreSQL().getSolutionForProblem(pro.getId());
					Solution solution = PersistentManager.getConnectionToPostgreSQL().getSolution(solution_id);
					model.getEditorTabbedPane().loadSolution(solution);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		GraphEd.refreshScenarios();
	}

	private void ProblemListValueChanged(ListSelectionEvent evt) {
		// System.out.println("ProblemList.valueChanged, event="+evt);

	}

	private void ProblemListVetoableChange(PropertyChangeEvent evt) {
		// System.out.println("ProblemList.vetoableChange, event="+evt);
	}

	private void ProblemListMousePressed(MouseEvent evt) {
		pro = (Problem) getProblemList().getSelectedValue();
		getDescriptionTextPane().setText(pro.getDescription());

		graph = new ProblemGraph();
		graph.insertProblem(pro);
		graph.setScale(0.20);
		graph.setEnabled(false);
		getGraphScrollPane().setViewportView(graph);
		graph.setPreferredSize(new java.awt.Dimension(223, 138));
       // GraphManager manager = new GraphManager();
		//manager.getloadedProblemGraph.add(graph);
	}

	public JScrollPane getGraphScrollPane() {
		return graphScrollPane;
	}

	private void descriptionTextAreaMousePressed(MouseEvent evt) {
	}

}
