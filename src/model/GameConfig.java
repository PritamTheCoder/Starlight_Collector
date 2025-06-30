package model;

// Configuration constants for the Starlight Collector game
public final class GameConfig {
    // Game window dimensions
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    // Basket dimensions
    public static final int BASKET_WIDTH = 50;
    public static final int BASKET_HEIGHT = 50;

    // Collectible object size
    public static final int OBJECT_SIZE = 20;

    // Scoring constants
    public static final int STAR_POINTS = 10;
    public static final int RARE_STAR5_POINTS = 50;
    public static final int RARE_STAR10_POINTS = 100;
    public static final int LEVEL_THRESHOLD = 50;

    // Basket movement speeds
    public static final int BASE_BASKET_SPEED = 5;
    public static final int FASTER_BASKET_SPEED = 8;

    // Power-up constants
    public static final int MAGNET_RANGE = 100;
    public static final long MAGNET_DURATION = 5000; // Duration in milliseconds

    // Prevent instantiation
    private GameConfig() {}
}