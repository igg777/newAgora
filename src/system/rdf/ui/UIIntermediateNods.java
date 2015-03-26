package system.rdf.ui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.swingx.JXHeader;
import org.jgraph.JGraph;
import org.jgraph.graph.Port;

import system.rdf.CustomCell;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.handlers.ProblemMarqueeHandler;
import system.rdf.ontology.OntologyTreeManager;
import system.rename.Tools;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;


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
/**
 * this class is to insert a node between two others
 * 
 * @author Leo
 * 
 */
public class UIIntermediateNods extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblNodes;
	private JTable nodesPanel;
	private JButton btnInsert;
	private JScrollPane nodesScroll;
	private JButton btnCancel;
	private static ArrayList<String> possiblesNodes;
	private static MyProblemCustomCell sourceCell, targetCell;
	private JXHeader bannerHeader;
	private static JGraph graph;
	private Tools tools;
	private RDFSerializator serializator = new RDFSerializator();

	public UIIntermediateNods(ArrayList<String> possiblesNodes, JGraph graph, MyProblemCustomCell sourceCell, MyProblemCustomCell targetCell) {
		super();
		this.possiblesNodes = possiblesNodes;
		this.sourceCell = sourceCell;
		this.targetCell = targetCell;
		this.graph = graph;
		this.tools = new Tools();
		initGUI();
		if (possiblesNodes.get(0).equals("No nodes found!")) {
			btnInsert.setEnabled(false);
		}

	}

	public UIIntermediateNods() {
		super();
		possiblesNodes = new ArrayList<String>();
		initGUI();
	}

	public static void main(String[] args) {
		if (possiblesNodes != null) {
			new UIIntermediateNods(possiblesNodes, graph, sourceCell,
					targetCell);
		} else {
			new UIIntermediateNods();
		}
	}

	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout(
					(JComponent) getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Insert intermediate Node");
			this.setVisible(true);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			this.setResizable(false);
			{
				nodesScroll = new JScrollPane();
				nodesScroll.getHorizontalScrollBar().setPreferredSize(
						new java.awt.Dimension(179, 15));
				{
					nodesPanel = new JTable();
					//ListModel nodesListModel = new DefaultComboBoxModel(
							//possiblesNodes.toArray());
					nodesPanel.setModel(getTableModel());
					nodesScroll.setViewportView(nodesPanel);
					nodesPanel.setFont(new java.awt.Font("Arial", 0, 12));

				}
			}

			{
				btnInsert = new JButton();
				btnInsert.setText("Insert");
				btnInsert.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
						11));
				btnInsert.setBackground(new java.awt.Color(210, 210, 200));
				btnInsert.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnInsertActionPerformed(evt);
					}
				});
			}
			{
				lblNodes = new JLabel();
				lblNodes.setText("Possible Nodes:");
				lblNodes.setFont(new java.awt.Font("Microsoft Sans Serif", 0,
						11));
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
				bannerHeader.setTitle("Intermediate node");
				bannerHeader.setDescription("Select a node to insert");
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.add(bannerHeader, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(lblNodes, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.add(nodesScroll, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
				.add(118)
				.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
				    .add(GroupLayout.BASELINE, btnInsert, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .add(GroupLayout.BASELINE, btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(29, 29));
			thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
				.add(GroupLayout.LEADING, bannerHeader, 0, 649, Short.MAX_VALUE)
				.add(thisLayout.createSequentialGroup()
				    .addPreferredGap(bannerHeader, lblNodes, LayoutStyle.INDENT)
				    .add(thisLayout.createParallelGroup()
				        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				            .add(lblNodes, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				            .add(314)
				            .add(btnInsert, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
				            .add(31)
				            .add(btnCancel, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
				            .add(16))
				        .add(GroupLayout.LEADING, nodesScroll, 0, 625, Short.MAX_VALUE))
				    .addContainerGap()));
			pack();
			this.setSize(655, 367);
		} catch (Exception e) {
			System.err.println("ERROR:");
			e.printStackTrace();
		}
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	/**
	 * Execute the button save
	 * 
	 * @param ActionEvent
	 */
	private void btnInsertActionPerformed(ActionEvent evt) {
		try {
			int selectecRow = this.getNodesPanel().getSelectedRow();
			TableModel model = this.getNodesPanel().getModel();
			ArrayList<Object> arrNodeToInsert = tools.desconcatenateNameAndProcess(model.getValueAt(selectecRow, 0)
					.toString());
			String nodeToInsertName = arrNodeToInsert.get(0).toString();
			String[] nodeToInsertProcess = (String[])arrNodeToInsert.get(1);
			
			//MyProblemCustomCell sourceCell = null;
			//MyProblemCustomCell targetCell = null;
			MyProblemCustomCell nodeToInsert = null;
            
			Object edge = ((ProblemGraph) graph).getEdge(
					sourceCell, targetCell);
			((ProblemGraph) graph).setSelectionCell(edge);
			((ProblemGraph) graph).removeCell();
			Point2D point = new Point(10, 10);
			((ProblemGraph) graph).insertCell(point, new MyProblemCustomCell(nodeToInsertName,
					nodeToInsertProcess));
			ProblemMarqueeHandler problemMarquee = new ProblemMarqueeHandler(
					graph);
			ArrayList<MyProblemCustomCell> cells = ((ProblemGraph) graph).getCells();
			for (MyProblemCustomCell object : cells) {
				if (object.equals(sourceCell)) 
				{
					sourceCell = object;
				} else if (object.equals(targetCell))
				{
					targetCell =  object;
				} else if (object.toString().equals(nodeToInsertName) && tools.concatenateProcess(object.getProcess()).equals
						(tools.concatenateProcess(nodeToInsertProcess))) {
					nodeToInsert =  object;
				}
			}
			Port sourcePort = ((ProblemGraph) graph).getDefaultPort(sourceCell);
			Port nodeToInsertPort = ((ProblemGraph) graph).getDefaultPort(nodeToInsert);
			Port targetPort = ((ProblemGraph) graph).getDefaultPort(targetCell);
			problemMarquee.connect(sourcePort, nodeToInsertPort);
			problemMarquee.connect(nodeToInsertPort, targetPort);

			this.dispose();
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

	public JTable getNodesPanel() {
		return nodesPanel;
	}

	/**
	 * 
	 * @return
	 */
	public DefaultTableModel getTableModel() {
		String columns[] = { "Node", "Frequency" };
		double frequency = 0;
		String[] tobesorted = new String[possiblesNodes.size()];
		for (int i = 0; i < possiblesNodes.size(); i++) {
			if(possiblesNodes.get(i).contains("--")){
				ArrayList<Object> cell = tools.desconcatenateNameAndProcess(possiblesNodes.get(i));
			frequency = serializator.calculateTotalFrequencyForProblems(sourceCell,
					new MyProblemCustomCell(cell.get(0).toString(), (String[])cell.get(1)));
			frequency += serializator.calculateTotalFrequencyForProblems(new MyProblemCustomCell(cell.get(0).toString(), (String[])cell.get(1)),targetCell);
			frequency = Double.parseDouble(tools.round(frequency / 2, 3));
			tobesorted[i] = possiblesNodes.get(i) + "  " + frequency;
		}
			else{
				tobesorted[i] = possiblesNodes.get(i) + "  " + frequency;
			}
		}
		Tools tools = new Tools();
		tobesorted = tools.quicksort(tobesorted, 0, tobesorted.length - 1);
		Object[][] rows = new Object[possiblesNodes.size()][2];

		for (int i = 0; i < possiblesNodes.size(); i++) {
			rows[i][0] = tobesorted[i].split("  ")[0];
			rows[i][1] = tobesorted[i].split("  ")[1];
		}

		DefaultTableModel modelToReturn = new DefaultTableModel(rows, columns);
		return modelToReturn;

	}
}
