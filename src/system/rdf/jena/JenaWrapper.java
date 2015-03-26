package system.rdf.jena;

import com.hp.hpl.jena.rdf.model.*;

import org.jgraph.*;
import java.io.*;


/**
 * This class encapsulate Jena model 
 * @author Irl�n Grangel
 */
public class JenaWrapper {
	
	private Model jModel;
	private RDFWriter jwriter;
	private JGraph graph;
	/**
	 * Constructor of the class
	 */
	public JenaWrapper () {
		this.jModel = ModelFactory.createDefaultModel();
		
	}
	
	public void setGraph ( JGraph graph ) {
		this.graph = graph;
	}
	public void StorageModel (String FilePath) throws IOException{
		jwriter = jModel.getWriter();
	    jwriter.setProperty("showXmlDeclaration","true");
	    jwriter.setProperty("tab","8");
	    jwriter.setProperty("relativeURIs","same-document,relative");
    	//OutputStream out = new FileOutputStream(FilePath + ".rdf");
	    // Edge counted as root in graphModel	    
    	//jModel.write(new PrintWriter(FilePath +".rdf"));
	}
}
