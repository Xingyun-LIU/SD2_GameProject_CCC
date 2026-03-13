package Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OpeningAnimationPanel extends JPanel implements KeyListener, MouseListener {
    GameFrame frame;
    //sun position
    int sunX = 50;
    int sunY = 500;
    int targetSunY = 220;
    //cloud positions
    int cloud1X = 0;
    int cloud2X = 300;
    float titleAlpha = 0f;
    boolean showButton = false;
    Rectangle startButton;
    int time = 0;
    public OpeningAnimationPanel(GameFrame frame){
        this.frame = frame;
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        Timer timer = new Timer(16, e -> {
            update();
            repaint();
        });
        timer.start();
    }
    private void update(){
        time++;
        //clouds move
        cloud1X += 1;
        cloud2X += 2;
        if(cloud1X > getWidth()) cloud1X = -200;
        if(cloud2X > getWidth()) cloud2X = -200;
        // sun rises
        if(sunY > targetSunY){
            sunY -= 2;
            sunX += 1;
        }
        //title fade in
        if(time > 100 && titleAlpha < 1){
            titleAlpha += 0.02f;
        }
        // show button after some time
        if(time > 100){
            showButton = true;
        }
    }
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        // sky
        g2.setColor(new Color(120,190,255));
        g2.fillRect(0,0,w,h);

        // ground
        g2.setColor(new Color(80,180,80));
        g2.fillRect(0,h-150,w,150);

        //grass
        g2.setColor(new Color(60,150,60));
        for(int i=0;i<w;i+=40){
            g2.fillRect(i,h-150,20,20);
        }

        // clouds
        drawCloud(g2,cloud1X,100);
        drawCloud(g2,cloud2X,140);

        // sun
        g2.setColor(new Color(255,220,100));
        g2.fillOval(sunX,sunY,80,80);

        //title
        g2.setComposite(
                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,titleAlpha));

        g2.setColor(new Color(255,240,140));
        g2.setFont(new Font("Monospaced",Font.BOLD,80));
        String title = "Golden Gay Game";
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(title))/2;
        g2.drawString(title,tx,220);

        // START GAME button
        if(showButton){

            int bw = 220;
            int bh = 50;

            int bx = (w-bw)/2;
            int by = 300;
            startButton = new Rectangle(bx,by,bw,bh);

            g2.setColor(new Color(40,40,40));
            g2.fillRect(bx,by,bw,bh);

            g2.setColor(Color.white);
            g2.drawRect(bx,by,bw,bh);

            g2.setFont(new Font("Monospaced",Font.BOLD,24));

            String text = "START GAME";

            int tx2 = bx + (bw - g2.getFontMetrics().stringWidth(text))/2;
            int ty2 = by + 32;

            g2.drawString(text,tx2,ty2);
        }

    }

    private void drawCloud(Graphics2D g,int x,int y){

        g.setColor(Color.white);

        g.fillRect(x,y,60,20);
        g.fillRect(x+20,y-10,60,20);
        g.fillRect(x+40,y,60,20);

    }

    // ENTER key starts game
    public void keyPressed(KeyEvent e){

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            frame.startGame();
        }

    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}

    // mouse click on button starts game
    public void mouseClicked(MouseEvent e){

        if(showButton && startButton != null){

            if(startButton.contains(e.getPoint())){

                frame.startGame();

            }

        }

    }

    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
