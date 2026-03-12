import java.awt.*;

public class GamePanel extends Panel {

    //Screen
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int screenCol = 30;
    public final int screenRow = 18;
    public final int screenWidth = screenCol * tileSize;
    public final int screenHeight = screenRow * tileSize;

    // World
    public final int worldCol = 128; //2048
    public final int worldRow = 128; //2048

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true); // JPanel can focus on receive key input
    }
}
