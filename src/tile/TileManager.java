package tile;

import Main.Camera;
import gamepanel.GamePanel;
import tool.ImageTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *This class is used to generate the whole map.
 * 1. Maintain an array of Tile, store all types of tiles in it, and set their index
 * 2. Read the map.txt file and put the index into a two-dimensional array
 * 3.According to the player's screenX and screenY calculate where each tile should be drawn
 */
public class TileManager {

    GamePanel gp;
    public Tile[] tiles;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        mapTileNum = new int[gp.worldCol][gp.worldRow];
        tiles = new Tile[20];

        getTileImage();
        loadMap();
    }

    //Put tiles into array
    public void getTileImage() {
        setUpTile(0, true, "/tiles/floor.png");
        setUpTile(1, false, "/tiles/plant/tree.png");

    }

    public void loadMap() {

        try {
            InputStream is = getClass().getResourceAsStream("/maps/testworld.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.worldCol && row < gp.worldRow) {
                String line = br.readLine(); // read line of text
                while (col < gp.worldCol) {

                    // catch char
                    char c = line.charAt(col);
                    // Change string into integer
                    int num = Character.getNumericValue(c);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.worldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {

        }
    }

    //Put the tiles into the array ,and set its attributes(collision ,image)
    public void setUpTile(int index, boolean collision, String imagePath) {

        try {
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            tiles[index].image = ImageTool.fixImage(tiles[index].image, gp.tileSize, gp.tileSize);
            tiles[index].collision = collision;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D g2, Camera cam,int viewWidth) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.worldCol && worldRow < gp.worldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            int screenX = worldX - cam.x;
            int screenY = worldY - cam.y;

            if (worldX + gp.tileSize > cam.x &&
                    worldX - gp.tileSize < cam.x + viewWidth &&
                    worldY + gp.tileSize > cam.y &&
                    worldY - gp.tileSize < cam.y + gp.screenHeight) {

                if (tiles[tileNum] != null) {
                    g2.drawImage(tiles[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }

            worldCol++;
            if (worldCol == gp.worldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
