package monster;

import Entity.Entity;
import Entity.Player;
import gamepanel.GamePanel;

/**
 * Bird1 is a monster that chases players and bites
 */
public class Bird1 extends Entity {

    public Bird1(GamePanel gp) {
        super(gp);

        name = "Bird1"; // 名字更新
        type = 2;
        speed = 1;
        maxLife = 6;
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
        up1    = setup("/monsters/birdstand1.png.png", gamePanel.tileSize, gamePanel.tileSize);
        up2    = setup("/monsters/birdstand1.png.png", gamePanel.tileSize, gamePanel.tileSize);
        down1  = setup("/monsters/birdstand1.png.png", gamePanel.tileSize, gamePanel.tileSize);
        down2  = setup("/monsters/birdstand1.png.png", gamePanel.tileSize, gamePanel.tileSize);
        left1  = setup("/monsters/birdstand1.png.png", gamePanel.tileSize, gamePanel.tileSize);
        left2  = setup("/monsters/birdstand1.png.png", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/monsters/birdstand1.png.png", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/monsters/birdstand1.png.png", gamePanel.tileSize, gamePanel.tileSize);
    }

    @Override
    public void setAction() {
        Player target = getNearestPlayer();
        if (target == null) return;

        int dx = target.worldX - this.worldX;
        int dy = target.worldY - this.worldY;

        // Move along the axis with the bigger gap (feels more natural)
        if (Math.abs(dx) >= Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }
    }

    private Player getNearestPlayer() {
        Player p1 = gamePanel.player1;
        Player p2 = gamePanel.player2;

        // Only think about the player that on the same map.
        boolean p1Valid = (p1 != null && p1.currentMap == this.currentMap);
        boolean p2Valid = (p2 != null && p2.currentMap == this.currentMap);

        if (!p1Valid && !p2Valid) return null; // 没有玩家在同一地图
        if (!p1Valid) return p2;
        if (!p2Valid) return p1;

        return (distance(p1) <= distance(p2)) ? p1 : p2;
    }

    private double distance(Player p) {
        int dx = p.worldX - this.worldX;
        int dy = p.worldY - this.worldY;
        return Math.sqrt(dx * dx + dy * dy);
    }
}