package view;

import controller.GameController;
import model.GameModel;

import javax.swing.*;
import java.awt.*;

// Main entry point for the Starlight Collector game
public class StarlightCollector {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize MVC components
            GameModel model = new GameModel();
            GameView view = new GameView();
            new GameController(model, view);

            // Set up the game window
            JFrame frame = new JFrame("Starlight Collector");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.getContentPane().setBackground(new Color(20, 20, 40));
            frame.add(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            view.switchState(GameState.MENU);
        });
    }
}