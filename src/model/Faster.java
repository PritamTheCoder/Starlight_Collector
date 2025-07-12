package model;

import java.util.Random;

public class Faster extends GameObject {
    private static final Random RAND = new Random();

    public Faster(int level) {
        super(RAND.nextInt(GameConfig.WIDTH - GameConfig.OBJECT_SIZE), 0,
              GameConfig.OBJECT_SIZE, Math.max(2, level + 1));
    }

    @Override
    public int getPoints() { return 0; }
}
