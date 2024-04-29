import bagel.Image;

public class GhostBlue extends Ghost{

    private final static String IMG_LOCATION = "res/ghostBlue.png";

    private final double NORMAL_SPEED = 2; //
    private final double FRENZY_SPEED = NORMAL_SPEED - FRENZY_SPEED_DECREASE;

    private int directionDown = 1;

    /**
     * Public constructor which takes in the image of ghost's starting position
     * and sends it along with the ghost image to its parent class
     */
    public GhostBlue(double x, double y) {
        super(IMG_LOCATION, x, y);
        reSpawn();
    }

    @Override
    protected void resetDirection() { //
        currentDirection = directionDown;
    }

    @Override
    protected void changeDirection() {
        currentDirection *= FLIP_DIRECTION_VALUE;
    }

    @Override
    protected void moveInNormalMode() {
        checkWallCollision(currentX, currentY + (NORMAL_SPEED * currentDirection));
        currentY += NORMAL_SPEED * currentDirection;
    }

    @Override
    protected void moveInFrenzyMode() {
        checkWallCollision(currentX, currentY + (FRENZY_SPEED * currentDirection));
        currentY += FRENZY_SPEED * currentDirection;
    }

}
