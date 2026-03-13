package Main;
import java.awt.*;

public class TileManager {

    int tileSize = 64;

    int worldWidth = 3000;
    int worldHeight = 2000;

    public void draw(Graphics g, Camera cam){

        for(int x=0;x<worldWidth;x+=tileSize){

            for(int y=0;y<worldHeight;y+=tileSize){

                int drawX = x - cam.x;
                int drawY = y - cam.y;

                g.setColor(new Color(110,200,110));
                g.fillRect(drawX,drawY,tileSize,tileSize);

                g.setColor(Color.black);
                g.drawRect(drawX,drawY,tileSize,tileSize);

            }

        }

    }

}
