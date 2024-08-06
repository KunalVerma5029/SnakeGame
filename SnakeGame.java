import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener {
    private final int GRID_SIZE = 20;
    private final int TILE_SIZE = 30;
    private final int WIDTH = GRID_SIZE * TILE_SIZE;
    private final int HEIGHT = GRID_SIZE * TILE_SIZE;
    private final int INIT_LENGTH = 3;

    private enum Direction {UP, DOWN, LEFT, RIGHT}

    private ArrayList<Point> snake;
    private Point fruit;
    private Direction direction;
    private boolean gameOver;
    private int score;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != Direction.DOWN) direction = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != Direction.UP) direction = Direction.DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != Direction.RIGHT) direction = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != Direction.LEFT) direction = Direction.RIGHT;
                        break;
                    case KeyEvent.VK_R:
                        if (gameOver) restartGame();
                        break;
                    case KeyEvent.VK_Q:
                        System.exit(0);
                        break;
                }
            }
        });
        restartGame();
    }

    private void restartGame() {
        snake = new ArrayList<>();
        for (int i = INIT_LENGTH - 1; i >= 0; i--) {
            snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2 + i));
        }
        direction = Direction.RIGHT;
        score = 0;
        gameOver = false;
        placeFruit();
        timer = new Timer(200, this);
        timer.start();
    }

    private void placeFruit() {
        Random rand = new Random();
        fruit = new Point(rand.nextInt(GRID_SIZE), rand.nextInt(GRID_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            timer.stop();
            return;
        }

        Point head = new Point(snake.get(0));
        switch (direction) {
            case UP:
                head.translate(0, -1);
                break;
            case DOWN:
                head.translate(0, 1);
                break;
            case LEFT:
                head.translate(-1, 0);
                break;
            case RIGHT:
                head.translate(1, 0);
                break;
        }

        if (head.equals(fruit)) {
            snake.add(0, fruit);
            score++;
            placeFruit();
        } else {
            snake.add(0, head);
            snake.remove(snake.size() - 1);
        }

        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE || snake.subList(1, snake.size()).contains(head)) {
            gameOver = true;
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over! Score: " + score, WIDTH / 2 - 50, HEIGHT / 2);
            g.drawString("Press R to Restart or Q to Quit", WIDTH / 2 - 100, HEIGHT / 2 + 20);
            return;
        }

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g.setColor(Color.RED);
        g.fillRect(fruit.x * TILE_SIZE, fruit.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 10);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
