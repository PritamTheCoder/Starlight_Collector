package controller;

import model.GameModel;
import view.PlayingPanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayingController extends KeyAdapter {
    private final GameModel model;
    private final PlayingPanel panel;

    public PlayingController(GameModel model, PlayingPanel panel) {
        this.model = model;
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (model == null) {
            System.err.println("Key input ignored: model is null");
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> model.moveBasketLeft();
            case KeyEvent.VK_RIGHT -> model.moveBasketRight();
        }

        panel.repaint(); // optional: forces redraw
    }
}
