/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.rdf.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import org.jvnet.substance.SubstanceDefaultComboBoxRenderer;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.SkinInfo;

/**
 *
 * @author Humberto Rodriguesz Avila
 */
public class SubstanceSkinComboSelector extends JComboBox {
    /**
     * 
     */
    private static final long serialVersionUID = -5387856638989742556L;
    JFrame a;
    public SubstanceSkinComboSelector(final JFrame a,Object[] skin) {

	super(skin);
	this.a = a;
	this.setSize(120,22);

	this.setRenderer(new SubstanceDefaultComboBoxRenderer(this) {
	    @Override
	    public Component getListCellRendererComponent(JList list,
		    Object value, int index, boolean isSelected,
		    boolean cellHasFocus) {
		return super.getListCellRendererComponent(list,
			((SkinInfo) value).getDisplayName(), index, isSelected,
			cellHasFocus);
	    }
	});

	this.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
			SubstanceLookAndFeel
			.setSkin(((SkinInfo) SubstanceSkinComboSelector.this
				.getSelectedItem()).getClassName());

			SwingUtilities.updateComponentTreeUI(getRootPane());
			SwingUtilities.updateComponentTreeUI(a);
		    }
		});
	    }
	});
    }


}

