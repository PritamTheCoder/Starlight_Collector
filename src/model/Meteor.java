package model;

import java.util.Random;

// Damaging object that reduces player lives
public class Meteor extends GameObject {
    private static final Random RAND = new Random();

    public Meteor(int level) {
        super(RAND.nextInt(GameConfig.WIDTH - GameConfig.OBJECT_SIZE), 0,
              GameConfig.OBJECT_SIZE, Math.max(3, level + 3));
    }

    @Override
    public int getPoints() { return 0; }
}
