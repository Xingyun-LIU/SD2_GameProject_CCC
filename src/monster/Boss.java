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
    private final int mapIndex;

    // Boss behavior status
    private enum State {
        IDLE, CHASE, MELEE_ATTACK, RANGED_ATTACK, BULLET_HELL, DASH_ATTACK, TELEPORT, PHASE_TRANSITION
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
    private final double DEGREE = Math.PI / 180.0; // Unit angle

    // Projectile list
    public List<BossProjectile> projectiles = new ArrayList<>();

    // Attack solid area (melee)
    public Rectangle meleeHitBox = new Rectangle(0, 0, 0, 0);
    private boolean meleeActive = false;
    private int rangedVolleyIndex = 0; // How many sets the attack comes
    private double hellSpinAngle = 0;
    private boolean bulletHellActive = false; // Mark whether the current skill is being executed
    private double dashVelocityX = 0;
    private double dashVelocityY = 0;
    private boolean dashHitPlayer1 = false;
    private boolean dashHitPlayer2 = false;

    // mapIndex here is used to enable projectiles collision checking.
    public Boss(GamePanel gamePanel, int mapIndex) {
        super(gamePanel);
        this.mapIndex = mapIndex;

        this.type = 2; // monster
        this.maxLife = 20;
        this.life = maxLife;
        this.speed = 2;

        solidArea = new Rectangle(10, 10, 44, 44);
        originalSolidAreaX = solidArea.x;
        originalSolidAreaY = solidArea.y;
        attackArea = new Rectangle(0, 0, 64, 64);

        direction = "down";

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
    }

    public void update() {
        if (!isBattleActive()) {
            return;
        }

        // Change phase
        checkPhaseTransition();

        // Update cooldown
        if (attackCooldown > 0) attackCooldown--;
        if (teleportCooldown > 0) teleportCooldown--;

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

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
            case BULLET_HELL:
                updateBulletHell(target);
                break;
            case DASH_ATTACK:
                updateDashAttack();
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
        gamePanel.collisionChecker.checkTile(this, mapIndex);
    }

    // The boss will only be activated when two players both enter the battle area.
    private boolean isBattleActive() {
        return isPlayerInBattleArea(gamePanel.player1) && isPlayerInBattleArea(gamePanel.player2);
    }

    private boolean isPlayerInBattleArea(Player player) {
        if (player == null || player.currentMap != mapIndex) {
            return false;
        }

        int centerX = player.worldX + player.solidArea.x + player.solidArea.width / 2;
        int centerY = player.worldY + player.solidArea.y + player.solidArea.height / 2;
        int col = centerX / gamePanel.tileSize;
        int row = centerY / gamePanel.tileSize;

        return col > 20 && col < 44 &&
                row > 18 && row < 42;
    }

    private void updateIdle(Player target) {
        stateTimer++;
        if (stateTimer > 60) {
            stateTimer = 0;
            currentState = State.CHASE;
        }
    }

    // In this state, decide whether to switch to melee, ranged, barrage, or teleport
    private void updateChase(Player target) {
        int dist = getDistance(target);

        // Speed up in stage two
        speed = (phase == 1) ? 2 : 3;

        // Melee attack in melee range
        if (dist < MELEE_RANGE && attackCooldown == 0) {
            currentState = State.MELEE_ATTACK;
            stateTimer = 0;
            return;
        }

        // In long range
        if (dist < RANGED_RANGE && attackCooldown == 0) {
            int dashChance = (phase == 1) ? 180 : 110;
            if (dist > MELEE_RANGE && dist < 280 && actionCounter % dashChance == 0) {
                currentState = State.DASH_ATTACK;
                stateTimer = 0;
                beginDashAttack(target);
                return;
            }

            if (phase == 2 && actionCounter % 180 == 0) {
                currentState = State.BULLET_HELL;
                stateTimer = 0;
                return;
            }

            int chance = (phase == 1) ? 120 : 60; // Phase two fires more often
            if (actionCounter % chance == 0) {
                currentState = State.RANGED_ATTACK;
                stateTimer = 0;
                return;
            }
        }

        // Random teleport
        if (phase == 2 && teleportCooldown == 0) {
            int teleportChance = 300; // Roughly one teleport chance every 300 frames
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
                case "up":
                    my -= gamePanel.tileSize;
                    break;
                case "down":
                    my += gamePanel.tileSize;
                    break;
                case "left":
                    mx -= gamePanel.tileSize;
                    break;
                case "right":
                    mx += gamePanel.tileSize;
                    break;
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
            attackCooldown = (phase == 1) ? 120 : 90;
            currentState = State.CHASE;
        }
    }

    // Charge briefly, then release several fan-shaped volleys toward the player.
    private void updateRangedAttack(Player target) {
        stateTimer++;

        if (stateTimer == 1) {
            rangedVolleyIndex = 0;
        }

        if (phase == 1) {
            if (stateTimer == 16 || stateTimer == 28) {
                fireDarkKnightVolley(target, 5, 48, 0);
                rangedVolleyIndex++;
            }
        } else {
            if (stateTimer == 12 || stateTimer == 22 || stateTimer == 32) {
                // Calculate the degree by rangedVolleyIndex, avoid bullet overlap
                double centerOffset = (rangedVolleyIndex % 2 == 0) ? -6 * DEGREE : 6 * DEGREE;
                fireDarkKnightVolley(target, 7, 72, centerOffset);
                fireDarkKnightVolley(target, 3, 18, 0);
                rangedVolleyIndex++;
            }
        }

        if (stateTimer > (phase == 1 ? 44 : 48)) {
            stateTimer = 0;
            rangedVolleyIndex = 0;
            attackCooldown = (phase == 1) ? 110 : 70;
            currentState = State.CHASE;
        }
    }

    private void updateBulletHell(Player target) {
        stateTimer++;
        bulletHellActive = true;

        // The directional angle from the Boss towards the player
        if (stateTimer == 1) {
            hellSpinAngle = Math.atan2(target.worldY - worldY, target.worldX - worldX);
        }

        // Accumulate strength
        if (stateTimer <= 14) {
            return;
        }

        // Shooting in the form of the sun
        if (stateTimer % 3 == 0) {
            sunSpinShot(10, hellSpinAngle, 8 * DEGREE, 5, 1, 7, new Color(255, 210, 90));
            sunSpinShot(10, hellSpinAngle + 18 * DEGREE, -8 * DEGREE, 5, 1, 7, new Color(255, 120, 40));
            hellSpinAngle += 30 * DEGREE; // Rotate 11 degree each time
        }

        // Tracking shoot
        if (stateTimer % 12 == 0) {
            fireTrackingBurst(target);
        }

        if (stateTimer % 24 == 0) {
            fireDarkKnightVolley(target, 5, 36, 0, 6, 1, 7, new Color(255, 90, 220));
        }

        if (stateTimer > 108) {
            bulletHellActive = false;
            stateTimer = 0;
            attackCooldown = 200;
            currentState = State.CHASE;
        }
    }

    private void updateDashAttack() {
        stateTimer++;

        if (stateTimer <= 8) {
            return;
        }

        if (stateTimer <= 22) {
            int nextX = (int) Math.round(worldX + dashVelocityX);
            int nextY = (int) Math.round(worldY + dashVelocityY);

            if (hitsSolidTile(nextX, nextY)) {
                finishDashAttack();
                return;
            }

            worldX = nextX;
            worldY = nextY;
            hitPlayerDash(gamePanel.player1, true);
            hitPlayerDash(gamePanel.player2, false);
            return;
        }

        finishDashAttack();
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
            speed = 3;
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

        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this, mapIndex);
        gamePanel.collisionChecker.checkPlayer(this, gamePanel.player1);
        gamePanel.collisionChecker.checkPlayer(this, gamePanel.player2);

        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
    }

    // Dash attack
    private void beginDashAttack(Player target) {
        double dx = target.worldX - worldX;
        double dy = target.worldY - worldY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) {
            dist = 1;
        }

        double dashSpeed = (phase == 1) ? 20 : 25;
        dashVelocityX = dx / dist * dashSpeed;
        dashVelocityY = dy / dist * dashSpeed;
        dashHitPlayer1 = false;
        dashHitPlayer2 = false;

        if (Math.abs(dx) > Math.abs(dy)) {
            direction = dx > 0 ? "right" : "left";
        } else {
            direction = dy > 0 ? "down" : "up";
        }
    }

    private void finishDashAttack() {
        dashVelocityX = 0;
        dashVelocityY = 0;
        stateTimer = 0;
        attackCooldown = (phase == 1) ? 120 : 85;
        currentState = State.CHASE;
    }

    private boolean hitsSolidTile(int nextWorldX, int nextWorldY) {
        int left = nextWorldX + solidArea.x;
        int right = nextWorldX + solidArea.x + solidArea.width - 1;
        int top = nextWorldY + solidArea.y;
        int bottom = nextWorldY + solidArea.y + solidArea.height - 1;

        return isSolidTile(left, top) ||
                isSolidTile(right, top) ||
                isSolidTile(left, bottom) ||
                isSolidTile(right, bottom);
    }

    private boolean isSolidTile(int x, int y) {
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

    /**
     * Shot single projectile.
     * From boss position, direction is angleRadians.
     * Then add it to arraylist.
     */
    private void fireProjectileByAngle(double angleRadians, int speed, int damage, int radius, Color color,
                                       boolean canBounce) {
        projectiles.add(new BossProjectile(
                gamePanel, worldX + gamePanel.tileSize / 2, worldY + gamePanel.tileSize / 2,
                angleRadians, speed, damage, radius, color, mapIndex, canBounce));
    }

    /**
     * Inspiration: The Dark Knight, a boss in Soul Knight.
     * target: The player targeted by the attack
     * bulletCount: Count of bullet
     * spreadDegrees: Bullets' angle of flight
     * angleOffsetRadius: Offset in the direction of target
     */
    private void fireDarkKnightVolley(Player target, int bulletCount, double spreadDegrees, double angleOffsetRadians) {
        fireDarkKnightVolley(target, bulletCount, spreadDegrees, angleOffsetRadians, 6, 1, 8, Color.MAGENTA);
    }

    private void fireDarkKnightVolley(Player target, int bulletCount, double spreadDegrees, double angleOffsetRadians,
                                      int speed, int damage, int radius, Color color) {
        if (bulletCount <= 0) return;

        double baseAngle = Math.atan2(
                target.worldY - worldY,
                target.worldX - worldX
        ) + angleOffsetRadians;

        if (bulletCount == 1) {
            fireProjectileByAngle(baseAngle, speed, damage, radius, color, false);
            return;
        }

        double startAngle = baseAngle - (spreadDegrees / 2.0) * DEGREE;
        double angleStep = (spreadDegrees * DEGREE) / (bulletCount - 1);

        for (int i = 0; i < bulletCount; i++) {
            fireProjectileByAngle(startAngle + angleStep * i, speed, damage, radius, color, false);
        }
    }

    // Shot projectile like a sun
    private void sunSpinShot(int bulletCount, double startAngle, double angleStep, int speed,
                             int damage, int radius, Color color) {
        for (int i = 0; i < bulletCount; i++) {
            fireProjectileByAngle(startAngle + angleStep * i, speed, damage, radius, color, false);
        }
    }

    // Shooting base on target's position (like tracking)
    private void fireTrackingBurst(Player target) {
        double baseAngle = Math.atan2(target.worldY - worldY, target.worldX - worldX);
        fireProjectileByAngle(baseAngle, 10, 1, 6, new Color(180, 255, 255), true);
        fireProjectileByAngle(baseAngle - 5 * DEGREE, 9, 1, 5, new Color(180, 255, 255), true);
        fireProjectileByAngle(baseAngle + 5 * DEGREE, 9, 1, 5, new Color(180, 255, 255), true);
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

    private void hitPlayerDash(Player player, boolean firstPlayer) {
        if (player == null) return;
        if (firstPlayer && dashHitPlayer1) return;
        if (!firstPlayer && dashHitPlayer2) return;

        Rectangle bossRect = new Rectangle(
                worldX + solidArea.x,
                worldY + solidArea.y,
                solidArea.width,
                solidArea.height
        );
        Rectangle playerRect = new Rectangle(
                player.worldX + player.solidArea.x,
                player.worldY + player.solidArea.y,
                player.solidArea.width,
                player.solidArea.height
        );

        if (bossRect.intersects(playerRect)) {
            player.life -= 2;
            if (firstPlayer) {
                dashHitPlayer1 = true;
            } else {
                dashHitPlayer2 = true;
            }
        }
    }

    public void draw(Graphics2D g2, Main.Camera cam, int viewWidth) {
        int screenX = worldX - cam.x;
        int screenY = worldY - cam.y;
        Image image;

        boolean bossVisible = !(worldX + gamePanel.tileSize <= cam.x ||
                worldX - gamePanel.tileSize >= cam.x + viewWidth ||
                worldY + gamePanel.tileSize <= cam.y ||
                worldY - gamePanel.tileSize >= cam.y + gamePanel.screenHeight);

        if (bossVisible) {
            switch (direction) {
                case "up":
                    image = up1;
                    break;
                case "left":
                    image = left1;
                    break;
                case "right":
                    image = right1;
                    break;
                case "down":
                default:
                    image = down1;
                    break;
            }

            Composite originalComposite = g2.getComposite();
            if (currentState == State.TELEPORT && stateTimer < 20) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
            }
            g2.drawImage(image, screenX, screenY, null);
            g2.setComposite(originalComposite);

            // Flashing during the second-stage transition
            if (currentState == State.PHASE_TRANSITION && transitionTimer % 10 < 5) {
                g2.setColor(new Color(255, 255, 255, 120));
                g2.fillOval(screenX, screenY, gamePanel.tileSize, gamePanel.tileSize);
            }

            if (currentState == State.BULLET_HELL && bulletHellActive) {
                g2.setColor(new Color(255, 220, 120, 80));
                g2.fillOval(screenX - 18, screenY - 18, gamePanel.tileSize + 36, gamePanel.tileSize + 36);
            }

            if (currentState == State.DASH_ATTACK) {
                g2.setColor(new Color(255, 255, 255, stateTimer <= 8 ? 120 : 70));
                g2.fillRect(screenX - 6, screenY - 6, gamePanel.tileSize + 12, gamePanel.tileSize + 12);
            }

            // HpBar
            drawHealthBar(g2, screenX, screenY);

            // Melee hitbox, for debug use---------------------------------
            if (meleeActive) {
                g2.setColor(new Color(255, 0, 0, 80));
                g2.fillRect(meleeHitBox.x - cam.x, meleeHitBox.y - cam.y,
                        meleeHitBox.width, meleeHitBox.height);
            }
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
        g2.fillRect(screenX, barY, (int) (barWidth * ratio), barHeight);

        g2.setColor(Color.WHITE);
        g2.drawRect(screenX, barY, barWidth, barHeight);
    }
}
