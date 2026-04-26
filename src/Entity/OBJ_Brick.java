package Entity;
import gamepanel.GamePanel;
import inventory.Item;

public class OBJ_Brick extends Entity {
    public OBJ_Brick(GamePanel gp) {
        super(gp);
        name = "Brick";
        this.item = new Item();
        this.item.image = setup("/objects/brick.png", gp.tileSize, gp.tileSize);

        this.down1 = item.image;
        this.collision = true;
    }
}
