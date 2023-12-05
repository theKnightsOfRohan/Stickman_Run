import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.text.DecimalFormat;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import java.util.function.Function;

public class Main extends PApplet {
    Player player;
    PImage background;
    Image titleText, gameOver;
    int ground, gameState, backgroundX;
    double score;
    DecimalFormat df = new DecimalFormat("#.0");
    ArrayList<Platform> platformList = new ArrayList<>();
    Minim loader;
    AudioPlayer jumpSound, gameOverSound, homeBgm, gameBgm;
    Button newGame;

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        loadFiles((Void v) -> {
            gameState = 1;
            platformList.clear();
            homeBgm.pause();
            homeBgm.rewind();
            score = 0;
            createPlatforms();
            player.y = ground - player.sprite.height;
            player.frameNumber = 0;
            gameBgm.play();
            return null;
        });

        ground = 460;
        gameState = 0;
        score = 0;

        player.sprite = player.runFrames.get(player.frameNumber);
        player.y = ground - player.sprite.height;

        createPlatforms();

        homeBgm.play();
    }

    public void draw() {
        // Game States: 0 = Home Screen, 1 = Game, 2 = Game Over
        if (gameState == 0) {
            drawBackground();
            player.act(this);

            fill(0);
            stroke(0);
            rect(0, ground, width, height - ground);

            titleText.draw(this);
            newGame.draw(this);
        } else if (gameState == 1) {
            drawBackground();

            image(player.sprite, player.x, player.y);
            fill(0);
            stroke(255);

            for (Platform currPlatform : platformList) {
                Platform previousPlatform = platformList.get(currPlatform.previousListIndex);

                currPlatform.act();
                gameState = currPlatform.playerContact(this, player);
                if (gameState == 2)
                    break;

                if (currPlatform.x + currPlatform.w < 0)
                    currPlatform.respawn(this, previousPlatform);

                rect(currPlatform.x, currPlatform.y, currPlatform.w, currPlatform.h);
            }

            stroke(0);
            rect(0, ground, width, height - ground);
            player.act(this);
            score += 0.02;
            fill(255);
            textSize(20);

            text("Score: " + Double.valueOf(df.format(score)) + " m", 10, 25);
        } else if (gameState == 2) {
            if (gameBgm.isPlaying()) {
                gameBgm.pause();
                gameBgm.rewind();
                gameOverSound.play();
                homeBgm.play();
            }
            background(0);
            gameOver.draw(this);
            newGame.draw(this);
        }
    }

    public void keyReleased() {
        switch (gameState) {
            case 0:
                if (key == ' ')
                    newGame.action.apply(null);
                break;
            case 1:
                if (key == ' ' && !player.isJumping) {
                    player.jump(jumpSound);
                }
                break;
            case 2:
                if (key == ' ')
                    newGame.action.apply(null);
                break;
        }
    }

    // Starts the game when the player clicks the "New Game" button
    public void mouseReleased() {
        switch (gameState) {
            case 0:
                if (newGame.isClicked(mouseX, mouseY))
                    newGame.action.apply(null);
                break;
            case 1:
                break;
            case 2:
                if (newGame.isClicked(mouseX, mouseY))
                    newGame.action.apply(null);
                break;
        }
    }

    // Creates the platforms that the player will jump on
    public void createPlatforms() {
        for (int i = 0; i < 10; i++) {
            Platform currPlatform = new Platform(width + 1000, i);
            currPlatform.y = ground - (int) (Math.random() * 50) - 50;

            currPlatform.getPlatformDimensions();

            if (i > 0) {
                Platform previousPlatform = platformList.get(currPlatform.previousListIndex);
                currPlatform.respawn(this, previousPlatform);
            }

            platformList.add(currPlatform);
        }
    }

    private void loadFiles(Function<Void, Void> newGameAction) {
        player = new Player();

        for (int i = 1; i <= 10; i++) {
            PImage runFrame = loadImage("assets/sprites/Stickman/run_frames/Frame" + i + ".png");
            runFrame.resize(50, 50);
            for (int j = 0; j < 3; j++) {
                player.runFrames.add(runFrame);
            }

            if (i <= 8) {
                PImage jumpFrame = loadImage("assets/sprites/Stickman/jump_frames/Frame" + i + ".png");
                jumpFrame.resize(50, 50);
                for (int j = 0; j < 3; j++) {
                    player.jumpFrames.add(jumpFrame);
                }
            }
        }

        newGame = new Button(width / 2 - 125, height / 2 + 50, 250, 50,
                loadImage("assets/sprites/images/New_Game.png"), newGameAction);

        titleText = new Image(width / 2 - 390 / 2, height / 4 - 5, 390, 45,
                loadImage("assets/sprites/images/Title_Text.png"));

        gameOver = new Image(width / 2 - 352 / 2, height / 2 - 147, 352, 147,
                loadImage("assets/sprites/images/Game_Over.png"));

        background = loadImage("assets/sprites/Background.jpeg");

        loader = new Minim(this);
        homeBgm = loader.loadFile("assets/sound_effects/Home_Background_Music.mp3");
        gameBgm = loader.loadFile("assets/sound_effects/Game_Background_Music.mp3");
        jumpSound = loader.loadFile("assets/sound_effects/Jump_Sound.mp3");
        gameOverSound = loader.loadFile("assets/sound_effects/Game_Over_Sound.mp3");
    }

    // Draws the background and makes it scroll
    public void drawBackground() {
        background(0);
        image(background, backgroundX, -65);
        image(background, backgroundX + background.width, -65);
        backgroundX--;
        if (backgroundX + background.width == 0) {
            backgroundX = 0;
        }
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}