package Main;

import gamepanel.GameFrame;
import gamepanel.GamePanel;
import npc.Gan;

import java.awt.event.*;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;
    GameFrame gameFrame;

    public boolean w, a, s, d;
    public boolean up, down, left, right;
    public boolean j, numpad1;
    public boolean f,numpad0;

    public KeyHandler(GamePanel gamePanel, GameFrame gameFrame) {
        this.gamePanel = gamePanel;
        this.gameFrame = gameFrame;
    }

    public void keyPressed(KeyEvent e) {

        // Play State
        int code = e.getKeyCode();
        if (gamePanel.gameState == gamePanel.playing) {
            if (code == KeyEvent.VK_W) w = true;
            if (code == KeyEvent.VK_A) a = true;
            if (code == KeyEvent.VK_S) s = true;
            if (code == KeyEvent.VK_D) d = true;
            if (code == KeyEvent.VK_F) f = true;

            if (code == KeyEvent.VK_UP) up = true;
            if (code == KeyEvent.VK_DOWN) down = true;
            if (code == KeyEvent.VK_LEFT) left = true;
            if (code == KeyEvent.VK_RIGHT) right = true;
            if (code == KeyEvent.VK_NUMPAD0) numpad0 = true;

            if (code == KeyEvent.VK_ESCAPE) {
                gamePanel.gameState = gamePanel.pausing;
            }
            if (code == KeyEvent.VK_M) {
                gamePanel.gameState = gamePanel.mapState;
            }

            // attack
            if (code == KeyEvent.VK_J) j = true;
            if (code == KeyEvent.VK_NUMPAD1) numpad1 = true;
        }
        // Pause State
        else if (gamePanel.gameState == gamePanel.pausing) {
            if (code == KeyEvent.VK_C) {
                gamePanel.gameState = gamePanel.playing;
            }
            if (code == KeyEvent.VK_R) {
                //return to opening animation
                gamePanel.gameState = gamePanel.opening;
                gameFrame.showOpening();
            }
        }
        // Map State
        else if (gamePanel.gameState == gamePanel.mapState) {

            if (code == KeyEvent.VK_M) {
                gamePanel.gameState = gamePanel.playing;
            }

        }
        //dialog state
        else if (gamePanel.gameState == gamePanel.dialogState) {
            Gan gan = gamePanel.activeGan;
            if (gan == null) return;

            if (gamePanel.dialogPlayer == gamePanel.player1) {
                if (code == KeyEvent.VK_W) gan.selectUp();
                if (code == KeyEvent.VK_S) gan.selectDown();
                if (code == KeyEvent.VK_F) {
                    gan.confirm(gamePanel.player1);
                    gamePanel.gameState = gamePanel.playing;
                    gamePanel.activeGan = null;
                    gamePanel.dialogPlayer = null;
                }
            } else {
                if (code == KeyEvent.VK_UP) gan.selectUp();
                if (code == KeyEvent.VK_DOWN) gan.selectDown();
                if (code == KeyEvent.VK_NUMPAD0) {
                    gan.confirm(gamePanel.player2);
                    gamePanel.gameState = gamePanel.playing;
                    gamePanel.activeGan = null;
                    gamePanel.dialogPlayer = null;
                }
            }
        }

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

        if (code == KeyEvent.VK_J) j = false;
        if (code == KeyEvent.VK_NUMPAD1) numpad1 = false;

        if (code == KeyEvent.VK_F) f = false;
        if (code == KeyEvent.VK_NUMPAD0) numpad0 = false;

    }

    public void keyTyped(KeyEvent e) {
    }

}
