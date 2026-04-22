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
    public void checkTile(Entity entity, int currentMap) {

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
                tileNum1 = gamePanel.tileManager.mapTileNum[currentMap][leftCol][topRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[currentMap][rightCol][topRow];
                if (!gamePanel.tileManager.tiles[tileNum1].collision || !gamePanel.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                bottomRow = (bottom - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[currentMap][leftCol][bottomRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[currentMap][rightCol][bottomRow];
                if (!gamePanel.tileManager.tiles[tileNum1].collision || !gamePanel.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                leftCol = (left - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[currentMap][leftCol][topRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[currentMap][leftCol][bottomRow];
                if (!gamePanel.tileManager.tiles[tileNum1].collision || !gamePanel.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                rightCol = (right - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[currentMap][rightCol][topRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[currentMap][rightCol][bottomRow];
                if (!gamePanel.tileManager.tiles[tileNum1].collision || !gamePanel.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    /**
     * Check if this entity crush on a player(player1 crush on player2, monster crush on a player)
     * */
    public boolean checkPlayer(Entity entity, Entity target) {

        boolean collisionPlayer = false;

        // Calculate world position of solidArea
        entity.solidArea.x = entity.solidArea.x + entity.worldX;
        entity.solidArea.y = entity.solidArea.y + entity.worldY;

        target.solidArea.x = target.solidArea.x + target.worldX;
        target.solidArea.y = target.solidArea.y + target.worldY;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }

        // intersects() check if two rectangle crush
        if (entity.solidArea.intersects(target.solidArea)) {
            entity.collisionOn = true;
            collisionPlayer = true;
        }

        // Set back to default value
        entity.solidArea.x = entity.originalSolidAreaX;
        entity.solidArea.y = entity.originalSolidAreaY;
        target.solidArea.x = target.originalSolidAreaX;
        target.solidArea.y = target.originalSolidAreaY;
        return collisionPlayer;
    }

    /**
     * If one of player crush on object, return its index in array, if a monster or npc crush on
     * object do nothing
     * */
    public int checkObject(Entity entity, boolean player) {
        //initial return index
        int index = 999;

        for (int i = 0; i < gamePanel.objects[1].length; i++) {
            if (gamePanel.objects[gamePanel.currentMap][i] != null) {

                // Get solid entity area position
                entity.solidArea.x = entity.solidArea.x + entity.worldX;
                entity.solidArea.y = entity.solidArea.y + entity.worldY;

                // Get object solid area position
                gamePanel.objects[gamePanel.currentMap][i].solidArea.x = gamePanel.objects[gamePanel.currentMap][i].worldX + gamePanel.objects[gamePanel.currentMap][i].solidArea.x;
                gamePanel.objects[gamePanel.currentMap][i].solidArea.y = gamePanel.objects[gamePanel.currentMap][i].worldY + gamePanel.objects[gamePanel.currentMap][i].solidArea.y;

                // Predict the position of the next frame
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                // intersects() check if two rectangle crush
                if (entity.solidArea.intersects(gamePanel.objects[gamePanel.currentMap][i].solidArea)) {
                    if (gamePanel.objects[gamePanel.currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                // Set back to default
                entity.solidArea.x = entity.originalSolidAreaX;
                entity.solidArea.y = entity.originalSolidAreaY;
                gamePanel.objects[gamePanel.currentMap][i].solidArea.x = gamePanel.objects[gamePanel.currentMap][i].originalSolidAreaX;
                gamePanel.objects[gamePanel.currentMap][i].solidArea.y = gamePanel.objects[gamePanel.currentMap][i].originalSolidAreaY;
            }
        }
        return index;
    }

    /**
     * Check if one entity crush on target entity, return the index of target.
     * */
    public int checkEntity(Entity entity, Entity[][] target, int currentMap) {

        int index = 999;

        for (int i = 0; i < target[currentMap].length; i++) {
            if (target[currentMap][i] != null) {

                // Get solid entity area position
                entity.solidArea.x = entity.solidArea.x + entity.worldX;
                entity.solidArea.y = entity.solidArea.y + entity.worldY;

                // Get object solid area position
                target[currentMap][i].solidArea.x = target[currentMap][i].worldX + target[currentMap][i].solidArea.x;
                target[currentMap][i].solidArea.y = target[currentMap][i].worldY + target[currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                if (entity.solidArea.intersects(target[currentMap][i].solidArea)) {

                    // if entity not equals to itself, set true
                    if (target[currentMap][i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                entity.solidArea.x = entity.originalSolidAreaX;
                entity.solidArea.y = entity.originalSolidAreaY;
                target[currentMap][i].solidArea.x = target[currentMap][i].originalSolidAreaX;
                target[currentMap][i].solidArea.y = target[currentMap][i].originalSolidAreaY;
            }

        }
        return index;
    }
}
