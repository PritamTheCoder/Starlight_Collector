package model;

import java.util.Random;

// Power-up that increases basket movement speed
public class Faster extends GameObject {
    private static final Random RAND = new Random();

    public Faster(int level) {
        super(RAND.nextInt(GameConfig.WIDTH - GameConfig.OBJECT_SIZE), 0,
                GameConfig.OBJECT_SIZE, 2 + level);
    }
}