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

    // EventChecker
    // make sure the event will not repeatedly trigger in a short period of time.
    public int currentMap = 0;
    public int eventPreviousX;
    public int eventPreviousY;
    public boolean canTouchEvent = true;

    int p1SpriteCounter = 0;
    int p2SpriteCounter = 0;

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
            up1 = setup("/player/player1_up1.png", gamePanel.tileSize, gamePanel.tileSize);
            up2 = setup("/player/player1_up2.png", gamePanel.tileSize, gamePanel.tileSize);
            down1 = setup("/player/player1_down1.png", gamePanel.tileSize, gamePanel.tileSize);
            down2 = setup("/player/player1_down2.png", gamePanel.tileSize, gamePanel.tileSize);
            left1 = setup("/player/player1_left1.png", gamePanel.tileSize, gamePanel.tileSize);
            left2 = setup("/player/player1_left2.png", gamePanel.tileSize, gamePanel.tileSize);
            right1 = setup("/player/player1_right1.png", gamePanel.tileSize, gamePanel.tileSize);
            right2 = setup("/player/player1_right2.png", gamePanel.tileSize, gamePanel.tileSize);
        } else {
            up1 = setup("/player/player2_up1.png", gamePanel.tileSize, gamePanel.tileSize);
            up2 = setup("/player/player2_up2.png", gamePanel.tileSize, gamePanel.tileSize);
            down1 = setup("/player/player2_down1.png", gamePanel.tileSize, gamePanel.tileSize);
            down2 = setup("/player/player2_down2.png", gamePanel.tileSize, gamePanel.tileSize);
            left1 = setup("/player/player2_left1.png", gamePanel.tileSize, gamePanel.tileSize);
            left2 = setup("/player/player2_left2.png", gamePanel.tileSize, gamePanel.tileSize);
            right1 = setup("/player/player2_right1.png", gamePanel.tileSize, gamePanel.tileSize);
            right2 = setup("/player/player2_right2.png", gamePanel.tileSize, gamePanel.tileSize);
        }

    }

    public void update(KeyHandler key) {

        // first set the direction, then check collision, then move
        if (p1) {

            if (key.w || key.s || key.d || key.a) {
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

                // Walk step by step
                p1SpriteCounter++;
                if (p1SpriteCounter > 12) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    p1SpriteCounter = 0;
                }
            }

        } else {
            if (key.up || key.down || key.left || key.right) {
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

                p2SpriteCounter++;
                if (p2SpriteCounter > 12) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 1;
                    }
                    p2SpriteCounter = 0;
                }
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

        //check object collision
        int objectIndex = gamePanel.collisionChecker.checkObject(this, true);

        //check npc collision
        int npcIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);

        //check monster collision
        int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);

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

        BufferedImage image = null;

        int drawX = worldX - camera.x;
        int drawY = worldY - camera.y;

        switch (direction) {

            //set different picture when moving different direction
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;

            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;

            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;

            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;

        }

        graphics2D.drawImage(image, drawX, drawY, null);
    }
}
