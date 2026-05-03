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
 * This class is used to generate the whole map.
 * 1. Maintain an array of Tile, store all types of tiles in it, and set their index
 * 2. Read the map.txt file and put the index into a two-dimensional array
 * 3.According to the player's screenX and screenY calculate where each tile should be drawn
 */
public class TileManager {

    GamePanel gp;
    public Tile[] tiles;
    public int[][][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        mapTileNum = new int[gp.totalMap][gp.worldCol][gp.worldRow];
        tiles = new Tile[30];

        getTileImage();
        loadMap("/maps/world0.txt", 0);
        loadMap("/maps/world1.txt", 1);
        loadMap("/maps/bossWorld.txt", 2);
    }

    //Put tiles into array
    public void getTileImage() {
        setUpTile(0, true, "/tiles/plant/grass.png");
        setUpTile(2, false, "/tiles/plant/treetop.png");
        setUpTile(3, false, "/tiles/plant/treebody.png");

        setUpTile(1, true, "/tiles/runway/runway.png");
        setUpTile(4, true, "/tiles/runway/middlerunway.png");
        setUpTile(5, true, "/tiles/runway/crossrunway.png");
        setUpTile(6, true, "/tiles/runway/leftdownrunway.png");
        setUpTile(7, true, "/tiles/runway/leftuprunway.png");
        setUpTile(8, true, "/tiles/runway/rightdownrunway.png");
        setUpTile(9, true, "/tiles/runway/rightuprunway.png");

        setUpTile(10, true, "/tiles/toilet/toiletfloor.png");
        setUpTile(11, true, "/tiles/toilet/mop.png");
        setUpTile(12, true, "/tiles/toilet/pail.png");
        setUpTile(13, true, "/tiles/toilet/squat.png");

        setUpTile(14, true, "/tiles/classroom/floor.png");

        setUpTile(15, true, "/tiles/lab/labfloor.png");

        setUpTile(16,true,"/Map2/muddyRoad.png");
        setUpTile(17,true,"/Map2/marsh.png");
        setUpTile(18,true,"/Map2/water.png");
        setUpTile(19,false,"/Map2/grass.png");

    }

    // Read the index of the tiles from a .txt file, then use these number to load the whole map,
    // map is the number of current map.
    public void loadMap(String filePath, int map) {

        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.worldCol && row < gp.worldRow) {
                String line = br.readLine(); // read line of text
                String numbers[] = line.split("\\s+");

                while (col < gp.worldCol) {

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][col][row] = num;
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

    /**
     * Split draw tiles in viewWidth by camera, tiles will not be drawn out of camera
     */
    public void draw(Graphics2D g2, Camera cam, int viewWidth, int currentMap) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.worldCol && worldRow < gp.worldRow) {

            int tileNum = mapTileNum[currentMap][worldCol][worldRow];

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
