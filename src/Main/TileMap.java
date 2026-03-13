package Main;

import java.awt.*;

public class TileMap {
    int tileSize = 64;
    int cols = 40;
    int rows = 10;

    public void draw(Graphics g,int camX,int camY){
        for(int c=0;c<cols;c++){
            for(int r=0;r<rows;r++){
                int x = c*tileSize - camX;
                int y = r*tileSize - camY + 300;
                g.setColor(new Color(100,200,100));
                g.fillRect(x,y,tileSize,tileSize);
                g.setColor(Color.black);
                g.drawRect(x,y,tileSize,tileSize);

            }

        }

    }

}