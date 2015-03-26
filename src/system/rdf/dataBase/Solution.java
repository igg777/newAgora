package system.rdf.dataBase;

import java.util.ArrayList;

public class Solution {
	
	protected int id; 
	protected String name;
	protected String description;
	protected ArrayList<RDFTriple> triples = new ArrayList<RDFTriple>();
    protected Problem associatedProblem;
 
    public Solution(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<RDFTriple> getTriples() {
		return triples;
	}

	public void setTriples(ArrayList<RDFTriple> triples) {
		this.triples = triples;
	}

	public Problem getAssociatedProblem() {
		return associatedProblem;
	}

	public void setAssociatedProblem(Problem associatedProblem) {
		this.associatedProblem = associatedProblem;
	}
    
    public String toString(){
    	return getName();
    }



}
