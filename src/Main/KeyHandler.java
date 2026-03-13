package Main;
import java.awt.event.*;

public class KeyHandler implements KeyListener {

    boolean w,a,s,d;
    boolean up,down,left,right;

    public void keyPressed(KeyEvent e){

        int code = e.getKeyCode();

        if(code==KeyEvent.VK_W) w=true;
        if(code==KeyEvent.VK_A) a=true;
        if(code==KeyEvent.VK_S) s=true;
        if(code==KeyEvent.VK_D) d=true;

        if(code==KeyEvent.VK_UP) up=true;
        if(code==KeyEvent.VK_DOWN) down=true;
        if(code==KeyEvent.VK_LEFT) left=true;
        if(code==KeyEvent.VK_RIGHT) right=true;

    }

    public void keyReleased(KeyEvent e){

        int code = e.getKeyCode();

        if(code==KeyEvent.VK_W) w=false;
        if(code==KeyEvent.VK_A) a=false;
        if(code==KeyEvent.VK_S) s=false;
        if(code==KeyEvent.VK_D) d=false;

        if(code==KeyEvent.VK_UP) up=false;
        if(code==KeyEvent.VK_DOWN) down=false;
        if(code==KeyEvent.VK_LEFT) left=false;
        if(code==KeyEvent.VK_RIGHT) right=false;

    }

    public void keyTyped(KeyEvent e){}

}
