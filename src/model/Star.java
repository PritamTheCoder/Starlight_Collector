package model;

import java.util.Random;

public class Star extends GameObject {
    private static final Random RAND = new Random();

    public Star(int level) {
        super(RAND.nextInt(GameConfig.WIDTH - GameConfig.OBJECT_SIZE), 0,
              GameConfig.OBJECT_SIZE, Math.max(2, level));
    }

    @Override
    public int getPoints() { return 1; }
}
