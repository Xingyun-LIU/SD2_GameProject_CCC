package Main;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    Player player1;
    Player player2;

    Camera cam1;
    Camera cam2;

    TileManager map;

    SplitScreenRenderer splitRenderer;

    KeyHandler key;

    Thread gameThread;

    public GamePanel(){

        setFocusable(true);

        key = new KeyHandler();
        addKeyListener(key);

        player1 = new Player(200,200,Color.BLUE);
        player2 = new Player(600,200,Color.RED);

        cam1 = new Camera();
        cam2 = new Camera();

        map = new TileManager();

        splitRenderer = new SplitScreenRenderer();

        startThread();
    }

    public void startThread(){

        gameThread = new Thread(this);
        gameThread.start();

    }

    public void run(){

        while(true){

            update();
            repaint();

            try{
                Thread.sleep(16);
            }catch(Exception e){}

        }

    }

    public void update(){

        player1.update(key,true);
        player2.update(key,false);

        cam1.follow(player1);
        cam2.follow(player2);

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        splitRenderer.render(g,this,player1,player2,cam1,cam2);

    }

    public void drawWorld(Graphics g, Camera cam){

        map.draw(g,cam);

        player1.draw(g,cam);
        player2.draw(g,cam);

    }

}