package Entity;

import Main.Camera;
import Main.KeyHandler;
import gamepanel.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    int spriteIndex = 0;
    int spriteTimer = 0;

    public boolean p1;

    public BufferedImage image;

    public Player(GamePanel gamePanel, boolean p1) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        this.p1 = p1;

        // Define the collision area for player, cannot be tile size,it looks unnatural
        solidArea = new Rectangle(12,18,24,24);
        setDefaultValue();
        getImage();
    }

    public void setDefaultValue() {

        if (p1) {
            worldX = gamePanel.tileSize * 62;
            worldY = gamePanel.tileSize * 64;
        } else {
            worldX = gamePanel.tileSize * 67;
            worldY = gamePanel.tileSize * 64;
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

    public void draw(Graphics2D graphics2D, Camera camera) {

        int drawX = worldX - camera.x;
        int drawY = worldY - camera.y;

        graphics2D.drawImage(image, drawX, drawY, null);

    }
}
