package system.rdf.table;

import javax.swing.table.AbstractTableModel;

public class TableModelManager {


	public static ProblemTableModel problem = new ProblemTableModel();



	public static ProblemTableModel getProblem() {
		return problem;
	}

	public static class ProblemTableModel extends CustomTableModel{

		public String[] columnNames = {"Properties","Values"};
		public Object[][] data = {
				{"", ""},
		};

		public ProblemTableModel(){
			super();
		}

		public int getColumnCount() {
			return super.getColumnCount();
		}

		public int getRowCount() {
			return super.getRowCount();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return super.getValueAt(rowIndex, columnIndex);
		}
		public void setValueAt(Object value, int row, int col) {
			super.setValueAt(value,row,col);
		}

	}
	public class SolutionTableModel extends CustomTableModel{

	}
}
