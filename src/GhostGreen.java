import bagel.Image;
import java.util.Random;

public class GhostGreen extends Ghost{

    private final static String IMG_LOCATION = "res/ghostGreen.png";

    private final double NORMAL_SPEED = 4;
    private final double FRENZY_SPEED = NORMAL_SPEED - FRENZY_SPEED_DECREASE;

    private int directionInitial = 1; // value that holds a number to mimic a direction after it is chosen

    private boolean directionHorizontal;

    /**
     * Public constructor which takes in the image of ghost's starting position
     * and sends it along with the ghost image to its parent class
     */
    public GhostGreen(double x, double y) {
        super(IMG_LOCATION, x, y);
        reSpawn();
        chooseDirection(); // direction is chosen in the beginning and retains it after respawning
    }

    @Override
    protected void resetDirection() {
        currentDirection = directionInitial;
    }

    @Override
    protected void changeDirection() {
        currentDirection *= FLIP_DIRECTION_VALUE;
    }

    @Override
    protected void moveInNormalMode() {

        if (directionHorizontal) {
            moveHorizontally(NORMAL_SPEED);
        } else {
            moveVertically(NORMAL_SPEED);
        }

    }

    @Override
    protected void moveInFrenzyMode() {
        if (directionHorizontal) {
            moveHorizontally(FRENZY_SPEED);
        } else {
            moveVertically(FRENZY_SPEED);
        }
    }

    /**
     * Method used to decide whether the green ghost moves horizontally or vertically
     */
    private void chooseDirection() {

        Random random = new Random();
        int direction = random.nextInt(2); // generates a random number between 0 and 1

        // 0 is horizontal, 1 is vertical
        switch (direction) {
            case 0:
                directionHorizontal = true;
                break;
            case 1:
                directionHorizontal = false;
                break;
        }

    }

    /**
     * Method used to move ghost horizontally
     */
    private void moveHorizontally(double speed) {
        checkWallCollision(currentX + (speed * currentDirection), currentY);
        currentX += (speed * currentDirection);
    }

    /**
     * Method used to move ghost vertically
     */
    private void moveVertically(double speed) {
        checkWallCollision(currentX, currentY + (speed * currentDirection));
        currentY += (speed * currentDirection);
    }

}
