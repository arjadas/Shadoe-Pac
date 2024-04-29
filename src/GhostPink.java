import bagel.Image;
import bagel.util.Rectangle;

import java.util.Random;

public class GhostPink extends Ghost{

    private final static String IMG_LOCATION = "res/ghostPink.png";

    private final double NORMAL_SPEED = 3;
    private final double FRENZY_SPEED = NORMAL_SPEED - FRENZY_SPEED_DECREASE;

    private int directionInitial = 1;
    private boolean directionHorizontal;

    // these are just to mimic math.random outputs as directions
    private int directionLeft = 0;
    private int directionRight = 1;
    private int directionUp = 2;
    private int directionDown = 3;

    /**
     * Public constructor which takes in the image of ghost's starting position
     * and sends it along with the ghost image to its parent class
     */
    public GhostPink(double x, double y) {
        super(IMG_LOCATION, x, y);
        reSpawn();
        chooseDirection();
    }

    @Override
    protected void resetDirection() {
        chooseDirection();
    }

    @Override
    protected void changeDirection() {

        // this part finds the direction the ghost was travelling just before colliding with wall
        // so that this direction is avoided next time
        /*if ((currentDirection == directionInitial * FLIP_DIRECTION_VALUE) && (directionHorizontal)) {
            chooseDirection(directionLeft);
        } else if ((currentDirection == directionInitial) && (directionHorizontal)) {
            chooseDirection(directionRight);
        } else if ((currentDirection == directionInitial * FLIP_DIRECTION_VALUE) && (!directionHorizontal)) {
            chooseDirection(directionUp);
        } else if ((currentDirection == directionInitial) && (!directionHorizontal)) {
            chooseDirection(directionDown);
        }*/

        // this results in less jiggling
        chooseDirection();


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
     * Method used to decide whether the green ghost moves up, down, left or right
     */
    private void chooseDirection() {
        Random random = new Random();
        int direction = random.nextInt(4); // generates a random number between 0 and 3

        // 0 - left, 1 - right, 2 - up, 3 - down
        switch (direction) {
            case 0:
                currentDirection = directionInitial * FLIP_DIRECTION_VALUE;
                directionHorizontal = true;
                break;
            case 1:
                currentDirection = directionInitial;
                directionHorizontal = true;
                break;
            case 2:
                currentDirection = directionInitial * FLIP_DIRECTION_VALUE;
                directionHorizontal = false;
                break;
            case 3:
                currentDirection = directionInitial;
                directionHorizontal = false;
                break;
        }
    }

    /**
     * Method used to decide whether the green ghost moves up, down, left or right, but also excludes
     * if the ghost gets the same direction after colliding with the wall
     */
    private void chooseDirection(int excludeNum) {
        Random random = new Random();
        int direction; // generates a random number between 0 and 4

        do {
            direction = random.nextInt(4);
        } while (direction == excludeNum);


        // 0 - left, 1 - right, 2 - up, 3 - down
        switch (direction) {
            case 0:
                currentDirection = directionInitial * FLIP_DIRECTION_VALUE;
                directionHorizontal = true;
                break;
            case 1:
                currentDirection = directionInitial;
                directionHorizontal = true;
                break;
            case 2:
                currentDirection = directionInitial * FLIP_DIRECTION_VALUE;
                directionHorizontal = false;
                break;
            case 3:
                currentDirection = directionInitial;
                directionHorizontal = false;
                break;
        }

        /*if (direction == excludeNum) {
            chooseDirection(direction);
        }*/
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
