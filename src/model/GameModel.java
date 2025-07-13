package model;

import view.GameState;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.Rectangle;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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
    private Clip catchSound, rareStarSound, powerupSound, bombSound, missSound, gameOverSound, backgroundMusic;
    private boolean magnetActive;
    private static final Random RAND = new Random();

    public GameModel() {
    objects = new ArrayList<>();
    initGame();
    loadImages();
    loadSounds();
    setState(GameState.MENU); 
}

    private void initGame() {
        basket = new Basket(GameConfig.WIDTH / 2, GameConfig.HEIGHT - GameConfig.BASKET_HEIGHT - 20);
        objects.clear();
        score = 0;
        level = 1;
        lives = 3;
        lastObjectTime = System.currentTimeMillis();
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
        return new ImageIcon(imgURL);
    }

    private void loadSounds() {
        try {
            catchSound = loadClip("assets/sound/bonus.wav");
            rareStarSound = loadClip("assets/sound/rarestar.wav");
            powerupSound = loadClip("assets/sound/equip.wav");
            bombSound = loadClip("assets/sound/explosion.wav");
            missSound = loadClip("assets/sound/miss.wav");
            gameOverSound = loadClip("assets/sound/complete.wav");
            backgroundMusic = loadClip("assets/sound/game_bg_music.wav");

            if (backgroundMusic != null) {
                FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-5.0f);
            }
        } catch (Exception e) {
            catchSound = null;
            rareStarSound = null;
            powerupSound = null;
            bombSound = null;
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
            try (InputStream bufferedIn = new BufferedInputStream(audioSrc)) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
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
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    private void loadHighScore() {
        File file = new File("res/highscore.txt");
        if (!file.exists()) {
            highScore = 0;
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String scoreStr = br.readLine();
            highScore = (scoreStr != null && !scoreStr.isEmpty()) ? Integer.parseInt(scoreStr) : 0;
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
        }
    }

    private void saveHighScore() {
        if (score <= highScore) return;
        highScore = score;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/highscore.txt"))) {
            bw.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.err.println("Failed to save high score: " + e.getMessage());
        }
    }

    public void update() {
        if (state != GameState.PLAYING) return;

        if (System.currentTimeMillis() - lastObjectTime > 1000 / level) {
            spawnObject();
            lastObjectTime = System.currentTimeMillis();
        }

        ArrayList<GameObject> toRemove = new ArrayList<>();

        for (GameObject obj : objects) {
            if (magnetActive && isCollectibleStar(obj)) {
                applyMagnetEffect(obj);
            }

            obj.update();

            if (obj.isOffScreen()) {
                toRemove.add(obj);
                if (obj instanceof Star) {
                    lives--;
                    playSound(missSound);
                    if (lives <= 0) {
                        saveHighScore();
                        state = GameState.GAME_OVER;
                        stopBackgroundMusic();
                        playSound(gameOverSound);
                        new java.util.Timer().schedule(new java.util.TimerTask() {
                            public void run() {
                                startBackgroundMusic();
                            }
                        }, 2500);
                    }
                }
            } else if (obj.getBounds().intersects(basket.getBounds())) {
                toRemove.add(obj);
                handleCollision(obj);
            }
        }
        objects.removeAll(toRemove);
        checkCollisions();
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
        if (roll < 0.02) objects.add(new RareStar10(level));
        else if (roll < 0.07) objects.add(new RareStar5(level));
        else if (roll < 0.17) objects.add(new Meteor(level));
        else if (roll < 0.22) objects.add(new Faster(level));
        else if (roll < 0.27) objects.add(new Magnet(level));
        else objects.add(new Star(level));
    }

    private void handleCollision(GameObject obj) {
        if (obj instanceof Star star) {
            score += star.getPoints();
            playSound(catchSound);
        } else if (obj instanceof RareStar5 rs5) {
            score += rs5.getPoints();
            playSound(rareStarSound);
        } else if (obj instanceof RareStar10 rs10) {
            score += rs10.getPoints();
            playSound(rareStarSound);
        } else if (obj instanceof Meteor) {
            lives--;
            playSound(bombSound);
            if (lives <= 0) {
                saveHighScore();
                state = GameState.GAME_OVER;
                stopBackgroundMusic();
                playSound(gameOverSound);
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    public void run() {
                        startBackgroundMusic();
                    }
                }, 2500);
            }
        } else if (obj instanceof Faster) {
            playSound(powerupSound);
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
            playSound(powerupSound);
            magnetActive = true;
            System.currentTimeMillis();
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
                    it.remove();
                    spawnStar();
                }
            }
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
        }

        if (state == GameState.MENU || state == GameState.PLAYING) {
            startBackgroundMusic();
        } else if (state == GameState.GAME_OVER) {
            stopBackgroundMusic();
        }
    }

    public void startLevel() {
        objects.clear();
        objects.add(new Star(level));
        objects.add(new RareStar5(level));
        objects.add(new RareStar10(level));
        objects.add(new Magnet(level));
        objects.add(new Faster(level));
        objects.add(new Meteor(level));
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
    public void spawnStar() { objects.add(new Star(level)); }
}
