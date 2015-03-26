package system.config;

import java.io.File;
import java.util.Hashtable;
import javax.imageio.stream.FileImageInputStream;

import system.rdf.ui.GraphEd;

/**
 * @author Irlan
 */
public class AppConfiguration {
	private XMLConfig config;

	public AppConfiguration() throws Exception {
		config = new XMLConfig(System.getProperty("user.dir")+ "/configuration.xml");
	}

    public XMLConfig getConfig() {
        return config;
    }

	public AppConfiguration(File file) throws Exception {
		config = new XMLConfig(file);
	}

	public AppConfiguration(String configfile) throws Exception {
		config = new XMLConfig(configfile);
	}

	public String getHost() {
		return config.getValue("db_server");
	}

	public String getPort() {
		return config.getValue("port");
	}

	public String getUser() {
		return config.getValue("user");
	}

	public String getPassword() {
		return config.getValue("pwd");
	}

	public String getDataBase() {
		return config.getValue("db_name");
	}
	
	public String getSCOROntUrl() {
		return config.getValue("scor_onto_file");
	}

	public String getDriver() {
		return config.getValue("driver");
	}

	public String getOracleJDBCType() {
		return config.getValue("oracle_jdbc_type");
	}

	public String getConnectionType() {
		return config.getValue("connection_type");
	}

	public String getVirtuosoHost() {
		return config.getValue("virtuoso_db_server");
	}

	public String getVirtuosoPort() {
		return config.getValue("virtuoso_port");
	}

	public String getVirtuosoUser() {
		return config.getValue("virtuoso_user");
	}

	public String getVirtuosoPassword() {
		return config.getValue("virtuoso_pwd");
	}

	public String getVirtuosoDataBase() {
		return config.getValue("virtuoso_db_name");
	}

	public String getVirtuosoDriver() {
		return config.getValue("virtuoso_driver");
	}

	public String getVirtuosoConnectionType() {
		return config.getValue("virtuoso_connection_type");
	}

}
