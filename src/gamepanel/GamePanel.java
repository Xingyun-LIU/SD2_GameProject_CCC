package gamepanel;

import Entity.Player;
import Main.Camera;
import Main.KeyHandler;
import Main.SplitScreenRenderer;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

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

    Player player1;
    Player player2;

    Camera cam1;
    Camera cam2;

    TileManager tileManager;
    SplitScreenRenderer splitRenderer;
    KeyHandler key = new KeyHandler();
    Thread gameThread;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true); // JPanel can focus on receive key input
        this.addKeyListener(key);
        setFocusable(true);

        // initialize
        player1 = new Player(this, tileSize * 62, tileSize * 64, Color.BLUE);
        player2 = new Player(this, tileSize * 67, tileSize * 64, Color.RED);
        cam1 = new Camera(this);
        cam2 = new Camera(this);
        tileManager = new TileManager(this);
        splitRenderer = new SplitScreenRenderer();

        startThread();
    }

    public void startThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    public void run() {

        while (true) {

            //Update information
            update();
            //Draw the screen with the updated information
            repaint();

            try {
                Thread.sleep(16);
            } catch (Exception e) {
            }
        }
    }

    public void update() {

        player1.update(key, true);
        player2.update(key, false);

        cam1.follow(player1);
        cam2.follow(player2);
    }

    public void paintComponent(Graphics graphics) {

        super.paintComponents(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        splitRenderer.render(graphics2D, this, player1, player2, cam1, cam2);

    }

    public void drawWorld(Graphics2D graphics2D, Camera cam) {

        //Draw the map
        tileManager.draw(graphics2D, cam);

        //Draw the player
        player1.draw(graphics2D, cam);
        player2.draw(graphics2D, cam);

    }

}