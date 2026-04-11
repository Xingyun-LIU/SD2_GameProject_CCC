package tile;

import gamepanel.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**This class is used to show whole map by a miniMap, It is depended on where the player1 in.*/
public class Map extends TileManager {

    GamePanel gamePanel;
    // Store all maps.
    BufferedImage worldMap[];
    public boolean showMiniMap = false;

    public Map(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        createWorldMap();
    }

    public void createWorldMap() {

        worldMap = new BufferedImage[gamePanel.totalMap];
        int worldMapWidth = gamePanel.worldCol * gamePanel.tileSize;
        int worldMapHeight = gamePanel.worldRow * gamePanel.tileSize;

        for (int i = 0; i < gamePanel.totalMap; i++) {

            worldMap[i] = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = (Graphics2D) worldMap[i].createGraphics();

            for (int j = 0; j < gamePanel.worldRow; j++) {
                for (int k = 0; k < gamePanel.worldCol; k++) {

                    int tileNum = mapTileNum[i][k][j];
                    graphics2D.drawImage(tiles[tileNum].image, gamePanel.tileSize * k, gamePanel.tileSize * j, null);

                }
            }
        }
    }

    public void drawMiniMap(Graphics2D graphics2D) {

        System.out.println("in");
        // Draw Background
        graphics2D.setColor(Color.black);
        graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        // Draw Map
        int width = 850;
        int height = 850;
        int x = gamePanel.screenWidth / 2 - width / 2;
        int y = gamePanel.screenHeight / 2 - height / 2;
        graphics2D.drawImage(worldMap[gamePanel.player1.currentMap], x, y, width, height, null);

        // Draw Player with the exact scale
        double scale = (double) (gamePanel.tileSize * gamePanel.worldCol) / width;

        int player1X = (int) (x + gamePanel.player1.worldX / scale);
        int player1Y = (int) (y + gamePanel.player1.worldY / scale);
        int player2X = (int) (x + gamePanel.player2.worldX / scale);
        int player2Y = (int) (y + gamePanel.player2.worldY / scale);

        int playerSize = (int) (gamePanel.tileSize / scale);

        graphics2D.drawImage(gamePanel.player1.down1, player1X, player1Y, playerSize, playerSize, null);
        graphics2D.drawImage(gamePanel.player2.down1, player2X, player2Y, playerSize, playerSize, null);
    }
}
