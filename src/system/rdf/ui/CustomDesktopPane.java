package system.rdf.ui;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;

public class CustomDesktopPane extends JDesktopPane {
	
	public CustomDesktopPane(){
		initGUI();
	}
	
	
	private void initGUI() {
		try {
			{
				this.setBackground(new java.awt.Color(10,100,160));
				this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				this.setForeground(new java.awt.Color(0,0,0));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Adds a new Modeler to the application
     * @param  UIModeler modeler
     */
    public void addModeler(UIModeler modeler){
    	this.add(modeler);
    }
}
