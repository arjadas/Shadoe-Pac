import bagel.Image;
import bagel.util.Rectangle;

public class GhostRed extends Ghost{

    private final static String IMG_LOCATION = "res/ghostRed.png";

    private final double NORMAL_SPEED = 1; //
    private final double FRENZY_SPEED = NORMAL_SPEED - FRENZY_SPEED_DECREASE;

    private int directionRight = 1;

    /**
     * Public constructor which takes in the image of ghost's starting position
     * and sends it along with the ghost image to its parent class
     */
    public GhostRed(double x, double y) {
        super(IMG_LOCATION, x, y);
        reSpawn();
    }

    @Override
    protected void resetDirection() {
        currentDirection = directionRight;
    }

    @Override
    protected void changeDirection() {
        currentDirection *= FLIP_DIRECTION_VALUE;
    }

    @Override
    protected void moveInNormalMode() {
        checkWallCollision(currentX + (NORMAL_SPEED * currentDirection), currentY);
        currentX += (NORMAL_SPEED * currentDirection);
    }

    @Override
    protected void moveInFrenzyMode() {
        checkWallCollision(currentX + (FRENZY_SPEED * currentDirection), currentY);
        currentX += (FRENZY_SPEED * currentDirection);
    }

}
