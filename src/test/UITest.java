package test;

import org.junit.jupiter.api.Test;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import Main.UI;
import gamepanel.GamePanel;
import Entity.Player;

public class UITest {

    @Test
    void testDrawHUD_noCrash() {

        //create a mock GamePanel and UI
        GamePanel gp = new GamePanel( null);

        UI ui = new UI(gp);

        //create mock player entities
        Entity.Player p1 = new Entity.Player(gp, true);
        p1.maxLife = 5;
        p1.life = 3;

        Entity.Player p2 = new Entity.Player(gp, false);
        p2.maxLife = 5;
        p2.life = 2;


        //create a mock Graphics2D object using BufferedImage
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = img.createGraphics();

        // inject the mock Graphics2D into UI
        ui.setGraphics2D(graphics2D);

        assertDoesNotThrow(() -> ui.drawHUD());
    }

    @Test
    void testPlayerLoseWhenLifeZero() {
        GamePanel gp = new GamePanel(null);
        Player player = new Player(null, true);
        UI ui = new UI(gp);
        player.life = 0;
        player.maxLife = 5;

        String result = (player.life <= 0) ? "YOU LOSE!!!" : "YOU WIN!!!";
        assertEquals("YOU LOSE!!!", result);
    }

    @Test
    void testPlayerLoseWhenLifeNegative() {
        Player player = new Player(null, true);
        GamePanel gp = new GamePanel(null);
        UI ui = new UI(gp);
        player.life = -1;

        String result = (player.life <= 0) ? "YOU LOSE!!!" : "YOU WIN!!!";
        assertEquals("YOU LOSE!!!", result);
    }

    @Test
    void testPlayerWinWhenLifeAboveZero() {
        Player player = new Player(null, true);
        GamePanel gp = new GamePanel(null);
        UI ui = new UI(gp);
        player.life = 4;

        String result = (player.life <= 0) ? "YOU LOSE!!!" : "YOU WIN!!!";
        assertEquals("YOU LOSE!!!", result);
    }

}

