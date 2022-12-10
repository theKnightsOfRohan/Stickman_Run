import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Main extends PApplet {
    Player player;
    PImage background;
    int backgroundX, ground;
    ArrayList<Platform> platformList = new ArrayList<>();

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        player = new Player();
        ground = 528;

        //For some reason, images MUST be loaded in setup. Oh well :/
        getFrames();
        player.sprite = player.runFrames.get(player.frameNumber);
        player.y = ground - player.sprite.height;

        createPlatforms();

        background = loadImage("Sprites/City_Background.png");
        background.resize(600, 600);
    }

    public void draw() {
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
        image(background, backgroundX, 100);
        image(background, backgroundX + background.width, 100);
        backgroundX--;
        if (backgroundX + (background.width * 2) == width) {
            backgroundX = 0;
        }
    }

    public void keyReleased() {
        if (key == ' ') {
            player.jump();
        }
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}