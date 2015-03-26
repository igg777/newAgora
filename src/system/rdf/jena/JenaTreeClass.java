package system.rdf.jena;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import org.semanticweb.owl.io.vocabulary.OWLVocabularyAdapter;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLException;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

public class JenaTreeClass {
	
	private static OWLVocabularyAdapter OWL = OWLVocabularyAdapter.INSTANCE;
	private static OntModel model;
	
	//Interface Synchro
	private JScrollPane viewController;
	
	// Ontology default class
	private OWLClass owlThing;
	private OWLClass owlNothing;
	
	//General behaviour
	public boolean useOldClassTreeReferenceforExpansion = false;
	public boolean useOldPropertyTreeReferenceforExpansion = false;

	TreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer() {
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			return this;
		}
	};

	public JenaTreeClass (OntModel model, JScrollPane viewController) {
		this.model = model;
		this.viewController = viewController;
	}
	
	DefaultMutableTreeNode createNode(Set set) {
		return new DefaultMutableTreeNode(set);
	}
	
	public boolean isInstances(String clsName ){
		return model.getIndividual(clsName) != null;
	}
	
	public Set getSubclassof(OWLClass cClass){
		
		return null;
	}
	
		
	public void renderSibling(DefaultMutableTreeNode node, OntClass cls){
		int index =0;
		for (ExtendedIterator sibling = cls.listSubClasses(); sibling.hasNext();){
			node.insert(new DefaultMutableTreeNode(sibling.next().toString().split("#")[1]), index);
			index++;
		}
	}
	
	public void addClassToNode(DefaultMutableTreeNode node, OntClass cls, List occurs){
		// List all subclass and them insert all child for class and each subclass
		renderSibling(node, cls);
		
		if (cls.canAs( OntClass.class )  &&  !occurs.contains( cls )) {
            for (Iterator i = cls.listSubClasses( true );  i.hasNext(); ) {
            	int index = 0;
                OntClass sub = (OntClass) i.next();
                DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(sub.toString().split("#")[1]);
                
                // we push this expression on the occurs list before we recurse
                occurs.add( cls );
                addClassToNode(subNode, sub, occurs);
                occurs.remove( cls );
                node.insert(new DefaultMutableTreeNode(cls.getModel().shortForm(cls.getURI())), index);
                //node.add(new DefaultMutableTreeNode(cls.getModel().shortForm(cls.getURI())));
            }
        }
		
	}
	
	public JTree getClassTree() throws OWLException {		
		if (model.isEmpty())
			return null;
		
		JTree classTree = null;
		
		DefaultMutableTreeNode thing = null;
		DefaultMutableTreeNode nothing = null;
		
		
		
		//First , obtain first class Thing, then iterate throw all subclass and child of their
		Iterator cls = model.listHierarchyRootClasses()
        .filterDrop( new Filter() {
                      public boolean accept( Object o ) {
                          return ((Resource) o).isAnon();
                      }} );
		DefaultMutableTreeNode nodeRoot = new DefaultMutableTreeNode("SCOR");
		
		while (cls.hasNext()){
			addClassToNode(nodeRoot, (OntClass) cls.next(), new ArrayList());
		}
		DefaultTreeModel treemodel = new DefaultTreeModel(nodeRoot);
		classTree = new JTree(treemodel);
		return classTree;
	}
	
	
	
}