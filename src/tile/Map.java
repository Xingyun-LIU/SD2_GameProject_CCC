package tile;

import gamepanel.GamePanel;

import java.awt.image.BufferedImage;

/**This class is used to show whole map by a miniMap, It is depended on where the player1 in.*/
public class Map extends TileManager {

    GamePanel gamePanel;
    BufferedImage worldMap[];
    public boolean showMiniMap = false;

    public Map(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;
    }



}
