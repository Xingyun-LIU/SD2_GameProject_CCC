package tile;

import gamepanel.GamePanel;

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

    public void setUpTile(int index, boolean collision,String imagePath){

    }
}
