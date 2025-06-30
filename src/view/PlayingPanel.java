package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

// Gameplay panel handling rendering and game loop
public class PlayingPanel extends JPanel implements Runnable {
    private final GameView gameView;
    private GameModel model;
    private Thread gameThread;
    private ImageIcon background;

    public PlayingPanel(GameView gameView) {
        this.gameView = gameView;
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (model == null) {
                    System.err.println("Key input ignored: model is null");
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    model.moveBasketLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    model.moveBasketRight();
                }
            }
        });
    }

    public void setModel(GameModel model) {
        if (model == null) {
            System.err.println("Error: setModel received null model");
            return;
        }
        this.model = model;
        System.out.println("Set model in PlayingPanel, level: " + model.getLevel());
        loadBackground(model.getLevel());
        repaint();
    }

    private void loadBackground(int level) {
        String path = switch (level) {
            case 1, 2, 3 -> "assets/images/bg1.png";
            case 4, 5, 6 -> "assets/images/bg2.png";
            default -> "assets/images/bg3.png";
        };
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Background image not found: " + path);
            background = null;
            return;
        }
        try {
            background = new ImageIcon(path);
            if (background.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                System.out.println("Loaded background: " + path);
            } else {
                System.err.println("Failed to load background (incomplete): " + path);
                background = null;
            }
        } catch (Exception e) {
            System.err.println("Failed to load background: " + path + ", " + e.getMessage());
            background = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (model == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("No model set", 10, 30);
            System.err.println("Model is null in PlayingPanel.paintComponent");
            return;
        }

        if (background != null && background.getImage() != null) {
            g.drawImage(background.getImage(), 0, 0, this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            System.err.println("Background image is null");
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + model.getScore(), 10, 30);
        g.drawString("Level: " + model.getLevel(), 10, 60);
        g.drawString("Lives: " + model.getLives(), 10, 90);

        ImageIcon basketImage = model.getBasketImage();
        if (basketImage != null && basketImage.getImage() != null) {
            g.drawImage(basketImage.getImage(), model.getBasket().getX(), model.getBasket().getY(),
                    GameConfig.BASKET_WIDTH, GameConfig.BASKET_HEIGHT, this);
        } else {
            g.setColor(Color.RED);
            g.fillRect(model.getBasket().getX(), model.getBasket().getY(),
                    GameConfig.BASKET_WIDTH, GameConfig.BASKET_HEIGHT);
            System.err.println("Basket image is null");
        }

        for (GameObject obj : model.getObjects()) {
            ImageIcon objImage = model.getImage(obj);
            if (objImage != null && objImage.getImage() != null) {
                g.drawImage(objImage.getImage(), obj.getX(), obj.getY(),
                        GameConfig.OBJECT_SIZE, GameConfig.OBJECT_SIZE, this);
            } else {
                g.setColor(Color.RED);
                g.fillRect(obj.getX(), obj.getY(), GameConfig.OBJECT_SIZE, GameConfig.OBJECT_SIZE);
                System.err.println("Object image is null for: " + obj.getClass().getSimpleName());
            }
        }
        System.out.println("Rendered frame, objects: " + model.getObjects().size());
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1_000_000_000.0 / 60.0; // 60 FPS
        double delta = 0;
        while (model != null && (model.getState() == GameState.PLAYING || model.getState() == GameState.LEVEL_UP)) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerFrame;
            lastTime = now;
            if (delta >= 1) {
                if (model.getState() == GameState.PLAYING) {
                    model.update();
                }
                if (model.getState() == GameState.GAME_OVER) {
                    gameView.switchState(GameState.GAME_OVER);
                    break;
                } else if (model.getState() == GameState.LEVEL_UP) {
                    gameView.showLevelUp();
                }
                loadBackground(model.getLevel());
                repaint();
                delta--;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Game loop interrupted: " + e.getMessage());
            }
        }
    }

    public void startGame() {
        if (model == null) {
            System.err.println("Cannot start game: model is null");
            return;
        }
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
            System.out.println("Started game thread");
        }
    }

    public GameModel getModel() {
        return model;
    }
}