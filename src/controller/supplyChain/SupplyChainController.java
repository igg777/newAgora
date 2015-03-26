package controller.supplyChain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;

import model.supplyChain.SupplyChain;

import system.rdf.dataBase.ExtractData;
import system.rdf.dataBase.PersistentManager;
import system.rdf.ui.GraphEd;

/**
 * 
 * @author Leo this class handler all the actions whit the enterprises
 */
public class SupplyChainController {
	SupplyChain supplyChain;

	/**
	 * empty constructor
	 */
	public SupplyChainController() {
		supplyChain = new SupplyChain();
	}

	/**
	 * constructor whit only the Supply Chain name
	 * 
	 * @param enterpriseName
	 */
	public SupplyChainController(String supplyChainName) {
		supplyChain = new SupplyChain(supplyChainName);
	}

	/**
	 * constructor using all fields of SupplyChain
	 * 
	 * @param supplyChainName
	 * @param supplyChainComments
	 */
	public SupplyChainController(String supplyChainName,
			String supplyChainComments) {
		try {
			supplyChain = new SupplyChain(supplyChainName, supplyChainComments);
		} catch (Exception e) {
			GraphEd.errorPane.printMessage(e);
		}
	}

	/**
	 * this method select all the names of the supply chain in the BD for fill
	 * the table in the CRUD view
	 * 
	 * @return the table model whit all the supply chain names
	 */
	public DefaultTableModel loadTableModelForCRUD() throws Exception {
		String[] columns = { "Supply Chain name" };
		String[][] rows;
		ArrayList<String> names = new ArrayList<String>();
		ResultSet rs = null;
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			try {
				try {
					rs = PersistentManager.getConnectionToPostgreSQL().select(
							"supply_chain");
					while (rs.next()) {
						names.add(rs.getString("name"));
					}
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
	 * this method extract all the enterprises from the DB an make a
	 * <code> DefaultComboBoxModel </code> whit them for the list in the insert
	 * view.
	 * 
	 * @return <code>DefaultComboBoxModel</code> the model for a list in the
	 *         insert view that have the enterprises in system
	 * @throws SQLException
	 */
	public DefaultComboBoxModel loadlistModelAllEnterprises()
			throws SQLException {
		Object[] rows;
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> id = new ArrayList<Integer>();
		try {
			ResultSet rs = PersistentManager.getConnectionToPostgreSQL()
					.select("enterprise");
			while (rs.next()) {
				names.add(rs.getString("name"));
				id.add(rs.getInt("id_enterprise"));
			}
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
		rows = new Object[names.size()];
		for (int i = 0; i < names.size(); i++) {
			rows[i] = new ExtractData(id.get(i), names.get(i));
		}
		DefaultComboBoxModel model = new DefaultComboBoxModel(rows);
		return model;
	}

	/**
	 * this method extract all the enterprises from the DB that aren't in the
	 * current supply chain an make a <code> DefaultComboBoxModel </code> whit
	 * them for the list in the modify view
	 * 
	 * @return <code>DefaultComboBoxModel</code> the model for a list in the
	 *         modify view that have the enterprises in system
	 * @throws SQLException
	 */
	public DefaultComboBoxModel loadlistModelAllEnterprisesForModify(
			ArrayList<ExtractData> currentEnterprises) throws SQLException {
		Object[] rows;
		ResultSet rs;
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> id = new ArrayList<Integer>();
		try {
			if (currentEnterprises.size() > 0) {
				String conditions = "name != '"
						+ currentEnterprises.get(0).getName() + "'";
				for (int i = 1; i < currentEnterprises.size(); i++) {
					conditions += " and name != '"
							+ currentEnterprises.get(i).getName() + "'";

				}
				rs = PersistentManager.getConnectionToPostgreSQL().select(
						"enterprise", conditions);
			} else {
				rs = PersistentManager.getConnectionToPostgreSQL().select(
						"enterprise");
			}
			while (rs.next()) {
				names.add(rs.getString("name"));
				id.add(rs.getInt("id_enterprise"));
			}
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
		rows = new Object[names.size()];
		for (int i = 0; i < names.size(); i++) {
			rows[i] = new ExtractData(id.get(i), names.get(i));
		}
		DefaultComboBoxModel model = new DefaultComboBoxModel(rows);
		return model;
	}

	/**
	 * make an empty <code>DefaultComboBoxModel</code>
	 * 
	 * @return <code>DefaultComboBoxModel</code> the model for a list in the
	 *         insert view that will store the supply chain enterprises
	 * @throws SQLException
	 */
	public DefaultComboBoxModel loadListModelEnterprisesForSC()
			throws SQLException {
		Object[] rows;

		rows = new Object[0];
		DefaultComboBoxModel model = new DefaultComboBoxModel(rows);
		return model;
	}

	/**
	 * seek in the DataBase for the enterprises that belongs to the supply chain
	 * and insert them in a <code>DefaultComboBoxModel</code>, then seek make
	 * another <code>DefaultComboBoxModel</code> whit the rest of the
	 * enterprises
	 * 
	 * @return <code>DefaultComboBoxModel[]</code> in the first position the
	 *         model for the rest of the enterprises, and in the other position
	 *         the model of the current enterprises
	 * @throws Exception
	 */
	public DefaultComboBoxModel[] getModelsForModifyInterface()
			throws Exception {
		DefaultComboBoxModel[] toReturn = new DefaultComboBoxModel[2];
		try {
			int id = supplyChain.getIdInDB();

			ArrayList<ExtractData> currentEnterprises = new ArrayList<ExtractData>();
			String sql = "select enterprise.name, enterprise.id_enterprise  from enterprise, supply_chain, supply_chain_enterprise where supply_chain.id_supply_chain = '"
					+ id
					+ "'"
					+ "and supply_chain_enterprise.id_enterprise = enterprise.id_enterprise "
					+ "and supply_chain_enterprise.id_supply_chain = supply_chain.id_supply_chain ";
			ResultSet rs = PersistentManager.getConnectionToPostgreSQL()
					.executeSql(sql);
			while (rs.next()) {
				currentEnterprises.add(new ExtractData(rs
						.getInt("id_enterprise"), rs.getString("name")));
			}
			DefaultComboBoxModel enterprise = loadlistModelAllEnterprises();
			DefaultComboBoxModel scenterprise = loadListModelEnterprisesForSC();
			enterprise = loadlistModelAllEnterprisesForModify(currentEnterprises);
			scenterprise = addDataToModel(currentEnterprises, scenterprise);
			toReturn[0] = enterprise;
			toReturn[1] = scenterprise;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return toReturn;
	}

	/**
	 * insert the supply chain on the DB
	 */
	public void insertSupplyChain() throws Exception {
		try {
			supplyChain.insertInDB();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * delete the enterprise from DB
	 */
	public void deleteSupplyChain() throws SQLException {
		try {
			supplyChain.deleteFromDB();
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * modify the supply chain
	 * 
	 * @param oldSupplyChainName
	 *            the name of the supply chain to modify
	 * @throws Exception
	 */
	public void updateSupplyChain(String oldSupplyChainName) throws Exception {
		try {
			supplyChain.updateInDB(oldSupplyChainName);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * add an enterprise to this supply chain
	 * 
	 * @param id_enterprise
	 *            the id of the enterprise to modify
	 * @throws SQLException
	 */
	public void addEnterprice(int id_enterprise) throws SQLException {
		supplyChain.addEnterprice(id_enterprise);
	}

	/**
	 * get a <code>ListModel</code> and insert additional data
	 * 
	 * @param newData
	 *            , the data to add to the <code>ListModel</code>
	 * @param oldModel
	 *            , the <code>ListModel</code> that have the current information
	 * @return <code>DefaultComboBoxModel</code> the model that have all the
	 *         data
	 */
	public DefaultComboBoxModel addDataToModel(ArrayList<ExtractData> newData,
			ListModel oldModel) {
		for (int i = 0; i < oldModel.getSize(); i++) {
			newData.add((ExtractData) oldModel.getElementAt(i));
		}
		DefaultComboBoxModel modelToReturn = new DefaultComboBoxModel(
				newData.toArray());
		return modelToReturn;
	}

	/**
	 * get a <code>ListModel</code> and delete data from it
	 * 
	 * @param dataToRemove
	 *            , the data to remove from the <code>ListModel</code>
	 * @param oldModel
	 *            , the <code>ListModel</code> that have the current information
	 * @return <code>DefaultComboBoxModel</code> the model that have remains
	 *         data
	 */
	public DefaultComboBoxModel removeDataFromModel(
			ArrayList<ExtractData> dataToRemove, ListModel oldModel) {
		ArrayList<ExtractData> oldData = new ArrayList<ExtractData>();
		for (int i = 0; i < oldModel.getSize(); i++) {
			oldData.add((ExtractData) oldModel.getElementAt(i));
		}
		for (ExtractData extractData : dataToRemove) {
			oldData.remove(extractData);

		}
		DefaultComboBoxModel modelToReturn = new DefaultComboBoxModel(
				oldData.toArray());
		return modelToReturn;
	}

	/**
	 * get the comment for this supply chain, this method is used when only is
	 * know the name of the supply chain and the supply chain was stored
	 * previously in the DataBase
	 * 
	 * @return String, the comments of this supply chain
	 * @throws Exception
	 */
	public String getCommentsFromDB() throws Exception {
		supplyChain.getCommentsFromDB();
		return supplyChain.getComments();
	}

	/**
	 * remove from DB all the enterprises associated to this supply chain
	 * 
	 * @throws Exception
	 */
	public void removeAllEnterprises() throws Exception {
		supplyChain.deleteAllEnterprises();
	}

}
