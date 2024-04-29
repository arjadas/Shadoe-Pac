import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class StationaryEntity {

    protected final Point POSITION;
    protected Image image;

    /**
     * Public constructor which takes in the image of the object and it's position
     */
    public StationaryEntity(String imageLocation, double x, double y) {
        POSITION = new Point(x, y);
        image = new Image(imageLocation);
    }

    /**
     * Method which draws image of the object on screen
     */
    public void draw() {
        image.drawFromTopLeft(POSITION.x, POSITION.y);
    }

    /**
     * Method returns a rectangle whose dimensions are similar to the object's image dimension
     */
    public Rectangle getRectangle() {
        return new Rectangle(POSITION.x, POSITION.y, image.getWidth(), image.getHeight());
    }

}
