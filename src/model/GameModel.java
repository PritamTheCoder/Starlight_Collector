package model;

import view.GameState;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.Rectangle;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// Manages game state and core logic
public class GameModel {
    private Basket basket;
    private final ArrayList<GameObject> objects;
    private int score;
    private int level;
    private int lives;
    private long lastObjectTime;
    private int highScore;
    private GameState state;
    private ImageIcon basketImage, starImage, rareStar5Image, rareStar10Image, meteorImage, fasterImage, magnetImage;
    private Clip catchSound, missSound, gameOverSound, backgroundMusic;
    private boolean magnetActive;
    private long magnetStartTime;
    private static final Random RAND = new Random();

    public GameModel() {
        objects = new ArrayList<>();
        initGame();
        loadImages();
        loadSounds();
    }

    private void initGame() {
        basket = new Basket(GameConfig.WIDTH / 2, GameConfig.HEIGHT - GameConfig.BASKET_HEIGHT - 20);
        objects.clear();
        score = 0;
        level = 1;
        lives = 3;
        lastObjectTime = System.currentTimeMillis();
        // Remove resetting state here to avoid overwriting setState calls
        // state = GameState.MENU;
        magnetActive = false;
        stopBackgroundMusic();
        loadHighScore();
    }

    private void loadImages() {
        basketImage = loadImage("assets/images/basket.png");
        starImage = loadImage("assets/images/star.png");
        rareStar5Image = loadImage("assets/images/rare_star.png");
        rareStar10Image = loadImage("assets/images/bonus_star.png");
        meteorImage = loadImage("assets/images/meteor.png");
        fasterImage = loadImage("assets/images/powerup_speed.png");
        magnetImage = loadImage("assets/images/powerup_magnet.png");
    }

    private ImageIcon loadImage(String path) {
        java.net.URL imgURL = getClass().getResource("/" + path);
        if (imgURL == null) {
            System.err.println("Image resource not found: " + path);
            return null;
        }
        System.out.println("Loaded image: " + path);
        return new ImageIcon(imgURL);
    }

