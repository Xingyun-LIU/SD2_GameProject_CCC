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

        // opening state
        if (gamePanel.gameState == gamePanel.opening) {

        }

        // playing state
        if (gamePanel.gameState == gamePanel.playing) {

        }

        //pausing state
        if (gamePanel.gameState == gamePanel.pausing) {
            drawPauseState();
        }
    }


    public void drawPauseState() {

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 80F));
        graphics2D.setColor(new Color(40, 40, 40));

        String text = "PAUSED";
        int x = getCenteredTextX(text);
        int y = gamePanel.screenHeight / 2;
        graphics2D.drawString(text, x, y);

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 40F));

        text = "Press 'C' to continue";
        x = getCenteredTextX(text);
        y += gamePanel.tileSize * 4;
        graphics2D.drawString(text, x, y);

        text = "Press 'R' to return to menu";
        x = getCenteredTextX(text);
        y += gamePanel.tileSize;
        graphics2D.drawString(text, x, y);
    }

    public int getCenteredTextX(String text) {
        int x;
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        x = gamePanel.screenWidth / 2 - length / 2;

        return x;
    }
}
