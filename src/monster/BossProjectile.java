package monster;

import Entity.Entity;
import gamepanel.GamePanel;
import java.awt.*;
/**The boss launched a ranged projectile.*/
public class BossProjectile extends Entity {

    public boolean active = true;
    int dirX, dirY;

    public BossProjectile(GamePanel gamePanel, int startX, int startY, int targetX, int targetY) {
        super(gamePanel);
        this.worldX = startX;
        this.worldY = startY;
        this.speed = 6;
        this.type = 3; // projectile type
        this.solidArea = new Rectangle(8, 8, 16, 16);
        this.originalSolidAreaX = solidArea.x;
        this.originalSolidAreaY = solidArea.y;

        // Initialize the flight direction
        int dx = targetX - startX;
        int dy = targetY - startY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) dist = 1;
        dirX = (int) (dx / dist * speed);
        dirY = (int) (dy / dist * speed);
    }

    public void update() {
        worldX += dirX;
        worldY += dirY;

        // Check if hit the player
        checkHitPlayer(gamePanel.player1);
        checkHitPlayer(gamePanel.player2);

        // If it flies off the map, it disappears
        if (worldX < 0 || worldX > gamePanel.worldCol * gamePanel.tileSize ||
                worldY < 0 || worldY > gamePanel.worldRow * gamePanel.tileSize) {
            active = false;
        }
    }

    private void checkHitPlayer(Entity player) {
        if (player == null) return;

        solidArea.x = worldX + originalSolidAreaX;
        solidArea.y = worldY + originalSolidAreaY;
        Rectangle playerArea = new Rectangle(
                player.worldX + player.solidArea.x,
                player.worldY + player.solidArea.y,
                player.solidArea.width,
                player.solidArea.height
        );

        if (solidArea.intersects(playerArea)) {
            player.life -= 1;
            active = false;
        }

        solidArea.x = originalSolidAreaX;
        solidArea.y = originalSolidAreaY;
    }

    public void draw(Graphics2D g2, Main.Camera cam, int viewWidth) {
        if (!active) return;
        int screenX = worldX - cam.x;
        int screenY = worldY - cam.y;
        g2.setColor(Color.MAGENTA);
        g2.fillOval(screenX + 8, screenY + 8, 16, 16);
    }
}
