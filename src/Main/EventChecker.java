package Main;

import Entity.Player;
import gamepanel.GamePanel;

import java.awt.*;

/**
 * This class is used to check event triggered by player.
 * It will implement a small point (rectangle) in the middle of the event tile,
 * when the player's solidArea touch the event area, the event will happen.
 * */
public class EventChecker {

    GamePanel gamePanel;

    EventRectangle eventRectangle[][];

    public EventChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        eventRectangle = new EventRectangle[gamePanel.worldCol][gamePanel.worldRow];

        for (int i = 0; i < gamePanel.worldRow; i++) {
            for (int j = 0; j < gamePanel.worldCol; j++) {
                eventRectangle[j][i] = new EventRectangle();
                eventRectangle[j][i].x = 23;
                eventRectangle[j][i].y = 23;
                eventRectangle[j][i].width = 2;
                eventRectangle[j][i].height = 2;
                eventRectangle[j][i].defaultX = eventRectangle[j][i].x;
                eventRectangle[j][i].defaultY = eventRectangle[j][i].y;

            }
        }
    }

    /** Check the type of event that player is triggered*/
    public void checkEvent(Player player) {

        //  Player must move one tile size away from event tile, can trigger the event again
        int xDistance = Math.abs(player.worldX - player.eventPreviousX);
        int yDistance = Math.abs(player.worldY - player.eventPreviousY);
        int distance = Math.max(xDistance, yDistance);

        if (distance > gamePanel.tileSize) {
            player.canTouchEvent = true;
        }

        if (player.canTouchEvent) {
            if (touch(player, 37, 43, "any")) {
                teleport(player, 22, 5);
            } else if (touch(player, 22, 5, "any")) {
                teleport(player, 37, 43);
            }
        }

    }

    // Check if player touch the event tile int world col, world row
    public boolean touch(Player player, int col, int row, String requireDirection) {

        boolean touch = false;

        player.solidArea.x = player.solidArea.x + player.worldX;
        player.solidArea.y = player.solidArea.y + player.worldY;
        eventRectangle[col][row].x = col * gamePanel.tileSize + eventRectangle[col][row].x;
        eventRectangle[col][row].y = row * gamePanel.tileSize + eventRectangle[col][row].y;

        if (player.solidArea.intersects(eventRectangle[col][row])) {
            if (player.direction.contentEquals(requireDirection) || requireDirection.contentEquals("any")) {
                touch = true;

                // Record the player's position when the event is triggered.
                player.eventPreviousX = player.worldX;
                player.eventPreviousY = player.worldY;

            }
        }

        player.solidArea.x = player.originalSolidAreaX;
        player.solidArea.y = player.originalSolidAreaY;
        eventRectangle[col][row].x = eventRectangle[col][row].defaultX;
        eventRectangle[col][row].y = eventRectangle[col][row].defaultY;

        return touch;
    }

    // The player will teleport to (col, row)
    public void teleport(Player player, int col, int row) {

        player.worldX = col * gamePanel.tileSize;
        player.worldY = row * gamePanel.tileSize;

        // After the teleport, immediately record the position to avoid teleport back and back again
        player.eventPreviousX = player.worldX;
        player.eventPreviousY = player.worldY;
        player.canTouchEvent = false;
    }

}