    private void loadSounds() {
        try {
            catchSound = loadClip("assets/sounds/catch.wav");
            missSound = loadClip("assets/sounds/miss.wav");
            gameOverSound = loadClip("assets/sounds/gameover.wav");
            backgroundMusic = loadClip("assets/sounds/bgm.wav");
            if (backgroundMusic != null) {
                FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-10.0f);
            }
        } catch (Exception e) {
            catchSound = null;
            missSound = null;
            gameOverSound = null;
            backgroundMusic = null;
            System.err.println("Failed to load sounds: " + e.getMessage());
        }
    }

    private Clip loadClip(String path) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        try (InputStream audioSrc = getClass().getResourceAsStream("/" + path)) {
            if (audioSrc == null) {
                System.err.println("Sound resource not found: " + path);
                return null;
            }
            // Add buffering for mark/reset support
            try (InputStream bufferedIn = new BufferedInputStream(audioSrc)) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                audioIn.close();
                System.out.println("Loaded sound: " + path);
                return clip;
            }
        }
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void startBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Started background music");
        } else if (backgroundMusic == null) {
            System.err.println("Background music clip is null");
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            System.out.println("Stopped background music");
        }
    }

    private void loadHighScore() {
        File file = new File("res/highscore.txt");
        if (!file.exists()) {
            System.err.println("High score file not found: res/highscore.txt");
            highScore = 0;
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String scoreStr = br.readLine();
            highScore = (scoreStr != null && !scoreStr.isEmpty()) ? Integer.parseInt(scoreStr) : 0;
            System.out.println("Loaded high score: " + highScore);
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
            System.err.println("Failed to load high score: " + e.getMessage());
        }
    }

    private void saveHighScore() {
        if (score <= highScore) return;
        highScore = score;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/highscore.txt"))) {
            bw.write(String.valueOf(highScore));
            System.out.println("Saved high score: " + highScore);
        } catch (IOException e) {
            System.err.println("Failed to save high score: " + e.getMessage());
        }
    }

    public void update() {
    if (state != GameState.PLAYING) {
        System.out.println("GameModel update skipped, state: " + state);
        return;
    }

    // Spawn new objects based on level/timer
    if (System.currentTimeMillis() - lastObjectTime > 1000 / level) {
        spawnObject();
        lastObjectTime = System.currentTimeMillis();
    }

    ArrayList<GameObject> toRemove = new ArrayList<>();

    for (GameObject obj : objects) {
        // Magnet effect
        if (magnetActive && isCollectibleStar(obj)) {
            applyMagnetEffect(obj);
        }

        System.out.println("Updating object at position: (" + obj.getX() + ", " + obj.getY() + ") with speed " + obj.getSpeed());
        obj.update(); // Update the object's position
        System.out.println("Updated object position to: (" + obj.getX() + ", " + obj.getY() + ")");

        // Remove if off screen
        if (obj.isOffScreen()) {
            toRemove.add(obj);
            if (!(obj instanceof Meteor)) {
                lives--;
                playSound(missSound);
                if (lives <= 0) {
                    saveHighScore();
                    state = GameState.GAME_OVER;
                    playSound(gameOverSound);
                    stopBackgroundMusic();
                }
            }
        }
        // Collision with basket
        else if (obj.getBounds().intersects(basket.getBounds())) {
            toRemove.add(obj);
            handleCollision(obj); // Increase score, spawn new star, etc.
            playSound(catchSound);
        }
    }
    objects.removeAll(toRemove);

    checkCollisions();  // Add this call to handle star removal and spawning
}
    private boolean isCollectibleStar(GameObject obj) {
        return obj instanceof Star || obj instanceof RareStar5 || obj instanceof RareStar10;
    }

    private void applyMagnetEffect(GameObject obj) {
        int dx = basket.getX() + basket.getWidth() / 2 - (obj.getX() + obj.getSize() / 2);
        int dist = (int) Math.sqrt(dx * dx);
        if (dist < GameConfig.MAGNET_RANGE && dist > 0) {
            obj.setX(obj.getX() + (dx * 5 / dist));
        }
    }

    private void spawnObject() {
        double roll = RAND.nextDouble();
        if (roll < 0.02) {
            objects.add(new RareStar10(level));
            System.out.println("Spawned RareStar10");
        } else if (roll < 0.07) {
            objects.add(new RareStar5(level));
            System.out.println("Spawned RareStar5");
        } else if (roll < 0.17) {
            objects.add(new Meteor(level));
            System.out.println("Spawned Meteor");
        } else if (roll < 0.22) {
            objects.add(new Faster(level));
            System.out.println("Spawned Faster");
        } else if (roll < 0.27) {
            objects.add(new Magnet(level));
            System.out.println("Spawned Magnet");
        } else {
            objects.add(new Star(level));
            System.out.println("Spawned Star");
        }
    }

    private void handleCollision(GameObject obj) {
        if (obj instanceof Star star) {
            score += star.getPoints();
        } else if (obj instanceof RareStar5 rareStar5) {
            score += rareStar5.getPoints();
        } else if (obj instanceof RareStar10 rareStar10) {
            score += rareStar10.getPoints();
        } else if (obj instanceof Meteor) {
            lives--;
            if (lives <= 0) {
                saveHighScore();
                state = GameState.GAME_OVER;
                playSound(gameOverSound);
                stopBackgroundMusic();
            }
        } else if (obj instanceof Faster) {
            basket.setSpeed(GameConfig.FASTER_BASKET_SPEED);
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    basket.setSpeed(GameConfig.BASE_BASKET_SPEED);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } else if (obj instanceof Magnet) {
            magnetActive = true;
            magnetStartTime = System.currentTimeMillis();
        }

        if (score >= GameConfig.LEVEL_THRESHOLD * level) {
            level++;
            state = GameState.LEVEL_UP;
            stopBackgroundMusic();
        }
    }

    private void checkCollisions() {
        Rectangle basketRect = new Rectangle(basket.getX(), basket.getY(), GameConfig.BASKET_WIDTH, GameConfig.BASKET_HEIGHT);
        Iterator<GameObject> it = objects.iterator();
        while (it.hasNext()) {
            GameObject obj = it.next();
            Rectangle objRect = new Rectangle(obj.getX(), obj.getY(), GameConfig.OBJECT_SIZE, GameConfig.OBJECT_SIZE);
            if (basketRect.intersects(objRect)) {
                if (obj instanceof Star) {
                    score += ((Star) obj).getPoints();
                    it.remove(); // Remove caught star
                    spawnStar(); // Add a new star
                }
                // Add more logic for other object types if needed
            }
            // Remove stars that fall off the screen
            if (obj.getY() > GameConfig.HEIGHT) {
                it.remove();
                spawnStar();
            }
        }
    }

public void setState(GameState state) {
        this.state = state;
        if (state == GameState.PLAYING) {
            initGame();
            startLevel();
            startBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }

    public void startLevel() {
        objects.clear();
        for (int i = 0; i < 3; i++) { // Start with 3 normal stars
            objects.add(new Star(level));
        }
        for (int i = 0; i < 1; i++) { // Start with 1 RareStar5
            objects.add(new RareStar5(level));
        }
        for (int i = 0; i < 1; i++) { // Start with 1 RareStar10
            objects.add(new RareStar10(level));
        }
        for (int i = 0; i < 1; i++) { // Start with 1 Magnet
            objects.add(new Magnet(level));
        }
        for (int i = 0; i < 1; i++) { // Start with 1 Faster
            objects.add(new Faster(level));
        }
        for (int i = 0; i < 1; i++) { // Start with 1 Meteor
            objects.add(new Meteor(level));
        }
    }

    public Basket getBasket() { return basket; }
    public ArrayList<GameObject> getObjects() { return objects; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
    public int getLives() { return lives; }
    public int getHighScore() { return highScore; }
    public GameState getState() { return state; }

    public ImageIcon getImage(GameObject obj) {
        if (obj instanceof Star) return starImage;
        if (obj instanceof RareStar5) return rareStar5Image;
        if (obj instanceof RareStar10) return rareStar10Image;
        if (obj instanceof Meteor) return meteorImage;
        if (obj instanceof Faster) return fasterImage;
        if (obj instanceof Magnet) return magnetImage;
        return null;
    }

    public ImageIcon getBasketImage() { return basketImage; }
    public void moveBasketLeft() { basket.moveLeft(); }
    public void moveBasketRight() { basket.moveRight(); }

    public void spawnStar() {
        objects.add(new Star(level));
    }
}
