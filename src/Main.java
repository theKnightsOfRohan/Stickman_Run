import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Main extends PApplet {
    Player player;
    PImage background, newGame, titleText, gameOver;
    int backgroundX, ground, gameState, newGameX, newGameY, score;
    ArrayList<Platform> platformList = new ArrayList<>();

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

            for (Platform currPlatform : platformList) {
                Platform previousPlatform = platformList.get(currPlatform.previousListIndex);

                fill(0);
                stroke(255);
                currPlatform.act();
                currPlatform.playerContact(player);

                if (currPlatform.x + currPlatform.length < 0) {
                    currPlatform.respawn(previousPlatform);
                }

                rect(currPlatform.x, currPlatform.y, currPlatform.length, currPlatform.stature);
            }

            stroke(0);
            rect(0, ground, width, height - ground);
            player.act();
        } else if (gameState == 2) {
            background(0);
            image(gameOver, width/2 - gameOver.width/2, height/2 - gameOver.height);
            image(newGame, newGameX, newGameY);
        }
    }

    public void getFrames() {
        for (int i = 1; i <= 10; i++) {
            PImage runFrame = loadImage("Sprites/Stickman/Run_Frames/Frame" + i + ".png");
            runFrame.resize(50, 50);
            PImage jumpFrame = loadImage("Sprites/Stickman/Jump_Frames/Frame" + i + ".png");
            jumpFrame.resize(50, 50);

            for (int j = 0; j < 3; j++) {
                player.runFrames.add(runFrame);
                player.jumpFrames.add(jumpFrame);
            }
        }
    }

    public void createPlatforms() {
        for (int i = 0; i < 10; i++) {
            Platform currPlatform = new Platform(width + 100, i);
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
        if (key == ' ' && gameState == 1) {
            player.jump();
        }
    }

    public void mouseReleased() {
        if (gameState != 1 && mouseX > newGameX && mouseX < newGameX + newGame.width && mouseY > newGameY && mouseY < newGameY + newGame.height) {
            gameState = 1;
        }
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}