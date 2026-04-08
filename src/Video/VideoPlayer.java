package Video;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.File;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

public class VideoPlayer {

    private JFrame frame;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    /**
     * Play a video and execute a callback when the video ends.
     *
     * @param resourcePath    Path inside the resources folder (e.g., "/video/opening.mp4")
     * @param onVideoFinished Callback to execute after video ends
     */
    public void playOpeningVideo(String resourcePath, Runnable onVideoFinished) {
        // 1. Discover VLC on the system
        // Note: For macOS, ensure VLC is installed in /Applications
        new NativeDiscovery().discover();

        // 2. Resolve the resource path to an absolute path
        // In Maven, resources are inside the classpath
        String absolutePath = "";
        try {
            URL resource = getClass().getResource(resourcePath);
            if (resource != null) {
                absolutePath = new File(resource.toURI()).getAbsolutePath();
            } else {
                System.err.println("Error: Video file not found at " + resourcePath);
                if (onVideoFinished != null) onVideoFinished.run();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Create a fullscreen JFrame
        frame = new JFrame("Intro Video");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 4. Initialize the media player
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        frame.add(mediaPlayerComponent, BorderLayout.CENTER);

        // Ensure the frame is visible BEFORE playing to avoid rendering issues
        frame.setVisible(true);

        // 5. Add event listener
        mediaPlayerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                SwingUtilities.invokeLater(() -> {
                    mediaPlayerComponent.release(); // Important: release resources
                    frame.dispose();
                    if (onVideoFinished != null) {
                        onVideoFinished.run();
                    }
                });
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.err.println("VLC Error: Failed to play video.");
                SwingUtilities.invokeLater(() -> {
                    frame.dispose();
                    if (onVideoFinished != null) onVideoFinished.run();
                });
            }
        });

        // 6. Start playing using the absolute path
        mediaPlayerComponent.mediaPlayer().media().play(absolutePath);
    }
}