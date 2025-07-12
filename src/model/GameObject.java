package model;

import java.awt.Rectangle;

public abstract class GameObject {
    protected int x, y, size;
    protected int speed;

    protected GameObject(int x, int y, int size, int speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    public int getSpeed() { return speed; }
    public void update() { y += speed; }
    public boolean isOffScreen() { return y > GameConfig.HEIGHT; }
    public Rectangle getBounds() { return new Rectangle(x, y, size, size); }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public int getSize() { return size; }

    // Points awarded for collecting this object
    public abstract int getPoints();
}

