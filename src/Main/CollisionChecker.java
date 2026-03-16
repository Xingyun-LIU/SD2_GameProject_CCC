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

//        switch ()
    }
}
