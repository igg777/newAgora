package view.suggest;

import info.clearthought.layout.TableLayout;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.SwingUtilities;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.handlers.ProblemMarqueeHandler;
import system.rdf.ui.GraphEd;
import system.rename.Tools;

public class UISuggested extends javax.swing.JFrame {
	private JTable jtSuggested;
	private JButton jbtnCancel;
	private JButton jbtnInsert;
	private JScrollPane jspTable;
	MyProblemCustomCell subjectCell;
	ProblemGraph graph;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UISuggested inst = new UISuggested();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UISuggested() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			TableLayout thisLayout = new TableLayout(new double[][] {
					{ TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL },
					{ TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL } });
			thisLayout.setHGap(5);
			thisLayout.setVGap(5);
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Suggested nodes");
			{
				jspTable = new JScrollPane();
				getContentPane().add(jspTable, "1, 1, 22, 24");
				{
					TableModel jtSuggestedModel = new DefaultTableModel(
							new String[][] {
									{ "C.C.T.Inventory-Days-of-Supply--",
											"Two", "wer" },
									{ "Three", "Four", "qwer" } },
							new String[] { "Node", "Relation frequency",
									"Gloval frequency" });
					jtSuggested = new JTable();
					jspTable.setViewportView(jtSuggested);
					jtSuggested.setModel(jtSuggestedModel);
				}
			}
			{
				jbtnInsert = new JButton();
				getContentPane().add(jbtnInsert, "14, 26, 16, 27");
				jbtnInsert.setText("Insert");
				jbtnInsert.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnInsertActionPerformed(evt);
					}
				});
			}
			{
				jbtnCancel = new JButton();
				getContentPane().add(jbtnCancel, "19, 26, 21, 27");
				jbtnCancel.setText("Cancel");
				jbtnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnCancelActionPerformed(evt);
					}
				});
			}
			pack();
			this.setSize(668, 415);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTableModel(TableModel model) {
		jtSuggested.setModel(model);
	}

	private void jbtnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void jbtnInsertActionPerformed(ActionEvent evt) {
		Tools tools = new Tools();
		GraphModel gm = graph.getModel();
		try {

			int selectecRow = jtSuggested.getSelectedRow();
			if (selectecRow == -1) {
				JOptionPane.showMessageDialog(null,
						"Please, select a node to insert");
			}

			TableModel model = jtSuggested.getModel();
			ArrayList<Object> arrNodeToInsert = tools
					.desconcatenateNameAndProcess(model.getValueAt(selectecRow,
							0).toString());
			String nodeToInsertName = arrNodeToInsert.get(0).toString();
			String[] nodeToInsertProcess = (String[]) arrNodeToInsert.get(1);
			MyProblemCustomCell nodeToInsert = new MyProblemCustomCell(
					nodeToInsertName, nodeToInsertProcess);
			Point2D point = new Point(10, 10);
			try {
				((ProblemGraph) graph).insertCell(point,nodeToInsert);
			} catch (Exception e) {
			}

			ProblemMarqueeHandler problemMarquee = new ProblemMarqueeHandler(
					graph);
			ArrayList<MyProblemCustomCell> cells = ((ProblemGraph) graph)
					.getCells();
			for (MyProblemCustomCell object : cells) {
				if (object.equals(nodeToInsert)) {
					nodeToInsert = object;
				}
			}
			//here we check that the selected cells are not connected already
			//if the cell are connected we don't connect them again.
			boolean isAlreadyConnected = false;
			ArrayList<Object> allEdges = graph.getAllEdges();
			for (Object object : allEdges) {
				MyProblemCustomCell sourceVertex = (MyProblemCustomCell) gm
						.getParent(gm.getSource((DefaultEdge) object));
				MyProblemCustomCell targetVertex = (MyProblemCustomCell) gm
						.getParent(gm.getTarget((DefaultEdge) object));
				if(targetVertex.equals(nodeToInsert) && subjectCell.equals(sourceVertex)){
					isAlreadyConnected = true;
				}	
			}
            if(isAlreadyConnected == false){
			Port sourcePort = ((ProblemGraph) graph)
					.getDefaultPort(subjectCell);
			Port targetPort = ((ProblemGraph) graph)
					.getDefaultPort(nodeToInsert);
			problemMarquee.connect(sourcePort, targetPort);
            }
			this.dispose();
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing because we show a joptionPane aloft
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

	public void init(MyProblemCustomCell subjectCell, ProblemGraph graph) {
		this.subjectCell = subjectCell;
		this.graph = graph;
	}

}
