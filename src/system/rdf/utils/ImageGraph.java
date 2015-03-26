/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package system.rdf.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import net.sourceforge.umljgraph.UMLGraphException;
import org.jgraph.JGraph;



/**
 * Class to write the rdf graph like an image
 *
 */
public class ImageGraph {

    public ImageGraph() {
    }


     public static void writePNG (JGraph graph, String file)
      throws UMLGraphException
   {
      try
      {
         // Create file output stream
         File outFile = new File(file);

         // Write the PNG file

         Dimension dim = graph.getPreferredSize();
         BufferedImage image = new BufferedImage(dim.width+100, dim.height+100,
               BufferedImage.TYPE_INT_RGB);
         Graphics g = image.getGraphics();
         graph.addNotify();
         graph.paint(g);
         ImageIO.write(image, "png", outFile);
      }
      catch (Exception ex)
      {
         throw new UMLGraphException(
               "An unexpected exception occured while writing graph as PNG",
               ex);
      }
   }

      public static void writeJPG (JGraph graph, String file)
      throws UMLGraphException
   {
      try
      {
         // Create file output stream
         File outFile = new File(file);

         // Write the JPG file

         Dimension dim = graph.getPreferredSize();
         BufferedImage image = new BufferedImage(dim.width, dim.height,
               BufferedImage.TYPE_INT_RGB);
         Graphics g = image.getGraphics();
         graph.addNotify();
         graph.paint(g);
         ImageIO.write(image, "jpg", outFile);
      }
      catch (Exception ex)
      {
         throw new UMLGraphException(
               "An unexpected exception occured while writing graph as JPG",
               ex);
      }
   }
}
