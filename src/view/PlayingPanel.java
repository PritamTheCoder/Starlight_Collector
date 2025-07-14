package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayingPanel extends JPanel implements Runnable, KeyListener {
    private final GameView gameView;
    private GameModel model;
    private Thread gameThread;
    private ImageIcon background;

    public PlayingPanel(GameView gameView) {
        this.gameView = gameView;
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
        setFocusable(true);
        requestFocusInWindow(); 
        addKeyListener(this);  
    }

    public void setModel(GameModel model) {
        if (model == null) {
            System.err.println("Error: setModel received null model");
            return;
        }
        this.model = model;
        System.out.println("Set model in PlayingPanel, level: " + model.getLevel() + ", state: " + model.getState());
        loadBackground(model.getLevel());
        repaint();
    }

    private void loadBackground(int level) {
        String path = switch (level) {
            case 1, 2, 3 -> "/assets/images/bg1.png";
            case 4, 5, 6 -> "/assets/images/bg2.png";
            default -> "/assets/images/bg3.png";
        };
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            System.err.println("Background image not found: " + path);
            background = null;
            return;
        }
        background = new ImageIcon(imgURL);
        System.out.println("Loaded background: " + path);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (model == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("No model set", 10, 30);
            return;
        }

        // Draw background image
        if (background != null && background.getImage() != null) {
            g.drawImage(background.getImage(), 0, 0, this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw score, level, lives
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + model.getScore(), 10, 30);
        g.drawString("Level: " + model.getLevel(), 10, 60);
        g.drawString("Lives: " + model.getLives(), 10, 90);

        // Draw basket
        ImageIcon basketImage = model.getBasketImage();
        if (basketImage != null && basketImage.getImage() != null) {
            g.drawImage(basketImage.getImage(), model.getBasket().getX(), model.getBasket().getY(),
                    GameConfig.BASKET_WIDTH, GameConfig.BASKET_HEIGHT, this);
        } else {
            g.setColor(Color.RED);
            g.fillRect(model.getBasket().getX(), model.getBasket().getY(),
                    GameConfig.BASKET_WIDTH, GameConfig.BASKET_HEIGHT);
        }

        // Draw all falling objects (stars, meteors, etc.)
        for (GameObject obj : model.getObjects()) {
            ImageIcon objImage = model.getImage(obj); // Image from GameModel
            if (objImage != null && objImage.getImage() != null) {
                g.drawImage(objImage.getImage(), obj.getX(), obj.getY(),
                        GameConfig.OBJECT_SIZE, GameConfig.OBJECT_SIZE, this);
            } else {
                g.setColor(Color.RED);
                g.fillRect(obj.getX(), obj.getY(), GameConfig.OBJECT_SIZE, GameConfig.OBJECT_SIZE);
            }
        }
    }

    @Override
    public void run() {
        System.out.println("PlayingPanel.run() started, initial model state: " + (model != null ? model.getState() : "null"));
        long lastTime = System.nanoTime();
        double nsPerFrame = 1_000_000_000.0 / 60.0; // 60 FPS
        double delta = 0;
        while (model != null && (model.getState() == GameState.PLAYING || model.getState() == GameState.LEVEL_UP)) {
            System.out.println("PlayingPanel.run() loop, model state: " + model.getState());
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerFrame;
            lastTime = now;
            if (delta >= 1) {
                System.out.println("Game loop tick - model state: " + model.getState());
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
        System.out.println("PlayingPanel.run() loop exited");
    }

    public void startGame() {
        if (model == null) {
            System.err.println("Cannot start game: model is null");
            return;
        }
        requestFocus();  // Ensure focus for key events
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
            System.out.println("Started game thread");
        }
    }

    public GameModel getModel() {
        return model;
    }

    //KEYLISTENER METHODS
    @Override
    public void keyPressed(KeyEvent e) {
        if (model == null) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> model.moveBasketLeft();
            case KeyEvent.VK_RIGHT -> model.moveBasketRight();
        }

        repaint(); // optional: immediate redraw
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public int getScore() {
    return model != null ? model.getScore() : 0;
}
}
