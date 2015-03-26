package system.rdf.handlers;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jgraph.JGraph;

import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.ui.GraphEd;
import system.rdf.utils.TabsComponentManager;

public class DragDropHandler implements DropTargetListener, DragSourceListener,
		DragGestureListener {

	public JGraph graph;

	public JTree tree;

	public String leaf;

	DropTarget dropTarget = null;

	DragSource dragSource = null;

	String[] mainProcess;

	String selectedNode = null;

	/**
	 * 
	 * @param graph
	 *            target
	 * @param tree
	 *            source
	 */
	public DragDropHandler(JGraph graph, final JTree tree) {

		super();

		this.graph = graph;
		this.tree = tree;

		dropTarget = new DropTarget(graph, this);
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree,
				DnDConstants.ACTION_MOVE, this);

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent evt) {

				leaf = evt.getPath().getLastPathComponent().toString();
			}
		});

	}

	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub

	}

	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public void drop(DropTargetDropEvent dropTarget) {
		try {
			if (leaf != null) {
				if (!executeDrop()) {
					// It can be two kinds of graph where to drop Ontology Tree
					// items
					if (graph.getClass().getSimpleName().equals("ProblemGraph")) {
						((ProblemGraph) graph).insertCell(
								dropTarget.getLocation(),
								new MyProblemCustomCell(leaf, mainProcess));
					}

					if (graph.getClass().getSimpleName()
							.equals("SolutionGraph")) {
						((SolutionGraph) graph).insertCell(
								dropTarget.getLocation(), leaf, mainProcess);
					}
				}
			}

		} catch (Exception e) {
			TabsComponentManager.getErrorPaneInstance().printMessage(e);
		}
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {

	}

	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		try {
			StringSelection text = new StringSelection(leaf);

			if (executeDrop()){
				// as the name suggests, starts the dragging
				dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop, text,this);
			}
			else{
				dragSource.startDrag(dge, DragSource.DefaultMoveDrop, text,this);
			}
		} catch (InvalidDnDOperationException e) {
			TabsComponentManager.getErrorPaneInstance().printMessage(e);
		}

	}

	/**
	 * Validate that only can be drop into the Modeling Space a node that is
	 * leaf(Means that is an indicator)
	 * 
	 * @return
	 */
	public boolean executeDrop() {
		boolean canDrop = false;
		try {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			mainProcess = new String[node.getPath().length - 2];
			for (int i = 1; i < mainProcess.length + 1; i++) {
				mainProcess[i - 1] = node.getPath()[i].toString();
			}
			// Only can be drop into modeling component if it is a leaf, means
			// that is an indicator
			if (node.isLeaf()) {
			} else {
				canDrop = true;
			}
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
		return canDrop;
	}

}
