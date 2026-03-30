package npc;

import gamepanel.GamePanel;
import Entity.Entity;
import java.util.Random;

public class Bro extends Entity {

    private boolean alreadyTalkedOnce = false;

    public Bro(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;

        loadPics();
    }

    private void loadPics() {

        up1    = setup("/npcs/bro.png", gamePanel.tileSize, gamePanel.tileSize);
        up2    = setup("/npcs/bro.png", gamePanel.tileSize, gamePanel.tileSize);
        down1  = setup("/npcs/bro.png",gamePanel.tileSize, gamePanel.tileSize);
        down2  = setup("/npcs/bro.png", gamePanel.tileSize, gamePanel.tileSize);
        left1  = setup("/npcs/bro.png", gamePanel.tileSize, gamePanel.tileSize);
        left2  = setup("/npcs/bro.png", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/npcs/bro.png", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/npcs/bro.png", gamePanel.tileSize, gamePanel.tileSize);
    }

    /**This method executed in update, npc will change it direction  each two second.*/
    public void setAction() {
        actionCounter++;

        if (actionCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            }
            else if (i <= 50) {
                direction = "down"; }
            else if (i <= 75) { direction = "left";
            }
            else {
                direction = "right";
            }

            actionCounter = 0;
        }
    }
}