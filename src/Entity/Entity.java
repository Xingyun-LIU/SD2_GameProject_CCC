package Entity;

import Main.Camera;
import gamepanel.GamePanel;

import java.awt.*;

public class Entity {

    public GamePanel gamePanel;

    public int worldX;
    public int worldY;

    public int speed = 4;

    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);

    public boolean collision = false;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics g, Camera cam) {

        int drawX = worldX - cam.x;
        int drawY = worldY - cam.y;

        g.fillRect(drawX, drawY, 32, 32);

    }

}
