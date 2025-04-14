// Main class to launch the game

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        flappyBird.requestFocus();
        frame.pack();  // Size frame to preferred size of FlappyBird panel
        frame.setVisible(true);
    }
}
