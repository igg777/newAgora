package system.rdf.graph;

import java.awt.geom.Point2D;
import java.util.Enumeration;

import org.jgraph.graph.DefaultPort;

import system.rdf.CustomCell;
import system.rename.Tools;

/**
 * this class is a default customCell but allow save the node's process 
 * @author Leo
 *
 */
public class MyProblemCustomCell extends CustomCell{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 860613;
	private String[] process;
	
	public MyProblemCustomCell(String cellName, String[] process){
		super(cellName);
		DefaultPort port = new DefaultPort("Floating");
		this.addPort();
		port.setParent(this);
		this.process = process;
	}

	public String[] getProcess() {
		return process;
	}

	public void setProcess(String[] process) {
		this.process = process;
	    super.setTextToUp("Leosssss");
	}
	
	/**
	 *  
	 * @param cellToCompare
	 * @return <code> true </code> if the cells have the same name and process, otherwise return <code> false </code>
	 */
	public boolean equals(MyProblemCustomCell cellToCompare){
		 final Tools tools = new Tools();
		if(this.toString().equals(cellToCompare.toString()) && tools.concatenateProcess(process).equals(tools.concatenateProcess(cellToCompare.getProcess())))
		return true;
		return false;
	}
	
	/**
	 * print in the console the name and the process that belong to this indicator
	 */
	public void printIndicator(){
		Tools tools = new Tools();
		System.out.println(this.toString() + " " + tools.concatenateProcess(process));
	}

}
