package model;

import java.awt.Rectangle;

// Abstract base class for all game objects
public abstract class GameObject {
    protected int x, y; // Position coordinates
    protected final int size; // Object size
    protected final int speed; // Falling speed

    protected GameObject(int x, int y, int size, int speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    // Update object position
    public void update() {
        y += speed;
    }

    // Check if object has moved off-screen
    public boolean isOffScreen() {
        return y > GameConfig.HEIGHT;
    }

    // Get collision bounds for the object
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }
}