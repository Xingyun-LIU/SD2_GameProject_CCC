package npc;

import Entity.Entity;
import Entity.Player;
import gamepanel.GamePanel;

/**
 * Gan is an NPC that can talk with players.
 * - Player 1 presses F to interact
 * - Player 2 presses Numpad0 to interact
 * - Wrong answer -> player loses 1 life
 * - Gan can be attacked and killed like a monster
 */
public class Gan extends Entity {

    public String question = "";
    public String[] options = {"A) 1", "B) 2", "C) 3"};
    public int correctAnswer = 1;

    // Dialog state
    public boolean talking = false;
    public int selectedOption = 0;

    public int interactCooldown = 0;

    public Gan(GamePanel gp) {
        super(gp);

        name = "Gan";
        type = 1;
        speed = 0;
        maxLife = 2;
        life = maxLife;

        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 32;
        solidArea.height = 32;
        originalSolidAreaX = solidArea.x;
        originalSolidAreaY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1    = setup("/objects/test.png", gamePanel.tileSize, gamePanel.tileSize);
        up2    = setup("/objects/test.png", gamePanel.tileSize, gamePanel.tileSize);
        down1  = setup("/objects/test.png", gamePanel.tileSize, gamePanel.tileSize);
        down2  = setup("/objects/test.png", gamePanel.tileSize, gamePanel.tileSize);
        left1  = setup("/objects/test.png", gamePanel.tileSize, gamePanel.tileSize);
        left2  = setup("/objects/test.png", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/objects/test.png", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/objects/test.png", gamePanel.tileSize, gamePanel.tileSize);
    }

    public void startTalking() {
        talking = true;
        selectedOption = 0;
    }

    public void selectUp() {
        selectedOption--;
        if (selectedOption < 0) selectedOption = options.length - 1;
    }

    public void selectDown() {
        selectedOption++;
        if (selectedOption >= options.length) selectedOption = 0;
    }

    public boolean confirm(Player player) {
        talking = false;
        if (selectedOption == correctAnswer) {
            return true;
        } else {
            if (!player.invincible) {
                player.life -= 1;
                player.invincible = true;
            }
            return false;
        }
    }
    public void takeDamage() {
        if (!invincible) {
            life -= 1;
            hpBarOn = true;
            hpBarCounter = 0;
            invincible = true;

            if (life <= 0) {
                dying = true;
                alive = false;
                talking = false;
            }
        }
    }

    public boolean isPlayerInFront(Player player) {
        int dx = Math.abs(player.worldX - this.worldX);
        int dy = Math.abs(player.worldY - this.worldY);
        return (dx <= gamePanel.tileSize && dy <= gamePanel.tileSize);
    }
}