package Entity;

import Main.Camera;
import Main.KeyHandler;
import gamepanel.GamePanel;

import java.awt.*;

public class Player extends Entity {

    int spriteIndex = 0;
    int spriteTimer = 0;

    public Color color;

    public Player(GamePanel gamePanel, int worldX, int worldY, Color color) {
        super(gamePanel);
        this.worldX = worldX;
        this.worldY = worldY;
        this.color = color;
    }

    public void update(KeyHandler key, boolean p1) {

        int dx = 0;
        int dy = 0;

        if (p1) {

            if (key.w) dy -= speed;
            if (key.s) dy += speed;
            if (key.a) dx -= speed;
            if (key.d) dx += speed;
        } else {

            if (key.up) dy -= speed;
            if (key.down) dy += speed;
            if (key.left) dx -= speed;
            if (key.right) dx += speed;
        }

        worldX += dx;
        worldY += dy;

        if (dx != 0 || dy != 0) {

            spriteTimer++;

            if (spriteTimer > 10) {
                spriteIndex = (spriteIndex + 1) % 3;
                spriteTimer = 0;
            }
        }
    }

    public void draw(Graphics2D graphics2D, Camera cam) {

        int drawX = worldX - cam.x;
        int drawY = worldY - cam.y;

        graphics2D.setColor(color);

        graphics2D.fillRect(drawX, drawY, 32, 32);
    }
}
