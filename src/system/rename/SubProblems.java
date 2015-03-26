package system.rename;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphModel;

import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.ui.GraphEd;
import system.rdf.ui.UIIntermediateNods;

/**
 * this class is to create the jmenuitems for the intermediate Nodes
 * 
 * @author Leo
 * 
 */
public class SubProblems {
	ConnectionToPostgres connect;
	JMenu findIntermediate;
	JGraph graph;
	Tools tools;

	public SubProblems(JGraph graph) {
		connect = PersistentManager.getConnectionToPostgreSQL();
		this.graph = graph;
		tools = new Tools();
	}

	/**
	 * this method returns the jmenuItem find Intermediate
	 * 
	 * @param sourceCell
	 * @param graph
	 * @return
	 */
	public JMenuItem getIntermediateNods(String sourceCell,
			String[] sourceCellProcess, JGraph graph) {
		// this ArrayList save all nodes that is directly connected whit the
		// selected one
		// ok
		ArrayList<String> connected = whitWhoIsConnected(sourceCell,
				sourceCellProcess, graph);
		// for every node in the previous ArrayList create an JMenuItem
		JMenuItem[] arrJMenuItems = createJMenuItems(connected, sourceCell, sourceCellProcess);
		findIntermediate = new JMenu();
		findIntermediate.setText("Find Intermediates");
		for (int i = 0; i < arrJMenuItems.length - 1; i++) {
			findIntermediate.add(arrJMenuItems[i]);
			findIntermediate.addSeparator();
		}
		if (arrJMenuItems.length > 0)
			findIntermediate.add(arrJMenuItems[arrJMenuItems.length - 1]);
		return findIntermediate;
	}

	/**
	 * get from the DB node's name that are subject of targetCell
	 * 
	 * @param sourceCell
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> lookForPossibleNods(String sourceCell, String[] arrSourceCellProcess)
			throws SQLException {
		String sourceCellProcess = tools.concatenateProcess(arrSourceCellProcess);
		ArrayList<String> arrToReturn = new ArrayList<String>();
		String sqlString = "select object, object_process from triple_rdf where subject = '"
				+ sourceCell + "' and subject_process = '"+sourceCellProcess+"'";
		ResultSet result = connect.executeSql(sqlString);
		
		while (result.next()) {
			String stringResult = result.getString("object")+"--"+result.getString("object_process");
			ArrayList<Object> cell = tools.desconcatenateNameAndProcess(stringResult);
			if (!arrToReturn.contains(stringResult)
					&& !((ProblemGraph) graph).isPainted(new MyProblemCustomCell(cell.get(0).toString(),(String[])cell.get(1))))
				arrToReturn.add(stringResult);
		}
		sqlString = "select object, object_process from inferred_rdf_triple where subject = '"
				+ sourceCell + "'";
		result = connect.executeSql(sqlString);
		while (result.next()) {
			String stringResult = result.getString("object")+"--"+result.getString("object_process");
			ArrayList<Object> cell = tools.desconcatenateNameAndProcess(stringResult);
			if (!arrToReturn.contains(stringResult)
					&& !((ProblemGraph) graph).isPainted(new MyProblemCustomCell(cell.get(0).toString(),(String[])cell.get(1))))
				arrToReturn.add(stringResult);
		}

		return arrToReturn;
	}

	/**
	 * get from the DB node's name that are object of sourceCell and subject of
	 * targetCell
	 * 
	 * @param sourceCell
	 * @param targetCell
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<String> intermediateNods(String sourceCell,String targetCell, String[] sourceCellProcess,String[] targetCellProcess) throws SQLException {
		ArrayList<String> arrToWork = lookForPossibleNods(sourceCell, sourceCellProcess);
		ArrayList<String> arrToReturn = new ArrayList<String>();
		for (String string : arrToWork) {
			ArrayList<Object> cell = tools.desconcatenateNameAndProcess(string);
			String sqlString = "select subject, subject_process from triple_rdf where subject = '"
					+ cell.get(0).toString()
					+ "' and object = '"
					+ targetCell
					+ "' and subject_process = '"
					+ tools.concatenateProcess((String[])cell.get(1))
					+ "'and object_process = '" + tools.concatenateProcess(targetCellProcess) + "'";
			ResultSet result = connect.executeSql(sqlString);
			if (result.next()) {
				String subject = result.getString("subject")+"--"+result.getString("subject_process");
				if (!arrToReturn.contains(subject))
					arrToReturn.add(subject);
			}
		}
		for (String string : arrToWork) {
			ArrayList<Object> cell = tools.desconcatenateNameAndProcess(string);
			String sqlString = "select subject, subject_process from inferred_rdf_triple where subject = '"
					+ cell.get(0).toString()
					+ "' and object = '" 
					+ targetCell 
					+ "' and subject_process = '"
					+ tools.concatenateProcess((String[])cell.get(1))
					+ "'and object_process = '" + tools.concatenateProcess(targetCellProcess) + "'";
			ResultSet result = connect.executeSql(sqlString);
			if (result.next()) {
				String subject = result.getString("subject")+"--"+result.getString("subject_process");
				if (!arrToReturn.contains(subject)) {
					arrToReturn.add(subject);
				}
			}
		}

		return arrToReturn;

	}

	/**
	 * 
	 * @param sourceCell
	 * @param graph
	 * @return the nods that are connected directly whit the sourceCell in the
	 *         graph
	 */
	public ArrayList<String> whitWhoIsConnected(String sourceCell,
			String[] sourceVertexProcess, JGraph graph) {
		ArrayList<String> arrToReturn = new ArrayList<String>();
		ArrayList<Object> edgesList = ((ProblemGraph) graph).getAllEdges();

		for (Object object : edgesList) {
			MyProblemCustomCell sourceVertex, targetVertex;
			GraphModel gm = graph.getModel();
			sourceVertex = (MyProblemCustomCell) gm.getParent(gm
					.getSource(object));
			targetVertex = (MyProblemCustomCell) gm.getParent(gm
					.getTarget(object));
			if (sourceVertex.toString().equals(sourceCell)
					&& tools.concatenateProcess(sourceVertex.getProcess()).equals(tools.concatenateProcess(sourceVertexProcess))) {
				arrToReturn.add(targetVertex.toString() + "--"
						+ tools.concatenateProcess(targetVertex.getProcess()));
			}

		}

		return arrToReturn;

	}

