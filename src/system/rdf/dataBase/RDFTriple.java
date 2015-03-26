package system.rdf.dataBase;

import java.awt.geom.Point2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Port;

import system.rdf.CustomCell;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.VertexManager;
/**
 * Class use to represent a simple RDF triple
 * @author Irlan
 *
 */
public class RDFTriple {

    protected Subject subject;
    protected Predicate predicate;
    protected RDFObject object;
    protected Map vertexPoint;
    //this is for know the process that's belong the vertex
    private String[] subjectProcess;
    private String[] objectProcess;
    protected boolean visited;

    /**
     * @param object
     * @param predicate
     * @param subject
     * @param vertexPoint
     */
  
    /*public RDFTriple(RDFObject object, Predicate predicate,
	    Subject subject, Map vertexPoint) {
	super();
	this.object = object;
	this.predicate = predicate;
	this.subject = subject;
	this.vertexPoint = vertexPoint;
    }*/
    
    /**
     * @param object2
     * @param predicate
     * @param subject
     * @param vertexPoint
     */
    public RDFTriple(Subject subject,Predicate predicate,RDFObject object2) {
	super();
	this.subject = subject;
	this.predicate = predicate;
	this.object = object2;
    }

    
    public RDFTriple() {
    }

    public RDFTriple(RDFObject object, Predicate predicate, Subject subject) {
	this.object = object;
	this.predicate = predicate;
	this.subject = subject;
    }

    

   

    

    public ArrayList<Object> getGraphTriple(){
    		ArrayList<Object> triple = new ArrayList<Object>();

    		MyProblemCustomCell source = new MyProblemCustomCell(subject.getLabel(), subjectProcess);
    		DefaultEdge edge = new DefaultEdge();
    		MyProblemCustomCell target = new MyProblemCustomCell(object.getLabel(), objectProcess);
    	//	System.out.println("Predicado: " + predicate.getLabel());
    		if (predicate.getLabel() != "null")
    			edge.setUserObject(predicate.getLabel());
    		source.addPort();
    		target.addPort();
    		//edge.setSource(source.getChildAt(0));
    		//edge.setTarget(target.getChildAt(0));

    		triple.add(0, source);
    		triple.add(1, target);
    		triple.add(2, edge);
    		return triple;
    	}
    

    public ArrayList<Object> getGraphTripleWithPt() {
	ArrayList<Object> triple = new ArrayList<Object>();

	CustomCell source = new CustomCell(subject.getLabel());
	DefaultEdge edge = new DefaultEdge();
	CustomCell target = new CustomCell(object.getLabel());

	if(predicate.getLabel()!= "null")
	   edge.setUserObject(predicate.getLabel());
	source.addPort();
	target.addPort();

	triple.add(0,source);
	triple.add(1,target);
	triple.add(2,edge);
	return triple;
    }

    public ArrayList getRDFTriple(){
	ArrayList<Object> triple = new ArrayList<Object>();

	triple.add(0,subject);
	triple.add(1,object);
	triple.add(2,predicate);
	return triple;
    }
    
    /**
     * Insert a given triple on Data Base 
     * @param triple
     * @param idInsertedGraph
     * @throws SQLException 
     */
    public static void saveTripleToBD(RDFTriple triple, int idInsertedGraph) throws SQLException{
	
	ConnectionToPostgres connection = PersistentManager.getConnectionToPostgreSQL();
	Hashtable<String, Object> insertTriple = new Hashtable<String, Object>();
	insertTriple.put("id_graph", idInsertedGraph);
	insertTriple.put("subject", triple.getSubject());
	insertTriple.put("predicate",triple.getPredicate());
	insertTriple.put("object", triple.getObject());
	connection.insert("triple_rdf",insertTriple);

    }
    /**
     * Insert a given array of triple on Data Base 
     * @param triple
     * @param idInsertedGraph
     * @throws SQLException 
     */
    public static void saveTripleArrayToBD(ArrayList<RDFTriple> tripleArray, int idInsertedGraph) throws SQLException{
	
	ConnectionToPostgres connection = PersistentManager.getConnectionToPostgreSQL();
	Hashtable<String, Object> insertTriple = new Hashtable<String, Object>();
	insertTriple.put("id_graph", idInsertedGraph);
	for (RDFTriple triple : tripleArray) {
	    insertTriple.put("subject", triple.getSubject());
	    insertTriple.put("predicate",triple.getPredicate());
	    insertTriple.put("object", triple.getObject());
	    connection.insert("triple_rdf",insertTriple);
	}
    }

    public Subject getSubject() {
    	return subject;
        }
        public void setSubject(Subject subject) {
    	this.subject = subject;
        }
        public Predicate getPredicate() {
    	return predicate;
        }
        public void setPredicate(Predicate predicate) {
    	this.predicate = predicate;
        }
        public RDFObject getObject() {
    	return object;
        }
        public void setObject(RDFObject object) {
    	this.object = object;
        }
        
        /**
         * @return the vertexPoint
         */
        protected Map getVertexPoint() {
    	return vertexPoint;
        }
        
        /**
         * @param vertexPoint the vertexPoint to set
         */
        protected void setVertexPoint(Map vertexPoint) {
    	this.vertexPoint = vertexPoint;
        }


		public String[] getSubjetProcess() {
			return subjectProcess;
		}


		public void setSubjectProcess(String[] subjetProcess) {
			this.subjectProcess = subjetProcess;
		}


		public String[] getObjectProcess() {
			return objectProcess;
		}


		public void setObjectProcess(String[] objectProcess) {
			this.objectProcess = objectProcess;
		}
        
        

}
