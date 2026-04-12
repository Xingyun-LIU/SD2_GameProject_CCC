package Entity;

import Main.Camera;
import gamepanel.GamePanel;
import tool.ImageTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    public GamePanel gamePanel;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage upAttack1, upAttack2, downAttack1, downAttack2, leftAttack1, leftAttack2, rightAttack1, rightAttack2;

    public int worldX;
    public int worldY;

    public String name;
    public int type; // player = 0, npc = 1, monster = 2
    public int speed = 4;
    public String direction = "down";
    public int maxLife;
    public int life;
    public boolean invincible = false;
    public boolean dying = false;
    public boolean alive = true;

    // Counter
    int invincibleCounter = 0;

    // Action
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int originalSolidAreaX;
    public int originalSolidAreaY;
    public boolean collision = false;
    public boolean collisionOn = false;
    public boolean attacking = false;

    // Counter
    public int spriteCounter = 0;
    public int actionCounter = 0;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**Initialize entity image with specified size.*/
    public BufferedImage setup(String imagePath, int width, int height) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            image = ImageTool.fixImage(image, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    // It will be executed automatically in subclasses.
    public void setAction() {
    }

    public void update() {

        setAction();

        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this);
        gamePanel.collisionChecker.checkObject(this, false);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters);

        // The npc or monster collision with player
        boolean collisionPlayer1 = gamePanel.collisionChecker.checkPlayer(this, gamePanel.player1);
        boolean collisionPlayer2 = gamePanel.collisionChecker.checkPlayer(this, gamePanel.player2);

        // If monster collision with the player, life decrease.
        if (this.type == 2 && collisionPlayer1) {
            gamePanel.player1.life -= 1;
        }
        if (this.type == 2 && collisionPlayer2) {
            gamePanel.player2.life -= 1;
        }

        //if collision is false move, true don't move
        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        if (invincible){
            invincibleCounter++;
            if (invincibleCounter>40){
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

    public void draw(Graphics2D graphics2D, Camera cam, int viewWidth) {

        BufferedImage image = null;

        // Same render logic with TileManager.
        int screenX = worldX - cam.x;
        int screenY = worldY - cam.y;
        if (worldX + gamePanel.tileSize > cam.x &&
                worldX - gamePanel.tileSize < cam.x + viewWidth &&
                worldY + gamePanel.tileSize > cam.y &&
                worldY - gamePanel.tileSize < cam.y + gamePanel.screenHeight) {
            switch (direction) {

                //set different picture when moving different direction
                case "up":
                    image = up1;
                    break;
                case "down":
                    image = down1;
                    break;
                case "left":
                    image = left1;
                    break;
                case "right":
                    image = right1;
                    break;
            }

            graphics2D.drawImage(image, screenX, screenY, null);
        }
    }

}
