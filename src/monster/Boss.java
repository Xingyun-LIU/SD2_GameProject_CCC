package monster;

import Entity.Entity;
import Entity.Player;
import gamepanel.GamePanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Boss extends Entity {

    // Boss stage
    public int phase = 1;

    // Boss behavior status
    private enum State {
        IDLE, CHASE, MELEE_ATTACK, RANGED_ATTACK, TELEPORT, PHASE_TRANSITION
    }
    private State currentState = State.IDLE;

    // Counter
    private int stateTimer = 0;       // Numbers of frames that the current state has lasted
    private int attackCooldown = 0;   // Attack cooldown
    private int teleportCooldown = 0; // Teleport cooldown
    private int transitionTimer = 0;  // Timer of second-stage transition

    // Attack range
    private final int MELEE_RANGE = 80;   // Melee attack range
    private final int RANGED_RANGE = 400; // Remote attack range

    // Projectile list
    public List<BossProjectile> projectiles = new ArrayList<>();

    // Attack solid area (melee)
    public Rectangle meleeHitBox = new Rectangle(0, 0, 0, 0);
    private boolean meleeActive = false;

    public Boss(GamePanel gamePanel) {
        super(gamePanel);

        this.type = 2; // monster
        this.maxLife = 20;
        this.life = maxLife;
        this.speed = 3;

        solidArea = new Rectangle(10, 10, 44, 44);
        originalSolidAreaX = solidArea.x;
        originalSolidAreaY = solidArea.y;
        attackArea = new Rectangle(0, 0, 64, 64);

        direction = "down";
    }

    public void setAction() {
    }

    public void update() {

        // Change phase
        checkPhaseTransition();

        // Update cooldown
        if (attackCooldown > 0) attackCooldown--;
        if (teleportCooldown > 0) teleportCooldown--;

        // Find the nearest player
        Player target = getNearestPlayer();
        if (target == null) return;

        // AI state machine
        switch (currentState) {
            case IDLE:
                updateIdle(target);
                break;
            case CHASE:
                updateChase(target);
                break;
            case MELEE_ATTACK:
                updateMeleeAttack(target);
                break;
            case RANGED_ATTACK:
                updateRangedAttack(target);
                break;
            case TELEPORT:
                updateTeleport(target);
                break;
            case PHASE_TRANSITION:
                updatePhaseTransition();
                break;
        }

        // update projectile
        projectiles.removeIf(p -> !p.active);
        for (BossProjectile p : projectiles) {
            p.update();
        }

        // Check collision
        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this);
    }

    private void updateIdle(Player target) {
        stateTimer++;
        if (stateTimer > 60) { // 静止 1 秒后开始追击
            stateTimer = 0;
            currentState = State.CHASE;
        }
    }

    private void updateChase(Player target) {
        int dist = getDistance(target);

        // Speed up in stage two
        speed = (phase == 1) ? 3 : 5;

        // Melee attack in melee range
        if (dist < MELEE_RANGE && attackCooldown == 0) {
            currentState = State.MELEE_ATTACK;
            stateTimer = 0;
            return;
        }

        // In long range
        if (dist < RANGED_RANGE && attackCooldown == 0) {
            int chance = (phase == 1) ? 120 : 60; // 二阶段更频繁
            if (actionCounter % chance == 0) {
                currentState = State.RANGED_ATTACK;
                stateTimer = 0;
                return;
            }
        }

        // Random teleport
        if (phase == 2 && teleportCooldown == 0) {
            int teleportChance = 300; // 每300帧有一次机会
            if (actionCounter % teleportChance == 0) {
                currentState = State.TELEPORT;
                stateTimer = 0;
                return;
            }
        }

        // Move to target
        moveToward(target);
        actionCounter++;
    }

    private void updateMeleeAttack(Player target) {
        stateTimer++;

        if (stateTimer == 10) {

            meleeActive = true;
            int mx = worldX, my = worldY;
            switch (direction) {
                case "up":    my -= gamePanel.tileSize; break;
                case "down":  my += gamePanel.tileSize; break;
                case "left":  mx -= gamePanel.tileSize; break;
                case "right": mx += gamePanel.tileSize; break;
            }
            meleeHitBox.x = mx;
            meleeHitBox.y = my;
            meleeHitBox.width = gamePanel.tileSize;
            meleeHitBox.height = gamePanel.tileSize;

            hitPlayerMelee(gamePanel.player1);
            hitPlayerMelee(gamePanel.player2);
        }

        if (stateTimer > 30) {
            meleeActive = false;
            stateTimer = 0;
            attackCooldown = (phase == 1) ? 90 : 60;
            currentState = State.CHASE;
        }
    }

    // Launch projectiles, followed by a second phase launching three shotgun shells
    private void updateRangedAttack(Player target) {
        stateTimer++;

        if (stateTimer == 15) {
            if (phase == 1) {
                fireProjectile(target);
            } else {
                fireProjectile(target);
                fireProjectileOffset(target, -20);
                fireProjectileOffset(target, 20);
            }
        }

        if (stateTimer > 40) {
            stateTimer = 0;
            attackCooldown = (phase == 1) ? 120 : 80;
            currentState = State.CHASE;
        }
    }

    private void updateTeleport(Player target) {
        stateTimer++;

        if (stateTimer == 20) {
            // Teleport to a random offset position next to the target
            int offsetX = (Math.random() > 0.5 ? 1 : -1) * gamePanel.tileSize * 2;
            int offsetY = (Math.random() > 0.5 ? 1 : -1) * gamePanel.tileSize * 2;
            worldX = target.worldX + offsetX;
            worldY = target.worldY + offsetY;
        }

        if (stateTimer > 40) {
            stateTimer = 0;
            teleportCooldown = 300;
            currentState = State.CHASE;
        }
    }

    private void updatePhaseTransition() {
        transitionTimer++;
        // 60 frames break between stage one and stage two
        if (transitionTimer > 60) {
            transitionTimer = 0;
            speed = 5;
            currentState = State.CHASE;
        }
    }

    /*From here, the following are the tool methods.*/

    private void checkPhaseTransition() {
        if (phase == 1 && life <= maxLife / 2) {
            phase = 2;
            currentState = State.PHASE_TRANSITION;
            // Second stage blood recovery
            life = Math.min(life + 3, maxLife);
        }
    }

    private Player getNearestPlayer() {
        Player p1 = gamePanel.player1;
        Player p2 = gamePanel.player2;
        if (p1 == null) return p2;
        if (p2 == null) return p1;
        return getDistance(p1) < getDistance(p2) ? p1 : p2;
    }

    private int getDistance(Entity target) {
        int dx = target.worldX - worldX;
        int dy = target.worldY - worldY;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    private void moveToward(Player target) {
        int dx = target.worldX - worldX;
        int dy = target.worldY - worldY;

        if (Math.abs(dx) > Math.abs(dy)) {
            direction = dx > 0 ? "right" : "left";
        } else {
            direction = dy > 0 ? "down" : "up";
        }

        if (!collisionOn) {
            switch (direction) {
                case "up":    worldY -= speed; break;
                case "down":  worldY += speed; break;
                case "left":  worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }
    }

    private void fireProjectile(Player target) {
        projectiles.add(new BossProjectile(
                gamePanel,
                worldX + gamePanel.tileSize / 2,
                worldY + gamePanel.tileSize / 2,
                target.worldX, target.worldY
        ));
    }

    private void fireProjectileOffset(Player target, int offset) {
        projectiles.add(new BossProjectile(
                gamePanel,
                worldX + gamePanel.tileSize / 2,
                worldY + gamePanel.tileSize / 2,
                target.worldX + offset, target.worldY + offset
        ));
    }

    private void hitPlayerMelee(Player player) {
        if (player == null) return;
        Rectangle playerRect = new Rectangle(
                player.worldX + player.solidArea.x,
                player.worldY + player.solidArea.y,
                player.solidArea.width,
                player.solidArea.height
        );
        if (meleeHitBox.intersects(playerRect)) {
            // Melee damage is higher than ranged damage
            player.life -= 2;
        }
    }

    public void draw(Graphics2D g2, Main.Camera cam, int viewWidth) {
        int screenX = worldX - cam.x;
        int screenY = worldY - cam.y;

        if (worldX + gamePanel.tileSize <= cam.x ||
                worldX - gamePanel.tileSize >= cam.x + viewWidth ||
                worldY + gamePanel.tileSize <= cam.y ||
                worldY - gamePanel.tileSize >= cam.y + gamePanel.screenHeight) return;

        // phase one: red phase two: purple
        g2.setColor(phase == 1 ? Color.RED : new Color(120, 0, 180));

        // Flashing during the second-stage transition
        if (currentState == State.PHASE_TRANSITION && transitionTimer % 10 < 5) {
            g2.setColor(Color.WHITE);
        }

        g2.fillRect(screenX, screenY, gamePanel.tileSize, gamePanel.tileSize);

        // Transparent during teleportation
        if (currentState == State.TELEPORT && stateTimer < 20) {
            g2.setColor(new Color(150, 0, 255, 100));
            g2.fillRect(screenX, screenY, gamePanel.tileSize, gamePanel.tileSize);
        }

        // HpBar
        drawHealthBar(g2, screenX, screenY);

        // Melee hitbox, for debug use---------------------------------
        if (meleeActive) {
            g2.setColor(new Color(255, 0, 0, 80));
            g2.fillRect(meleeHitBox.x - cam.x, meleeHitBox.y - cam.y,
                    meleeHitBox.width, meleeHitBox.height);
        }

        for (BossProjectile p : projectiles) {
            p.draw(g2, cam, viewWidth);
        }
    }

    private void drawHealthBar(Graphics2D g2, int screenX, int screenY) {
        int barWidth = gamePanel.tileSize;
        int barHeight = 6;
        int barY = screenY - 12;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(screenX, barY, barWidth, barHeight);

        float ratio = (float) life / maxLife;
        g2.setColor(phase == 1 ? Color.GREEN : Color.ORANGE);
        g2.fillRect(screenX, barY, (int)(barWidth * ratio), barHeight);

        g2.setColor(Color.WHITE);
        g2.drawRect(screenX, barY, barWidth, barHeight);
    }
}
