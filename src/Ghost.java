import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Ghost {

    protected double currentX;
    protected double currentY;
    protected final Point START_POSITION;
    public final int POINTS = 30;

    protected Image image;

    protected final static String FRENZY_LOCATION = "res/ghostFrenzy.png";
    protected final static Image FRENZY = new Image(FRENZY_LOCATION);
    protected final static double FRENZY_SPEED_DECREASE = 0.5;

    protected boolean isEaten = false;
    protected int currentDirection;
    protected final int FLIP_DIRECTION_VALUE = -1;

    /**
     * Public constructor which takes in the image of ghost and it's starting position
     */
    public Ghost(String imageLocation, double x, double y) {
        START_POSITION = new Point(x, y);
        image = new Image(imageLocation);
        reSpawn();
    }

    /**
     * Method which makes the ghost appear from its initial position with initial direction
     */
    public void reSpawn() {
        resetPosition();
        resetDirection();
    }

    /**
     * Method which resets the position of ghost when needed
     */
    protected void resetPosition() {
        currentX = START_POSITION.x;
        currentY = START_POSITION.y;
    }

    /**
     * Method which resets the direction of ghost when needed
     */
    protected abstract void resetDirection();

    /**
     * Method used to update ghosts' state whether it is eaten by the player during the frenzy mode
     */
    public void eatGhost() {
        isEaten = true;
    }

    /**
     * Getter Method used to provide information whether it was eaten by the player during the frenzy mode
     */
    public boolean getIsEaten() {
        return isEaten;
    }
    /**
     * Method used to display ghost's image on screen
     */
    protected void displayGhostImage() {
        image.drawFromTopLeft(currentX, currentY);
    }

    /**
     * Method returns a rectangle whose dimensions are similar to the ghost's image dimension
     */
    public Rectangle getRectangle() {
        return new Rectangle(currentX, currentY, image.getWidth(), image.getHeight());
    }

    /**
     * Method which draws the ghosts on the screen
     */
    public void draw() {

        if (!ShadowPac.getFrenzyState()) {
            // frenzyMode off

            if (isEaten) {
                // the ghost was eaten in frenzy mode, hence respawning from start position
                reSpawn();
                isEaten = false; // resetting the isEaten value so that this part is avoided next time
            }
            displayGhostImage();

        } else if (ShadowPac.getFrenzyState() && !isEaten) {
            // frenzyMode on & ghost is not eaten yet
            FRENZY.drawFromTopLeft(currentX, currentY);
        }
    }

    /**
     * Method which moves the ghosts
     */
    public void move() {

        if (!ShadowPac.getFrenzyState()) {
            moveInNormalMode();
        } else if (ShadowPac.getFrenzyState() && !isEaten){
            moveInFrenzyMode();
        }

    }

    /**
     * Method which checks whether the ghost collided with the wall and performs necessary action
     */
    protected void checkWallCollision(double futureX, double futureY) {
        Rectangle ghostBox = this.getRectangle();
        for (Wall w: ShadowPac.getWalls()) {

            Rectangle wallBox = w.getRectangle();

            if (ghostBox.intersects(wallBox)) {
                changeDirection();
            }
        }
    }

    /**
     * Method which changes the direction of ghost when needed
     */
    protected abstract void changeDirection();

    /**
     * Method used to move ghost normally
     */
    protected abstract void moveInNormalMode();

    /**
     * Method used to move ghost during Frenzy mode with limited features
     */
    protected abstract void moveInFrenzyMode();
}
