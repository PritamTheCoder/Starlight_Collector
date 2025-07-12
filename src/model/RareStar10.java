package model;

import java.util.Random;

public class RareStar10 extends GameObject {
    private static final Random RAND = new Random();

    public RareStar10(int level) {
        super(RAND.nextInt(GameConfig.WIDTH - GameConfig.OBJECT_SIZE), 0,
              GameConfig.OBJECT_SIZE, Math.max(2, level + 2));
    }

    @Override
    public int getPoints() { return 10; }
}
