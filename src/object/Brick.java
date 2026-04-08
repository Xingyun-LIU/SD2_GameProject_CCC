package object;

import Entity.Entity;
import gamepanel.GamePanel;

public class Brick extends Entity {
    public Brick(GamePanel gamePanel) {
        super(gamePanel);

        name = "Brick";
        down1 = setup("/objects/brick.png", gamePanel.tileSize, gamePanel.tileSize);
    }
}
