package system.rdf.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

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
import system.rdf.graph.GraphManager;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import javax.swing.JSpinner;
import javax.swing.DebugGraphics;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JToggleButton;


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
public class UIRetrievedProblem extends javax.swing.JFrame {
	private JXHeader jXHeader1;
	private static GraphEd graphed = null;
	JScrollPane graphScrollPane;
	private ProblemGraph graph;
	public Problem pro;
	public ConnectionToPostgres con;
	private ArrayList<ProblemGraph> problems;
	private JScrollBar scrollBar;
	private JTextArea txtDescription;
	private JLabel label_1;
	private static Hashtable<ProblemGraph, String> retrievedProblems;
	private JLabel label;


	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIRetrievedProblem inst = new UIRetrievedProblem(graphed, retrievedProblems);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIRetrievedProblem(GraphEd graphed, Hashtable<ProblemGraph, String> retrievedProblems) {
		super();
		this.retrievedProblems = retrievedProblems;
		this.problems = new ArrayList<ProblemGraph>();
		setResizable(false);
		initGUI();
		Load();
	}
	
	private void Load()
	{
		for (ProblemGraph problemGraph : retrievedProblems.keySet()) {
			problems.add(problemGraph);
		}
		
//		con = PersistentManager.getConnectionToPostgreSQL();
//		con.connect();
//		try {
//			problems = con.getProblems();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		con.disconnect();
//		
		scrollBar.setMinimum(0);
		scrollBar.setMaximum(problems.size());
		scrollBar.setUnitIncrement(1);
		
		label_1.setText("Loaded problems: " + problems.size());
		
//		pro = (Problem) getProblemList().getSelectedValue();
//		getDescriptionTextPane().setText(pro.getDescription());
//
//		graph = new ProblemGraph();
//		graph.insertProblem(pro);
//		graph.setScale(0.20);
//		graph.setEnabled(false);
//		getGraphScrollPane().setViewportView(graph);
//		graph.setPreferredSize(new java.awt.Dimension(223, 138));
	}
	
	
	private void ValueChanged(int value){
//		pro = (Problem) getProblemList().getSelectedValue();
//		getDescriptionTextPane().setText(pro.getDescription());
//
		graph = new ProblemGraph();
		graph = problems.get(value);
		graph.setScale(0.40);
		graph.setEnabled(false);
		graphScrollPane.setViewportView(graph);
		graph.setPreferredSize(new java.awt.Dimension(223, 138));
		
		String statusText = retrievedProblems.get(graph);
		String status = "";
		
		if(statusText.equals("Equals")){
			status = "This problem is equals to the modeled one";
		}else if(statusText.equals("Subgraph")){
			status = "The modeled problem is a subgraph of this one";
		}else if(statusText.equals("Container")){
			status = "This problem is a subgraph of the modeled one";
		} 
		
		txtDescription.setText("Name: \n" + graph.getProblem_list().get(0).getName() + "\nDescription: \n" + graph.getProblem_list().get(0).getDescription()  + "\nStatus: \n" + status);
		label.setText("Problem number: " + (int)(value + 1));
	}
	
	private void initGUI() {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			getContentPane().setLayout(null);
			
			scrollBar = new JScrollBar();
			scrollBar.setOrientation(JScrollBar.HORIZONTAL);
			scrollBar.setToolTipText("");
			scrollBar.addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					ValueChanged(scrollBar.getValue());
				}
			});
			
			graphScrollPane = new JScrollPane();			
			graphScrollPane.setBounds(58, 12, 388, 313);
			getContentPane().add(graphScrollPane);
			
			graph = new ProblemGraph();
			graph.setAutoResizeGraph(true);
			graphScrollPane.setViewportView(graph);
			graph.insertProblem(pro);
			graph.setScale(0.20);
			graph.setEnabled(false);
			scrollBar.setBounds(59, 327, 387, 23);
			getContentPane().add(scrollBar);
			
			txtDescription = new JTextArea();
			txtDescription.setLineWrap(true);
			txtDescription.setBounds(12, 392, 488, 143);
			getContentPane().add(txtDescription);
			
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btnCancel.setBounds(371, 546, 97, 25);
			getContentPane().add(btnCancel);
			
			JButton btnLoad = new JButton("Load");
			btnLoad.setBounds(58, 546, 97, 25);
			getContentPane().add(btnLoad);
			
			label_1 = new JLabel("");
			label_1.setBounds(314, 358, 184, 25);
			getContentPane().add(label_1);
			
			label = new JLabel("Loaded problems: 0");
			label.setBounds(58, 357, 184, 25);
			getContentPane().add(label);
			
			this.setTitle("Retrieved Problems from Database");
			this.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10));
			jXHeader1 = new JXHeader();
			jXHeader1.setTitle("Retrieved Problems");
			jXHeader1.setDescription("Retrieved Problems from Database");
			jXHeader1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
					new java.awt.Color(192, 192, 192)));
			jXHeader1.setIcon(new ImageIcon(getClass().getClassLoader()
					.getResource("system/rdf/resources/database.png")));
			this.setSize(515, 610);
	}
}
