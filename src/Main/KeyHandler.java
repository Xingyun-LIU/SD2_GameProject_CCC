package Main;

import gamepanel.GameFrame;
import gamepanel.GamePanel;

import java.awt.event.*;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;
    GameFrame gameFrame;

    public boolean w, a, s, d;
    public boolean up, down, left, right;

    public KeyHandler(GamePanel gamePanel, GameFrame gameFrame) {
        this.gamePanel = gamePanel;
        this.gameFrame = gameFrame;
    }

    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        if (gamePanel.gameState == gamePanel.playing) {
            if (code == KeyEvent.VK_W) w = true;
            if (code == KeyEvent.VK_A) a = true;
            if (code == KeyEvent.VK_S) s = true;
            if (code == KeyEvent.VK_D) d = true;

            if (code == KeyEvent.VK_UP) up = true;
            if (code == KeyEvent.VK_DOWN) down = true;
            if (code == KeyEvent.VK_LEFT) left = true;
            if (code == KeyEvent.VK_RIGHT) right = true;

            if (code == KeyEvent.VK_ESCAPE) {
                gamePanel.gameState = gamePanel.pausing;
            }
        } else if (gamePanel.gameState == gamePanel.pausing) {
            if (code == KeyEvent.VK_C) {
                gamePanel.gameState = gamePanel.playing;
            }
            if (code == KeyEvent.VK_R) {
                //return to opening animation
                gamePanel.gameState = gamePanel.opening;
                gameFrame.showOpening();
            }
        }

        // player 1 hotbar: 1~5
        if(code == KeyEvent.VK_1) gamePanel.player1.selectedSlot = 0;
        if(code == KeyEvent.VK_2) gamePanel.player1.selectedSlot = 1;
        if(code == KeyEvent.VK_3) gamePanel.player1.selectedSlot = 2;
        if(code == KeyEvent.VK_4) gamePanel.player1.selectedSlot = 3;
        if(code == KeyEvent.VK_5) gamePanel.player1.selectedSlot = 4;

        // player 2 hotbar: 6~0
        if(code == KeyEvent.VK_6) gamePanel.player2.selectedSlot = 0;
        if(code == KeyEvent.VK_7) gamePanel.player2.selectedSlot = 1;
        if(code == KeyEvent.VK_8) gamePanel.player2.selectedSlot = 2;
        if(code == KeyEvent.VK_9) gamePanel.player2.selectedSlot = 3;
        if(code == KeyEvent.VK_0) gamePanel.player2.selectedSlot = 4;

    }

    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) w = false;
        if (code == KeyEvent.VK_A) a = false;
        if (code == KeyEvent.VK_S) s = false;
        if (code == KeyEvent.VK_D) d = false;

        if (code == KeyEvent.VK_UP) up = false;
        if (code == KeyEvent.VK_DOWN) down = false;
        if (code == KeyEvent.VK_LEFT) left = false;
        if (code == KeyEvent.VK_RIGHT) right = false;

    }

    public void keyTyped(KeyEvent e) {
    }


    public void mouseWheelMoved(MouseWheelEvent e){

        int rotation = e.getWheelRotation();
        // player 1 hotbar selection with mouse wheel
        if(rotation > 0){
            gamePanel.player1.selectedSlot++;
        }else{
            gamePanel.player1.selectedSlot--;
        }

        //loop hotbar selection
        if(gamePanel.player1.selectedSlot < 0){
            gamePanel.player1.selectedSlot = 4;
        }
        //loop hotbar selection
        if(gamePanel.player1.selectedSlot > 4){
            gamePanel.player1.selectedSlot = 0;
        }
    }




}
