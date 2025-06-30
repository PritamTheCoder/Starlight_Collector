package model;

import java.awt.Rectangle;

// Represents the player-controlled basket
public class Basket {
    private int x, y; // Basket position
    private final int width, height; // Basket dimensions
    private int speed; // Movement speed

    public Basket(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = GameConfig.BASKET_WIDTH;
        this.height = GameConfig.BASKET_HEIGHT;
        this.speed = GameConfig.BASE_BASKET_SPEED;
    }

    // Move basket left, constrained within screen bounds
    public void moveLeft() {
        x = Math.max(0, x - speed);
    }

    // Move basket right, constrained within screen bounds
    public void moveRight() {
        x = Math.min(GameConfig.WIDTH - width, x + speed);
    }

    // Get collision bounds for the basket
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}