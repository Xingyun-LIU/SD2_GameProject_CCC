package Main;
import java.awt.*;

public class SplitScreenRenderer {

    public boolean split = true;

    public void render(Graphics g,
                       GamePanel panel,
                       Player p1,
                       Player p2,
                       Camera cam1,
                       Camera cam2){

        int distance = Math.abs(p1.x - p2.x);

        if(distance < 350){
            split = false;
        }else{
            split = true;
        }

        if(split){

            int half = panel.getWidth()/2;

            Graphics g1 = g.create(0,0,half,panel.getHeight());
            panel.drawWorld(g1,cam1);

            Graphics g2 = g.create(half,0,half,panel.getHeight());
            panel.drawWorld(g2,cam2);

        }
        else{

            Camera shared = new Camera();

            shared.x = (p1.x + p2.x)/2 - 400;
            shared.y = (p1.y + p2.y)/2 - 300;

            panel.drawWorld(g,shared);

        }

    }

}
