package system.rdf.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;

import javax.swing.WindowConstants;
import org.jdesktop.swingx.JXHeader;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;

import system.rdf.graph.MyProblemCustomCell;
import system.rdf.graph.MySolutionEdge;

import javax.swing.SwingUtilities;

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
public class UIAssignCorrectiveAction extends javax.swing.JFrame {
	private JXHeader bannerHeader;
	private JScrollPane correctiveScrollPane;
	private JTextPane correctiveTextPane;
	private JButton btnCancel;
	private JButton btnAdds;
	private JSeparator jSeparator1;
	private JLabel correctiveLabel;

	public DefaultEdge edge;
	public JGraph graph;

	{
		// Gets the system Look and Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIAssignCorrectiveAction inst = new UIAssignCorrectiveAction();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public UIAssignCorrectiveAction() {
		super();
		initGUI();
	}

	public UIAssignCorrectiveAction(DefaultEdge edge) {
		super();
		this.edge = edge;
		initGUI();

	}

	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout(
					(JComponent) getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setBackground(new java.awt.Color(210, 210, 200));
			this.setTitle("Adds Corrective Action");
			{
				bannerHeader = new JXHeader();
				bannerHeader.setTitle("Corrective Action");
				bannerHeader.setDescription("Adds a corrective action");
				bannerHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1,
						0, new java.awt.Color(192, 192, 192)));
			}
			{
				correctiveLabel = new JLabel();
				correctiveLabel.setText("Corrective Action:");
			}
			{
				jSeparator1 = new JSeparator();
			}
			{
				btnAdds = new JButton();
				btnAdds.setText("Add");
				btnAdds.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnAddsActionPerformed(evt);
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
				correctiveScrollPane = new JScrollPane();
				correctiveScrollPane.getHorizontalScrollBar().setEnabled(true);
				{
					correctiveTextPane = new JTextPane();
					try {
						if (edge.getUserObject().toString().contains("<<")) {
							String oldCorrective = ((MySolutionEdge) edge)
									.getCorrectiveAction();
							correctiveTextPane.setText(oldCorrective);
						}
					} catch (Exception e) {

					}
					correctiveScrollPane
							.setViewportView(getCorrectiveTextPane());
					correctiveTextPane.setPreferredSize(new java.awt.Dimension(
							428, 120));
				}
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
					.addComponent(bannerHeader, GroupLayout.PREFERRED_SIZE, 50,
							GroupLayout.PREFERRED_SIZE).addPreferredGap(
							LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(correctiveLabel, GroupLayout.PREFERRED_SIZE,
							GroupLayout.PREFERRED_SIZE,
							GroupLayout.PREFERRED_SIZE).addComponent(
							correctiveScrollPane, 0, 144, Short.MAX_VALUE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE,
							GroupLayout.PREFERRED_SIZE,
							GroupLayout.PREFERRED_SIZE).addGap(17).addGroup(
							thisLayout.createParallelGroup(
									GroupLayout.Alignment.BASELINE)
									.addComponent(btnAdds,
											GroupLayout.Alignment.BASELINE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE)
									.addComponent(btnCancel,
											GroupLayout.Alignment.BASELINE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE,
											GroupLayout.PREFERRED_SIZE))
					.addContainerGap(26, 26));
			thisLayout
					.setHorizontalGroup(thisLayout
							.createParallelGroup()
							.addGroup(
									GroupLayout.Alignment.LEADING,
									thisLayout
											.createParallelGroup()
											.addComponent(
													bannerHeader,
													GroupLayout.Alignment.LEADING,
													0, 566, Short.MAX_VALUE)
											.addComponent(
													jSeparator1,
													GroupLayout.Alignment.LEADING,
													0, 566, Short.MAX_VALUE))
							.addGroup(
									thisLayout
											.createSequentialGroup()
											.addPreferredGap(
													bannerHeader,
													correctiveLabel,
													LayoutStyle.ComponentPlacement.INDENT)
											.addGroup(
													thisLayout
															.createParallelGroup()
															.addGroup(
																	GroupLayout.Alignment.LEADING,
																	thisLayout
																			.createSequentialGroup()
																			.addComponent(
																					correctiveLabel,
																					GroupLayout.PREFERRED_SIZE,
																					120,
																					GroupLayout.PREFERRED_SIZE)
																			.addGap(
																					249)
																			.addComponent(
																					btnAdds,
																					GroupLayout.PREFERRED_SIZE,
																					75,
																					GroupLayout.PREFERRED_SIZE)
																			.addPreferredGap(
																					LayoutStyle.ComponentPlacement.RELATED)
																			.addComponent(
																					btnCancel,
																					GroupLayout.PREFERRED_SIZE,
																					75,
																					GroupLayout.PREFERRED_SIZE)
																			.addGap(
																					18))
															.addComponent(
																	correctiveScrollPane,
																	GroupLayout.Alignment.LEADING,
																	GroupLayout.PREFERRED_SIZE,
																	543,
																	GroupLayout.PREFERRED_SIZE))
											.addContainerGap()));
			pack();
			this.setSize(574, 327);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLocationRelativeTo(Component c) {
		super.setLocationRelativeTo(c);
		graph = (JGraph) c;

	}

	public JButton getBtnAdds() {
		return btnAdds;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JTextPane getCorrectiveTextPane() {
		return correctiveTextPane;
	}

	private void btnCancelActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void btnAddsActionPerformed(ActionEvent evt) {
		/*
		 * String oldCorrective = edge.getUserObject().toString(); String
		 * frequency = oldCorrective.split("<<")[0]; String corrective =
		 * getCorrectiveTextPane().getText(); edge.setUserObject(frequency +
		 * "<<" + corrective + ">>");
		 */
		((MySolutionEdge) edge).assingCorrectiveAction(getCorrectiveTextPane()
				.getText());
		Map attr = ((DefaultEdge) edge).getAttributes();
		graph.getGraphLayoutCache().editCell(edge, attr);

		this.dispose();
	}

}
