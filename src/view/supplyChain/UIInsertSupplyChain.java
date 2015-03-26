package view.supplyChain;

import info.clearthought.layout.TableLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

import system.rdf.dataBase.ExtractData;
import system.rdf.ui.GraphEd;

import controller.supplyChain.SupplyChainController;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class UIInsertSupplyChain extends javax.swing.JFrame {
	private JButton jbtnRemove;
	private JLabel lblComments;
	private JLabel jlblCSLabel;
	private JLabel jlblEnterpriseLabel;
	private JTextPane jtpComments;
	private JLabel jlblenterprisesss;
	private JLabel jlblnameee;
	private JScrollPane jscpSCEnterprises;
	private JScrollPane jscpEnterprises;
	private JList jlSCEnterprises;
	private JList jlEnterprises;
	private JButton jbtnCancel;
	private JButton jbtnInsert;
	private JLabel lblInformation;
	private JTextField jtfName;
	private JLabel jlblName;
	private JButton jbtnAdd;
	UISupplyChainInformationGestor gestor;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIInsertSupplyChain inst = new UIInsertSupplyChain();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIInsertSupplyChain() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			SupplyChainController controller = new SupplyChainController();
			TableLayout thisLayout = new TableLayout(new double[][] {
					{ TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL,
						TableLayout.FILL, TableLayout.FILL },
						{ TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL } });
			thisLayout.setHGap(5);
			thisLayout.setVGap(5);
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Insert supply chain");
			{
				jbtnAdd = new JButton();
				getContentPane().add(jbtnAdd, "9, 16, 11, 17, c, f");
				jbtnAdd.setText("     Add     ");
				jbtnAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnAddActionPerformed(evt);
					}
				});
			}
			{
				jbtnRemove = new JButton();
				getContentPane().add(jbtnRemove, "9, 19, 11, 20, c, f");
				jbtnRemove.setText("  Remove  ");
				jbtnRemove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnRemoveActionPerformed(evt);
					}
				});
			}
			{
				jlblName = new JLabel();
				getContentPane().add(jlblName, "1, 1, 3, 1");
				jlblName.setText("Name:");
			}
			{
				jtfName = new JTextField();
				getContentPane().add(jtfName, "1, 2, 7, 3");
			}
			{
				lblInformation = new JLabel();
				getContentPane().add(lblInformation, "1, 9, 7, 10");
				lblInformation
				.setText("Please, select the supply chain's enterprises");
			}
			{
				jbtnInsert = new JButton();
				getContentPane().add(jbtnInsert, "13, 33, 15, 34");
				jbtnInsert.setText("Insert");
				jbtnInsert.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnInsertActionPerformed(evt);
					}
				});
			}
			{
				jbtnCancel = new JButton();
				getContentPane().add(jbtnCancel, "17, 33, 19, 34");
				jbtnCancel.setText("Cancel");
				jbtnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnCancelActionPerformed(evt);
					}
				});
			}
			{
				jlblEnterpriseLabel = new JLabel();
				getContentPane().add(jlblEnterpriseLabel, "1, 12, 6, 12");
				jlblEnterpriseLabel.setText("Enterprises in the system:");
			}
			{
				jlblCSLabel = new JLabel();
				getContentPane().add(jlblCSLabel, "12, 12, 19, 12");
				jlblCSLabel
				.setText("Enterprises that belong to the supply chain:");
			}
			{
				lblComments = new JLabel();
				getContentPane().add(lblComments, "12, 1, 14, 1");
				lblComments.setText("Comments:");
			}
			{
				jtpComments = new JTextPane();
				getContentPane().add(jtpComments, "12, 2, 19, 8");
			}
			{
				jscpEnterprises = new JScrollPane();
				jscpEnterprises.getHorizontalScrollBar().setAutoscrolls(true);
				jscpEnterprises.getVerticalScrollBar().setAutoscrolls(true);
				getContentPane().add(jscpEnterprises, "1, 13, 8, 31");
				{
					ListModel jlEnterprisesModel = controller
					.loadlistModelAllEnterprises();
					jlEnterprises = new JList();
					jscpEnterprises.setViewportView(jlEnterprises);
					jlEnterprises.setModel(jlEnterprisesModel);
					jlEnterprises.setPreferredSize(new java.awt.Dimension(213, 277));
				}
			}
			{
				jscpSCEnterprises = new JScrollPane();
				jscpSCEnterprises.getHorizontalScrollBar().setAutoscrolls(true);
				jscpSCEnterprises.getVerticalScrollBar().setAutoscrolls(true);
				getContentPane().add(jscpSCEnterprises, "12, 13, 19, 31");
				{
					ListModel jlSCEnterprisesModel = controller
					.loadListModelEnterprisesForSC();
					jlSCEnterprises = new JList();
					jscpSCEnterprises.setViewportView(jlSCEnterprises);
					jlSCEnterprises.setModel(jlSCEnterprisesModel);
				}
			}
			{
				jlblnameee = new JLabel();
				getContentPane().add(jlblnameee, "8, 2, 9, 3");
				jlblnameee.setText("*");
				jlblnameee.setFont(new java.awt.Font("Tahoma",0,18));
				jlblnameee.setForeground(new java.awt.Color(255,0,128));
			}
			{
				jlblenterprisesss = new JLabel();
				getContentPane().add(jlblenterprisesss, "8, 9, 8, 10");
				jlblenterprisesss.setText("*");
				jlblenterprisesss.setFont(new java.awt.Font("Tahoma",0,18));
				jlblenterprisesss.setForeground(new java.awt.Color(255,0,128));
			}
			pack();
			this.setSize(671, 573);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbtnAddActionPerformed(ActionEvent evt) {
		SupplyChainController controller = new SupplyChainController();
		ListModel  enterpriseModel= jlEnterprises.getModel(); 
		ListModel supplyChainModel = jlSCEnterprises.getModel();
		int [] selected  = jlEnterprises.getSelectedIndices();
		ArrayList<ExtractData> selectedData = new ArrayList<ExtractData>();
		for (int i = selected.length - 1; i >= 0 ; i--) {
			selectedData.add((ExtractData)enterpriseModel.getElementAt(selected[i]));
		}
		jlSCEnterprises.setModel(controller.addDataToModel(selectedData, supplyChainModel));
		jlEnterprises.setModel(controller.removeDataFromModel(selectedData, enterpriseModel));
	}

	private void jbtnRemoveActionPerformed(ActionEvent evt) {
		SupplyChainController controller = new SupplyChainController();
		ListModel  enterpriseModel= jlEnterprises.getModel(); 
		ListModel supplyChainModel = jlSCEnterprises.getModel();
		int [] selected  = jlSCEnterprises.getSelectedIndices();
		ArrayList<ExtractData> selectedData = new ArrayList<ExtractData>();
		for (int i = selected.length - 1; i >= 0 ; i--) {
			selectedData.add((ExtractData)supplyChainModel.getElementAt(selected[i]));
		}
		jlEnterprises.setModel(controller.addDataToModel(selectedData, enterpriseModel));
		jlSCEnterprises.setModel(controller.removeDataFromModel(selectedData, supplyChainModel));
	}

	public UISupplyChainInformationGestor getGestor() {
		return gestor;
	}

	public void setGestor(UISupplyChainInformationGestor gestor) {
		this.gestor = gestor;
	}

	private void jbtnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void jbtnInsertActionPerformed(ActionEvent evt) {
		try{
		ListModel model = jlSCEnterprises.getModel();
		int size = model.getSize();
		ArrayList<ExtractData> enterprises = new ArrayList<ExtractData>();
		for (int i = 0; i < size; i++) {
			enterprises.add((ExtractData) model.getElementAt(i));
		}
		if (enterprises.size() > 0) {
			if (jtfName.getText().equals("") == false
					|| jtpComments.getText().equals("") == false) {
				SupplyChainController controller = new SupplyChainController(
						jtfName.getText(), jtpComments.getText());
				controller.insertSupplyChain();
				for (ExtractData extractData : enterprises) {
					controller.addEnterprice(extractData.getId());
				}
				gestor.refreshTable();
				this.dispose();
			} else
				JOptionPane
				.showMessageDialog(null,
						"Please, enter the name and the comments of the supply chain");
		} else
			JOptionPane.showMessageDialog(null,
			"The supply chain most have at least one enterprise");
	}
		catch (SQLException e) {
			GraphEd.errorPane.printMessage(e);
		}
		catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}
}
