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

        if (p1 == null) return p2;
        if (p2 == null) return p1;

        double dist1 = distance(p1);
        double dist2 = distance(p2);

        return (dist1 <= dist2) ? p1 : p2;
    }

    private double distance(Player p) {
        int dx = p.worldX - this.worldX;
        int dy = p.worldY - this.worldY;
        return Math.sqrt(dx * dx + dy * dy);
    }
}