package system.rdf.ui;

import javax.swing.JTextPane;

import system.rdf.handlers.ExceptionHandler;
import system.rdf.listeners.PopupListener;

public class ErrorPane extends JTextPane {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	protected ExceptionHandler exceptionHandler = new ExceptionHandler();

	public ErrorPane() {
		initGUI();
	}

	private void initGUI() {
		try {
			{
				this.setPreferredSize(new java.awt.Dimension(413, 106));
				this.setFont(new java.awt.Font("Calibri", 0, 12));
				this.setSelectedTextColor(new java.awt.Color(130, 0, 0));
				this.setEditable(false);
				this.addMouseListener(new PopupListener(this));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Clear the error pane
	 */
	public void clear() {
		this.setText("");
	}

	/**
	 * Display the error message
	 * 
	 * @param e
	 */
	public void printMessage(Exception e) {
		// String text = this.getText();
//		e.printStackTrace();
		String msg = exceptionHandler.error(e);
		if(msg.equals(""))
			msg = "Error!!!";
		this.setText(msg);
		GraphEd.manageTabbedPane.setSelectedIndex(0);

	}

}
