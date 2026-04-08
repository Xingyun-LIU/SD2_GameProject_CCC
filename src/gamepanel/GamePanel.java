package gamepanel;

import Entity.Entity;
import Entity.Player;
import Main.*;
import animation.OpeningAnimationPanel;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    public final int worldCol = 64; //2048
    public final int worldRow = 64; //2048
    public final int totalMap = 10;
    public int currentMap = 0;

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

    // Objects and NPC
    public Entity[][] objects = new Entity[totalMap][20];
    public Entity[][] npc = new Entity[totalMap][20];
    public Entity[][] monsters = new Entity[totalMap][20];
    ArrayList<Entity> entities = new ArrayList<>();


    // System
    GameFrame gameFrame;
    public TileManager tileManager = new TileManager(this);
    SplitScreenRenderer splitRenderer = new SplitScreenRenderer();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public EventChecker eventChecker = new EventChecker(this);
    KeyHandler key;
    Thread gameThread;
    UI ui = new UI(this);
    public Setter setter = new Setter(this);

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
        this.gameState = opening;
    }

    public void setUpGame() {

        player1 = new Player(this, true);
        player2 = new Player(this, false);

        cam1 = new Camera(this);
        cam2 = new Camera(this);

        setter.setObject();
        setter.setNPC();
        setter.setMonster();

        gameState = playing;
    }

    /**each time gameThread.start() ,run() will be executed in the new thread*/
    public void startThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
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
            // player
            player1.update(key);
            player2.update(key);

            cam1.follow(player1);
            cam2.follow(player2);

            // Update all maps
            for (int m = 0; m < totalMap; m++) {
                // npc
                for (int i = 0; i < npc[m].length; i++) {
                    if (npc[m][i] != null) {
                        npc[m][i].update();
                    }
                }

                // monster
                for (int i = 0; i < monsters[1].length; i++) {
                    if (monsters[m][i] != null) {
                        monsters[m][i].update();
                    }
                }
            }
        }

        if (gameState == pausing) {
            //do nothing
        }
    }

    public void paintComponent(Graphics graphics) {

        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        if (gameState == playing) {

            splitRenderer.render(graphics2D, this, player1, player2, cam1, cam2);

//        // Add entities to the list
//        for (int i = 0; i < npc.length; i++) {
//            if (npc[i] != null) {
//                entities.add(npc[i]);
//            }
//        }
//        for (int i = 0; i < objects.length; i++) {
//            if (objects[i] != null) {
//                entities.add(objects[i]);
//            }
//        }
//        for (int i = 0; i < monsters.length; i++) {
//            if (monsters[i] != null) {
//                entities.add(monsters[i]);
//            }
//        }
//
//        // Sort entity list by worldY
//        Collections.sort(entities, new Comparator<Entity>() {
//            @Override
//            public int compare(Entity e1, Entity e2) {
//                int result = Integer.compare(e1.worldY, e2.worldY);
//                return result;
//            }
//        });
//
//        // draw entities
//        for (int i = 0; i < entities.size(); i++) {
//            entities.get(i).draw(graphics2D,cam);
//        }
//
//        ui.draw(graphics2D);
        }

        graphics2D.dispose();
    }

    public void drawWorld(Graphics2D graphics2D, Camera cam, int viewWidth, int currentMap) {

        //Draw the map
        tileManager.draw(graphics2D, cam, viewWidth, currentMap);

        //Draw the player
        player1.draw(graphics2D, cam);
        player2.draw(graphics2D, cam);

        // Add entities to the list
        for (int i = 0; i < npc[1].length; i++) {
            if (npc[currentMap][i] != null) {
                entities.add(npc[currentMap][i]);
            }
        }
        for (int i = 0; i < objects[1].length; i++) {
            if (objects[currentMap][i] != null) {
                entities.add(objects[currentMap][i]);
            }
        }
        for (int i = 0; i < monsters[1].length; i++) {
            if (monsters[currentMap][i] != null) {
                entities.add(monsters[currentMap][i]);
            }
        }

        // Sort entity list by worldY
        Collections.sort(entities, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            }
        });

        // Draw entities
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).draw(graphics2D, cam, viewWidth);
        }

        // Ensure that entities will load again every frame.
        entities.clear();

        ui.draw(graphics2D);

    }

}