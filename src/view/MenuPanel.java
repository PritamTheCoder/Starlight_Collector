package view;

import controller.GameController;
import model.GameConfig;
import model.GameModel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// Main menu screen panel
public class MenuPanel extends JPanel implements MouseListener, MouseMotionListener {
    private final Rectangle[] buttons;
    private final String[] options = {"Start", "High Scores", "Exit"};
    private final boolean[] isHovered;
    private int highScore;
    private ImageIcon background;


    // Adding GameController and GameModel references
    private GameController gameController;
    private GameModel gameModel; 

    // Modifying constructor to accept GameController
    public MenuPanel(GameView gameView) {
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
        addMouseListener(this);
        addMouseMotionListener(this);

        buttons = new Rectangle[options.length];
        isHovered = new boolean[options.length];
        for (int i = 0; i < options.length; i++) {
            buttons[i] = new Rectangle(300, 200 + i * 100, 200, 50);
        }

        java.net.URL imgURL = getClass().getResource("/assets/images/bg1.png");
        background = new ImageIcon(imgURL);
    }

    // Adding setter for GameController
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    // Adding setter for GameModel
    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    // Update high score display
    public void setHighScore(int highScore) {
        this.highScore = highScore;
        repaint();
    }

    public void updateHighScoreFromModel() {
    if (gameModel != null) {
        this.highScore = gameModel.getHighScore();
        repaint();
    }
}
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw background image
        if (background != null) {
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
        }

        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        drawCenteredString(g, "Starlight Collector", GameConfig.WIDTH, 100);

        // Draw high score
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("High Score: " + highScore, 300, 150);

        // Draw buttons
        for (int i = 0; i < options.length; i++) {
            drawButton(g, buttons[i], options[i], isHovered[i]);
        }
    }

    // Draw centered text
    private void drawCenteredString(Graphics g, String text, int width, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    // Draw a button with hover effect
    private void drawButton(Graphics g, Rectangle rect, String text, boolean isHovered) {
        g.setColor(isHovered ? new Color(100, 150, 255) : new Color(50, 100, 200));
        g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, textX, textY);
        g.setColor(Color.WHITE);
        g.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        
        // Play click sound first
        if (gameModel != null) {
            gameModel.playClickSound();
        }

        if (buttons[0].contains(p)) {
            if (gameController != null) {
                gameController.startGame(); 
            } else {
                System.err.println("GameController not set in MenuPanel");
            }
        } else if (buttons[1].contains(p)) {
            JOptionPane.showMessageDialog(this, "High Score: " + highScore);
        } else if (buttons[2].contains(p)) {
            System.exit(0);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        boolean needsRepaint = false;
        for (int i = 0; i < buttons.length; i++) {
            boolean wasHovered = isHovered[i];
            isHovered[i] = buttons[i].contains(p);
            if (wasHovered != isHovered[i]) needsRepaint = true;
        }
        if (needsRepaint) repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
}
