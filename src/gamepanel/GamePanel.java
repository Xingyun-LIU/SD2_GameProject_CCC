package gamepanel;

import tile.TileSetter;

import java.awt.*;

public class GamePanel extends Panel implements Runnable {

    //Screen
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int screenCol = 30;
    public final int screenRow = 18;
    public final int screenWidth = screenCol * tileSize;
    public final int screenHeight = screenRow * tileSize;

    // World
    public final int worldCol = 128; //2048
    public final int worldRow = 128; //2048

    //System
    Thread gameThread;
    TileSetter tileSetter = new TileSetter(this);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true); // JPanel can focus on receive key input
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        while (gameThread != null) {

            //Update information
            update();
            //Draw the screen with the updated information
            repaint();
        }
    }

    //Override update
    public void update() {

    }

    public void paintComponents(Graphics graphics) {

        super.paintComponents(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        tileSetter.draw(graphics2D);
    }
}
