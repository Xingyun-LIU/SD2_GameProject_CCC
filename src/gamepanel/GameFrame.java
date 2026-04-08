package gamepanel;

import animation.OpeningAnimationPanel;
import Video.VideoPlayer;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    CardLayout layout = new CardLayout();
    JPanel container = new JPanel(layout);

    GamePanel gamePanel;
    OpeningAnimationPanel opening;

    public GameFrame() {
        setTitle("Golden Gay Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //set up the game panel and opening animation panel
        gamePanel = new GamePanel(this);
        opening = new OpeningAnimationPanel(this);

        //set up the container with CardLayout to hold both panels
        container.add(opening, "opening");
        container.add(gamePanel, "game");
        add(container);

        this.pack();
        setLocationRelativeTo(null);

        //start the initial flow: play video, then show opening animation, then start game
        startInitialFlow();
    }

    private void startInitialFlow() {
        //play the opening video first, and hide the main window until the video finishes
        this.setVisible(false);

        VideoPlayer videoPlayer = new VideoPlayer();
        //make sure to change this path to your actual video file path
        String videoPath = "/video/opening.mp4";

        videoPlayer.playOpeningVideo(videoPath, () -> {


            //show the main window and the opening animation after the video finishes
            this.setVisible(true);

            // (OpeningAnimationPanel)
            showOpening();
        });
    }

    //called by OpeningAnimationPanel when the animation finishes, to switch to the game panel
    public void showOpening() {
        layout.show(container, "opening");
        opening.requestFocusInWindow();
        if (gamePanel != null) {
            gamePanel.gameState = gamePanel.opening;
        }
    }

    //called by OpeningAnimationPanel when the animation finishes, to switch to the game panel
    public void startGame() {
        layout.show(container, "game");
        gamePanel.requestFocusInWindow();


        gamePanel.setUpGame();
        gamePanel.startThread();
    }
}