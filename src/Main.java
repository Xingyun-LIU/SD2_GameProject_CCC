import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setTitle("Golden Gay Adventure");

        GamePanel gamePanel = new GamePanel();
        jFrame.add(gamePanel);
        jFrame.pack();

        jFrame.setLocationRelativeTo(null);

        jFrame.setVisible(true);
    }
}
