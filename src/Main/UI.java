package Main;

import gamepanel.GamePanel;
import Entity.Player;


import java.awt.*;

public class UI {

    GamePanel gamePanel;
    Graphics2D graphics2D;

    Font monospaced;
    Font itemFont;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        monospaced = new Font("Monospaced", Font.BOLD, 80);
        itemFont = new Font("Monospaced", Font.BOLD, 12);
    }

    public void draw(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;

        //system.out.println("Drawing UI, gameState: " + gamePanel.gameState);
        if (gamePanel.gameState == gamePanel.opening) {
            drawOpening();
            return;
        }

        if (gamePanel.gameState == gamePanel.playing) {
            drawSplitHotbar();
        }

        if (gamePanel.gameState == gamePanel.pausing) {
            drawSplitHotbar();
            drawPauseState();
        }
    }
    // 🎬 Opening UI
    public void drawOpening() {

        String text = "START GAME";
        int x = getCenteredTextX(text);
        int y = gamePanel.screenHeight / 2;

        graphics2D.setFont(monospaced);
        graphics2D.setColor(Color.white);
        graphics2D.drawString(text, x, y);

        // Define the clickable area for the start button
        gamePanel.startButtonRect = new Rectangle(x, y - 60, 300, 80);
    }
    //split hotbar for 2 players
    public void drawSplitHotbar() {

        int halfWidth = gamePanel.screenWidth / 2;

        //left screen
        Graphics2D gLeft = (Graphics2D) graphics2D.create();
        gLeft.setClip(0, 0, halfWidth, gamePanel.screenHeight);

        drawHotbar(gLeft, gamePanel.player1, 60);

        gLeft.dispose();

        //right screen
        Graphics2D gRight = (Graphics2D) graphics2D.create();
        gRight.setClip(halfWidth, 0, halfWidth, gamePanel.screenHeight);
        gRight.translate(halfWidth, 0);

        drawHotbar(gRight, gamePanel.player2, 60);

        gRight.dispose();
    }
    // Pause UI
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
    //Hotbar UI
    public void drawHotbar(Graphics2D g2, Player player, int offsetY) {

        int slotSize = 40;
        int slots = 5;

        int totalWidth = slotSize * slots;
        int startX = (gamePanel.screenWidth / 2 - totalWidth) / 2;
        int y = gamePanel.screenHeight - offsetY;

        for (int i = 0; i < slots; i++) {

            int x = startX + i * slotSize;

            //background
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(x, y, slotSize, slotSize);

            //border
            g2.setColor(Color.white);
            g2.drawRect(x, y, slotSize, slotSize);


        }}
    // Helper method to calculate the x-coordinate for centered text
    public int getCenteredTextX(String text) {
        int length = (int) graphics2D.getFontMetrics()
                .getStringBounds(text, graphics2D)
                .getWidth();

        return gamePanel.screenWidth / 2 - length / 2;
    }
}
