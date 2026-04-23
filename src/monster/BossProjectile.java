package monster;

import Entity.Entity;
import gamepanel.GamePanel;
import java.awt.*;
/**The boss launched a ranged projectile.*/
public class BossProjectile extends Entity {

    public boolean active = true;
    private final int mapIndex;
    private double exactX, exactY;
    private double velocityX, velocityY;
    private final int damage;
    private final int radius;
    private final Color color;
    private final boolean canBounce;
    private final int maxBounces;
    private int bounceCount = 0;

    public BossProjectile(GamePanel gamePanel, int startX, int startY, double angleRadians,
                          int speed, int damage, int radius, Color color, int mapIndex, boolean canBounce) {
        super(gamePanel);
        this.worldX = startX;
        this.worldY = startY;
        this.mapIndex = mapIndex;
        this.exactX = startX;
        this.exactY = startY;
        this.speed = speed;
        this.type = 3; // projectile type
        this.damage = damage;
        this.radius = radius;
        this.color = color;
        this.canBounce = canBounce;
        this.maxBounces = canBounce ? 5 : 0;
        this.solidArea = new Rectangle(16 - radius, 16 - radius, radius * 2, radius * 2);
        this.originalSolidAreaX = solidArea.x;
        this.originalSolidAreaY = solidArea.y;
        this.velocityX = Math.cos(angleRadians) * speed;
        this.velocityY = Math.sin(angleRadians) * speed;
    }

    public void update() {
        handleTileCollision();
        if (!active) {
            return;
        }

        exactX += velocityX;
        exactY += velocityY;
        worldX = (int) Math.round(exactX);
        worldY = (int) Math.round(exactY);

        // Check if hit the player
        checkHitPlayer(gamePanel.player1);
        checkHitPlayer(gamePanel.player2);

        // If it flies off the map, it disappears
        if (worldX < 0 || worldX > gamePanel.worldCol * gamePanel.tileSize ||
                worldY < 0 || worldY > gamePanel.worldRow * gamePanel.tileSize) {
            active = false;
        }
    }

    // Bounceable projectiles reflect up to maxBounces times; the others disappear on impact.
    private void handleTileCollision() {
        boolean hitsX = hitsBounceTile(exactX + velocityX, exactY);
        boolean hitsY = hitsBounceTile(exactX, exactY + velocityY);
        boolean hitsCorner = !hitsX && !hitsY && hitsBounceTile(exactX + velocityX, exactY + velocityY);

        if (!hitsX && !hitsY && !hitsCorner) {
            return;
        }

        if (!canBounce) {
            active = false;
            return;
        }

        registerBounce();
        if (!active) {
            return;
        }

        if (hitsX) {
            velocityX = -velocityX;
        }
        if (hitsY) {
            velocityY = -velocityY;
        }

        if (!hitsX && !hitsY && hitsCorner) {
            velocityX = -velocityX;
            velocityY = -velocityY;
        }
    }

    private void registerBounce() {
        bounceCount++;
        if (bounceCount > maxBounces) {
            active = false;
        }
    }

    // When projectile move to nextX, nextY, if this position collision on a collision tile.
    private boolean hitsBounceTile(double nextX, double nextY) {
        int left = (int) Math.floor(nextX + originalSolidAreaX);
        int right = (int) Math.floor(nextX + originalSolidAreaX + solidArea.width - 1);
        int top = (int) Math.floor(nextY + originalSolidAreaY);
        int bottom = (int) Math.floor(nextY + originalSolidAreaY + solidArea.height - 1);

        return isBounceTile(left, top) ||
                isBounceTile(right, top) ||
                isBounceTile(left, bottom) ||
                isBounceTile(right, bottom);
    }

    // Match the current project behavior:
    // tiles with collision == false are the tiles that actually block movement.
    private boolean isBounceTile(int x, int y) {
        if (x < 0 || y < 0 ||
                x >= gamePanel.worldCol * gamePanel.tileSize ||
                y >= gamePanel.worldRow * gamePanel.tileSize) {
            return true;
        }

        int col = x / gamePanel.tileSize;
        int row = y / gamePanel.tileSize;
        int tileNum = gamePanel.tileManager.mapTileNum[mapIndex][col][row];

        return gamePanel.tileManager.tiles[tileNum] != null &&
                !gamePanel.tileManager.tiles[tileNum].collision;
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
            player.life -= damage;
            active = false;
        }

        solidArea.x = originalSolidAreaX;
        solidArea.y = originalSolidAreaY;
    }

    public void draw(Graphics2D g2, Main.Camera cam, int viewWidth) {
        if (!active) return;
        int screenX = worldX - cam.x;
        int screenY = worldY - cam.y;
        g2.setColor(color);
        g2.fillOval(screenX + 16 - radius, screenY + 16 - radius, radius * 2, radius * 2);
    }
}
