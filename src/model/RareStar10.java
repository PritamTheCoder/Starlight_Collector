package model;

import java.util.Random;

// Very rare star collectible worth 100 points
public class RareStar10 extends GameObject {
    private static final Random RAND = new Random();

    public RareStar10(int level) {
        super(RAND.nextInt(GameConfig.WIDTH - GameConfig.OBJECT_SIZE), 0,
                GameConfig.OBJECT_SIZE, 2 + level);
    }

    // Points awarded for collecting this star
    public int getPoints() {
        return GameConfig.RARE_STAR10_POINTS;
    }
}