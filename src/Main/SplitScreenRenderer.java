package Main;

import Entity.Player;
import gamepanel.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SplitScreenRenderer {

    public boolean split = true;

    private BufferedImage buffer1;
    private BufferedImage buffer2;

    /**Split panel into half, then draw each buffer base on the camera's position*/
    public void render(Graphics2D graphics2D,
                       GamePanel panel,
                       Player p1,
                       Player p2,
                       Camera cam1,
                       Camera cam2) {

//        int distance = Math.abs(p1.x - p2.x);
//        if (distance < 350) {
//            split = false;
//        } else {
//            split = true;
//        }

        if (split) {

//            int half = panel.getWidth() / 2;
//
//            Graphics2D g1 = (Graphics2D) graphics2D.create(0, 0, half, panel.getHeight());
//            panel.drawWorld(g1, cam1);
//
//            Graphics2D g2 = (Graphics2D) graphics2D.create(half, 0, half, panel.getHeight());
//            panel.drawWorld(g2, cam2);

            int half = panel.getWidth() / 2;
            int h = panel.getHeight();

            if (buffer1 == null || buffer1.getWidth() != half || buffer1.getHeight() != h) {
                buffer1 = new BufferedImage(half, h, BufferedImage.TYPE_INT_RGB);
                buffer2 = new BufferedImage(half, h, BufferedImage.TYPE_INT_RGB);
            }

            Graphics2D g1 = buffer1.createGraphics();
            g1.setColor(panel.getBackground());
            g1.fillRect(0, 0, half, h);
            panel.drawWorld(g1, cam1, half, panel.player1.currentMap);
            g1.dispose();

            Graphics2D g2 = buffer2.createGraphics();
            g2.setColor(panel.getBackground());
            g2.fillRect(0, 0, half, h);
            panel.drawWorld(g2, cam2, half, panel.player2.currentMap);
            g2.dispose();

            graphics2D.drawImage(buffer1, 0, 0, null);
            graphics2D.drawImage(buffer2, half, 0, null);

            graphics2D.setColor(Color.DARK_GRAY);
            // split row: 2px
            graphics2D.fillRect(half - 1, 0, 2, h);
        }
//        else {
//
//            Camera shared = new Camera();
//
//            shared.x = (p1.x + p2.x) / 2 - 400;
//            shared.y = (p1.y + p2.y) / 2 - 300;
//
//            panel.drawWorld(graphics2D, shared);
//
//        }
    }
}
