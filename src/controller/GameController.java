package controller;

import model.GameModel;
import view.GameState;
import view.GameView;

// Coordinates interactions between model and view
public class GameController {
    private final GameModel model;
    private final GameView view;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        updateView();
    }

    // Add this method to provide high score
    public int getHighScore() {
        return model.getHighScore();
    }
    // Update view with model data
    public void updateView() {
        if (model == null) {
            System.err.println("Error: model is null in GameController.updateView");
            return;
        }
        System.out.println("Updating view with high score: " + model.getHighScore());
        view.getMenuPanel().setHighScore(model.getHighScore());
        view.getPlayingPanel().setModel(model);
    }

    // Start a new game
    public void startGame() {
        System.out.println("Starting game, setting state to PLAYING");
        model.setState(GameState.PLAYING);
        view.switchState(GameState.PLAYING);
        updateView();
        // Start the game loop thread after model state is set and PlayingPanel model updated
        view.getPlayingPanel().startGame();
    }

    // Return to main menu
    public void goToMenu() {
        System.out.println("Returning to menu, setting state to MENU");
        model.setState(GameState.MENU);
        view.switchState(GameState.MENU);
        updateView();
    }
}
