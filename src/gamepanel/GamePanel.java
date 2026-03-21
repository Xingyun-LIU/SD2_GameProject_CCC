package gamepanel;

import Entity.Player;
import Main.*;
import animation.OpeningAnimationPanel;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    public final int FPS = 60;

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

    // Game State
    public int gameState;
    public final int opening = 0;
    public final int playing = 1;
    public final int pausing = 2;

    // Player
    public Player player1;
    public Player player2;
    Camera cam1;
    Camera cam2;

    // System
    GameFrame gameFrame;
    public TileManager tileManager = new TileManager(this);
    SplitScreenRenderer splitRenderer = new SplitScreenRenderer();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    KeyHandler key;
    Thread gameThread;
    UI ui = new UI(this);

    public GamePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        key = new KeyHandler(this, gameFrame);

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true); // JPanel can focus on receive key input
        this.addKeyListener(key);
        setFocusable(true);

        startThread();
    }

    public void setUpGame() {

        player1 = new Player(this, true);
        player2 = new Player(this, false);

        cam1 = new Camera(this);
        cam2 = new Camera(this);

        gameState = playing;
    }

    /**each time gameThread.start() ,run() will be executed in the new thread*/
    public void startThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {

        double drawBreak = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawBreak;

        while (gameThread != null) {

            //Update information
            update();
            //Draw the screen with the updated information
            repaint();

            try {
//                Thread.sleep(16);
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawBreak;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {


        if (gameState == playing) {
            player1.update(key);
            player2.update(key);

            cam1.follow(player1);
            cam2.follow(player2);

            System.out.println(player2.collisionOn);
        }

        if (gameState == pausing) {
            //do nothing
        }
    }

    public void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        if (gameState == opening) {

        }

        splitRenderer.render(graphics2D, this, player1, player2, cam1, cam2);

        ui.draw(graphics2D);

    }

    public void drawWorld(Graphics2D graphics2D, Camera cam) {

        //Draw the map
        tileManager.draw(graphics2D, cam);

        //Draw the player
        player1.draw(graphics2D, cam);
        player2.draw(graphics2D, cam);

    }

}