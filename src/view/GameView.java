package view;

import model.GameConfig;

import javax.swing.*;
import java.awt.*;

// Main view panel managing different game screens

public class GameView extends JPanel {
    private final CardLayout cardLayout;
    private final MenuPanel menuPanel;
    private final PlayingPanel playingPanel;
    private GameOverPanel gameOverPanel;
    private final LevelUpPanel levelUpPanel;
    private controller.GameController gameController;  // Add this field

    public GameView() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));

        // Initialize panels
        menuPanel = new MenuPanel(this);
        playingPanel = new PlayingPanel(this);
        gameOverPanel = new GameOverPanel(this, 0, 0);
        levelUpPanel = new LevelUpPanel(this);

        // Add panels to card layout
        add(menuPanel, GameState.MENU.name());
        add(playingPanel, GameState.PLAYING.name());
        add(gameOverPanel, GameState.GAME_OVER.name());
        add(levelUpPanel, GameState.LEVEL_UP.name());
    }

    // Add this method to set GameController in MenuPanel and GameView
    public void setGameController(controller.GameController gameController) {
        this.gameController = gameController;  // Save reference
        menuPanel.setGameController(gameController);
        
        // ADD THIS LINE - Set the GameModel in MenuPanel so it can play sounds
        menuPanel.setGameModel(gameController.getGameModel());
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
           case GAME_OVER -> {
                // Get score and high score
                int currentScore = playingPanel.getScore();
                int highScore = gameController.getHighScore();

                // Remove old panel if it exists
                if (gameOverPanel != null) {
                    remove(gameOverPanel);
                }

                // Create new GameOverPanel with updated score
                gameOverPanel = new GameOverPanel(this, currentScore, highScore);
                add(gameOverPanel, GameState.GAME_OVER.name());

                // Show and focus
                gameOverPanel.requestFocusInWindow();
                cardLayout.show(this, GameState.GAME_OVER.name());
            }
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