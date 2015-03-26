package system.rdf.dataBase;
/**
 * Class to represent the label of the predicate in a RDF triple
 * @author Irlan
 *
 */
public class Predicate {

    protected String label;

    public Predicate(String label) {
	this.label = label;
    }

    public Predicate() {
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }


}
