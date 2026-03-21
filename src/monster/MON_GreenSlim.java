package monster;

import Entity.Entity;
import gamepanel.GamePanel; // 确保 package 路径正确

import java.util.Random;

public class MON_GreenSlim extends Entity {

    public MON_GreenSlim(GamePanel gp) {
        super(gp);

        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;

        // 史莱姆碰撞箱：通常比图片小，方便玩家钻空隙或避免判定过大
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        originalSolidAreaX = solidArea.x;
        originalSolidAreaY = solidArea.y;

        getImage();
    }

    public void getImage() {
        // 史莱姆四个方向图片通常一致，通过 setup 预处理缩放
        up1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
        up2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
        down1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
        down2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
        left1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
        left2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
    }

    public void setAction() {
        actionCounter++;

        // 每 120 帧（约 2 秒）随机变换一次方向
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