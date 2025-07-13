package view;

import model.GameConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// Game over screen panel with hover detection
public class GameOverPanel extends JPanel implements MouseListener, MouseMotionListener {
    private final GameView gameView;
    private final Rectangle menuButton = new Rectangle(300, 400, 200, 50);
    private boolean isHoveringMenu = false;
    private final int currentScore;
    private int highScore;

    public GameOverPanel(GameView gameView, int currentScore, int highScore) {
        this.gameView = gameView;
        this.currentScore = currentScore;
        this.highScore = highScore;
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
        addMouseListener(this);
        addMouseMotionListener(this); // Add motion listener
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw gradient background
        GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 30, 60), 0, getHeight(), new Color(10, 10, 30));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw "Game Over" text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 54));
        String title = "Game Over";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, 200);

        // Draw current score
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        String currentScoreText = "Score: " + currentScore;
        int currentScoreWidth = g2d.getFontMetrics().stringWidth(currentScoreText);
        g2d.drawString(currentScoreText, (getWidth() - currentScoreWidth) / 2, 280);

        // Draw high score
        String highScoreText = "High Score: " + highScore;
        int highScoreWidth = g2d.getFontMetrics().stringWidth(highScoreText);
        g2d.drawString(highScoreText, (getWidth() - highScoreWidth) / 2, 330);

        // Draw main menu button
        g2d.setFont(new Font("Arial", Font.BOLD, 26));
        drawButton(g2d, menuButton, "Main Menu", isHoveringMenu);
    }

    // Draw a styled button
    private void drawButton(Graphics2D g, Rectangle rect, String text, boolean isHover) {
        Color baseColor = new Color(70, 130, 180);
        Color hoverColor = new Color(100, 160, 210);

        // Shadow
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(rect.x + 4, rect.y + 4, rect.width, rect.height, 20, 20);

        // Button background
        g.setColor(isHover ? hoverColor : baseColor);
        g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        // Button border
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 20, 20);

        // Button text
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, textX, textY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (menuButton.contains(p)) {
            gameView.switchState(GameState.MENU);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    // Now works properly because we implement MouseMotionListener
    @Override
    public void mouseMoved(MouseEvent e) {
        boolean hovering = menuButton.contains(e.getPoint());
        if (hovering != isHoveringMenu) {
            isHoveringMenu = hovering;
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not used but required by interface
    }
}
