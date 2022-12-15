import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.text.DecimalFormat;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

public class Main extends PApplet {
    Player player;
    PImage background, newGame, titleText, gameOver;
    int backgroundX, ground, gameState, newGameX, newGameY;
    double score;
    DecimalFormat df = new DecimalFormat("#.0");
    ArrayList<Platform> platformList = new ArrayList<>();
    Minim loader;
    AudioPlayer jumpSound, gameOverSound, homeBgm, gameBgm;

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        player = new Player();
        ground = 460;
        gameState = 0;
        score = 0;

        //For some reason, images MUST be loaded in setup. Oh well :/
        getFrames();
        player.sprite = player.runFrames.get(player.frameNumber);
        player.y = ground - player.sprite.height;

        createPlatforms();

        background = loadImage("Sprites/Background.jpeg");
        background.resize(1066, 600);

        newGame = loadImage("Sprites/Images/New_Game.png");
        newGame.resize(250, 50);
        newGameX = width/2 - newGame.width/2;
        newGameY = height/2 + newGame.height;

        titleText = loadImage("Sprites/Images/Title_Text.png");
        titleText.resize(390, 45);

        gameOver = loadImage("Sprites/Images/Game_Over.png");

        loader = new Minim(this);
        homeBgm = loader.loadFile("Sound_Effects/Home_Background_Music.mp3");
        gameBgm = loader.loadFile("Sound_Effects/Game_Background_Music.mp3");
        jumpSound = loader.loadFile("Sound_Effects/Jump_Sound.mp3");
        gameOverSound = loader.loadFile("Sound_Effects/Game_Over_Sound.mp3");

        homeBgm.play();
    }

    public void draw() {
        if (gameState == 0) {
            drawBackground();
            image(player.sprite, player.x, player.y);
            player.act();

            fill(0);
            stroke(0);
            rect(0, ground, width, height - ground);

            image(titleText, width/2 - titleText.width/2, height/4 - 5);
            image(newGame, newGameX, newGameY);
        } else if (gameState == 1) {
            drawBackground();

            image(player.sprite, player.x, player.y);
            fill(0);
            stroke(255);

            for (Platform currPlatform : platformList) {
                Platform previousPlatform = platformList.get(currPlatform.previousListIndex);

                currPlatform.act();
                gameState = currPlatform.playerContact(player);
                if (gameState == 2) {
                    break;
                }

                if (currPlatform.x + currPlatform.length < 0) {
                    currPlatform.respawn(previousPlatform);
                }

                rect(currPlatform.x, currPlatform.y, currPlatform.length, currPlatform.stature);
            }

            stroke(0);
            rect(0, ground, width, height - ground);
            player.act();
            score += 0.1;
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
            image(gameOver, width/2 - gameOver.width/2, height/2 - gameOver.height);
            image(newGame, newGameX, newGameY);
        }
    }

    public void getFrames() {
        for (int i = 1; i <= 10; i++) {
            PImage runFrame = loadImage("Sprites/Stickman/Run_Frames/Frame" + i + ".png");
            runFrame.resize(50, 50);
            for (int j = 0; j < 3; j++) {
                player.runFrames.add(runFrame);
            }

            if (i <= 8) {
                PImage jumpFrame = loadImage("Sprites/Stickman/Jump_Frames/Frame" + i + ".png");
                jumpFrame.resize(50, 50);
                for (int j = 0; j < 3; j++) {
                    player.jumpFrames.add(jumpFrame);
                }
            }
        }
    }

    public void createPlatforms() {
        for (int i = 0; i < 10; i++) {
            Platform currPlatform = new Platform(width + 1000, i);
            currPlatform.y = ground - (int)(Math.random() * 50) - 50;

            currPlatform.getPlatformDimensions();

            if (i > 0) {
                Platform previousPlatform = platformList.get(currPlatform.previousListIndex);
                currPlatform.respawn(previousPlatform);
            }

            platformList.add(currPlatform);
        }
    }

    public void drawBackground() {
        background(0);
        image(background, backgroundX, 0);
        image(background, backgroundX + background.width, 0);
        backgroundX--;
        if (backgroundX + background.width == 0) {
            backgroundX = 0;
        }
    }

    public void keyReleased() {
        if (key == ' ' && gameState == 1 && !player.isJumping) {
            player.jump();
            jumpSound.play();
            jumpSound.rewind();
        }
    }

    public void mouseReleased() {
        if (gameState != 1 && mouseX > newGameX && mouseX < newGameX + newGame.width && mouseY > newGameY && mouseY < newGameY + newGame.height) {
            platformList.clear();
            player.runFrames.clear();
            homeBgm.pause();
            setup();
            gameState = 1;
            homeBgm.pause();
            homeBgm.rewind();
            gameBgm.play();
        }
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}