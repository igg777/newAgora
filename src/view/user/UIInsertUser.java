package view.user;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXHeader;

import system.rdf.dataBase.ExtractData;
import system.rdf.utils.Encript;
import controller.user.UserController;

public class UIInsertUser extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1704390489643265266L;

	{
	//Set Look & Feel
	try {
	    javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }

    public UIUsersInfomationGestor getGestor() {
		return gestor;
	}

	public void setGestor(UIUsersInfomationGestor gestor) {
		this.gestor = gestor;
	}
	private UIUsersInfomationGestor gestor;
	private JXHeader bannerHeader;
    private JLabel roleLabel;
    private JLabel lblEnterprise;
    private JComboBox jcbenterpice;
    private JButton btnCancel;
    private JButton btnInsert;
    private JSeparator separator;
    private JPasswordField passwordField;
    private JLabel jlblrole;
    private JLabel jlblenterprse;
    private JLabel jlblpasssss;
    private JLabel jlbluserrr;
    private JLabel passLabel;
    private JComboBox roleComboBox;
    private JTextField userTextField;
    private JLabel userLabel;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		UIInsertUser inst = new UIInsertUser();
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);
	    }
	});
    }

    public UIInsertUser() {
	super();
	initGUI();
    }

    private void initGUI() {
	try {
		UserController controller = new UserController();
	    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    getContentPane().setBackground(new java.awt.Color(210,210,200));
	    this.setTitle("Insert User");
	    GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
	    getContentPane().setLayout(thisLayout);
	    this.setResizable(false);
	    this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("system/rdf/configure/resource/user.png")).getImage());
	    {
		bannerHeader = new JXHeader();
		bannerHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(192,192,192)));
		bannerHeader.setTitle("Insert User");
		bannerHeader.setDescription("Insert a system user");
	    }
	    {
		userLabel = new JLabel();
		userLabel.setText("User name:");
		userLabel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
	    }
	    {
		userTextField = new JTextField();
		userTextField.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
		userTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	    }
	    {
		roleLabel = new JLabel();
		roleLabel.setText("User role:");
		roleLabel.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
	    }
	    {

		ComboBoxModel roleComboBoxModel = 
		    new DefaultComboBoxModel(controller.getRoles().toArray());
		roleComboBox = new JComboBox();
		roleComboBox.setModel(roleComboBoxModel);
		roleComboBox.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		roleComboBox.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
		roleComboBox.setBackground(new java.awt.Color(255,255,255));
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
		separator = new JSeparator();
	    }
	    {
	    	jlbluserrr = new JLabel();
	    	jlbluserrr.setText("*");
	    	jlbluserrr.setFont(new java.awt.Font("Tahoma",0,18));
	    	jlbluserrr.setForeground(new java.awt.Color(255,0,128));
	    }
	    {
	    	jlblrole = new JLabel();
	    	jlblrole.setText("*");
	    	jlblrole.setFont(new java.awt.Font("Tahoma",0,18));
	    	jlblrole.setForeground(new java.awt.Color(255,0,128));
	    }
	    {
	    	jlblpasssss = new JLabel();
	    	jlblpasssss.setText("*");
	    	jlblpasssss.setFont(new java.awt.Font("Tahoma",0,18));
	    	jlblpasssss.setForeground(new java.awt.Color(255,0,128));
	    }
	    {
	    	jlblenterprse = new JLabel();
	    	jlblenterprse.setText("*");
	    	jlblenterprse.setFont(new java.awt.Font("Tahoma",0,18));
	    	jlblenterprse.setForeground(new java.awt.Color(255,0,128));
	    }
	    {
	    	lblEnterprise = new JLabel();
	    	lblEnterprise.setText("User Enterprise:");
	    }
	    {
	    	
	    	ComboBoxModel jcbenterpiceModel = 
	    		new DefaultComboBoxModel(controller.getCBModel());
	    	jcbenterpice = new JComboBox();
	    	jcbenterpice.setModel(jcbenterpiceModel);
	    	jcbenterpice.setEnabled(true);
	    }
	    {
		btnInsert = new JButton();
		btnInsert.setText("Insert");
		btnInsert.setBackground(new java.awt.Color(192,192,192));
		btnInsert.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		btnInsert.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
		btnInsert.setFocusable(false);
		btnInsert.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
			btnInsertActionPerformed(evt);
		    }
		});
	    }
	    {
		btnCancel = new JButton();
		btnCancel.setText("Cancel");
		btnCancel.setBackground(new java.awt.Color(192,192,192));
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
	    	.addComponent(bannerHeader, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
	    	.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
	    	.addComponent(userLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	    	    .addComponent(userTextField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
	    	    .addComponent(jlbluserrr, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
	    	.addComponent(roleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	    	    .addComponent(roleComboBox, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	    .addComponent(jlblrole, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
	    	.addComponent(passLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	    	    .addComponent(passwordField, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
	    	    .addComponent(jlblpasssss, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
	    	.addComponent(lblEnterprise, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
	    	.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	    	    .addComponent(jcbenterpice, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	    .addComponent(jlblenterprse, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
	    	.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
	    	.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	    	    .addComponent(btnCancel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	    .addComponent(btnInsert, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addContainerGap(21, 21));
	    thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
	    	.addGroup(thisLayout.createSequentialGroup()
	    	    .addGroup(thisLayout.createParallelGroup()
	    	        .addComponent(bannerHeader, GroupLayout.Alignment.LEADING, 0, 215, Short.MAX_VALUE)
	    	        .addComponent(separator, GroupLayout.Alignment.LEADING, 0, 215, Short.MAX_VALUE))
	    	    .addContainerGap(17, 17))
	    	.addGroup(thisLayout.createSequentialGroup()
	    	    .addPreferredGap(bannerHeader, userLabel, LayoutStyle.ComponentPlacement.INDENT)
	    	    .addGroup(thisLayout.createParallelGroup()
	    	        .addGroup(thisLayout.createSequentialGroup()
	    	            .addGroup(thisLayout.createParallelGroup()
	    	                .addComponent(userTextField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(roleComboBox, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(passwordField, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(jcbenterpice, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE))
	    	            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
	    	            .addGroup(thisLayout.createParallelGroup()
	    	                .addComponent(jlbluserrr, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(jlblrole, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(jlblpasssss, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(jlblenterprse, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
	    	        .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
	    	            .addGroup(thisLayout.createParallelGroup()
	    	                .addComponent(lblEnterprise, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(userLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(roleLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
	    	                .addComponent(passLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
	    	            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, Short.MAX_VALUE)
	    	            .addComponent(btnInsert, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
	    	            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 1, GroupLayout.PREFERRED_SIZE)
	    	            .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
	    	            .addGap(35)))));
	    pack();
	    this.setSize(238, 336);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public JTextField getUserTextField() {
	return userTextField;
    }

    public JComboBox getRoleComboBox() {
	return roleComboBox;
    }

    public JPasswordField getPasswordField() {
	return passwordField;
    }

    public JButton getBtnInsert() {
	return btnInsert;
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
	this.dispose();

    }

    private void btnInsertActionPerformed(ActionEvent evt) {
	String user = getUserTextField().getText();
	ExtractData role = (ExtractData)getRoleComboBox().getSelectedItem();
	ExtractData enterprise = (ExtractData)jcbenterpice.getSelectedItem();
	String pass =getPasswordField().getText();
	try {
		UserController controller = new UserController(user, pass, role, enterprise);
	    controller.inserUser();
	    gestor.refreshUserTable();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	this.dispose();
    }

}
