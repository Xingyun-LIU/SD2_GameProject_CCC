package monster;

import Entity.Entity;
import gamepanel.GamePanel;
import java.util.Random;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Egg extends Entity {

    public Egg(GamePanel gp) {
        super(gp);

        name = "Egg";
        type = 2;
        speed = 1;
        maxLife = 4;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        originalSolidAreaX = solidArea.x;
        originalSolidAreaY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("/monsters/egg.png", gamePanel.tileSize, gamePanel.tileSize);
        up2 = setup("/monsters/egg.png", gamePanel.tileSize, gamePanel.tileSize);
        down1 = setup("/monsters/egg.png", gamePanel.tileSize, gamePanel.tileSize);
        down2 = setup("/monsters/egg.png", gamePanel.tileSize, gamePanel.tileSize);
        left1 = setup("/monsters/egg.png", gamePanel.tileSize, gamePanel.tileSize);
        left2 = setup("/monsters/egg.png", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/monsters/egg.png", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/monsters/egg.png", gamePanel.tileSize, gamePanel.tileSize);
    }

    public void setAction() {
        actionCounter++;

        if (actionCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }

            actionCounter = 0;
        }
    }

}