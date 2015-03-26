package system.rename;

/**
 * @ * @author Leo
 */
public class Triplex {
	private String subject = null;
	private String property = null;
	private String object = null;

	public Triplex(String subject, String property, String object) {
		this.subject = subject;
		this.property = property;
		this.object = object;
	}

	public String getSubject() {
		return subject;
	}

	public String getProperty() {
		return property;
	}

	public String getObject() {
		return object;
	}

	public String toString() {
		String toReturn = subject + " " + property + "  " + object;
		return toReturn;
	}
}
