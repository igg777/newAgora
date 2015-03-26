package view.user;

import info.clearthought.layout.TableLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import system.rdf.dataBase.ExtractData;
import system.rdf.ui.GraphEd;
import system.rdf.utils.Encript;
import model.user.User;
import controller.user.AuthenticateUserController;
import java.awt.Window.Type;

public class AuthenticateUser extends javax.swing.JFrame {
	private JLabel jlblUser;
	private JTextField jtfUser;
	private JLabel jlblPass;
	private JButton jbtnCancel;
	private JButton jbtnOk;
	private JPasswordField jftfPass;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AuthenticateUser inst = new AuthenticateUser();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public AuthenticateUser() {
		super();
		setType(Type.UTILITY);
		initGUI();
	}

	private void initGUI() {
		try {
			TableLayout thisLayout = new TableLayout(new double[][] {
					{ TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
							3.0, 6.0, TableLayout.FILL, 3.0, TableLayout.FILL,
							TableLayout.FILL },
					{ TableLayout.FILL, TableLayout.FILL, 6.0,
							TableLayout.FILL, TableLayout.FILL, 6.0,
							TableLayout.FILL, TableLayout.FILL,
							TableLayout.FILL, 6.0, TableLayout.FILL } });
			thisLayout.setHGap(5);
			thisLayout.setVGap(5);
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jlblUser = new JLabel();
				getContentPane().add(jlblUser, "1, 1, 2, 2");
				jlblUser.setText("User:");
			}
			{
				jtfUser = new JTextField();
				getContentPane().add(jtfUser, "3, 1, 7, 2");
			}
			{
				jlblPass = new JLabel();
				getContentPane().add(jlblPass, "1, 4, 2, 5");
				jlblPass.setText("Password:");
			}
			{
				jftfPass = new JPasswordField();
				getContentPane().add(jftfPass, "3, 4, 7, 5");
			}
			{
				jbtnOk = new JButton();
				jbtnOk.setFocusCycleRoot(true);
				getContentPane().add(jbtnOk, "1, 8, 3, 9");
				jbtnOk.setText("Ok");
				jbtnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnOkActionPerformed(evt);
					}
				});
			}
			{
				jbtnCancel = new JButton();
				getContentPane().add(jbtnCancel, "5, 8, 7, 9");
				jbtnCancel.setText("Cancel");
				jbtnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jbtnCancelActionPerformed(evt);
					}
				});
			}
			pack();
			this.setSize(288, 215);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbtnOkActionPerformed(ActionEvent evt) {
		try {
			AuthenticateUserController controller = new AuthenticateUserController();
			User user = controller.seekUserInDB(jtfUser.getText(), Encript
					.encript(jftfPass.getText()));
			if (user == null) {

				JOptionPane.showMessageDialog(null,
						"Please, check the user and password");
			} else {

				GraphEd ed = new GraphEd(user);
				this.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void jbtnCancelActionPerformed(ActionEvent evt) {
		System.exit(0);
	}

}
