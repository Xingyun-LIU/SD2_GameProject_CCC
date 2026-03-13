package Main;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    CardLayout layout = new CardLayout();
    JPanel container = new JPanel(layout);

    GamePanel gamePanel;

    public GameFrame(){

        setTitle("Golden Gay Game");
        setSize(1200,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        OpeningAnimationPanel opening = new OpeningAnimationPanel(this);

        gamePanel = new GamePanel();

        container.add(opening,"opening");
        container.add(gamePanel,"game");

        add(container);

        layout.show(container,"opening");

        setVisible(true);
    }

    public void startGame(){

        layout.show(container,"game");
        gamePanel.requestFocus();

    }

}