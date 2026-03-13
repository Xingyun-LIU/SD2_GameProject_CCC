package Main;
import java.awt.*;
public class DialogueBox {
    String text = "";
    public void setText(String t){
        text = t;
    }
    public void draw(Graphics g,int width,int height){
        g.setColor(new Color(0,0,0,180));
        g.fillRect(100,height-150,width-200,100);
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD,20));
        g.drawString(text,130,height-100);

    }

}