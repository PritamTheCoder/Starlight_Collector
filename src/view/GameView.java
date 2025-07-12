package view;

import model.GameConfig;

import javax.swing.*;
import java.awt.*;

// Main view panel managing different game screens

public class GameView extends JPanel {
    private final CardLayout cardLayout;
    private final MenuPanel menuPanel;
    private final PlayingPanel playingPanel;
    private final GameOverPanel gameOverPanel;
    private final LevelUpPanel levelUpPanel;

    public GameView() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));

        // Initialize panels
        menuPanel = new MenuPanel(this);
        playingPanel = new PlayingPanel(this);
        gameOverPanel = new GameOverPanel(this);
        levelUpPanel = new LevelUpPanel(this);

        // Add panels to card layout
        add(menuPanel, GameState.MENU.name());
        add(playingPanel, GameState.PLAYING.name());
        add(gameOverPanel, GameState.GAME_OVER.name());
        add(levelUpPanel, GameState.LEVEL_UP.name());
    }

    // Add this method to set GameController in MenuPanel
    public void setGameController(controller.GameController gameController) {
        menuPanel.setGameController(gameController);
    }

    // Switch to a different game state
    public void switchState(GameState state) {
        cardLayout.show(this, state.name());
        switch (state) {
            case PLAYING -> {
                playingPanel.requestFocusInWindow();
                playingPanel.startGame();
            }
            case MENU -> menuPanel.requestFocusInWindow();
            case GAME_OVER -> gameOverPanel.requestFocusInWindow();
            case LEVEL_UP -> levelUpPanel.requestFocusInWindow();
        }
    }

    // Show level-up screen
    public void showLevelUp() {
        levelUpPanel.showPanel();
        cardLayout.show(this, GameState.LEVEL_UP.name());
    }

    // Getters for panels
    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public PlayingPanel getPlayingPanel() {
        return playingPanel;
    }

    public GameOverPanel getGameOverPanel() {
        return gameOverPanel;
    }
}
