package system.config;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.Element ;


/** 
 * Reads from the file cdm/avisosys/config/configuration.xml
 * @author Irlan
 */
public class XMLConfig {
    Document configDoc;
    List parametros;
    Element raiz;
    File configFile;

    public XMLConfig(File configFile) throws Exception {
        try{
    	this.configFile = configFile;
        SAXBuilder builder = new SAXBuilder(false);
        //DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        configDoc = builder.build(configFile);
        raiz = configDoc.getRootElement();
        parametros = raiz.getChildren("param");
        }
        catch (Exception e) {
        	throw new Exception("An error ocurrs loadind the DB_config_file");
		}
    }

    public XMLConfig(String fileURL) throws Exception {
        this(new File(fileURL));
    }

    public XMLConfig(URI fileURI) throws Exception {
        this(new File(fileURI));
    }


    public Element getParam(String paramName) {
        for (Iterator iterator = parametros.iterator(); iterator.hasNext(); ) {
            Element parm = (Element)iterator.next();
            if (parm.getAttributeValue("name").equals(paramName))
                return parm;
        }

        return null;
    }

    public String getValue(String paramName) {
        for (Iterator iterator = parametros.iterator(); iterator.hasNext(); ) {
            Element parm = (Element)iterator.next();
            if (parm.getAttributeValue("name").equals(paramName))
                return parm.getAttributeValue("value");
        }

        return null;
    }

    public String getDescriptionValue(String paramName) {
        for (Iterator iterator = parametros.iterator(); iterator.hasNext(); ) {
            Element parm = (Element)iterator.next();
            if (parm.getAttributeValue("name").equals(paramName))
                return parm.getAttributeValue("descripcion");
        }

        return null;
    }

    public boolean setValue(String paramName, String newValue) {
        Element elemento = getParam(paramName);
        if (elemento != null) {
            elemento.setAttribute("value", newValue);
            try {
                XMLOutputter out = new XMLOutputter();
                FileOutputStream file = new FileOutputStream(configFile);
                out.output(configDoc, file);
                file.flush();
                file.close();
                out.output(configDoc, System.out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
