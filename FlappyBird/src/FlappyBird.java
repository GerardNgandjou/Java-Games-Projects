import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // Game board dimensions
    int boardHeight = 640;
    int boardWidth = 360;

    // Game images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;  // Corrected variable name (was TopPipeImg)
    Image bottomPipeImg;  // Corrected variable name (was BottomPipeImg)

    // Bird properties
    int birdHeight = 24;
    int birdWidth = 34;
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;

    // Bird class to manage bird properties
    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;  // Corrected variable name (was imge)

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipe properties
    int pipeX = boardWidth;  // Changed from boardHeight to boardWidth (corrected)
    int pipeY = 0;
    int pipeWidth = 64;    // Scaled by 1/6
    int pipeHeight = 512;

    // Pipe class to manage pipe properties
    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;  // Corrected spelling (was paased)
        
        Pipe(Image img) {
            this.img = img;
        }
    }

    // Game logic variables
    Bird bird;
    int velocityY = 0;    // Bird's vertical velocity
    int velocityX = -4;   // Pipe movement speed (negative for left movement)    
    int gravity = 1;      // Gravity effect

    ArrayList<Pipe> pipes;  // List to store pipes
    Random random = new Random();
    double score = 0;
    
    // Game timers
    Timer gameLoop;
    Timer placePipeTimer;  // Corrected variable name (was placePipTimer)
    boolean gameOver = false;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load game images
        try {
            backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
            birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
            topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
            bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }

        // Initialize bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // Timer to place new pipes
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipe();
            }
        });
        placePipeTimer.start();

        // Main game loop timer (60 FPS)
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Draw all game elements
    public void draw(Graphics g) {
        // Draw background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        
        // Draw bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // Draw pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Draw score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        if (gameOver) {
            g.drawString("Game Over: " + (int)score, 10, 35);
        } else {
            g.drawString(String.valueOf((int)score), 10, 35);
        }
    }

    // Create new pipes
    public void placePipe() {
        int openingSpace = boardHeight / 4;
        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        
        // Create top pipe
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        // Create bottom pipe (aligned with top pipe)
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    // Update game state
    public void move() {
        // Bird movement
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // Prevent bird from going above screen

        // Pipe movement and collision detection
        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            // Score increment when passing a pipe
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; // 0.5 per pipe (1 point per pair)
            }

            // Collision detection
            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        // Game over if bird falls below screen
        if (bird.y > boardHeight) {
            gameOver = true;
        }
        
        // Remove off-screen pipes to save memory
        pipes.removeIf(pipe -> pipe.x + pipe.width < 0);
    }

    // Collision detection between bird and pipe
    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   // Bird's right edge vs pipe's left edge
               a.x + a.width > b.x &&   // Bird's left edge vs pipe's right edge
               a.y < b.y + b.height &&   // Bird's bottom edge vs pipe's top edge
               a.y + a.height > b.y;     // Bird's top edge vs pipe's bottom edge
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        } else {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Flap on spacebar
            velocityY = -9;
            
            // Restart game if game over
            if (gameOver) {
                resetGame();
            }
        }
    }

    // Reset game state
    private void resetGame() {
        bird.y = birdY;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        gameLoop.start();
        placePipeTimer.start();
    }

    // Unused KeyListener methods (required by interface)
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}

