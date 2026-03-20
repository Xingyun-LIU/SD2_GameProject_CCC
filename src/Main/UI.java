package Main;
import object.Inventory;
import gamepanel.GamePanel;
import object.PickUpItem;
import Entity.Player;


import java.awt.*;
import object.Item;

public class UI {

    GamePanel gamePanel;
    Graphics2D graphics2D;

    Font monospaced;

    public UI(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

        monospaced = new Font("Monospaced", Font.BOLD, 80);
    }

    public void draw(Graphics2D graphics2D) {

            int halfWidth = gamePanel.screenWidth / 2;

            // left player1
            Graphics2D gLeft = (Graphics2D) graphics2D.create();
            gLeft.setClip(0, 0, halfWidth, gamePanel.screenHeight);

            drawHotbar(gamePanel.player1, 60);

            gLeft.dispose();

            // right player2
            Graphics2D gRight = (Graphics2D) graphics2D.create();
            gRight.setClip(halfWidth, 0, halfWidth, gamePanel.screenHeight);

            gRight.translate(halfWidth, 0);

            drawHotbar(gamePanel.player2, 60);

            gRight.dispose();


        // opening state
        if (gamePanel.gameState == gamePanel.opening){

        }

        // playing state
        if (gamePanel.gameState == gamePanel.playing) {
        }

        //pausing state
        if (gamePanel.gameState == gamePanel.pausing) {
            drawPauseState();
        }


        //hotbar state
        if(gamePanel.gameState == gamePanel.playing){
            drawHotbar(gamePanel.player1, 60);
            drawHotbar(gamePanel.player2, 110);}

        //first animation

        //second animation state

        //third animation state


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
    public Item draggedItem = null;
    public int mouseX, mouseY;
    Font ItemFont = new Font("Monospaced", Font.BOLD, 12);


    public void drawHotbar(Player player, int offsetY) {

        int slotSize = 40;
        int slots = 5;

        int totalWidth = slotSize * slots;
        int startX = (gamePanel.screenWidth - totalWidth) / 2;
        int y = gamePanel.screenHeight - offsetY;

        Font itemFont = new Font("Monospaced", Font.BOLD, 12);

        for (int i = 0; i < slots; i++) {

            int x = startX + i * slotSize;

            // background
            graphics2D.setColor(new Color(0,0,0,150));
            graphics2D.fillRect(x,y,slotSize,slotSize);

            // border
            graphics2D.setColor(Color.white);
            graphics2D.drawRect(x,y,slotSize,slotSize);

            // selected slot
            if(i == player.selectedSlot){
                graphics2D.setColor(Color.yellow);
                graphics2D.drawRect(x-2,y-2,slotSize+4,slotSize+4);
            }

            // item
            if(i < player.inventory.items.size()){

                var item = player.inventory.items.get(i);

                if(item.image != null){
                    graphics2D.drawImage(item.image,x+4,y+4,32,32,null);
                }

                if(item.amount > 1){
                    graphics2D.setFont(itemFont);
                    graphics2D.setColor(Color.white);
                    graphics2D.drawString(String.valueOf(item.amount),x+22,y+34);
                }
            }
        }
    }



}
