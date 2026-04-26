package gamepanel;

import Entity.Entity;
import Entity.Player;
import Main.*;
import animation.OpeningAnimationPanel;
import npc.Gan;
import tile.Map;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import inventory.Item;

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
    public final int mapState = 3;
    public final int dialogState = 4;
    public Gan activeGan = null;
    public Player dialogPlayer = null;

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
    public KeyHandler key;
    public Thread gameThread;
    public Map map = new Map(this);
    public UI ui = new UI(this);
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

    /**
     * each time gameThread.start() ,run() will be executed in the new thread
     */
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
                for (int i = 0; i < monsters[m].length; i++) {
                    if (monsters[m][i] != null) {
                        if (monsters[m][i].alive || !monsters[m][i].dying) {
                            monsters[m][i].update();
                        }

                        if (!monsters[m][i].alive || monsters[m][i].dying) {
                            monsters[m][i] = null;
                        }
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

        // Play State
        if (gameState == playing || gameState == pausing) {
            splitRenderer.render(graphics2D, this, player1, player2, cam1, cam2);
        }
        // Map State
        else if (gameState == mapState) {
            map.drawMiniMap(graphics2D);
        }

        ui.drawFullScreen(graphics2D);
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
    }

    public ArrayList<Item> itemList = new ArrayList<>();

}