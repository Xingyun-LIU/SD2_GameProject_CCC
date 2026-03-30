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

    public int worldX;
    public int worldY;

    public String name;
    public int type; // player = 0, npc = 1, monster = 2
    public int speed = 4;
    public String direction = "down";
    public int maxLife;
    public int life;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int originalSolidAreaX;
    public int originalSolidAreaY;
    public boolean collision = false;
    public boolean collisionOn = false;

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

    // It will
    public void setAction() {
    }

    public void update() {

        setAction();

        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this);
        gamePanel.collisionChecker.checkObject(this, false);

        // the npc or monster collision with player
        boolean collisionPlayer1 = gamePanel.collisionChecker.checkPlayer(this, gamePanel.player1);
        boolean collisionPlayer2 = gamePanel.collisionChecker.checkPlayer(this, gamePanel.player2);

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

    }

    public void draw() {

    }

}
