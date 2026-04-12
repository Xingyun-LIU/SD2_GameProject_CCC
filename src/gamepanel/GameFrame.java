package gamepanel;

import animation.OpeningAnimationPanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    CardLayout layout = new CardLayout();
    JPanel container = new JPanel(layout);

    GamePanel gamePanel;

    public GameFrame() {

        setTitle("Golden Gay Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel(this);

        OpeningAnimationPanel opening = new OpeningAnimationPanel(this);
        opening.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));

        container.add(opening, "opening");
        container.add(gamePanel, "game");
        add(container);

        this.pack();

        setLocationRelativeTo(null);
        layout.show(container, "opening");
        setVisible(true);
    }

    // Use setUpGame() here for press R to return to menu in pausing state
    public void startGame() {

        layout.show(container, "game");
        gamePanel.requestFocus();

        gamePanel.setUpGame();
        gamePanel.gameState = gamePanel.playing;

    }

    // Switch back to opening state
    public void showOpening() {

        layout.show(container, "opening");
        gamePanel.gameState = gamePanel.opening;
    }
}