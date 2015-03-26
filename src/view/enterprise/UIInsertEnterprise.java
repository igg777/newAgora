package view.enterprise;

import info.clearthought.layout.TableLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.SwingUtilities;

import controller.enterprise.EnterpriseController;

import system.rdf.ui.GraphEd;

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
public class UIInsertEnterprise extends javax.swing.JFrame {
	private JButton jbtnInsert;
	private JButton jbtnCancel;
	private JLabel jlblDescr;
	private JLabel jlblinserttt;
	private JTextPane jtxpDesc;
	private JTextField jtfEmail;
	private JLabel lblEmail;
	private JTextField jtfName;
	private JLabel jlblName;
	private UIEnterpriseGestor entGestor;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIInsertEnterprise inst = new UIInsertEnterprise();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIInsertEnterprise() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			TableLayout thisLayout = new TableLayout(new double[][] {
					{ TableLayout.FILL, 24.0, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL },
					{ 20.0, 25.0, 10.0, 20.0, 20.0, 20.0, 10.0, 25.0, 20.0,
							25.0, 25.0, 25.0 } });
			thisLayout.setHGap(5);
			thisLayout.setVGap(5);
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Insert enterprise");
			{
				jbtnInsert = new JButton();
				getContentPane().add(jbtnInsert, "8, 9, 11, 9");
				jbtnInsert.setText("Insert");
				jbtnInsert.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnInsertActionPerformed(evt);
					}
				});
			}
			{
				jbtnCancel = new JButton();
				getContentPane().add(jbtnCancel, "13, 9, 16, 9");
				jbtnCancel.setText("Cancel");
				jbtnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnCancelActionPerformed(evt);
					}
				});
			}
			{
				jlblName = new JLabel();
				getContentPane().add(jlblName, "2, 1, 6, 1");
				jlblName.setText("Enterprise name:");
			}
			{
				jtfName = new JTextField();
				getContentPane().add(jtfName, "8, 1, 15, 1");
				jtfName.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
			}
			{
				jlblDescr = new JLabel();
				getContentPane().add(jlblDescr, "2, 3, 5, 3");
				jlblDescr.setText("Description:");
			}
			{
				lblEmail = new JLabel();
				getContentPane().add(lblEmail, "2, 7, 6, 7");
				lblEmail.setText("Enterprise E-mail");
			}
			{
				jtfEmail = new JTextField();
				getContentPane().add(jtfEmail, "8, 7, 15, 7");
				jtfEmail.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
			}
			{
				jtxpDesc = new JTextPane();
				getContentPane().add(jtxpDesc, "8, 3, 15, 5");
				jtxpDesc.setBorder(new LineBorder(new java.awt.Color(0,0,0), 1, false));
			}
			{
				jlblinserttt = new JLabel();
				getContentPane().add(jlblinserttt, "16, 1");
				jlblinserttt.setText("*");
				jlblinserttt.setFont(new java.awt.Font("Tahoma",0,18));
				jlblinserttt.setForeground(new java.awt.Color(255,0,128));
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbtnInsertActionPerformed(ActionEvent evt) {
		String name = jtfName.getText();
		String desc = jtxpDesc.getText();
		String email = jtfEmail.getText();
		try {
			if (name.equals("") || desc.equals("") || email.equals("")) {
				JOptionPane.showMessageDialog(null, "Please, fill all fields");
			} else {
				EnterpriseController controller = new EnterpriseController(
						name, desc, email);
				controller.insertEnterprise();
				entGestor.refreshTable();
				this.dispose();
			}

		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

	private void jbtnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	public void setEnterpriseGestor(UIEnterpriseGestor gestor) {
		this.entGestor = gestor;
	}

}
