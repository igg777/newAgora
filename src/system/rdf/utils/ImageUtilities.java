package system.rdf.utils;

import java.awt.Image;
import java.beans.Beans;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * <code>KImageUtilities</code> A collection of utility methods for image work.
 *
 * @version  1.0, 09/10/11
 * @author   Humberto Rodríguez Ávila
 * @since    1.0
 *
 */
public class ImageUtilities {

    /**
     * @param imageName image name; must be stored in the "image" folder accessible
     *                  by the classpath
     * 
     * @return Image object
     */
    public static Image getImage(String imageName) {
        return getImage(imageName, ImageUtilities.class);
    }
    /**
     * @param imagePath image name; must be stored in the "image" folder accessible by the classpath
     * @param clazz
     *
     * @return Image object
     */
    @SuppressWarnings("unchecked")
    public static Image getImage(String imagePath, Class clazz) {
        if (Beans.isDesignTime()) {
            return new ImageIcon(ImageUtilities.class.getResource(imagePath)).getImage();
        }

        try {
            Image i = null;

            Class jimi = null;
            try {
                jimi = Class.forName("com.sun.jimi.core.Jimi");
            } catch (ClassNotFoundException ex1) {
            }
            if (jimi != null
                    && (imagePath.toLowerCase().endsWith(".ico")
                    || imagePath.toLowerCase().endsWith(".bmp")
                    || imagePath.toLowerCase().endsWith(".png")
                    || imagePath.toLowerCase().endsWith(".pic")
                    || imagePath.toLowerCase().endsWith(".pcx")
                    || imagePath.toLowerCase().endsWith(".tif")
                    || imagePath.toLowerCase().endsWith(".tiff"))) {
                i = (Image) jimi.getMethod("getImage", new Class[]{URL.class}).invoke(null, new Object[]{ImageUtilities.class.getResource("/images/" + imagePath)});
            } else if (imagePath.toLowerCase().endsWith(".tif")
                    || imagePath.toLowerCase().endsWith(".tiff")
                    || imagePath.toLowerCase().endsWith(".bmp")
                    || imagePath.toLowerCase().endsWith(".png")) {
                i = ImageIO.read(clazz.getResource(imagePath));
            } else {
                i = new ImageIcon(clazz.getResource(imagePath)).getImage();
            }
            if (i != null) {
                return i;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }
}

