package system.rename;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.table.TableModel;

import org.jgraph.JGraph;

import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.ui.GraphEd;
import system.rdf.ui.GraphEd;

/**
 * 
 * @author Leo
 * 
 */
public class Tools {
	
	ConnectionToPostgres connex;
	
	private RDFSerializator serializator = new RDFSerializator();
	public Tools() {
		connex = PersistentManager.getConnectionToPostgreSQL();
	}

	/**
	 * make an exhaustive search in the DB for return all the nodes that have
	 * that subject. returns an String's ArrayList that have the object and the
	 * objectProcess link together
	 * 
	 * @param subject
	 * @param subjectProcess
	 * 
	 * @return ArrayList<Object--objectProcess>
	 */
	public ArrayList<String> getObjectsFromDb(String subject,
			String[] subjectProcess) {
        String subjectProcessArr = concatenateProcess(subjectProcess);
		ArrayList<String> array = new ArrayList<String>();
		if (connex.tryConnection()) {

			try {
				ResultSet result = connex
						.executeSql("select object, object_process from triple_rdf,problem where subject = '"
								+ subject
								+ "'and subject_process = '"
								+ subjectProcessArr
								+ "' and triple_rdf.id_graph = problem.id_graph");
				while (result.next()) {
					String object = result.getString("object");
					String object_process = result.getString("object_process");
					if (array.contains(object + "--" + object_process) == false)
						array.add(object + "--" + object_process);
				}
				result = connex
						.executeSql("select object, object_process from inferred_rdf_triple where subject = '"
								+ subject
								+ "' and subject_process = '"
								+ subjectProcessArr + "'");
				while (result.next()) {
					String object = result.getString("object");
					String process = result.getString("object_process");
					if (array.contains(object + "--" + process) == false)
						array.add(object + "--" + process);
				}
			} catch (Exception e) {
				GraphEd.errorPane.printMessage(e);
			}
		} else if (GraphEd.errorPane.getText().equals(
				"The file to configure the database is missing !!!") == false)
			GraphEd.errorPane.printMessage(new Exception(
					"No database connection found!!"));
		return array;
	}

	/**
	 * @param elements
	 * @return an array whit the elements without repetitions
	 */
	public ArrayList<String> arrayWithoutRepetition(ArrayList<String> array) {
		ArrayList<String> finalArray = new ArrayList<String>();
		for (String object : array) {
			if (!(finalArray.contains(object))) {
				finalArray.add(object);
			}
		}
		return finalArray;
	}

