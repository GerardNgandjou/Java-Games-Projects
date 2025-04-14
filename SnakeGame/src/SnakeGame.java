
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;


public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private class Title {
        int x;
        int y;

        Title(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    int boardHieght;
    int boardWidht;
    int titleSize = 25;

    //Snake
    Title snakeHead;
    ArrayList<Title> snakeBody;

    //food
    Title food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidht, int boardHieght){
        this.boardWidht = boardWidht;
        this.boardHieght = boardHieght;
        setPreferredSize(new Dimension(boardWidht, boardHieght));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Title(5, 5);
        snakeBody = new ArrayList<Title>();

        food = new Title(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //Grid 
        // for (int i = 0; i < boardWidht / titleSize; i++) {
        //     //(x1, y1, x2, y2)
        //     g.drawLine(i * titleSize, 0, i * titleSize, boardHieght);
        //     g.drawLine(0, i * titleSize, boardWidht, i * titleSize);
        // }

        //food
        g.setColor(Color.RED);
        //g.fillRect(food.x * titleSize, food.y * titleSize, titleSize, titleSize);
        g.fill3DRect(food.x * titleSize, food.y * titleSize, titleSize, titleSize, true);
        
        // Snake Head
        g.setColor(Color.GREEN);
        //g.fillRect(food.x * titleSize, food.y * titleSize, titleSize, titleSize);
        g.fill3DRect(snakeHead.x * titleSize, snakeHead.y * titleSize, titleSize, titleSize, true);

        //Snake body
        for (int i = 0; i < snakeBody.size(); i++) {
            Title snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * titleSize, snakePart.y * titleSize, titleSize, titleSize);
            g.fill3DRect(snakePart.x * titleSize, snakePart.y * titleSize, titleSize, titleSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), titleSize - 16, titleSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), titleSize - 16, titleSize);
        }

    }

    public void placeFood() {
        food.x = random.nextInt(boardWidht / titleSize); //600/25 = 24
        food.y = random.nextInt(boardHieght / titleSize);
    }

    public boolean collision(Title title1, Title title2) {
        return title1.x == title2.x && title1.y == title2.y;
    }

    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Title(food.x, food.y));
            placeFood(); 
        }

        //Snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Title snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Title prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //gsme over conditions
        for (int i =0; i < snakeBody.size(); i++) {
            Title snakePart = snakeBody.get(i);
            //collide with the snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            } 
        }

        if (snakeHead.x * titleSize < 0 || snakeHead.x * titleSize > boardWidht ||
            snakeHead.y * titleSize < 0 || snakeHead.y * titleSize > boardHieght ) {
            gameOver = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");

        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");

        if(arg0.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } 
        else if(arg0.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } 
        else if(arg0.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } 
        else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        } 

    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}

}
