package Main;
import java.awt.*;

public class Entity {

    public int x;
    public int y;

    public int speed = 4;

    public Rectangle solidArea = new Rectangle(0,0,32,32);

    public boolean collision = false;

    public void draw(Graphics g, Camera cam){

        int drawX = x - cam.x;
        int drawY = y - cam.y;

        g.fillRect(drawX,drawY,32,32);

    }

}
