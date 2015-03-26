package system.rdf.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.swingx.JXHeader;
import javax.swing.SwingUtilities;

import system.rdf.dataBase.PersistentManager;

public class UIConfigureConnection extends javax.swing.JFrame {
	private JLabel jLabel1;
	private JTextField userTextField;
	private JLabel userLabel;
	private JTextField databaseTextField;
	private JLabel databaseLabel;
	private JTextField portTextField;
	private JXHeader bannerHeader;
	private JPasswordField passwordField;
	private JLabel passLabel;
	private JLabel portLabel;
	private JTextField hostTextField;
	private JButton btnCancel;
	private JButton btnAceptar;
	private JSeparator jSeparator1;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIConfigureConnection inst = new UIConfigureConnection();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIConfigureConnection() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setBackground(new java.awt.Color(210,210,200));
			this.setTitle("Configure Connection");
			this.setResizable(false);
			this.setVisible(true);
			this.setLocationRelativeTo(null);
			this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("system/rdf/resources/database_edit.png")).getImage());
			{
				jLabel1 = new JLabel();
				jLabel1.setText("Host:");
				jLabel1.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
				jLabel1.setBackground(new java.awt.Color(22,21,18));
			}
			{
				jSeparator1 = new JSeparator();
				jSeparator1.setOpaque(true);
				jSeparator1.setBorder(BorderFactory.createCompoundBorder(
						null, 
						new LineBorder(new java.awt.Color(192,192,192), 0, true)));
			}
			{
				bannerHeader = new JXHeader();
				bannerHeader.setTitle("Database Configuration");
				bannerHeader.setDescription("Configure dabase connection");
				bannerHeader.setIcon(new ImageIcon(getClass().getClassLoader().getResource("system/rdf/resources/database_status.png")));
			}
			{
				passLabel = new JLabel();
				passLabel.setText("Password:");
				passLabel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
			}
			{
				passwordField = new JPasswordField();
				passwordField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				databaseLabel = new JLabel();
				databaseLabel.setText("Database:");
				databaseLabel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
			}
			{
				databaseTextField = new JTextField();
				databaseTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				userLabel = new JLabel();
				userLabel.setText("User:");
				userLabel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
			}
			{
				userTextField = new JTextField();
				userTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				hostTextField = new JTextField();
				hostTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				portLabel = new JLabel();
				portLabel.setText("Port:");
				portLabel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
			}
			{
				portTextField = new JTextField();
				portTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				btnAceptar = new JButton();
				btnAceptar.setText("Accept");
				btnAceptar.setBackground(new java.awt.Color(210,210,200));
				btnAceptar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				btnAceptar.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
				btnAceptar.setFocusable(false);
				btnAceptar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAceptarActionPerformed(evt);
					}
				});
			}
			{
				btnCancel = new JButton();
				btnCancel.setText("Cancel");
				btnCancel.setBackground(new java.awt.Color(210,210,200));
				btnCancel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				btnCancel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
				btnCancel.setFocusable(false);
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCancelActionPerformed(evt);
					}
				});
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.add(bannerHeader, 0, 51, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
				    .add(GroupLayout.BASELINE, jLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .add(GroupLayout.BASELINE, portLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
				    .add(GroupLayout.BASELINE, hostTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				    .add(GroupLayout.BASELINE, portTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(databaseLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.add(databaseTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(userLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.add(userTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(passLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.add(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.RELATED)
				.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
				    .add(GroupLayout.BASELINE, btnAceptar, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
				    .add(GroupLayout.BASELINE, btnCancel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(24, 24));
			thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
				.add(GroupLayout.LEADING, thisLayout.createParallelGroup()
				    .add(GroupLayout.LEADING, jSeparator1, 0, 294, Short.MAX_VALUE)
				    .add(GroupLayout.LEADING, bannerHeader, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE))
				.add(thisLayout.createSequentialGroup()
				    .addPreferredGap(jSeparator1, hostTextField, LayoutStyle.INDENT)
				    .add(thisLayout.createParallelGroup()
				        .add(GroupLayout.LEADING, hostTextField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
				        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				            .add(thisLayout.createParallelGroup()
				                .add(GroupLayout.LEADING, databaseLabel, 0, 89, Short.MAX_VALUE)
				                .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				                    .add(jLabel1, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
				                    .add(20))
				                .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				                    .add(userLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
				                    .add(55))
				                .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				                    .add(passLabel, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
				                    .add(20)))
				            .add(btnAceptar, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
				        .add(GroupLayout.LEADING, databaseTextField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
				        .add(GroupLayout.LEADING, userTextField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
				        .add(GroupLayout.LEADING, passwordField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
				    .addPreferredGap(LayoutStyle.UNRELATED)
				    .add(thisLayout.createParallelGroup()
				        .add(GroupLayout.LEADING, btnCancel, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
				        .add(GroupLayout.LEADING, portTextField, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
				        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
				            .add(portLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
				            .add(37)))
				    .addContainerGap(39, 39)));
			pack();
			this.setSize(300, 320);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JButton getBtnAceptar() {
		return btnAceptar;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JTextField getHostTextField() {
		return hostTextField;
	}

	public JTextField getPortTextField() {
		return portTextField;
	}

	public JTextField getDatabaseTextField() {
		return databaseTextField;
	}

	public JTextField getUserTextField() {
		return userTextField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void btnAceptarActionPerformed(ActionEvent evt) {
		
		String host = getHostTextField().getText();
	    String port = getPortTextField().getText();
	    String database = getDatabaseTextField().getText();
	    String user = getUserTextField().getText();
	    String pass = getPasswordField().getText();
	
	    PersistentManager.ConfigureConnectionToPostgres(host, port, database, user, pass);
	    this.dispose();
	}

}
