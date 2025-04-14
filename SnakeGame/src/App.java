import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {

        int boardWidht = 600;
        int boardHieght = boardWidht;
        
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(boardWidht, boardHieght);
        frame.setTitle("SnakeGame");
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardWidht, boardHieght);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
