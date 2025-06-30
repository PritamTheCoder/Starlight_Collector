package model;

import java.util.Random;

// Power-up that attracts stars to the basket
public class Magnet extends GameObject {
    private static final Random RAND = new Random();

    public Magnet(int level) {
        super(RAND.nextInt(GameConfig.WIDTH - GameConfig.OBJECT_SIZE), 0,
                GameConfig.OBJECT_SIZE, 2 + level);
    }
}