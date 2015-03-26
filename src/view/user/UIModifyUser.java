package view.user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import org.jdesktop.swingx.JXHeader;

import system.rdf.dataBase.ExtractData;
import system.rdf.utils.Encript;
import controller.user.UserController;



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
public class UIModifyUser extends javax.swing.JFrame {

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
	private String oldUserName;
	private JLabel jlblenterprisesss;
	private JLabel jlblpassss;
	private JLabel jlblrole;
	private JLabel jlbluserrr;

	private UIUsersInfomationGestor gestor;
	private JXHeader bannerHeader;
    private JLabel roleLabel;
    private JLabel lblEnterprise;
    private JComboBox jcbenterpice;
    private JButton btnCancel;
    private JButton btnModify;
    private JSeparator separator;
    private JPasswordField passwordField;
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
		UIModifyUser inst = new UIModifyUser();
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);
	    }
	});
    }

    public UIModifyUser() {
	super();
	initGUI();
    }

    private void initGUI() {
	try {
		UserController controller = new UserController();
	    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    getContentPane().setBackground(new java.awt.Color(210,210,200));
	    this.setTitle("Modify User");
	    GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
	    getContentPane().setLayout(thisLayout);
	    this.setResizable(false);
	    this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("system/rdf/configure/resource/user.png")).getImage());
	    {
		bannerHeader = new JXHeader();
		bannerHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(192,192,192)));
		bannerHeader.setTitle("Modify User");
		bannerHeader.setDescription("Modify a system user");
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
	    	jlblpassss = new JLabel();
	    	jlblpassss.setText("*");
	    	jlblpassss.setFont(new java.awt.Font("Tahoma",0,18));
	    	jlblpassss.setForeground(new java.awt.Color(255,0,128));
	    }
	    {
	    	jlblenterprisesss = new JLabel();
	    	jlblenterprisesss.setText("*");
	    	jlblenterprisesss.setFont(new java.awt.Font("Tahoma",0,18));
	    	jlblenterprisesss.setForeground(new java.awt.Color(255,0,128));
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
		btnModify = new JButton();
		btnModify.setText("Modify");
		btnModify.setBackground(new java.awt.Color(192,192,192));
		btnModify.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		btnModify.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
		btnModify.setFocusable(false);
		btnModify.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
			btnModifyActionPerformed(evt);
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
	    	.add(bannerHeader, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
	    	.addPreferredGap(LayoutStyle.UNRELATED)
	    	.add(userLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
	    	    .add(GroupLayout.BASELINE, userTextField, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
	    	    .add(GroupLayout.BASELINE, jlbluserrr, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addPreferredGap(LayoutStyle.UNRELATED)
	    	.add(roleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
	    	    .add(GroupLayout.BASELINE, roleComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	    .add(GroupLayout.BASELINE, jlblrole, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addPreferredGap(LayoutStyle.UNRELATED)
	    	.add(passLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
	    	    .add(GroupLayout.BASELINE, passwordField, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
	    	    .add(GroupLayout.BASELINE, jlblpassss, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addPreferredGap(LayoutStyle.UNRELATED)
	    	.add(lblEnterprise, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.addPreferredGap(LayoutStyle.RELATED)
	    	.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
	    	    .add(GroupLayout.BASELINE, jcbenterpice, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	    .add(GroupLayout.BASELINE, jlblenterprisesss, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
	    	.addPreferredGap(LayoutStyle.UNRELATED)
	    	.add(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	.addPreferredGap(LayoutStyle.UNRELATED)
	    	.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
	    	    .add(GroupLayout.BASELINE, btnCancel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
	    	    .add(GroupLayout.BASELINE, btnModify, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
	    	.addContainerGap(18, 18));
	    thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
	    	.add(thisLayout.createSequentialGroup()
	    	    .add(thisLayout.createParallelGroup()
	    	        .add(GroupLayout.LEADING, bannerHeader, 0, 215, Short.MAX_VALUE)
	    	        .add(GroupLayout.LEADING, separator, 0, 215, Short.MAX_VALUE))
	    	    .addContainerGap(17, 17))
	    	.add(thisLayout.createSequentialGroup()
	    	    .addPreferredGap(bannerHeader, userLabel, LayoutStyle.INDENT)
	    	    .add(thisLayout.createParallelGroup()
	    	        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
	    	            .add(thisLayout.createParallelGroup()
	    	                .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
	    	                    .add(thisLayout.createParallelGroup()
	    	                        .add(GroupLayout.LEADING, userLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
	    	                        .add(GroupLayout.LEADING, roleLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
	    	                        .add(GroupLayout.LEADING, passLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
	    	                    .add(btnModify, 0, 65, Short.MAX_VALUE))
	    	                .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
	    	                    .add(lblEnterprise, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
	    	                    .add(62)))
	    	            .addPreferredGap(LayoutStyle.UNRELATED)
	    	            .add(btnCancel, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
	    	            .add(29))
	    	        .add(thisLayout.createSequentialGroup()
	    	            .add(thisLayout.createParallelGroup()
	    	                .add(GroupLayout.LEADING, userTextField, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
	    	                .add(GroupLayout.LEADING, roleComboBox, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
	    	                .add(GroupLayout.LEADING, passwordField, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
	    	                .add(GroupLayout.LEADING, jcbenterpice, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE))
	    	            .addPreferredGap(LayoutStyle.RELATED)
	    	            .add(thisLayout.createParallelGroup()
	    	                .add(GroupLayout.LEADING, jlbluserrr, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
	    	                .add(GroupLayout.LEADING, jlblrole, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
	    	                .add(GroupLayout.LEADING, jlblpassss, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
	    	                .add(GroupLayout.LEADING, jlblenterprisesss, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))))));
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

    public JButton getBtnModify() {
	return btnModify;
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
	this.dispose();

    }

    private void btnModifyActionPerformed(ActionEvent evt) {
	String user = getUserTextField().getText();
	ExtractData role = (ExtractData)getRoleComboBox().getSelectedItem();
	ExtractData enterprise = (ExtractData)jcbenterpice.getSelectedItem();
	String pass = Encript.encript(getPasswordField().getText());
	try {
		UserController controller = new UserController(user, pass, role, enterprise);
	    controller.updateUser(oldUserName);
	    gestor.refreshUserTable();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	this.dispose();
    }

    public String getOldUserName() {
		return oldUserName;
	}

	public void setOldUserName(String oldUserName) {
		this.oldUserName = oldUserName;
	}
}
