package system.rdf.utils;

import javax.swing.BorderFactory;
import javax.swing.JMenu;

public class MenuFactory {

	public JMenu dataMenu = null;

	public JMenu classfyMenu = null;

	public JMenu systemMenu = null;

	public JMenu helpMenu = null;

	public JMenu saveProblem = null;


	public JMenu getDataMenu() {

		if (dataMenu == null) {
			dataMenu = new JMenu();
			dataMenu.setText("Data");
			dataMenu.setBorder(BorderFactory.createEmptyBorder());
		}
		return dataMenu;
	}

	public JMenu getClassfyMenu() {
		classfyMenu = new JMenu();
		classfyMenu.setText("Classifications");
		classfyMenu.setBorder(BorderFactory.createEmptyBorder());

		return classfyMenu;
	}

	public JMenu getSystemMenu() {

		systemMenu = new JMenu();
		systemMenu.setText("System");
		systemMenu.setBorder(BorderFactory.createEmptyBorder());

		return systemMenu;
	}
	
	public JMenu getHelpMenu() {

		helpMenu = new JMenu();
		helpMenu.setText("Help");
		helpMenu.setBorder(BorderFactory.createEmptyBorder());

		return helpMenu;
	}
	
	public JMenu getSaveProblem() {
		return saveProblem;
	}









}