	/**
	 * the jmenuItems inside Find Intermediates
	 * 
	 * 
	 * @param connectedWhit
	 * @param sourceCell
	 * @return
	 */
	public JMenuItem[] createJMenuItems(ArrayList<String> connectedWhit,
			String sourceCell, String[] sourceCellProcess) {
		
		//each node inside the connectedWhithas the following structure
		// nodeName--nodeProcess--nodeSubprecess(if have)
		final String newSourceCell = sourceCell;
		final String[] newSourceCellProcess = sourceCellProcess;
		JMenuItem temp;
		JMenuItem[] jMenuItemToCreate = new JMenuItem[connectedWhit.size()];
		if (connectedWhit.size() == 0) {
			jMenuItemToCreate = new JMenuItem[1];
			temp = new JMenuItem();
			temp.setEnabled(false);
			temp.setText("This node is not connectec");
			jMenuItemToCreate[0] = temp;

		} else
			for (int i = 0; i < connectedWhit.size(); i++) {
				ArrayList<Object> node = tools.desconcatenateNameAndProcess(connectedWhit.get(i));
				final String nodeName = node.get(0).toString();
				final String[] nodeProcess = (String[])node.get(1);
				String text = "Between this and " + connectedWhit.get(i);
				temp = new JMenuItem();
				temp.setText(text);
				temp.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						new Thread(new Runnable() {
							public void run() {
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										try {
											// see for the possibilities
											ArrayList<String> possiblesNods = intermediateNods(
													newSourceCell, nodeName, newSourceCellProcess, nodeProcess);
											MyProblemCustomCell SourceCell = new MyProblemCustomCell(newSourceCell,newSourceCellProcess );
											MyProblemCustomCell node = new MyProblemCustomCell(nodeName, nodeProcess);
											//possiblesNods.remove(nodeName);
											if (possiblesNods.isEmpty())
												possiblesNods
														.add("No nodes found!");
											UIIntermediateNods obj = new UIIntermediateNods(
													possiblesNods, graph,
													SourceCell, node);
										} catch (Exception e2) {
											GraphEd.errorPane.printMessage(e2);
										}
									}
								});
							}
						}).start();
					}
				});
				jMenuItemToCreate[i] = temp;
			}
		return jMenuItemToCreate;
	}

}
