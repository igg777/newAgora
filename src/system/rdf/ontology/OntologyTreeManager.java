package system.rdf.ontology;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;

/**
 * Handle the Ontology creating a JTree with the Ontology component
 * @author Irlan
 */
public class OntologyTreeManager {

	protected static OntologyTree tree;
	protected static DefaultTreeModel treeModel;
	protected static DefaultMutableTreeNode root;
	protected static OntologyLoader ontLoader = new OntologyLoader();
	protected static int counter = 0;

	/**
	 * Empty constructor
	 */
	public OntologyTreeManager() {
	}

	/**
	 * @param parent
	 * @return
	 */
	public List createChildTreeNode(Iterator parent) {
		// This function is not recursive due to instance can't act as parent of
		// other instances
		Map map = new TreeMap();
		while (parent.hasNext()) {
			OntResource child = (OntResource) parent.next();
			map.put(child.getLocalName(), child);
		}
		List childList = new ArrayList();
		Iterator addChild = map.values().iterator();
		while (addChild.hasNext()) {
			childList.add(new DefaultMutableTreeNode(((OntResource) addChild
					.next()).getLocalName()));
		}
		return childList;
	}

	/**
	 * This method read the needful data from ontology to form the Jtree. It is
	 * Recursive
	 * @param rootCls
	 * @return rootList
	 */
	public List createTreeNodes1(Iterator rootCls) {
		// The TreeMap contains the ontology classes for a given proof
		// The TreeMap allows having a sorted list and thus, a sorted JTree
		Map map = new TreeMap();
		while (rootCls.hasNext()) {
			OntClass o = (OntClass) rootCls.next();
			// Ask for Domain Process
			if (o.getLocalName().equals("Domain_Process")) {
				// Getting into the SubClasses
				Iterator<?> subClasses = o.listSubClasses();

				while (subClasses.hasNext()) {
					OntClass sub = (OntClass) subClasses.next();
					// Ask for Main Process
					if (sub.getLocalName().equals("Main_Process")) {
						Iterator<?> i = sub.listInstances();
						// Get all the individuals for Main_Process
						while (i.hasNext()) {
							Individual ind = (Individual) i.next();
							map.put(ind.getLocalName(), ind);
						}// end last while
					}
				}
			}
		}

		// rootList contains all nodes for a given level
		// rootList Contains the individuals that Belong to Main_Process Class
		List rootList = new ArrayList();
		Iterator a = map.values().iterator();
		while (a.hasNext())
			rootList.add(new DefaultMutableTreeNode(((Individual) a.next())
					.getLocalName()));
		Iterator it = map.values().iterator();
		for (int i = 0; i < map.values().size(); i++) {
			Individual o = (Individual) it.next();
			// Property that join the individuals with the Configuration Process
			Property ownerCfgPLevel = OntologyLoader.modelToScor
					.getProperty(OntologyLoader.NS + "ownerCfgPLevel");
			// Getting all the individuals that are Connected using the Property
			// ownerCfgPLevel
			ExtendedIterator child = o.listPropertyValues(ownerCfgPLevel);
			// Calling Method createChildTreeNode and assigning to List Child
			List childsOfCfgPLevel = createChildTreeNode(o.listPropertyValues(
					ownerCfgPLevel).filterDrop(new Filter() {
				public boolean accept(Object o) {
					return ((Resource) o).isAnon()
							|| ((Resource) o).getLocalName().equals("Nothing");
				}
			}));

			// Here is the point to Create the Nodes and then Iterate over them
			DefaultMutableTreeNode treeChild = (DefaultMutableTreeNode) rootList
					.get(i);
			Iterator childIt = childsOfCfgPLevel.iterator();
			while (childIt.hasNext()) {
				treeChild.add((DefaultMutableTreeNode) childIt.next());
			}
		}
		return rootList;
	}

