package system.rdf.utils;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.table.ColumnControlButton;

import system.rdf.ontology.OntologyTree;
import system.rdf.ui.ErrorPane;

public class TabsComponentManager {

	/**
	 * Shared singleton instance.
	 */
	protected static TabsComponentManager sharedInstance = new TabsComponentManager();
	protected static JTextPane insertedStatementsInstance = new JTextPane();
	protected static ErrorPane errorPaneInstance = new ErrorPane();
	protected static OntologyTree ontologyTree = new OntologyTree();
	// protected static PropertiesTable propertiesTable = new PropertiesTable();
	protected static JTable tableInsertedStatement = new JTable();

	public static ErrorPane getErrorPaneInstance() {
		return errorPaneInstance;
	}

	/*
	 * public static PropertiesTable getPropertiesTable() { return
	 * propertiesTable; }
	 */
	public static TabsComponentManager getSharedInstance() {
		return sharedInstance;
	}

	public static OntologyTree getOntologyTree() {
		return ontologyTree;
	}

	public static JTextPane getInsertedStatementInstance() {
		return insertedStatementsInstance;
	}

	public static JTable getTableInsertedStatement() {
		Object temp[][] = new Object[1][1];
		String Columns[] = { "Asserted Statements", "Inferred Statements" };
		DefaultTableModel tableModel = new DefaultTableModel(temp, Columns);
		tableInsertedStatement.setModel(tableModel);
		return tableInsertedStatement;
	}

}
