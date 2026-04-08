package Main;

import gamepanel.GameFrame;
import Video.VideoPlayer;


public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            VideoPlayer vp = new VideoPlayer();
            vp.playOpeningVideo("/video/opening.mp4", () -> {
                new GameFrame();
            });
        });

    }
}

