package view;

import model.GameConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Game over screen panel
public class GameOverPanel extends JPanel implements MouseListener {
    private final GameView gameView;
    private final Rectangle retryButton = new Rectangle(300, 300, 200, 50);
    private final Rectangle menuButton = new Rectangle(300, 400, 200, 50);

    public GameOverPanel(GameView gameView) {
        this.gameView = gameView;
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw game over text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Game Over", 320, 200);

        // Draw buttons
        g.setFont(new Font("Arial", Font.BOLD, 24));
        drawButton(g, retryButton, "Retry");
        drawButton(g, menuButton, "Main Menu");
    }

    // Draw a button with text
    private void drawButton(Graphics g, Rectangle rect, String text) {
        g.setColor(new Color(50, 100, 200));
        g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textX = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int textY = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, textX, textY);
        g.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (retryButton.contains(p)) {
            gameView.switchState(GameState.PLAYING);
        } else if (menuButton.contains(p)) {
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
}