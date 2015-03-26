package system.rdf.table;

import javax.swing.table.AbstractTableModel;

/**
 * The custom model of the cell properties table
 * @author Armando Carracedo
 */
public class CustomTableModel extends AbstractTableModel {

	private boolean DEBUG = true;

	public String[] columnNames = {"Properties","Values"};
	public Object[][] data = {
			{"Action", ""},
			{"Indicator of performance", ""}
	};

	/**
	 * Constructor
	 */
	public CustomTableModel(){
		super();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	/**
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/**
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant,
		//no matter where the cell appears onscreen.
		if (col < 1) { 
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		/*if (DEBUG) {
			System.out.println("Setting value at " + row + "," + col
					+ " to " + value
					+ " (an instance of " 
					+ value.getClass() + ")");
		}*/

		if (data[0][col] instanceof Integer && !(value instanceof Integer)) {                  

			try {
				data[row][col] = new Integer(value.toString());
				fireTableCellUpdated(row, col);
			} catch (NumberFormatException e) {

			}
		} else {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}

	}

	/**
	 * Print the table values on console
	 */
	public void printDebugData() {

		int numRows = getRowCount();
		int numCols = getColumnCount();

		for (int i=0; i < numRows; i++) {
			for (int j=0; j < numCols; j++) {
				System.out.print("  " + data[i][j]);
			}
		}
	}

	/**
	 * Clears the table values
	 */
	public void clearTableValue(){

		int numRows = getRowCount();
		int numCols = getColumnCount();
		for (int i=0; i < numRows; i++) {
			for (int j=1; j < numCols; j++) {

				data[i][j] = "";
				fireTableCellUpdated(i, j);


			}
		}

	}

	

}
