package view.enterprise;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.swingx.JXHeader;

import controller.enterprise.EnterpriseController;

import system.rdf.ui.GraphEd;

public class UIEnterpriseGestor extends javax.swing.JFrame {

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JXHeader bannerHeader;
	private JButton btnDelete;
	static public JTable enterpriseTable;
	private JButton btnCancel;
	private JButton btnApply;
	private JLabel enterpriseInformationLabel;
	private JScrollPane tableScrollPane;
	private JButton btnAceptar;
	private JSeparator separator;
	private JButton btnModify;
	private JButton btnInsertar;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIEnterpriseGestor inst = new UIEnterpriseGestor();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIEnterpriseGestor() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			GroupLayout thisLayout = new GroupLayout(
					(JComponent) getContentPane());
			getContentPane().setLayout(thisLayout);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			this.setTitle("Enterprise Information");
			this.setResizable(false);
			this.setIconImage(new ImageIcon(getClass().getClassLoader()
					.getResource("system/rdf/configure/resource/table.png"))
					.getImage());
			{
				bannerHeader = new JXHeader();
				bannerHeader.setTitle("System enterprises");
				bannerHeader.setBackground(new java.awt.Color(255, 255, 255));
				bannerHeader.setDescription("Manage the system enterprises");
				bannerHeader.setBorder(BorderFactory.createMatteBorder(1, 1, 1,
						1, new java.awt.Color(192, 192, 192)));
			}
			{
				separator = new JSeparator();
			}
			{
				enterpriseInformationLabel = new JLabel();
				enterpriseInformationLabel.setText("Enterprise's Information");
				enterpriseInformationLabel
						.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				tableScrollPane = new JScrollPane();
				{
					enterpriseTable = new JTable();
					tableScrollPane.setViewportView(enterpriseTable);
					EnterpriseController controller = new EnterpriseController();
					enterpriseTable.setModel(controller.loadTableModel());
					enterpriseTable.getTableHeader().setBounds(0, 0, 306, -13);
				}
			}
			{
				btnAceptar = new JButton();
				btnAceptar.setText("Accept");
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAceptarActionPerformed(evt);
					}
				});
			}
			{
				btnApply = new JButton();
				btnApply.setText("Apply");
				btnApply.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnApplyActionPerformed(evt);
					}
				});
			}
			{
				btnCancel = new JButton();
				btnCancel.setText("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCancelActionPerformed(evt);
					}
				});
			}
			{
				btnInsertar = new JButton();
				btnInsertar.setText("Insert");
				btnInsertar
						.setIcon(new ImageIcon(
								getClass()
										.getClassLoader()
										.getResource(
												"system/rdf/configure/resource/table_row_insert.png")));
				btnInsertar.setIconTextGap(2);
				btnInsertar.setMargin(new java.awt.Insets(2, 0, 2, 14));
				btnInsertar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnInsertarActionPerformed(evt);
					}
				});
			}
			{
				btnDelete = new JButton();
				btnDelete.setText("Delete");
				btnDelete
						.setIcon(new ImageIcon(
								getClass()
										.getClassLoader()
										.getResource(
												"system/rdf/configure/resource/table_row_delete.png")));
				btnDelete.setIconTextGap(2);
				btnDelete.setMargin(new java.awt.Insets(2, 0, 2, 14));
				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDeleteActionPerformed(evt);
					}
				});
			}
			{
				btnModify = new JButton();
				btnModify.setText("Modify");
				btnModify
						.setIcon(new ImageIcon(
								getClass()
										.getClassLoader()
										.getResource(
												"system/rdf/configure/resource/table_refresh.png")));
				btnModify.setIconTextGap(2);
				btnModify.setMargin(new java.awt.Insets(2, 0, 2, 14));
				btnModify.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnModifyActionPerformed(evt);
					}
				});
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().add(
					bannerHeader, GroupLayout.PREFERRED_SIZE, 50,
					GroupLayout.PREFERRED_SIZE).add(26).add(
					enterpriseInformationLabel, GroupLayout.PREFERRED_SIZE,
					GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.add(
							thisLayout.createParallelGroup().add(
									GroupLayout.LEADING,
									thisLayout.createSequentialGroup().add(
											btnInsertar,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(
													LayoutStyle.UNRELATED).add(
													btnModify,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(
													LayoutStyle.UNRELATED).add(
													btnDelete,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.PREFERRED_SIZE,
													GroupLayout.PREFERRED_SIZE)
											.add(63)).add(GroupLayout.LEADING,
									tableScrollPane,
									GroupLayout.PREFERRED_SIZE, 154,
									GroupLayout.PREFERRED_SIZE)).add(38).add(
							separator, GroupLayout.PREFERRED_SIZE,
							GroupLayout.PREFERRED_SIZE,
							GroupLayout.PREFERRED_SIZE).add(16).add(
							thisLayout
									.createParallelGroup(GroupLayout.BASELINE)
									.add(GroupLayout.BASELINE, btnCancel,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE).add(
											GroupLayout.BASELINE, btnAceptar,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE).add(
											GroupLayout.BASELINE, btnApply,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE))
					.addContainerGap(19, 19));
			thisLayout
					.setHorizontalGroup(thisLayout
							.createParallelGroup()
							.add(
									GroupLayout.LEADING,
									thisLayout.createParallelGroup().add(
											GroupLayout.LEADING, bannerHeader,
											0, 456, Short.MAX_VALUE).add(
											GroupLayout.LEADING, separator, 0,
											456, Short.MAX_VALUE))
							.add(
									thisLayout
											.createSequentialGroup()
											.add(15)
											.add(
													thisLayout
															.createParallelGroup()
															.add(
																	GroupLayout.LEADING,
																	tableScrollPane,
																	GroupLayout.PREFERRED_SIZE,
																	309,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	enterpriseInformationLabel,
																	0,
																	309,
																	Short.MAX_VALUE)
															.add(
																	GroupLayout.LEADING,
																	thisLayout
																			.createSequentialGroup()
																			.add(
																					0,
																					135,
																					Short.MAX_VALUE)
																			.add(
																					btnAceptar,
																					GroupLayout.PREFERRED_SIZE,
																					75,
																					GroupLayout.PREFERRED_SIZE)
																			.add(
																					24)
																			.add(
																					btnApply,
																					GroupLayout.PREFERRED_SIZE,
																					75,
																					GroupLayout.PREFERRED_SIZE)))
											.add(23)
											.add(
													thisLayout
															.createParallelGroup()
															.add(
																	GroupLayout.LEADING,
																	btnCancel,
																	GroupLayout.PREFERRED_SIZE,
																	75,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	btnInsertar,
																	GroupLayout.PREFERRED_SIZE,
																	75,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	btnModify,
																	GroupLayout.PREFERRED_SIZE,
																	75,
																	GroupLayout.PREFERRED_SIZE)
															.add(
																	GroupLayout.LEADING,
																	btnDelete,
																	GroupLayout.PREFERRED_SIZE,
																	75,
																	GroupLayout.PREFERRED_SIZE))
											.addContainerGap(34, 34)));
			pack();
			this.setSize(462, 367);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JButton getBtnInsertar() {
		return btnInsertar;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JButton getBtnModify() {
		return btnModify;
	}

	public JButton getBtnAceptar() {
		return btnAceptar;
	}

	public JButton getBtnApply() {
		return btnApply;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	private void btnInsertarActionPerformed(ActionEvent evt) {
        UIInsertEnterprise obj = new UIInsertEnterprise();
        obj.setEnterpriseGestor(this);
        obj.setLocationRelativeTo(null);
        obj.setVisible(true);
	}

	private void btnModifyActionPerformed(ActionEvent evt) {
		try{
			String enterpriseName = "";
			TableModel model = enterpriseTable.getModel();
		
		int selectedRow = enterpriseTable.getSelectedRow();
		if (selectedRow >= 0) {
			enterpriseName = (String) model.getValueAt(selectedRow,
					0);
		} else {
			throw new Exception("Please, select the enterprise to modify");
		}
		UIModifyEnterprise obj = new UIModifyEnterprise();
        obj.setEnterpriseGestor(this);
        obj.setOldEnterpriseName(enterpriseName);
        obj.setLocationRelativeTo(null);
        obj.setVisible(true);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void btnDeleteActionPerformed(ActionEvent evt) {

		try {
			TableModel model = enterpriseTable.getModel();
			int selectedRow = enterpriseTable.getSelectedRow();
			if (selectedRow >= 0) {
				String enterpriseName = (String) model.getValueAt(selectedRow,
						0);
				EnterpriseController controller = new EnterpriseController(
						enterpriseName);
				controller.deleteEnterprise();
				refreshTable();
			} else {
				JOptionPane.showMessageDialog(null,
						"You must select an enterprise to delete");
			}
		} catch (Exception e) {
			e.printStackTrace();
			GraphEd.errorPane.printMessage(e);
		}

	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void btnApplyActionPerformed(ActionEvent evt) {

	}

	private void btnAceptarActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	public void refreshTable() {
		try {
			EnterpriseController controller = new EnterpriseController();
			enterpriseTable.setModel(controller.loadTableModel());
		} catch (Exception e) {
			e.printStackTrace();
			GraphEd.errorPane.printMessage(e);
		}
	}

}
