package system.rdf.configure;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXHeader;

import system.rdf.ui.UIConfigureConnection;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public class UIConfigureSystem extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel dataBasePanel;
	private JLabel gestor;
	private JButton jButton1;
	private JButton btnCancel;
	private JSeparator jSeparator;
	private JButton configureDatabase;
	private JXHeader bannerHeader;
	private JComboBox dataBaseGestors;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIConfigureSystem inst = new UIConfigureSystem();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIConfigureSystem() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Configure System");
			{
				dataBasePanel = new JPanel();
				GroupLayout dataBasePanelLayout = new GroupLayout((JComponent)dataBasePanel);
				dataBasePanel.setLayout(dataBasePanelLayout);
				dataBasePanel.setSize(100, 100);
				dataBasePanel.setBackground(new java.awt.Color(210,210,200));
				dataBasePanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(new java.awt.Color(192,192,192), 1, false), "Data Base Settings", TitledBorder.LEADING, TitledBorder.TOP, new java.awt.Font("Tahoma",0,11), new java.awt.Color(0,0,0)));
				{
					gestor = new JLabel();
					gestor.setText("Data Base Gestor:");
				}
				{
					ComboBoxModel dataBaseGestorsModel = 
						new DefaultComboBoxModel(
								new String[] { "PostgreSQL", "MySQL" });
					dataBaseGestors = new JComboBox();
					dataBaseGestors.setModel(dataBaseGestorsModel);
					dataBaseGestors.setBackground(new java.awt.Color(255,255,255));
				}
				{
					configureDatabase = new JButton();
					configureDatabase.setText("Configure Database");
					configureDatabase.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							configureDatabaseActionPerformed(evt);
						}
					});
				}
				dataBasePanelLayout.setHorizontalGroup(dataBasePanelLayout.createSequentialGroup()
						.addComponent(gestor, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(dataBasePanelLayout.createParallelGroup()
								.addComponent(dataBaseGestors, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
								.addComponent(configureDatabase, GroupLayout.Alignment.LEADING, 0, 132, Short.MAX_VALUE))
								.addContainerGap());
				dataBasePanelLayout.setVerticalGroup(dataBasePanelLayout.createSequentialGroup()
						.addGap(7)
						.addGroup(dataBasePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(dataBaseGestors, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(gestor, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(0, 25, Short.MAX_VALUE)
								.addComponent(configureDatabase, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addContainerGap());
			}
			{
				jButton1 = new JButton();
				jButton1.setText("Accept");
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}
			{
				jSeparator = new JSeparator();
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
				bannerHeader = new JXHeader();
				bannerHeader.setTitle("System Configuration");
				bannerHeader.setDescription("Configure the system");
				bannerHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(192,192,192)));
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
					.addComponent(bannerHeader, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(dataBasePanel, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
					.addGap(65)
					.addComponent(jSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(jButton1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(getBtnCancel(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(21, 21));
			thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
					.addGroup(GroupLayout.Alignment.LEADING, thisLayout.createParallelGroup()
							.addComponent(bannerHeader, GroupLayout.Alignment.LEADING, 0, 292, Short.MAX_VALUE)
							.addComponent(jSeparator, GroupLayout.Alignment.LEADING, 0, 292, Short.MAX_VALUE))
							.addGroup(thisLayout.createSequentialGroup()
									.addGap(20)
									.addGroup(thisLayout.createParallelGroup()
											.addComponent(dataBasePanel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
											.addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
													.addGap(88)
													.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
													.addGap(20)
													.addComponent(getBtnCancel(), GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
													.addContainerGap(22, 22)));
			pack();
			this.setSize(300, 322);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			getContentPane().setBackground(new java.awt.Color(210,210,200));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public JComboBox getDataBaseGestors() {
		return dataBaseGestors;
	}

	public JButton getConfigureDatabase() {
		return configureDatabase;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();

	}

	private void jButton1ActionPerformed(ActionEvent evt) {
		this.dispose();

	}

	private void configureDatabaseActionPerformed(ActionEvent evt) {
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIConfigureConnection con = new UIConfigureConnection();
						con.setVisible(true);
						con.setLocation(150, 150);
					}
				});
			}
		}).start();
	}

}
