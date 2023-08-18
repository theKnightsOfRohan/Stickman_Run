import java.util.function.Function;
import processing.core.PImage;

public class Button extends Image {
    Function<Void, Void> action;

    public Button(int x, int y, int w, int h, PImage img, Function<Void, Void> action) {
        super(x, y, w, h, img);
        this.action = action;
    }

    public boolean isClicked(int mouseX, int mouseY) {
        return (mouseX > this.x && mouseX < this.x + this.w && mouseY > this.y
                && mouseY < this.y + this.h);
    }
}
