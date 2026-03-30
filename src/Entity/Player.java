package Entity;

import Main.Camera;
import Main.KeyHandler;
import gamepanel.GamePanel;
import object.Inventory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    public Inventory inventory=new Inventory();
    public int selectedSlot = 0;
    public int worldX, worldY;

    public boolean p1;

    public BufferedImage image;

    // EventChecker
    // make sure the event will not repeatedly trigger in a short period of time.
    public int eventPreviousX;
    public int eventPreviousY;
    public boolean canTouchEvent = true;

    public Player(GamePanel gamePanel, boolean p1) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        this.p1 = p1;

        // Define the collision area for player, cannot be tile size,it looks unnatural
        solidArea = new Rectangle(12, 18, 24, 24);
        originalSolidAreaX = solidArea.x;
        originalSolidAreaY = solidArea.y;

        setDefaultValue();
        getImage();
    }

    public void setDefaultValue() {

        if (p1) {
            worldX = gamePanel.tileSize * 30;
            worldY = gamePanel.tileSize * 47;
        } else {
            worldX = gamePanel.tileSize * 37;
            worldY = gamePanel.tileSize * 47;
        }

    }

    public void getImage() {

        System.out.println("Loading image: " + (p1 ? "P1" : "P2"));

        if (p1) {
            image = setup("/player/player1.png", gamePanel.tileSize, gamePanel.tileSize);
        } else {
            image = setup("/player/player2.png", gamePanel.tileSize, gamePanel.tileSize);
        }

    }

    public void update(KeyHandler key) {

        // first set the direction, then check collision, then move
        if (p1) {

            if (key.w) {
                direction = "up";
            }
            if (key.s) {
                direction = "down";
            }
            if (key.a) {
                direction = "left";
            }
            if (key.d) {
                direction = "right";
            }
        } else {

            if (key.up) {
                direction = "up";
            }
            if (key.down) {
                direction = "down";
            }
            if (key.left) {
                direction = "left";
            }
            if (key.right) {
                direction = "right";
            }
        }

        // Check tile collision
        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this);

        // Check for collisions between players
        if (p1) {
            gamePanel.collisionChecker.checkPlayer(this, gamePanel.player2);
        } else {
            gamePanel.collisionChecker.checkPlayer(this, gamePanel.player1);
        }

        // Check event
        gamePanel.eventChecker.checkEvent(this);

        if (!collisionOn) {
            if (p1) {

                if (key.w) {
                    worldY -= speed;
                }
                if (key.s) {
                    worldY += speed;
                }
                if (key.a) {
                    worldX -= speed;
                }
                if (key.d) {
                    worldX += speed;
                }
            } else {

                if (key.up) {
                    worldY -= speed;
                }
                if (key.down) {
                    worldY += speed;
                }
                if (key.left) {
                    worldX -= speed;
                }
                if (key.right) {
                    worldX += speed;
                }
            }
        }


    }

    public void draw(Graphics2D graphics2D, Camera camera) {

        int drawX = worldX - camera.x;
        int drawY = worldY - camera.y;

        graphics2D.drawImage(image, drawX, drawY, null);

    }
}
