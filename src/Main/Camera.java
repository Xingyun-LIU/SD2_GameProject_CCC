package Main;
public class Camera {
    public int x;
    public int y;
    public void moveTo(int targetX, int targetY){
        x = targetX;
        y = targetY;
        if(x<targetX){
            x+=2;
        }
    }
    public void follow(Player player){
        int targetX = player.x - 400;
        int targetY = player.y - 300;
        moveTo(targetX,targetY);
    }
}
