public class Platform {
    int w, h, listIndex, previousListIndex;
    float x, y, xSpeed;

    Platform(int x, int listIndex) {
        this.x = x;
        this.listIndex = listIndex;
        this.xSpeed = 4;
        this.getPlatformDimensions();

        this.previousListIndex = this.listIndex - 1;
        if (this.listIndex == 0) {
            this.previousListIndex = 9;
        }
    }

    public void act() {
        this.x -= this.xSpeed;
    }

    public void respawn(Main main, Platform previousPlatform) {
        this.x = previousPlatform.x + previousPlatform.w + (int) (Math.random() * 200) - 50;

        if (previousPlatform.y <= 400) {
            this.y = previousPlatform.y + (int) (Math.random() * 100);
        } else if (previousPlatform.y > (main.ground - 50)) {
            this.y = previousPlatform.y - (int) (Math.random() * 100);
        } else {
            this.y = previousPlatform.y + (int) (Math.random() * 100) - 50;
        }

        this.getPlatformDimensions();
    }

    public void getPlatformDimensions() {
        this.w = (int) (Math.random() * 200) + 50;
        this.h = (int) (Math.random() * 100) + 50;
    }

    public int playerContact(Main main, Player player) {
        boolean playerAbovePlatform = player.y < this.y && player.y + player.sprite.height > this.y;
        boolean playerOnPlatform = player.y == this.y - player.sprite.height;
        boolean playerHitsPlatform = player.x + player.sprite.width > this.x && player.x < this.x + this.w;
        boolean playerFallsOffPlatform = player.x > this.x + this.w;
        boolean playerHitsSideOrUnderPlatform = ((player.x + player.sprite.width > this.x && player.x < this.x
                && player.y < this.y && player.y + player.sprite.height > this.y)
                || (player.x + player.sprite.width > this.x && player.x < this.x + this.w && player.y > this.y
                        && player.y < this.y + this.h));

        if (playerHitsPlatform && playerAbovePlatform && player.isJumping && player.ySpeed < 0) {
            player.isJumping = false;
            player.ySpeed = 0;
            player.y = this.y - player.sprite.height;
            return 1;
        } else if (playerFallsOffPlatform && playerOnPlatform && !player.isJumping) {
            player.isJumping = true;
            player.frameNumber = 18;
            return 1;
        } else if (playerHitsSideOrUnderPlatform) {
            main.score = 0.0;
            return 2;
        } else {
            return 1;
        }
    }
}