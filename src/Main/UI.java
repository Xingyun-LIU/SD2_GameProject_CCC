package Main;

import gamepanel.GamePanel;

import java.awt.*;

import Entity.Entity;
import Entity.Player;
import inventory.Item;
import inventory.Inventory;
import npc.Gan;

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

        if (gamePanel.gameState == gamePanel.dialogState){
            drawDialog();
        }
    }

    public void drawDialog(Gan gan, Player player) {
        int boxW = 700, boxH = 300;
        int boxX = gamePanel.screenWidth / 2 - boxW / 2;
        int boxY = gamePanel.screenHeight / 2 - boxH / 2;

        graphics2D.setColor(new Color(0, 0, 0, 210));
        graphics2D.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke(2));
        graphics2D.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        graphics2D.setFont(monospaced.deriveFont(Font.BOLD, 22f));
        graphics2D.setColor(Color.YELLOW);
        graphics2D.drawString(gan.question, boxX + 30, boxY + 50);

        graphics2D.setFont(monospaced.deriveFont(Font.PLAIN, 20f));
        int optionY = boxY + 100;
        for (int i = 0; i < gan.options.length; i++) {
            graphics2D.setColor(i == gan.selectedOption ? Color.GREEN : Color.WHITE);
            graphics2D.drawString((i == gan.selectedOption ? "> " : "  ") + gan.options[i], boxX + 40, optionY);
            optionY += 35;
        }

        graphics2D.setFont(monospaced.deriveFont(Font.ITALIC, 16f));
        graphics2D.setColor(Color.LIGHT_GRAY);
        String hint = (player == gamePanel.player1)
                ? "W/S to select   F to confirm"
                : "UP/DOWN to select   Numpad0 to confirm";
        graphics2D.drawString(hint, boxX + 30, boxY + boxH - 20);
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

        int x = gamePanel.tileSize / 2;
        int y = gamePanel.tileSize;

        Player p = (Player) player;

//        graphics2D.setFont(monospaced);
//        graphics2D.setColor(Color.WHITE);
//        graphics2D.drawString(isLeft ? "P1 HP: " + p.life : "P2 HP: " + p.life, x, y);

        for (int i = 0; i < p.maxLife; i++) {
            graphics2D.setColor(i < p.life ? Color.RED : Color.GRAY);
            graphics2D.fillRect(x, y - 30, 20, 20);
            x += 25;
        }

//        graphics2D.setColor(Color.WHITE);
//        graphics2D.setFont(monospaced);
//        int textY = y + gamePanel.tileSize;
//        graphics2D.drawString(isLeft ? "P1 Score: 5" : "P2 Score: 5", x, textY);
    }
}
