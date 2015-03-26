package model.ga.internal;

import java.util.ArrayList;

/**
 * This Class is used to represent the indicators of the SCOR Ontology and the
 * relations that it has with the others indicators in the generation process
 * 
 * @author Ing. Irl�n Grangel Gonz�lez
 * 
 */

public class Indicators {
	protected String name;
	protected ArrayList<Integer> Connections;
	private String[] process;
	// Used to save the relations that come out from the indicators
	/**
	 * @param connections
	 * @param name
	 */
	/*public Indicators(String name, ArrayList<Integer> connections) {
		super();
		Connections = connections;
		this.name = name;
	     this.process = process;
	}*/
	public Indicators(String name,String[] process ) {
		super();
		
		this.name = name;
	     this.process = process;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Indicators))
			return false;
		Indicators other = (Indicators) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the connections
	 */
	protected ArrayList<Integer> getConnections() {
		return Connections;
	}
         
	/**
	 * @return the process
	 */
	public String[] getProcess() {
	    return process;
	}

	/**
	 * @param process the process to set
	 */
	public void setProcess(String[] process) {
	    this.process = process;
	}

	/**
	 * @param connections
	 *            the connections to set
	 */
	protected void setConnections(ArrayList<Integer> connections) {
		Connections = connections;
	}
}