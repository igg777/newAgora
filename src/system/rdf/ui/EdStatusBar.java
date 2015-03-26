package system.rdf.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import system.rdf.ui.StatusBarGraphListener;

public class EdStatusBar extends StatusBarGraphListener {
	protected JLabel leftSideStatus;

	protected JLabel rightSideStatus;

	protected JLabel centerSideStatus;

	public EdStatusBar() {
		super();
		// Add this as graph model change listener
		setLayout(new BorderLayout());
		leftSideStatus = new JLabel("Ágora v 1.0");
		centerSideStatus = new JLabel("");
		rightSideStatus = new JLabel("0/0Mb");
		leftSideStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		rightSideStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		add(rightSideStatus, BorderLayout.EAST);
		add(centerSideStatus, BorderLayout.CENTER);
		add(leftSideStatus, BorderLayout.WEST);

	}

	protected void updateCenterStatusBar(String nodeSelected) {
		centerSideStatus.setText(nodeSelected);
	}

	/**
	 * @return Returns the leftSideStatus.
	 */
	public JLabel getLeftSideStatus() {
		return leftSideStatus;
	}

	/**
	 * @param leftSideStatus
	 *            The leftSideStatus to set.
	 */
	public void setLeftSideStatus(JLabel leftSideStatus) {
		this.leftSideStatus = leftSideStatus;
	}

	/**
	 * @return Returns the rightSideStatus.
	 */
	public JLabel getRightSideStatus() {
		return rightSideStatus;
	}

	public void setTextCenterSideStatus(String msg) {
		this.centerSideStatus.setText(msg);
	}

	/**
	 * @param rightSideStatus
	 *            The rightSideStatus to set.
	 */
	public void setRightSideStatus(JLabel rightSideStatus) {
		this.rightSideStatus = rightSideStatus;
	}
}