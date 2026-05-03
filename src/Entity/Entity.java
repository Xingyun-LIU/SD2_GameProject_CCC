package Entity;

import Main.Camera;
import gamepanel.GamePanel;
import tool.ImageTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import inventory.Item;
import inventory.ITM_Brick;

public class Entity {

    public GamePanel gamePanel;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage upAttack1, upAttack2, downAttack1, downAttack2, leftAttack1, leftAttack2, rightAttack1, rightAttack2;

    public int currentMap = 0;
    public int worldX;
    public int worldY;
    public inventory.Item item;// The item that the entity can pick up, null if no item.
    // State
    public String name;
    public int type; // player = 0, npc = 1, monster = 2
    public int speed = 4;
    public String direction = "down";
    public int maxLife;
    public int life;
    public boolean invincible = false;
    public boolean dying = false;
    public boolean alive = true;
    public boolean hpBarOn = false;

    // Counter
    public int invincibleCounter = 0;
    public int spriteCounter = 0;
    public int actionCounter = 0;
    public int hpBarCounter = 0;

    // Action
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int originalSolidAreaX;
    public int originalSolidAreaY;
    public boolean collision = false;
    public boolean collisionOn = false;
    public boolean attacking = false;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Initialize entity image with specified size.
     */
    public BufferedImage setup(String imagePath, int width, int height) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            image = ImageTool.fixImage(image, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    // It will be executed automatically in subclasses.
    public void setAction() {
    }

    public void update() {

        setAction();

        collisionOn = false;
        gamePanel.collisionChecker.checkTile(this, gamePanel.currentMap);
        gamePanel.collisionChecker.checkObject(this, false);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.npc, gamePanel.currentMap);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.monsters, gamePanel.currentMap);

        // The npc or monster collision with player
        boolean collisionPlayer1 = gamePanel.collisionChecker.checkPlayer(this, gamePanel.player1);
        boolean collisionPlayer2 = gamePanel.collisionChecker.checkPlayer(this, gamePanel.player2);

        // If monster collision with the player, life decrease.
        if (this.type == 2 && collisionPlayer1) {
            if (!gamePanel.player1.invincible) {
                gamePanel.player1.life -= 1;
                gamePanel.player1.invincible = true;
            }
        }
        if (this.type == 2 && collisionPlayer2) {
            if (!gamePanel.player2.invincible) {
                gamePanel.player2.life -= 1;
                gamePanel.player2.invincible = true;
            }
        }

        //if collision is false move, true don't move
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

        if (invincible) {
            hpBarOn = true;
            hpBarCounter = 0; // reset the counter if player attack the monster in 5s again
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

    }

    public void draw(Graphics2D graphics2D, Camera cam, int viewWidth) {

        BufferedImage image = null;

        // Same render logic with TileManager.
        int screenX = worldX - cam.x;
        int screenY = worldY - cam.y;
        if (worldX + gamePanel.tileSize > cam.x &&
                worldX - gamePanel.tileSize < cam.x + viewWidth &&
                worldY + gamePanel.tileSize > cam.y &&
                worldY - gamePanel.tileSize < cam.y + gamePanel.screenHeight) {
            switch (direction) {

                //set different picture when moving different direction
                case "up":
                    image = up1;
                    break;
                case "down":
                    image = down1;
                    break;
                case "left":
                    image = left1;
                    break;
                case "right":
                    image = right1;
                    break;
            }

            // monster HP bar
            if (type == 2 && hpBarOn) {

                hpBarCounter++;

                graphics2D.setColor(new Color(35, 35, 35));
                graphics2D.fillRect(screenX - 1, screenY - 16, gamePanel.tileSize + 2, 12);

                graphics2D.setColor(new Color(255, 0, 30));
                graphics2D.fillRect(screenX, screenY - 15, gamePanel.tileSize * life / maxLife, 10);

                if (hpBarCounter == 300) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            graphics2D.drawImage(image, screenX, screenY, null);
        }
    }

    // This class represents an item that can be picked up by the player.
    public Item[] inventory = new Item[5];
    public int selectedItem = 0;

    //switch to next item (right)
    public void nextItem() {
        selectedItem++;
        if (selectedItem >= inventory.length) {
            selectedItem = 0;
        }
    }

    //switch to previous item (left)
    public void previousItem() {
        selectedItem--;
        if (selectedItem < 0) {
            selectedItem = inventory.length - 1;
        }


    }

    private void removeObjectFromWorld(Entity obj) {

        for (int i = 0; i < gamePanel.objects[gamePanel.currentMap].length; i++) {
            if (gamePanel.objects[gamePanel.currentMap][i] == obj) {
                gamePanel.objects[gamePanel.currentMap][i] = null;
                break;
            }
        }
    }


    public void pickUpItem(Entity itemEntity) {
         Item item = itemEntity.item;

            //first check if there is empty slot in inventory

            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i] == null) {
                    inventory[i] = item;
                    removeObjectFromWorld(itemEntity);
                    // Mark the item as picked up

                    return;
                }
            }


        // If inventory is full, drop the currently selected item
        dropItem(selectedItem);

        // Add the new item to the now-empty slot
        inventory[selectedItem] = item;
        itemEntity.alive = false;
    }

    public void dropItem(int index) {

        if (inventory[index] == null) return;

        Item itemToDrop = inventory[index];
        inventory[index] = null;

        //create a new item entity in the world at the player's position
        int dropX = this.worldX;
        int dropY = this.worldY;

        // adjust drop position based on player's direction to avoid dropping on top of the player
        switch(direction) {
            case "up":    dropY -= gamePanel.tileSize; break;
            case "down":  dropY += gamePanel.tileSize; break;
            case "left":  dropX -= gamePanel.tileSize; break;
            case "right": dropX += gamePanel.tileSize; break;
        }
        //replace with actual method to add item entity to the world
        spawnDroppedItem(itemToDrop, dropX, dropY);
    }

    private void spawnDroppedItem(Item item, int x, int y) {

        //determine the type of item and create corresponding entity
        Entity dropped = null;

        if (item instanceof ITM_Brick) {
            dropped = new OBJ_Brick(gamePanel);
        }
        // else if (item instanceof ITM_Key) { dropped = new OBJ_Key(gamePanel); }

        if (dropped == null) return;

        dropped.worldX = x;
        dropped.worldY = y;
        dropped.item = item;
        dropped.alive = true;

        for (int i = 0; i < gamePanel.objects[gamePanel.currentMap].length; i++) {
            if (gamePanel.objects[gamePanel.currentMap][i] == null) {
                gamePanel.objects[gamePanel.currentMap][i] = dropped;
                break;
            }
        }
    }


}