	public List createTreeNodes(Iterator rootCls, String propName) {
		// The treemap contains the ontology classes for a given proof
		// The treemap allows having a sorted list and thus, a sorted JTree
		Map map = new TreeMap();
		while (rootCls.hasNext()) {
			OntResource o = (OntResource) rootCls.next();
			map.put(o.getLocalName(), o);
		}
		// rootList contains all nodes for a given level
		// nodes will be added to the JTree
		List rootList = new ArrayList();
		Iterator a = map.values().iterator();
		while (a.hasNext())
			rootList.add(new DefaultMutableTreeNode(((OntResource) a.next())
					.getLocalName()));
		// for each class, let's list its subclasses
		// anonymous classes and 'owl:Nothing' classes are dropped
		Iterator it = map.values().iterator();
		for (int i = 0; i < map.values().size(); i++) {
			OntResource o = (OntResource) it.next();
			List sub;
			String toChange = "ownerCfgPLevel";
			if (toChange.equals("ownerCfgPLevel")) {
				Property propertyToGet = OntologyLoader.modelToScor
						.getProperty(OntologyLoader.NS + toChange);
				// Getting all the individuals that are Connected using the
				// Property ownerCfgPLevel
				ExtendedIterator child = o.listPropertyValues(propertyToGet);
				sub = createTreeNodes(o.listPropertyValues(propertyToGet),
						toChange);

				DefaultMutableTreeNode temp = (DefaultMutableTreeNode) rootList
						.get(i);
				Iterator subit = sub.iterator();
				// adds all subnodes
				while (subit.hasNext()) {
					temp.add((DefaultMutableTreeNode) subit.next());
				}
			}
			toChange = "measureby";
			if (toChange.equals("measureby")) {
				Property propertyToGet = OntologyLoader.modelToScor
						.getProperty(OntologyLoader.NS + toChange);
				// Getting all the individuals that are Connected using the
				// Property ownerCfgPLevel
				ExtendedIterator child = o.listPropertyValues(propertyToGet);
				sub = createTreeNodes(o.listPropertyValues(propertyToGet),
						toChange);

				DefaultMutableTreeNode temp = (DefaultMutableTreeNode) rootList
						.get(i);
				Iterator subit = sub.iterator();
				// adds all subnodes
				while (subit.hasNext()) {
					temp.add((DefaultMutableTreeNode) subit.next());
				}
			}

			toChange = "ownerOfIndicators";
			if (toChange.equals("ownerOfIndicators")) {
				Property propertyToGet = OntologyLoader.modelToScor
						.getProperty(OntologyLoader.NS + toChange);
				// Getting all the individuals that are Connected using the
				// Property ownerCfgPLevel
				ExtendedIterator child = o.listPropertyValues(propertyToGet);
				sub = createTreeNodes(o.listPropertyValues(propertyToGet),
						toChange);

				DefaultMutableTreeNode temp = (DefaultMutableTreeNode) rootList
						.get(i);
				Iterator subit = sub.iterator();
				// adds all subnodes
				while (subit.hasNext()) {
					temp.add((DefaultMutableTreeNode) subit.next());
				}
			}

		}
		return rootList;
	}

	/**
	 * Create a JTree instances with the ontology elements
	 * 
	 * @param rootNodes
	 * @return JTree instances
	 */
	public OntologyTree createTree(List rootNodes) {
		Iterator sibling;

		// owl:Thing is super class of all class in any ontology
		root = new DefaultMutableTreeNode("owl:SCOR");
		// Add all node to root node
		sibling = rootNodes.iterator();
		while (sibling.hasNext()) {
			root.add((DefaultMutableTreeNode) sibling.next());
		}
		treeModel = new DefaultTreeModel(root);
		tree = new OntologyTree(treeModel);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		return tree;
	}

	/**
	 * Gets the ontology tree
	 * @return OntologyTree
	 */
	public static OntologyTree getOntologyTree() {
		tree = ontLoader.getTree();
		return tree;
	}

	/**
	 * Gets the Ontology Loader instances
	 * @return OntologyLoader
	 */
	public static OntologyLoader getOntologyLoader() {
		return ontLoader;
	}

}
