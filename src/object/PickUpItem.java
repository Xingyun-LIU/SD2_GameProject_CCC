package object;
import Main.Camera;
import java.awt.*;

public class PickUpItem {
    /// This class represents an item that can be picked up by the player.
    public int x;
    public int y;
    public int worldX;
    public int worldY;

    public Item item;
    // This boolean indicates whether the item has been picked up by the player.
    // If true, it should not be drawn or interactable.
    public boolean picked = false;
    public PickUpItem(int x,int y,String name){
        this.x = x;
        this.y = y;
        item = new Item(name);
    }
    // This method draws the item on the screen. If the item has been picked up, it will not be drawn.
    public void draw(Graphics g, Camera cam){
        if(picked) return;
        int drawX = x - cam.x;
        int drawY = y - cam.y;
        g.setColor(Color.YELLOW);
        g.fillRect(drawX,drawY,20,20);
    }
}
