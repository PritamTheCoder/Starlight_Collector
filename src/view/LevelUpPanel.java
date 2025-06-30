package view;

import model.GameConfig;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Level-up screen panel with fading effect
public class LevelUpPanel extends JPanel implements MouseListener {
    private final GameView gameView;
    private Timer fadeTimer;
    private int alpha = 255;

    public LevelUpPanel(GameView gameView) {
        this.gameView = gameView;
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
        setOpaque(false);
        addMouseListener(this);

        // Set up fade-out timer
        fadeTimer = new Timer(50, e -> {
            alpha -= 10;
            if (alpha <= 0) {
                fadeTimer.stop();
                gameView.switchState(GameState.PLAYING);
            }
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255.0f));
        g2d.setColor(new Color(0, 0, 0, alpha / 2));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(new Color(255, 215, 0, alpha));
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        drawCenteredString(g2d, "Level Up!", GameConfig.WIDTH, GameConfig.HEIGHT / 2);
        g2d.dispose();
    }

    // Draw centered text
    private void drawCenteredString(Graphics2D g2d, String text, int width, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
    }

    // Show the level-up panel
    public void showPanel() {
        alpha = 255;
        if (fadeTimer.isRunning()) {
            System.err.println("Warning: LevelUpPanel fadeTimer was already running");
            fadeTimer.stop();
        }
        fadeTimer.start();
        requestFocusInWindow(); // Ensure panel captures input
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Skip animation on click
        fadeTimer.stop();
        alpha = 0;
        gameView.switchState(GameState.PLAYING);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}