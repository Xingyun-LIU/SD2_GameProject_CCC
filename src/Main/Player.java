package Main;
import java.awt.*;

public class Player extends Entity {

    int spriteIndex = 0;
    int spriteTimer = 0;

    public Color color;

    public Player(int x,int y,Color color){

        this.x = x;
        this.y = y;
        this.color = color;

    }

    public void update(KeyHandler key, boolean p1){

        int dx = 0;
        int dy = 0;

        if(p1){

            if(key.w) dy -= speed;
            if(key.s) dy += speed;
            if(key.a) dx -= speed;
            if(key.d) dx += speed;

        }else{

            if(key.up) dy -= speed;
            if(key.down) dy += speed;
            if(key.left) dx -= speed;
            if(key.right) dx += speed;

        }

        x += dx;
        y += dy;

        //动画
        if(dx!=0 || dy!=0){

            spriteTimer++;

            if(spriteTimer > 10){
                spriteIndex = (spriteIndex+1)%3;
                spriteTimer = 0;
            }

        }

    }

    public void draw(Graphics g, Camera cam){

        int drawX = x - cam.x;
        int drawY = y - cam.y;

        g.setColor(color);

        g.fillRect(drawX,drawY,32,32);

    }

}
