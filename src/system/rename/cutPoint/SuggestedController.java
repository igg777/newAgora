package system.rename.cutPoint;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import system.rename.Tools;

public class SuggestedController {

	ArrayList<ElementOfThePopulation> elements;

	public SuggestedController(ArrayList<ElementOfThePopulation> elements) {
		super();
		this.elements = elements;
	}

	public ArrayList<ElementOfThePopulation> getElements() {
		return elements;
	}

	public void setElements(ArrayList<ElementOfThePopulation> elements) {
		this.elements = elements;
	}
	
	public TableModel getTableModel(){
		Tools tools = new Tools();
		Object[][] nodes = new Object[elements.size()][3];
		for (int i = 0; i < elements.size();i++) {
			nodes[i][0] = elements.get(i).getPossibleNode().toString()+"--" + tools.concatenateProcess(elements.get(i).getPossibleNode().getProcess()) ;
			nodes[i][1] = tools.round(elements.get(i).getFrequencyForThisConnection(),3);
			nodes[i][2] = tools.round(elements.get(i).getGlovalFrequency(), 3);
		}
		TableModel model = new DefaultTableModel(
				nodes,
				new String[] { "Node", "Relation frequency", "Gloval frequency" });
		return model;
	}
}
