package system.rdf.ui;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class StartPanel extends javax.swing.JPanel {
	private JLabel welcomenText;
	private JButton btnConfigureSystem;
	private JMenuItem jMenuItem1;

	/**
	 * Auto-generated main method to display this 
	 * JPanel inside a new JFrame.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new StartPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public StartPanel() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			this.setBackground(new java.awt.Color(200,200,200));
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), 10dlu, 197dlu, 10dlu, 5dlu", 
			"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
			this.setLayout(thisLayout);
			this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			this.setFocusable(false);
			this.setFont(new java.awt.Font("Arial",0,12));
			this.setVisible(true);
			
			{
				welcomenText = new JLabel();
				this.add(welcomenText, new CellConstraints("3, 2, 1, 1, default, default"));
				welcomenText.setText("Welcomen to ...");
				welcomenText.setFont(new java.awt.Font("Arial",0,24));
				welcomenText.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				btnConfigureSystem = new JButton();
				FormLayout btnConfigureSystemLayout = new FormLayout(
						"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)", 
				"max(p;5dlu), max(p;5dlu), max(p;5dlu), max(p;5dlu)");
				btnConfigureSystem.setLayout(btnConfigureSystemLayout);
				this.add(getBtnConfigureSystem(), new CellConstraints("3, 4, 1, 1, default, default"));
				btnConfigureSystem.setText("Formulated new Decisional Problem");
				btnConfigureSystem.setFont(new java.awt.Font("Arial",1,12));
				btnConfigureSystem.setBorder(new LineBorder(new java.awt.Color(200,200,200), 1, false));
				btnConfigureSystem.setFocusable(false);
				btnConfigureSystem.setBackground(new java.awt.Color(200,200,200));
				btnConfigureSystem.setOpaque(false);
				btnConfigureSystem.setCursor(new Cursor(12));
				btnConfigureSystem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnConfigureSystemActionPerformed(evt);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JButton getBtnConfigureSystem() {
		return btnConfigureSystem;
	}

	private void btnConfigureSystemActionPerformed(ActionEvent evt) {
       this.setVisible(false);
	}

	private JMenuItem getJMenuItem1() {
		if(jMenuItem1 == null) {
			jMenuItem1 = new JMenuItem();
			jMenuItem1.setText("jMenuItem1");
		}
		return jMenuItem1;
	}

}
