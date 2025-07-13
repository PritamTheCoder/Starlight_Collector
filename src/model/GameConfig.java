package model;

// Configuration constants for the Starlight Collector game
public final class GameConfig {
    // Game window dimensions
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    // Basket dimensions
    public static final int BASKET_WIDTH = 100;
    public static final int BASKET_HEIGHT = 100;

    // Collectible object size
    public static final int OBJECT_SIZE = 50;

    // Scoring constants
    public static final int STAR_POINTS = 1;
    public static final int RARE_STAR5_POINTS = 5;
    public static final int RARE_STAR10_POINTS = 10;
    public static final int LEVEL_THRESHOLD = 50;

    // Basket movement speeds
    public static final int BASE_BASKET_SPEED = 7;
    public static final int FASTER_BASKET_SPEED = 10;

    // Power-up constants
    public static final int MAGNET_RANGE = 10;
    public static final long MAGNET_DURATION = 500; // Duration in milliseconds

    // Prevent instantiation
    private GameConfig() {}
}
