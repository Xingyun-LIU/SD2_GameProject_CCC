package Main;

import java.awt.*;
public class NPC {
    int x;
    int y;
    int direction = 1;
    public NPC(int x,int y){
        this.x = x;
        this.y = y;
    }
    public void update(){
        x += direction;
        if(x > 800 || x < 200){
            direction *= -1;
        }
    }

    public void draw(Graphics g,int camX,int camY){
        int drawX = x - camX;
        int drawY = y - camY;
        g.setColor(Color.orange);
        g.fillRect(drawX,drawY,32,32);

    }
}
