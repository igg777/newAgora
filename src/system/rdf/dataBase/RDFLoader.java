package system.rdf.dataBase;

import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;

public class RDFLoader {

    ProblemGraph problemGraph;
    SolutionGraph solotionGraph;
    /**
     * Load a problem from database
     */
    public ProblemGraph loadProblem (int id){
	problemGraph = new ProblemGraph();
	problemGraph.addMouseListener();

	PersistentManager.getConnectionToPostgreSQL().connect(); 
	Problem p = PersistentManager.getConnectionToPostgreSQL().getProblem(3);
	//System.out.println(p.toString());
	for (int i = 0; i < p.getTriples().size(); i++) {
	    problemGraph.insertTriple(p.getTriples().get(i));
	}

	return problemGraph;
    }
    
    public void loadSolution(){
	solotionGraph = new SolutionGraph();
	PersistentManager.getConnectionToPostgreSQL().connect();
    }

}
