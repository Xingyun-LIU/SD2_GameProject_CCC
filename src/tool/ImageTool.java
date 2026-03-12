package tool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTool {

    /** Input an image with its width and height you want it be, and it will output the image with
     * correct format*/
    public static BufferedImage fixImage(BufferedImage original, int width, int height) {

        // create a "panel" in width and height in memory
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);

        // Release operating system resources used by the graphic
        g2.dispose();

        return scaledImage;
    }
}