	/**
	 * @param elements
	 * @param elementToLookingFor
	 * @return number of apparitions in the array
	 */
	public int appearInArray(ArrayList<String> array, String object) {
		int count = 0;
		for (String string : array) {
			if (object.equals(string)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @param elements
	 * @return Object [element][frequency]
	 */
	public Object[][] getFrequency(ArrayList<String> arrayList, String subject,
			String[] subjectProcess) {
		Object[][] doubleArray = new Object[0][0];
		try {
			// ArrayList<String> arrayWithoutRepetition =
			// arrayWithoutRepetition(arrayList);
			String frequency = "";
			doubleArray = new Object[arrayList.size()][2];
			for (int i = 0; i < arrayList.size(); i++) {
				ArrayList<Object> object = desconcatenateNameAndProcess(arrayList.get(i));
				doubleArray[i][0] = arrayList.get(i);
				frequency = round(
						serializator
								.calculateTotalFrequencyForProblems(
										new MyProblemCustomCell(subject,
												subjectProcess),
										new MyProblemCustomCell(object.get(0).toString(),(String[])object.get(1))), 3);
				doubleArray[i][1] = frequency;
			}
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
		return doubleArray;
	}

	/**
	 * 
	 * @param Object
	 *            [element][frequency]
	 * @return an array whit the three elements that have the bigger frequency
	 */
	public Object[][] arrangeDoubleArray(Object[][] doubleArray) {
		Object[][] arrToReturn = new Object[0][0];

		if (doubleArray.length > 0) {
			if (doubleArray.length <= 3) {
				arrToReturn = new Object[doubleArray.length][2];

			} else {
				arrToReturn = new Object[3][2];
			}

			arrToReturn[0] = doubleArray[0];
			for (int i = 1; i < doubleArray.length; i++) {
				Object[] temp = doubleArray[i];
				if (Float.parseFloat(temp[1].toString()) > Float
						.parseFloat(arrToReturn[0][1].toString())) {
					if (arrToReturn[1][1] != null) {
						arrToReturn[2] = arrToReturn[1];
						arrToReturn[1] = arrToReturn[0];
						arrToReturn[0] = temp;
					} else {
						arrToReturn[1] = arrToReturn[0];
						arrToReturn[0] = temp;
					}
				} else if (arrToReturn[1][1] == null) {
					arrToReturn[1] = temp;
				} else if (Float.parseFloat(temp[1].toString()) > Float
						.parseFloat(arrToReturn[1][1].toString())) {
					arrToReturn[2] = arrToReturn[1];
					arrToReturn[1] = temp;
				} else if (arrToReturn[2][1] == null
						|| Float.parseFloat(temp[1].toString()) > Float
								.parseFloat(arrToReturn[2][1].toString())) {
					arrToReturn[2] = temp;
				}
			}

		}
		return arrToReturn;
	}

	/**
	 * make an exhaustive search in the DB for all nodes that are objects and
	 * have this node as subject in a triple, then calculate the frequency of
	 * apparition of each element in the array an return the bigger's (top three
	 * of them) for suggest at user a possible way to model a problem
	 * 
	 * @param node
	 * @param nodeProcess
	 * @return the nodes to suggest
	 */
	public Object[][] getPotentialsNodes(String node, String[] nodeProcess) {
		ArrayList<String> array = new ArrayList<String>();
		Object[][] doubleArray;
		array = getObjectsFromDb(node, nodeProcess);
		doubleArray = getFrequency(array, node, nodeProcess);
		doubleArray = arrangeDoubleArray(doubleArray);
		return doubleArray;
	}

	/**
	 * 
	 * @param the
	 *            array
	 * @param first
	 *            position to be sorted
	 * @param last
	 *            position to be sorted
	 * @return The sorted array
	 */

	public String[] quicksort(String s[], int l, int h) {
		int p; /* index of partition */
		if ((h - l) > 0) {
			p = partition(s, l, h);
			quicksort(s, l, p - 1);
			quicksort(s, p + 1, h);
		}
		return s;
	}

	public int partition(String s[], int l, int h) {
		int i; /* counter */
		int p; /* pivot element index */
		int firsthigh; /* divider position for pivot */
		p = h;
		firsthigh = l;
		for (i = l; i < h; i++)
			if (Double.parseDouble(s[i].split("  ")[1]) > Double
					.parseDouble(s[p].split("  ")[1])) {
				s = swap(s, i, firsthigh);
				firsthigh++;
			}
		s = swap(s, p, firsthigh);
		return (firsthigh);
	}

	public String[] swap(String[] arr, int a, int b) {
		String temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
		return arr;
	}

	/**
	 * 
	 * @param Number
	 * @param size
	 *            to round
	 * @return
	 */
	public String round(double d, int places) {
		String temp = "";
		for (int i = 0; i < places; i++)
			temp += "0";
		int ceros = Integer.parseInt("1" + temp);
		int num = (int) d;
		double resto = d - num;
		//
		String t = String.valueOf(resto), a = "";
		int sd = t.indexOf(".") + 1;

		while (sd < t.length() && t.charAt(sd) == '0') {
			a += "0";
			sd++;
		}
		//
		long r = Math.round(resto * ceros);
		String resp = "";
		if (r == ceros) {
			num++;
			return resp = String.valueOf(num + "." + temp);
		} else if (r == 0) {
			return resp = String.valueOf(num + "." + temp);
		} else {
			return resp = String.valueOf(num + "." + a + r);
		}
	}

	/**
	 *
	 * Get the implicit triples in the graph that have isCauseOf as predicate
	 * 
	 * @return Object [][]
	 * 
	 */
	public Object[][] getInferredTriples() {
		ArrayList<String> subjectArray = new ArrayList<String>();
		ArrayList<String> objectArray = new ArrayList<String>();
		TableModel model = GraphEd.tableInsertedStatement.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			String a = model.getValueAt(i, 1).toString();
			String[] arr = a.split("    ");
			String subjec = arr[0];
			String predicate = arr[1];
			String object = arr[2];
			if (predicate.equals("isCauseOf")) {
				subjectArray.add(subjec);
				objectArray.add(object);
			}
		}
		Object[][] toReturn = new Object[subjectArray.size()][2];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i][0] = subjectArray.get(i);
			toReturn[i][1] = objectArray.get(i);

		}

		return toReturn;
	}

	/**
	 * Get the explicit triples in the graph
	 * 
	 *
	 * @return Object [][]
	 * 
	 */
	public Object[][] getAssertedTriples() {
		ArrayList<String> subjectArray = new ArrayList<String>();
		ArrayList<String> objectArray = new ArrayList<String>();
		TableModel model = GraphEd.tableInsertedStatement.getModel();
		System.out.println();
//		for (int i = 0; i < model.getRowCount(); i++) {
//			String a = model.getValueAt(i, 0).toString();
//			String[] arr = a.split("  ");
//			if (arr.length > 2) {
//				String subjec = arr[0];
//				String object = arr[2];
//
//				subjectArray.add(subjec);
//				objectArray.add(object);
//			}
//		}
//		Object[][] toReturn = new Object[subjectArray.size()][2];
//		for (int i = 0; i < toReturn.length; i++) {
//			toReturn[i][0] = subjectArray.get(i);
//			toReturn[i][1] = objectArray.get(i);
//
//		}
return null;
//		return toReturn;
	}

	/**
	 *  
	 * check in the graph to see if there is a cyclic connection between nodes, if the graph have a cyclic connection returns
	 * the nodes that make a cyclic, otherwise return an empty array.  
	 * 
	 * @return the nodes, if have, that make a cycle in the current graph
	 */
	public ArrayList<String> isCyclic() {
		ArrayList<String> toReturn = new ArrayList<String>();
		Object[][] arr = getInferredTriples();
		for (Object[] objects : arr) {
			if (objects[0].equals(objects[1])) {
				toReturn.add(objects[0].toString());
			}
		}
		return toReturn;
	}

	/**
	 * Fill the tableInsertedStatement in the GraphEd class
	 * 
	 * @author Leo
	 * @param graph
	 * @throws Exception
	 *             if the tableAsserted&&Inferred is empty
	 */
	public void fillTable(JGraph graph) throws Exception {
		try {
			InsertStatements objectt = new InsertStatements(graph);
			objectt.createJenaModel();
			objectt.getAssertedTriples();
			objectt.addDataFromTriples();
			objectt.bindReasoner();
			objectt.addModel();
			objectt.assertedStatement();
			objectt.inferredStatement();
			objectt.fillTable();
			GraphEd.manageTabbedPane.setSelectedIndex(1);
		} catch (Exception e) {
			e.printStackTrace();
			GraphEd.errorPane.printMessage(e);
		}
	}
	
	/**
	 * se le pasa una cadena donde el estan concatenados el nombre y los procesos, el metodo devuelve un arrayList
	 * que en la primera posicion va a tener el nombre y en la segunda un array con los procesos.
	 * @param allConcatenated
	 * @return in the first position he name of the indicator, in the second the process {@link ArrayList} 
	 */
	public ArrayList<Object> desconcatenateNameAndProcess(String allConcatenated){
		ArrayList<Object> toRetrurn = new ArrayList<Object>();
		String name = allConcatenated.split("--")[0];
		String [] arrProcess = new String[allConcatenated.split("--").length-1];
		for (int i = 1; i < arrProcess.length + 1; i++) {
			arrProcess[i-1] = allConcatenated.split("--")[i];
		}
		toRetrurn.add(name);
		toRetrurn.add(arrProcess);
		return toRetrurn;
		
	}
	
	/**
	 * se le pasa un arreglo con los procesos y devuelve un string con los procesos concatenados
	 * por --
	 * @param arr
	 * @return
	 */
	public String concatenateProcess(String[] arr){
		String toReturn = arr[0];
		for (int i = 1; i < arr.length; i++) {
			toReturn += "--" + arr[i];
		}
		return toReturn;
	}	
}
