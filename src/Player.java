import processing.core.PImage;
import java.util.ArrayList;

public class Player extends Main {
    PImage sprite;
    int x, y, ySpeed, gravity, frameNumber;
    boolean isJumping;
    ArrayList<PImage> runFrames = new ArrayList<>();
    ArrayList<PImage> jumpFrames = new ArrayList<>();
    public Player() {
        this.x = 100;
        this.ySpeed = 0;
        this.gravity = 1;
        this.frameNumber = 0;
        this.isJumping = false;
        ground = 460;
    }

    public void act() {
        //Apply acceleration to speed.
        this.ySpeed -= gravity;

        //If the player is jumping, then move them according to speed.
        if (this.isJumping) {
            this.y -= this.ySpeed;
            this.sprite = jumpFrames.get(frameNumber);

            //If the player lands on the ground while they are jumping, set them to ground level and stop jump processes.
            if (this.y + this.sprite.height >= ground) {
                this.y = ground - this.sprite.height;
                this.isJumping = false;
            }
        } else {
            //If the player is not jumping, their ySpeed is set to 0.
            //Continuous setting for safety, could be cut and debugged later.
            this.ySpeed = 0;
            this.sprite = this.runFrames.get(this.frameNumber);
        }

        this.frameNumber++;
        if (this.frameNumber == 30) {
            this.frameNumber = 0;
        }
    }

    public void jump() {
        //Conditional to prevent double jumping.
        if (!this.isJumping) {
            //Sets frameNumber to 0 to make sure it starts at the beginning of the jump animation.
            this.frameNumber = 0;
            this.isJumping = true;
            this.ySpeed = 17;
        }
    }
}