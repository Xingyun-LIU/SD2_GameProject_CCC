package Main;

import gamepanel.GamePanel;

import java.awt.event.*;

public class KeyHandler implements KeyListener {

    GamePanel gamePanel;

    public boolean w, a, s, d;
    public boolean up, down, left, right;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
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

    }

    public void keyTyped(KeyEvent e) {
    }

}
