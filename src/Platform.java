import processing.core.PImage;

public class Platform extends Main {
    PImage sprite;
    int x, y, xSpeed, length, stature, listIndex, previousListIndex;

    Platform(int x, int listIndex) {
        this.x = x;
        this.listIndex = listIndex;
        this.xSpeed = 3;
        this.getPlatformDimensions();

        this.previousListIndex = this.listIndex - 1;
        if (this.listIndex == 0) {
            previousListIndex = 9;
        }
    }

    public void act() {
        this.x -= this.xSpeed;
    }

    public void respawn(Platform previousPlatform) {
        this.x = previousPlatform.x + previousPlatform.length + (int)(Math.random() * 100) + 50;

        if (previousPlatform.y <= 450) {
            this.y = previousPlatform.y + (int)(Math.random() * 70);
        } else if (previousPlatform.y > ground - 80) {
            this.y = previousPlatform.y - (int)(Math.random() * 70);
        } else {
            this.y = previousPlatform.y + (int)(Math.random() * 70) - 25;
        }

        this.getPlatformDimensions();
    }

    public void getPlatformDimensions() {
        this.length = (int)(Math.random() * 100) + 50;
        this.stature = (int)(Math.random() * 100) + 50;
    }

    public void playerContact(Player player) {
        //I would try to simplify the conditionals, but unfortunately each check is slightly different from the others
        //In other words: AAAAAAAAAAAAA
        if (player.x + player.sprite.width > this.x && player.x < this.x + this.length && player.y < this.y && player.y + player.sprite.height > this.y && player.isJumping && player.ySpeed < 0) {
            player.isJumping = false;
            player.ySpeed = 0;
            player.y = this.y - player.sprite.height;
        } else if (player.x > this.x + this.length && player.y == this.y - player.sprite.height && !player.isJumping) {
            player.isJumping = true;
            player.frameNumber = 18;
        } else if ((player.x + player.sprite.width > this.x && player.x < this.x && player.y < this.y && player.y + player.sprite.height > this.y)
                || (player.x + player.sprite.width > this.x && player.x < this.x + this.length && player.y > this.y && player.y < this.y + this.stature)) {
            System.exit(0);
        }
    }
}