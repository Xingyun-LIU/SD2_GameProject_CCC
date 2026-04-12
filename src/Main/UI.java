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


    public void draw(Graphics2D graphics2D,Player player1, Player player2) {
        this.graphics2D = graphics2D;

        graphics2D.setFont(monospaced);
        graphics2D.setColor(Color.black);

        // opening state
        if (gamePanel.gameState == gamePanel.opening) {

        }

        // playing state
        if (gamePanel.gameState == gamePanel.playing) {
            drawInventory(gamePanel.player1, true);
            drawInventory(gamePanel.player2, false);
        }

        //pausing state
        if (gamePanel.gameState == gamePanel.pausing) {
            drawPauseState();
        }

        if (gamePanel.gameState == gamePanel.playing) {
            drawHUD();
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

    // Draw the inventory bar for the player. This will show the items in the player's inventory and highlight the selected item.
    public void drawInventory( Player p, boolean isLeft) {

        if (p == null || p.inventory == null) return;
        int slotSize = 50;
        int spacing = 10;
        int totalSlots = Math.min(5, p.inventory.length);

        int totalWidth = totalSlots * slotSize + (totalSlots - 1) * spacing;

        int startX = isLeft
                ? gamePanel.screenWidth / 4 - totalWidth / 2
                : gamePanel.screenWidth * 3 / 4 - totalWidth / 2;

        int y = gamePanel.screenHeight - slotSize - 20;

        for (int i = 0; i < totalSlots; i++) {

            System.out.println("in");

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

            graphics2D.drawRect(500, 500, 48, 48);

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

    // =============================
// HUD for 2 Players
// =============================
    public void drawHUD() {
        this.graphics2D = graphics2D;
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
       Player p = (Player) player;

        // Draw HP label
        graphics2D.setFont(monospaced);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(isLeft ? "P1 HP:5" : "P2 HP:5", x, y);

        x += gamePanel.tileSize;

        // Draw HP blocks
        for (int i = 0; i < p.maxLife; i++) {

            if (i < p.life) {
                graphics2D.setColor(Color.RED);
            } else {
                graphics2D.setColor(Color.GRAY);
            }

            graphics2D.fillRect(x, y - 30, 20, 20);
            x += 25;
        }

        // Draw Score
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(monospaced);

        String text = (isLeft ? "P1 Score: 5" : "P2 Score:5 ");

        int textX;
        if (isLeft) {
            textX = gamePanel.tileSize / 2;
        } else {
            textX = gamePanel.screenWidth - getTextWidth(text) - gamePanel.tileSize / 2;
        }

        int textY = y + gamePanel.tileSize;

        graphics2D.drawString(text, textX, textY);
        drawInventory(p, isLeft);


    }
}
