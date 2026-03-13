package Main;

import gamepanel.GamePanel;

import java.awt.*;

public class UI {

    GamePanel gamePanel;
    Graphics2D graphics2D;

    Font monospaced;

    public UI(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

        monospaced = new Font("Monospaced", Font.BOLD, 80);
    }

    public void draw(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;

        graphics2D.setFont(monospaced);
        graphics2D.setColor(Color.black);

        // playing state
        if (gamePanel.gameState == gamePanel.playing) {

        }

        //pausing state
        if (gamePanel.gameState == gamePanel.pausing) {

        }
    }

    public void playOpeningAnimation() {

    }
}
