package tile;

import gamepanel.GamePanel;

import java.awt.*;

/**
 *This class is used to generate the whole map.
 * 1. Maintain an array of Tile, store all types of tiles in it, and set their index
 * 2. Read the map.txt file and put the index into a two-dimensional array
 * 3.According to the player's screenX and screenY calculate where each tile should be drawn
 */
public class TileSetter {

    GamePanel gamePanel;
    public Tile[] tiles;
    public int mapTileNum[][];

    public TileSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        mapTileNum = new int[gamePanel.worldCol][gamePanel.worldRow];
        tiles = new Tile[20];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {

    }

    public void loadMap() {

    }

    public void setUpTile(int index, boolean collision, String imagePath) {

    }

    public void draw(Graphics2D graphics2D){

    }
}
