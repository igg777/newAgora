package system.rdf.ui;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.swingx.JXHyperlink;

import system.rdf.configure.AuthenticatedUser;
import system.rdf.configure.SessionManager;
import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.utils.Encript;

public class UILoginDialog extends javax.swing.JDialog {

	{
		//Gets the system Look and Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JTextField userTextField;
	private JLabel userLabel;
	private JPasswordField passwordField;
	private JLabel passLabel;

	private GraphEd app;
	private JXHyperlink link;
	private JLabel info;
	private JButton btnCancel;
	private JButton btnEnter;
	/**
	 * Auto-generated main method to display this JDialog
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				UILoginDialog inst = new UILoginDialog(frame);
				inst.setVisible(true);
			}
		});
	}

	public UILoginDialog(JFrame frame) {
		super(frame);
		initGUI();
		//GraphEd.splash.dispose();
	}

	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			this.setTitle("Authenticated Dialog");
			getContentPane().setBackground(new java.awt.Color(210,210,200));
			this.setResizable(false);
			{
				userTextField = new JTextField();
				userTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				userTextField.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
			}
			{
				userLabel = new JLabel();
				userLabel.setText("User:");
				userLabel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
			}
			{
				btnEnter = new JButton();
				btnEnter.setText("Enter");
				btnEnter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnEnterActionPerformed(evt);
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
				info = new JLabel();
				info.setText("User or Password no valid!!!");
				info.setVisible(false);
			}
			{
				link = new JXHyperlink();
				link.setText("Retry");
				link.setVisible(false);
				link.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						linkActionPerformed(evt);
					}
				});
			}
			{
				passwordField = new JPasswordField();
				passwordField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				passLabel = new JLabel();
				passLabel.setText("Password:");
				passLabel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
					.addContainerGap(24, 24)
					.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
							.add(GroupLayout.BASELINE, userTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							.add(GroupLayout.BASELINE, userLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.RELATED)
							.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
									.add(GroupLayout.BASELINE, passwordField, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
									.add(GroupLayout.BASELINE, passLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.RELATED)
									.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
											.add(GroupLayout.BASELINE, info, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
											.add(GroupLayout.BASELINE, getLink(), GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(LayoutStyle.RELATED)
											.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
													.add(GroupLayout.BASELINE, btnEnter, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
													.add(GroupLayout.BASELINE, btnCancel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
													.addContainerGap(16, 16));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
					.addContainerGap()
					.add(thisLayout.createParallelGroup()
							.add(GroupLayout.LEADING, passLabel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
							.add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
									.add(userLabel, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
									.add(13)))
									.addPreferredGap(LayoutStyle.RELATED)
									.add(thisLayout.createParallelGroup()
											.add(GroupLayout.LEADING, passwordField, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
											.add(GroupLayout.LEADING, userTextField, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
											.add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
													.add(info, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
													.addPreferredGap(LayoutStyle.RELATED)
													.add(getLink(), GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
													.add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
															.add(0, 24, Short.MAX_VALUE)
															.add(btnEnter, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
															.addPreferredGap(LayoutStyle.UNRELATED)
															.add(btnCancel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
															.addContainerGap(50, 50));
			this.setSize(302, 159);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void setLocationRelativeTo(Component c) {
		super.setLocationRelativeTo(c);
		this.app = (GraphEd) c;
	}


	public JTextField getUserTextField() {
		return userTextField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
		if(app != null)
			app.exit();
	}

	private void btnEnterActionPerformed(ActionEvent evt) {
		String user = getUserTextField().getText();
		String pass = getPasswordField().getText();
		String role;
		ResultSet rsRole;
		AuthenticatedUser authUser = new AuthenticatedUser(user,pass);
		ConnectionToPostgres connection = PersistentManager.getConnectionToPostgreSQL();
		String condicion = "user_name = '"+user+"'";
		ResultSet rsUser;
		String dbPass = null; 

		try {
			boolean flag = connection.tryConnection();
			if(!flag){
				UINoConnectionToEnter inst = new UINoConnectionToEnter();
				this.dispose();
				inst.setVisible(true);
				if (app != null) {
					inst.setLocationRelativeTo(app);
				}

			}
			connection.connect();
			rsUser = connection.select("system_users", condicion);

			while(rsUser.next()){
				String tmp = "'" + rsUser.getString("id_user_role") + "'";
				String con = "role=" + tmp;
				dbPass = rsUser.getString("password");

				rsRole = connection.select("users_role",con);
				rsRole.next();
				role = rsRole.getString("role");
				authUser.setUserRole(tmp);

				//SessionManager.getSession().setAuthUser(authUser);
				this.setVisible(false);
				this.dispose();
				//If the user does not have the authorization
				if(authUser.getUserRole().equals("Analist"))
					app.mnSystem.setEnabled(false);
				app.setVisible(true);
				authUser.setValid(true);
			}
		} 

		catch (SQLException e) {
			e.printStackTrace();
		}	
		catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}

	}

	public JButton getBtnEnter() {
		return btnEnter;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JLabel getInfo() {
		return info;
	}

	public JXHyperlink getLink() {
		return link;
	}

	private void linkActionPerformed(ActionEvent evt) {
		getUserTextField().setText("");
		getPasswordField().setText("");
		getInfo().setVisible(false);
		getLink().setVisible(false);
	}

}
