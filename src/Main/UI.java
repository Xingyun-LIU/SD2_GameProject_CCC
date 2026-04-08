package Main;

import gamepanel.GamePanel;

import java.awt.*;
import Entity.Player;

public class UI {

    GamePanel gamePanel;
    Graphics2D g2;

    Font largeFont;
    Font mediumFont;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        // Initialize fonts
        largeFont = new Font("Monospaced", Font.BOLD, 80);
        mediumFont = new Font("Monospaced", Font.BOLD, 40);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // Opening state (e.g. title screen or intro video trigger)
        if (gamePanel.gameState == gamePanel.opening) {
            drawOpeningState();
        }

        // Playing state (nothing UI-heavy yet)
        if (gamePanel.gameState == gamePanel.playing) {
            // You can add HUD here later (HP, score, etc.)
        }

        // Pausing state
        if (gamePanel.gameState == gamePanel.pausing) {
            drawPauseState();
        }

        if (gamePanel.gameState == gamePanel.playing) {
            drawHUD();
        }

    }
    // Opening Screen
    public void drawOpeningState() {

        g2.setFont(largeFont);
        g2.setColor(Color.BLACK);

        String text = "MY GAME";
        int x = getCenteredTextX(text);
        int y = gamePanel.screenHeight / 2;

        g2.drawString(text, x, y);

        g2.setFont(mediumFont);

        text = "Press ENTER to Start";
        x = getCenteredTextX(text);
        y += gamePanel.tileSize * 2;

        g2.drawString(text, x, y);
    }

    // Pause Screen
    public void drawPauseState() {

        // Draw semi-transparent overlay (better UX)
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

        // Draw "PAUSED"
        g2.setFont(largeFont);
        g2.setColor(new Color(200, 200, 200));

        String text = "PAUSED";
        int x = getCenteredTextX(text);
        int y = gamePanel.screenHeight / 2;

        g2.drawString(text, x, y);

        // Draw instructions
        g2.setFont(mediumFont);

        text = "Press 'C' to continue";
        x = getCenteredTextX(text);
        y += gamePanel.tileSize * 2;
        g2.drawString(text, x, y);

        text = "Press 'R' to return to menu";
        x = getCenteredTextX(text);
        y += gamePanel.tileSize;
        g2.drawString(text, x, y);
    }

    // Utility: Center text horizontally
    public int getCenteredTextX(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gamePanel.screenWidth / 2 - length / 2;
    }
    public int getTextWidth(String text) {
        return (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    }

    // =============================
// HUD for 2 Players
// =============================
    public void drawHUD() {

        drawPlayerHUD(gamePanel.player1, true);  // Left side
        drawPlayerHUD(gamePanel.player2, false);// Right side
    }
    public void drawPlayerHUD(Object player, boolean isLeft) {

        int x;
        int y = gamePanel.tileSize;

        // Left or Right side positioning
        if (isLeft) {
            x = gamePanel.tileSize / 2;
        } else {
            x = gamePanel.screenWidth - gamePanel.tileSize * 5;
        }

        // Cast
        Entity.Entity p = (Entity.Entity) player;

        // Draw HP label
        g2.setFont(mediumFont);
        g2.setColor(Color.WHITE);
        g2.drawString(isLeft ? "P1 HP:" : "P2 HP:", x, y);

        x += gamePanel.tileSize;

        // Draw HP blocks
        for (int i = 0; i < p.maxLife; i++) {

            if (i < p.life) {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.GRAY);
            }

            g2.fillRect(x, y - 30, 20, 20);
            x += 25;
        }

        // Draw Score
        g2.setColor(Color.WHITE);
        g2.setFont(mediumFont);

        String text = (isLeft ? "P1 Score: " : "P2 Score: ");

        int textX;
        if (isLeft) {
            textX = gamePanel.tileSize / 2;
        } else {
            textX = gamePanel.screenWidth - getTextWidth(text) - gamePanel.tileSize / 2;
        }

        int textY = y + gamePanel.tileSize;

        g2.drawString(text, textX, textY);
    }




}
