package model;

import java.util.Random;

// Rare star collectible worth 50 points
public class RareStar5 extends GameObject {
    private static final Random RAND = new Random();

    public RareStar5(int level) {
        super(RAND.nextInt(GameConfig.WIDTH - GameConfig.OBJECT_SIZE), 0,
                GameConfig.OBJECT_SIZE, 2 + level);
    }

    // Points awarded for collecting this star
    public int getPoints() {
        return GameConfig.RARE_STAR5_POINTS;
    }
}