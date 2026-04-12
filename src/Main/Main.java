package Main;

import gamepanel.GameFrame;
import Video.VideoPlayer;


public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameFrame();
        });
    }
}

