package Main;

import Entity.Player;
import gamepanel.GamePanel;
/**
 * This class is used to mapping the worldX, worldY into screenX, screenY
 * */
public class Camera {
    public int x;
    public int y;

    GamePanel gamePanel;

    public Camera(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void moveTo(int targetX, int targetY) {
        x = targetX;
        y = targetY;
        if (x < targetX) {
            x += 2;
        }
    }

    public void follow(Player player) {
        int targetX = player.worldX - gamePanel.screenWidth / 4 + gamePanel.tileSize / 2;
        int targetY = player.worldY - gamePanel.screenHeight / 2 + gamePanel.tileSize / 2;
        moveTo(targetX, targetY);
    }
}
