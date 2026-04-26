package Main;

import gamepanel.GamePanel;

import java.awt.*;

import Entity.Entity;
import Entity.Player;
import inventory.Item;
import inventory.Inventory;

public class UI {

    GamePanel gamePanel;
    Graphics2D graphics2D;

    Font monospaced;

    public UI(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

        monospaced = new Font("Monospaced", Font.PLAIN, 20);
    }

    public void setGraphics2D(Graphics2D g2) {
        this.graphics2D = graphics2D;
    }

    public void drawSplitScreen(Graphics2D graphics2D, Player player, boolean isLeft) {
        this.graphics2D = graphics2D;

        graphics2D.setFont(monospaced);
        graphics2D.setColor(Color.black);

        // playing state
        if (gamePanel.gameState == gamePanel.playing) {

            drawPlayerHUD(player, isLeft);
            drawInventory(player, isLeft);
        }
    }

    public void drawFullScreen(Graphics2D graphics2D) {

        this.graphics2D = graphics2D;

        graphics2D.setFont(monospaced);
        graphics2D.setColor(Color.black);

        //pausing state
        if (gamePanel.gameState == gamePanel.pausing) {
            drawPauseState();
        }

        //draw HUD for playing and game over state
        drawHUD();

        //if game over, draw result messages
        if (gamePanel.gameState == gamePanel.gameOverState) {
            drawResultMessages();
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

    // Draw the inventory bar for the player.
    // This will show the items in the player's inventory and highlight the selected item.
    public void drawInventory(Player p, boolean isLeft) {

        if (p == null || p.inventory == null) return;
        int slotSize = 50;
        int spacing = 10;
        int totalSlots = Math.min(5, p.inventory.length);
        int totalWidth = totalSlots * slotSize + (totalSlots - 1) * spacing;

        int startX = gamePanel.screenWidth / 4 - totalWidth / 2;

        int y = gamePanel.screenHeight - slotSize - 100;

        for (int i = 0; i < totalSlots; i++) {

            int x = startX + i * (slotSize + spacing);

            graphics2D.setColor(new Color(0, 0, 0, 180));
            graphics2D.fillRect(x, y, slotSize, slotSize);

            if (i == p.selectedItem) {
                graphics2D.setColor(Color.YELLOW);
                graphics2D.setStroke(new BasicStroke(3));
            } else {
                graphics2D.setColor(Color.WHITE);
                graphics2D.setStroke(new BasicStroke(2));
            }

            graphics2D.drawRect(x, y, slotSize, slotSize);

            if (p.inventory[i] != null && p.inventory[i].image != null) {
                graphics2D.drawImage(p.inventory[i].image, x + 5, y + 5,
                        slotSize - 10, slotSize - 10, null);
            }
        }
    }


    // Utility: Center text horizontally
    public int getCenteredTextX(String text) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return gamePanel.screenWidth / 2 - length / 2;
    }

    public int getTextWidth(String text) {
        return (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
    }

    // For test use
    public void drawHUD() {
        drawPlayerHUD(gamePanel.player1, true);  // Left side
        drawPlayerHUD(gamePanel.player2, false);// Right side
    }

    // HUD for 2 players
    public void drawPlayerHUD(Object player, boolean isLeft) {

        int x ;
        int y = gamePanel.tileSize;

        Player p = (Player) player;


        if (isLeft) {
            // left half screen (from left edge to center)
            x = gamePanel.tileSize / 2;
        } else {
            // right half screen (from center to right edge)
            x = gamePanel.screenWidth / 2 + gamePanel.tileSize / 2;
        }

        for (int i = 0; i < p.maxLife; i++) {
            graphics2D.setColor(i < p.life ? Color.RED : Color.GRAY);
            graphics2D.fillRect(x, y - 30, 20, 20);
            x += 25;
        }

    }

    private void drawResultMessages() {
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 60F));

        //calculate center positions for both players
        int leftCenterX = gamePanel.screenWidth / 4;
        int rightCenterX = (gamePanel.screenWidth / 4) * 3;
        int centerY = gamePanel.screenHeight / 2;

        //player 1 (left screen)
        if (gamePanel.player1.life <= 0) {
            drawCenteredText("YOU LOSE!!!", Color.RED, leftCenterX, centerY);
        } else {
            drawCenteredText("YOU WIN!!!", Color.YELLOW, leftCenterX, centerY);
        }

        //player 2 (right screen)
        if (gamePanel.player2.life <= 0) {
            drawCenteredText("YOU LOSE!!!", Color.RED, rightCenterX, centerY);
        } else {
            drawCenteredText("YOU WIN!!!", Color.YELLOW, rightCenterX, centerY);
        }
    }

    private void drawCenteredText(String text, Color color, int centerX, int y) {
        graphics2D.setColor(Color.BLACK);
        int x = centerX - (int)graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth() / 2;
        graphics2D.drawString(text, x + 3, y + 3);

        graphics2D.setColor(color);
        graphics2D.drawString(text, x, y);
    }
}
