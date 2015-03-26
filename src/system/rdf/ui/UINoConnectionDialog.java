package system.rdf.ui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

public class UINoConnectionDialog extends javax.swing.JDialog {
	private JLabel infoLabel;
	private JLabel infoConfigure;
	private JButton btnClose;
	private JButton btnConfigure;

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				UINoConnectionDialog inst = new UINoConnectionDialog(frame);
				inst.setVisible(true);
			}
		});
	}
	
	public UINoConnectionDialog(JFrame frame) {
		super(frame);
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				getContentPane().setBackground(new java.awt.Color(210,210,200));
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				this.setResizable(false);
				GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
				this.setName("UINoConnectionDialog");
				this.setVisible(true);
				this.setLocationRelativeTo(null);
				getContentPane().setLayout(thisLayout);
				{
					infoLabel = new JLabel();
					GridBagLayout infoLabelLayout = new GridBagLayout();
					infoLabel.setText("No connection with database");
					infoLabelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					infoLabelLayout.rowHeights = new int[] {7, 7, 7, 7};
					infoLabelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					infoLabelLayout.columnWidths = new int[] {7, 7, 7, 7};
					infoLabel.setLayout(infoLabelLayout);
					infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
					infoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
					infoLabel.setFont(new java.awt.Font("Microsoft Sans Serif",1,11));
					infoLabel.setSize(256, 21);
				}
				{
					btnConfigure = new JButton();
					btnConfigure.setText("Configure");
					btnConfigure.setFocusable(false);
					btnConfigure.setSize(75, 25);
					btnConfigure.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
					btnConfigure.setBackground(new java.awt.Color(210,210,200));
					btnConfigure.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
					btnConfigure.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnConfigureActionPerformed(evt);
						}
					});
				}
				{
					btnClose = new JButton();
					btnClose.setText("Close");
					btnClose.setFocusable(false);
					btnClose.setSize(75, 25);
					btnClose.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
					btnClose.setBackground(new java.awt.Color(210,210,200));
					btnClose.setFont(new java.awt.Font("Microsoft Sans Serif",0,11));
					btnClose.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCloseActionPerformed(evt);
						}
					});
				}
				{
					infoConfigure = new JLabel();
					infoConfigure.setText("To configure the connection choose Configure");
					infoConfigure.setFont(new java.awt.Font("Microsoft Sans Serif",1,11));
					infoConfigure.setHorizontalAlignment(SwingConstants.CENTER);
					infoConfigure.setHorizontalTextPosition(SwingConstants.CENTER);
				}
					thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.add(infoLabel, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.add(infoConfigure, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.RELATED, 0, GroupLayout.PREFERRED_SIZE)
						.add(thisLayout.createParallelGroup(GroupLayout.BASELINE)
						    .add(GroupLayout.BASELINE, btnConfigure, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						    .add(GroupLayout.BASELINE, btnClose, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addContainerGap());
					thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
						.add(GroupLayout.LEADING, infoConfigure, 0, 294, Short.MAX_VALUE)
						.add(thisLayout.createSequentialGroup()
						    .add(36)
						    .add(thisLayout.createParallelGroup()
						        .add(thisLayout.createSequentialGroup()
						            .add(0, 0, Short.MAX_VALUE)
						            .add(infoLabel, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE))
						        .add(GroupLayout.LEADING, thisLayout.createSequentialGroup()
						            .add(0, 21, Short.MAX_VALUE)
						            .add(btnConfigure, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						            .addPreferredGap(LayoutStyle.UNRELATED)
						            .add(btnClose, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
						            .add(60)))
						    .addPreferredGap(LayoutStyle.RELATED, 2, 2)));
			}
			this.setSize(300, 110);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JLabel getInfoLabel() {
		return infoLabel;
	}
	
	public JButton getBtnConfigure() {
		return btnConfigure;
	}
	
	public JButton getBtnClose() {
		return btnClose;
	}
	
	public JLabel getInfoConfigure() {
		return infoConfigure;
	}
	
	private void btnConfigureActionPerformed(ActionEvent evt) {
		UIConfigureConnection frame =  new UIConfigureConnection();
		this.dispose();
	}
	
	private void btnCloseActionPerformed(ActionEvent evt) {
		this.dispose();
	}

}
