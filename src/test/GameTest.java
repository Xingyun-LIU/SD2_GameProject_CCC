package test;

import org.junit.jupiter.api.Test;
import tool.ImageTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testFixImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/tiles/001.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage fixedImage = ImageTool.fixImage(image, 40, 40);

        assertEquals(40, fixedImage.getWidth());
        assertEquals(40, fixedImage.getHeight());
    }
}