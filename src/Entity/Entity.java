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

}
