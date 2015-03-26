package system.rdf;

/**
 * Handle the up label
 * @author Armando Carracedo Velázquez
 */
public class AccionText {
	
	public String at = null;
	public static final String ACCION_NULL = "...";
	
	/**
	 * 	Empty Constructor
	 * @param action
	 */
	public AccionText() {}
	
	public AccionText(String accion) {
      this.at = accion;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public static String getACCION_NULL() {
		return ACCION_NULL;
	}
}
