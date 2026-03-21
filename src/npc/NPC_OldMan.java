package npc;

import gamepanel.GamePanel;
import Entity.Entity;
import java.util.Random;
import Entity.Player;

public class NPC_OldMan extends Entity {

    private boolean alreadyTalkedOnce = false;

    public NPC_OldMan(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;

        loadPics();
    }

    private void loadPics() {

        up1    = setup("/npc/oldman_up_1", gamePanel.tileSize, gamePanel.tileSize);
        up2    = setup("/npc/oldman_up_2", gamePanel.tileSize, gamePanel.tileSize);
        down1  = setup("/npc/oldman_down_1",gamePanel.tileSize, gamePanel.tileSize);
        down2  = setup("/npc/oldman_down_2", gamePanel.tileSize, gamePanel.tileSize);
        left1  = setup("/npc/oldman_left_1", gamePanel.tileSize, gamePanel.tileSize);
        left2  = setup("/npc/oldman_left_2", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/npc/oldman_right_1", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/npc/oldman_right_2", gamePanel.tileSize, gamePanel.tileSize);
    }



    public void setAction() {
        actionCounter++;

        if (actionCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) { direction = "up"; }
            else if (i <= 50) { direction = "down"; }
            else if (i <= 75) { direction = "left"; }
            else { direction = "right"; }

            actionCounter = 0;
        }
    }
}