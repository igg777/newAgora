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
import org.jvnet.substance.theme.ThemeInfo;

/**
 *
 * @author Humberto Rodriguez Avila
 */
public class SubstanceThemeComboSelector extends JComboBox {
	JFrame a;
  public SubstanceThemeComboSelector(final JFrame a,Object[] theme) {
  
    super(theme);
      this.a = a;
    this.setSize(120, 22);
    this.setRenderer(new SubstanceDefaultComboBoxRenderer(this) {
      @Override
      public Component getListCellRendererComponent(JList list,
          Object value, int index, boolean isSelected,
          boolean cellHasFocus) {
        return super.getListCellRendererComponent(list,
            ((ThemeInfo) value).getDisplayName(), index, isSelected,
            cellHasFocus);
      }
    });
    
    this.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            SubstanceLookAndFeel.setCurrentTheme(((ThemeInfo) SubstanceThemeComboSelector.this
                    .getSelectedItem()).getClassName());
            SwingUtilities.updateComponentTreeUI(getRootPane());
            SwingUtilities.updateComponentTreeUI(a);
          }
        });
      }
    });
  }
}

