package Main;

import Entity.Entity;
import gamepanel.GamePanel;

/**
 * This class is used of check different collision, for example,player to tile,
 * player to monster, player to npc, player to object, monster to player...
 * */
public class CollisionChecker {

    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // Check if the entity is collided with an entity that cannot be collided with
    public void checkTile(Entity entity) {

        // Calculate world position of entity's solidArea
        int left = entity.worldX + entity.solidArea.x;
        int right = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int top = entity.worldY + entity.solidArea.y;
        int bottom = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Check the cols and rows the solidArea belongs to
        int leftCol = left / gamePanel.tileSize;
        int rightCol = right / gamePanel.tileSize;
        int topRow = top / gamePanel.tileSize;
        int bottomRow = bottom / gamePanel.tileSize;

        int tileNum1;
        int tileNum2;

        switch (entity.direction) {
            case "up":
                // Find the two tiles in front of the player
                topRow = (top - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[leftCol][topRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[rightCol][topRow];
                if (!gamePanel.tileManager.tiles[tileNum1].collision || !gamePanel.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                bottomRow = (bottom - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[leftCol][bottomRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[rightCol][bottomRow];
                if (!gamePanel.tileManager.tiles[tileNum1].collision || !gamePanel.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                leftCol = (left - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[leftCol][topRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[leftCol][bottomRow];
                if (!gamePanel.tileManager.tiles[tileNum1].collision || !gamePanel.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                rightCol = (right - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[rightCol][topRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[rightCol][bottomRow];
                if (!gamePanel.tileManager.tiles[tileNum1].collision || !gamePanel.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }


}
