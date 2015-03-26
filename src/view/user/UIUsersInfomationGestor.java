package view.user;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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

import controller.user.UserController;

import system.rdf.ui.GraphEd;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class UIUsersInfomationGestor extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JXHeader bannerHeader;
	private JButton btnDelete;
	static public JTable userTable;
	private JMenuItem deleteUserMenuItem;
	private JMenuItem establishPassMenuItem;
	private JPopupMenu popupMenu;
	private JButton btnCancel;
	private JButton btnApply;
	private JLabel userInformationLabel;
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
				UIUsersInfomationGestor inst = new UIUsersInfomationGestor();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIUsersInfomationGestor() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			getContentPane().setBackground(new java.awt.Color(210,210,200));
			this.setTitle("User Information");
			this.setResizable(false);
			this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("system/rdf/configure/resource/table.png")).getImage());
			{
				bannerHeader = new JXHeader();
				bannerHeader.setTitle("System Users");
				bannerHeader.setBackground(new java.awt.Color(255,255,255));
				bannerHeader.setDescription("Manage the system users");
				bannerHeader.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(192,192,192)));
			}
			{
				separator = new JSeparator();
			}
			{
				userInformationLabel = new JLabel();
				userInformationLabel.setText("Users Information");
				userInformationLabel.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				tableScrollPane = new JScrollPane();
				{
					UserController controller = new UserController();
					userTable = new JTable();
					tableScrollPane.setViewportView(userTable);
					userTable.setModel(controller.loadTableModel());
					userTable.getTableHeader().setBounds(0, 0, 306, -13);
					userTable.setToolTipText("Right to view options");
					{
						popupMenu = new JPopupMenu();
						setComponentPopupMenu(userTable, getPopupMenu());
						popupMenu.setSize(30, 50);
						{
							establishPassMenuItem = new JMenuItem();
							popupMenu.add(establishPassMenuItem);
							establishPassMenuItem.setText("Establish Password");
							establishPassMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									establishPassMenuItemActionPerformed(evt);
								}
							});
						}
						{
							deleteUserMenuItem = new JMenuItem();
							popupMenu.add(getDeleteUserMenuItem());
							deleteUserMenuItem.setText("Delete User");
							deleteUserMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									deleteUserMenuItemActionPerformed(evt);
								}
							});
						}
					}
					userTable.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent evt) {
							userTableMousePressed(evt);
						}
					});
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
				btnInsertar.setIcon(new ImageIcon(getClass().getClassLoader().getResource("system/rdf/configure/resource/table_row_insert.png")));
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
				btnDelete.setIcon(new ImageIcon(getClass().getClassLoader().getResource("system/rdf/configure/resource/table_row_delete.png")));
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
				btnModify.setIcon(new ImageIcon(getClass().getClassLoader().getResource("system/rdf/configure/resource/table_refresh.png")));
				btnModify.setIconTextGap(2);
				btnModify.setMargin(new java.awt.Insets(2, 0, 2, 14));
				btnModify.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnModifyActionPerformed(evt);
					}
				});
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
					.add(bannerHeader, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.add(26)
					.add(userInformationLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.add(thisLayout.createParallelGroup()
							.add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
									.add(btnInsertar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.UNRELATED)
									.add(btnModify, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.UNRELATED)
									.add(btnDelete, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.add(63))
									.add(GroupLayout.LEADING, tableScrollPane, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))
									.add(38)
									.add(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.add(16)
									.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
											.add(GroupLayout.BASELINE, btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
											.add(GroupLayout.BASELINE, btnAceptar, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
											.add(GroupLayout.BASELINE, btnApply, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
											.addContainerGap(19, 19));
			thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
					.add(GroupLayout.LEADING, thisLayout.createParallelGroup()
							.add(GroupLayout.LEADING, bannerHeader, 0, 456, Short.MAX_VALUE)
							.add(GroupLayout.LEADING, separator, 0, 456, Short.MAX_VALUE))
							.add(thisLayout.createSequentialGroup()
									.add(15)
									.add(thisLayout.createParallelGroup()
											.add(GroupLayout.LEADING, tableScrollPane, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
											.add(GroupLayout.LEADING, userInformationLabel, 0, 309, Short.MAX_VALUE)
											.add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
													.add(0, 135, Short.MAX_VALUE)
													.add(btnAceptar, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
													.add(24)
													.add(btnApply, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
													.add(23)
													.add(thisLayout.createParallelGroup()
															.add(GroupLayout.LEADING, btnCancel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
															.add(GroupLayout.LEADING, btnInsertar, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
															.add(GroupLayout.LEADING, btnModify, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
															.add(GroupLayout.LEADING, btnDelete, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
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
		UIInsertUser uiInsert = new UIInsertUser();
		uiInsert.setGestor(this);
		uiInsert.setVisible(true);
		uiInsert.setLocationRelativeTo(this);


	}

	private void btnModifyActionPerformed(ActionEvent evt) {
		try{
			String userName = "";
			TableModel model = userTable.getModel();
		
		int selectedRow = userTable.getSelectedRow();
		if (selectedRow >= 0) {
			userName = (String) model.getValueAt(selectedRow,
					0);
		} else {
			throw new Exception("Please, select the user to modify");
		}
		UIModifyUser obj = new UIModifyUser();
        obj.setGestor(this);
        obj.setOldUserName(userName);
        obj.setLocationRelativeTo(null);
        obj.setVisible(true);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void btnDeleteActionPerformed(ActionEvent evt) {

		try {
			TableModel model = userTable.getModel();
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow >= 0) {
				String userName = (String) model.getValueAt(selectedRow,
						0);
				UserController controller= new UserController(
						userName);
				controller.deleteUser();
				refreshUserTable();
			} else {
				JOptionPane.showMessageDialog(null,
						"You must select an user to delete");
			}
		} catch (Exception e) {
			e.printStackTrace();
			GraphEd.errorPane.printMessage(e);
		}

	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void btnApplyActionPerformed(ActionEvent evt){
	
	
	}
	private void btnAceptarActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void userTableMousePressed(MouseEvent evt) {
		
		System.out.println("event="+evt);
	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	/**
	 * Auto-generated method for setting the popup menu for a component
	 */
	private void setComponentPopupMenu(final java.awt.Component parent, final javax.swing.JPopupMenu menu) {
		parent.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent e) {
				if(e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if(e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
		});
	}

	public JMenuItem getEstablishPassMenuItem() {
		return establishPassMenuItem;
	}

	public JMenuItem getDeleteUserMenuItem() {
		return deleteUserMenuItem;
	}

	private void establishPassMenuItemActionPerformed(ActionEvent evt) {
		System.out.println("establishPassMenuItem.actionPerformed, event="+evt);
		//TODO add your code for establishPassMenuItem.actionPerformed
	}

	private void deleteUserMenuItemActionPerformed(ActionEvent evt) {
		System.out.println("deleteUserMenuItem.actionPerformed, event="+evt);
		//TODO add your code for deleteUserMenuItem.actionPerformed
	}
	
	public void refreshUserTable(){
		try{
		UserController controller = new UserController();
		userTable.setModel(controller.loadTableModel());
		}
		catch (SQLException e) {
			GraphEd.errorPane.printMessage(e);
		
	}
		
	}
}
