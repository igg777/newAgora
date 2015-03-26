package controller.enterprise;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import model.enterprise.Enterprise;


import system.rdf.dataBase.PersistentManager;
import system.rdf.ui.GraphEd;

/**
 * 
 * @author Leo
 *this class handler all the actions whit the enterprises
 */
public class EnterpriseController {
Enterprise enterprise;

/**
 * empty constructor
 */
public EnterpriseController(){
	enterprise = new Enterprise();
}

/**
 * constructor whit only the enterprise name
 * @param enterpriseName
 */
public EnterpriseController(String enterpriseName){
	enterprise = new Enterprise(enterpriseName);
}
/**
 * constructor using all fields of enterprise
 * @param enterpriseName
 * @param enterpriseDesc
 * @param enterpriseEmail
 */
public EnterpriseController (String enterpriseName, String enterpriseDesc, String enterpriseEmail){
	try{
	enterprise = new Enterprise(enterpriseName, enterpriseDesc, enterpriseEmail);
	}
	catch (Exception e) {
		GraphEd.errorPane.printMessage(e);
	}
}

/**
 * this method select all the names of the enterprises in the BD for fill the table in the CRUD view
 * @return the table model whit all the entrprise's name
 */
public DefaultTableModel loadTableModel()throws Exception{
	String [] columns = {"Enterprise name"};
	String [][] rows;
	ArrayList<String> names = new ArrayList<String>();
	ResultSet rs = null;
	if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
		try {
			try {
				rs = PersistentManager.getConnectionToPostgreSQL().select("enterprise");
			} catch (NullPointerException e) {
				GraphEd.errorPane
						.printMessage(new Exception(
								"The file to configure the database is missing !!!"));
			}
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	} else if (GraphEd.errorPane.getText().equals(
			"The file to configure the database is missing !!!") == false)
		GraphEd.errorPane.printMessage(new Exception(
				"No database connection found!!"));
	rows = new String[names.size()][1];
	for (int i = 0; i < names.size(); i++) {
		rows[i][0] = names.get(i);
	}
	DefaultTableModel model = new DefaultTableModel(rows, columns);
	return model;
}

/**
 * insert the enterprise on the DB
 */
public void insertEnterprise()throws Exception{
	try {
		enterprise.insertInDB();
	} catch (Exception e) {
		throw new Exception(e.getMessage());
	}
}

/**
 * delete the enterprise from DB
 */
public void deleteEnterprise()throws SQLException{
	try {
		enterprise.deleteFromDB();
	} catch (SQLException e) {
		throw new SQLException(e.getMessage());
	}
}

public void updateEnterprise(String oldEnterpriseName)throws Exception{
	try {
		enterprise.updateInDB(oldEnterpriseName);
	} catch (Exception e) {
		throw new Exception(e.getMessage());
	}
}









}

