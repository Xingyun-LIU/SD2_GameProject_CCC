package gamepanel;

import Entity.Player;
import Main.*;
import animation.OpeningAnimationPanel;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    // Game State
    public int gameState;
    public final int opening = 0;
    public final int playing = 1;
    public final int pausing = 2;

    // --- SYSTEM & COMPONENTS ---
    GameFrame gameFrame;
    public TileManager tileManager = new TileManager(this);
    public SplitScreenRenderer splitRenderer = new SplitScreenRenderer();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public KeyHandler key;
    public UI ui = new UI(this);
    Thread gameThread;

    // Player
    public Player player1;
    public Player player2;
    Camera cam1;
    Camera cam2;

    public GamePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        key = new KeyHandler(this, gameFrame);

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true); // JPanel can focus on receive key input
        this.addKeyListener(key);
        this.MouseListener();

//        setUpGame();

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

    public void addNotify() {
        super.addNotify();
        this.requestFocusInWindow();
    }

    public void run() {
        double drawInterval = 1000.0 / 60; // 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                //Update information
                update();
                //Draw the screen with the updated information
                repaint();
                delta--;
            }

            try {
                Thread.sleep(1);
            } catch (Exception e) {
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
            ui.draw(graphics2D);
        }else if (gameState == playing || gameState == pausing) {
            splitRenderer.render(graphics2D, this, player1, player2, cam1, cam2);

            graphics2D.setClip(0, 0, screenWidth, screenHeight);
            ui.draw(graphics2D);
        }
        graphics2D.dispose();
    }

    public void drawWorld(Graphics2D graphics2D, Camera cam) {

        //Draw the map
        tileManager.draw(graphics2D, cam);

        //Draw the player
        player1.draw(graphics2D, cam);
        player2.draw(graphics2D, cam);

    }

    public Rectangle startButtonRect =new Rectangle(0,0,0,0);
    public void MouseListener() {


   this.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {

            int mx = e.getX();
            int my = e.getY();

            if(gameState == opening){

                if(startButtonRect != null && startButtonRect.contains(mx, my)){
                    gameState = playing;
                    System.out.println("Start clicked");
                }
            }
        }
    });




    }
}