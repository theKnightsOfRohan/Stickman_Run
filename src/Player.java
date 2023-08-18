import processing.core.PImage;
import java.util.ArrayList;
import ddf.minim.AudioPlayer;

public class Player {
    PImage sprite;
    int gravity, frameNumber;
    float x, y, ySpeed;
    boolean isJumping;
    ArrayList<PImage> runFrames = new ArrayList<>();
    ArrayList<PImage> jumpFrames = new ArrayList<>();

    public Player() {
        this.x = 100;
        this.ySpeed = 0;
        this.gravity = 1;
        this.frameNumber = 0;
        this.isJumping = false;
    }

    public void act(Main main) {
        // Apply acceleration to speed.
        this.ySpeed -= this.gravity;

        jumpMovement(main);

        main.image(this.sprite, this.x, this.y);
        this.frameNumber++;
        if (this.frameNumber == 30) {
            this.frameNumber = 0;
        }
    }

    private void jumpMovement(Main main) {
        // If the player is jumping, then move them according to speed.
        if (this.isJumping) {
            this.y -= this.ySpeed;
            if (this.frameNumber < 21) {
                this.sprite = this.jumpFrames.get(this.frameNumber);
            }

            // If the player lands on the ground while they are jumping, set them to ground
            // level and stop jump processes.
            if (this.y + this.sprite.height >= main.ground) {
                this.y = main.ground - this.sprite.height;
                this.isJumping = false;
            }
        } else {
            // If the player is not jumping, their ySpeed is set to 0.
            // Continuous setting for safety, could be cut and debugged later.
            this.ySpeed = 0;
            this.sprite = this.runFrames.get(this.frameNumber);
        }
    }

    public void jump(AudioPlayer jumpSound) {
        // Sets frameNumber to 0 to make sure it starts at the beginning of the jump
        // animation.
        this.frameNumber = 0;
        this.isJumping = true;
        this.ySpeed = 17;
        jumpSound.play();
        jumpSound.rewind();
    }
}