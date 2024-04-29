import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.lang.Math;

public class Player {

    private final static String MOUTH_CLOSED_IMG_LOCATION = "res/pac.png";
    private final static String MOUTH_OPENED_IMG_LOCATION = "res/pacOpen.png";

    private final static Image PACMAN = new Image(MOUTH_CLOSED_IMG_LOCATION);
    private final static Image PACMAN_OPEN = new Image(MOUTH_OPENED_IMG_LOCATION);

    private double currentX;
    private double currentY;
    private final Point START_POSITION;
    private final static double PLAYER_SPEED = 3;
    private final static double FRENZY_SPEED_BOOST = 1;
    private int playerScore = 0;
    private double currentSpeed = PLAYER_SPEED;

    private final DrawOptions rotation = new DrawOptions();

    private static int heartCount = 3;
    private int frameCount = 0;

    private double currentDirection;
    private double directionUp = Math.toRadians(270.0);
    private double directionDown = Math.toRadians(90.0);
    private double directionRight = Math.toRadians(0.0);
    private double directionLeft = Math.toRadians(180.0);

    private boolean wallCollision = false;

    private Rectangle playerBox, wallBox, ghostBox, dotBox, cherryBox, pelletBox, entityBox;

    /**
     * Public constructor which takes in the player's starting position
     */
    public Player(double x, double y) {
        START_POSITION = new Point(x, y);
        resetPosition();
    }

    /**
     * Method which resets the position of player when needed
     */
    private void resetPosition() {
        currentX = START_POSITION.x;
        currentY = START_POSITION.y;
    }

    /**
     * Method which returns the heart count of player
     */
    public int getHeartCount() {
        return heartCount;
    }

    /**
     * Method which resets the player score to 0 when levelled up
     */
    public void notifyLeveledUp() {
        playerScore = 0;
    }

    /**
     * Method which returns player's score
     */
    public int getCurrentScore() {
        return playerScore;
    }

    /**
     * Method used to keep track of frames for updating the player's mouth state
     */
    public void checkPacmanMouth() {
        frameCount++;
        if (frameCount == 30) {
            frameCount = 0;
        }
    }

    /**
     * Method used for deciding and updating the state of player's mouth
     */
    public void draw() {
        if (frameCount < 15) {
            drawMouthClosed();
        } else {
            drawMouthOpened();
        }
    }

    /**
     * Method which draws pacman with mouth closed
     */
    private void drawMouthClosed() {
        PACMAN.drawFromTopLeft(currentX, currentY, rotation.setRotation(currentDirection));
    }

    /**
     * Method which draws pacman with mouth opened
     */
    private void drawMouthOpened() {
        PACMAN_OPEN.drawFromTopLeft(currentX, currentY, rotation.setRotation(currentDirection));
    }

    /**
     * Method used to update the player's position on the screen
     */
    public void move(Input input) {

        // this section determines whether the intended move collides with a wall
        if (input.isDown(Keys.LEFT)) {
            wallCollision = checkWallCollision((currentX - currentSpeed), currentY);
        } else if (input.isDown(Keys.RIGHT)) {
            wallCollision = checkWallCollision((currentX + currentSpeed), currentY);
        } else if (input.isDown(Keys.UP)) {
            wallCollision = checkWallCollision(currentX, (currentY - currentSpeed));
        } else if (input.isDown(Keys.DOWN)) {
            wallCollision = checkWallCollision(currentX, (currentY + currentSpeed));
        }

        // player hasn't collided with wall, it can proceed, otherwise not
        if (!wallCollision) {
            if (input.isDown(Keys.LEFT)) {
                currentX -= currentSpeed;
                currentDirection = directionLeft;
            } else if (input.isDown(Keys.RIGHT)) {
                currentX += currentSpeed;
                currentDirection = directionRight;
            } else if (input.isDown(Keys.UP)) {
                currentY -= currentSpeed;
                currentDirection = directionUp;
            } else if (input.isDown(Keys.DOWN)) {
                currentY += currentSpeed;
                currentDirection = directionDown;
            }
            // this method records all the other collisions and performs their desired output
            checkOtherCollision();
        }

    }

    /**
     * Method which checks whether the player collided with the wall and performs necessary action
     */
    private boolean checkWallCollision(double futureX, double futureY) {

        for (Wall w: ShadowPac.getWalls()) {

            playerBox = new Rectangle(futureX, futureY, PACMAN.getWidth(), PACMAN.getHeight());
            wallBox = w.getRectangle();

            if (playerBox.intersects(wallBox)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method which checks whether the player collided with the entities and performs necessary action
     */
    private void checkOtherCollision() {

        playerBox = new Rectangle(currentX, currentY, PACMAN.getWidth(), PACMAN.getHeight());

        // dot collision
        for (int i = 0; i < ShadowPac.getDots().size(); i++) {

            dotBox = ShadowPac.getDots().get(i).getRectangle();

            if (playerBox.intersects(dotBox)) {
                playerScore += Dot.POINTS;
                ShadowPac.getDots().remove(i);
            }

        }

        // cherry collision
        for (int i = 0; i < ShadowPac.getCherries().size(); i++) {

            cherryBox = ShadowPac.getCherries().get(i).getRectangle();

            if (playerBox.intersects(cherryBox)) {
                playerScore += Cherry.POINTS;
                ShadowPac.getCherries().remove(i);
            }

        }

        // pellet collision
        for (int i = 0; i < ShadowPac.getPellets().size(); i++) {

            pelletBox = ShadowPac.getPellets().get(i).getRectangle();

            if (playerBox.intersects(pelletBox)) {
                activateFrenzyMode();
                ShadowPac.getPellets().remove(i);
            }

        }

        // ghost collision
        for (int i = 0; i < ShadowPac.getGhosts().size(); i++) {

            ghostBox = ShadowPac.getGhosts().get(i).getRectangle();

            if (playerBox.intersects(ghostBox)) {

                if (!ShadowPac.getFrenzyState()) {
                    heartCount--;
                    resetPosition();
                    ShadowPac.getGhosts().get(i).reSpawn();
                } else if (!ShadowPac.getGhosts().get(i).getIsEaten()) {
                    ShadowPac.getGhosts().get(i).eatGhost();
                    playerScore += ShadowPac.getGhosts().get(i).POINTS;
                }
            }
        }
    }

    /**
     * Method used to activate frenzy mode
     */
    private void activateFrenzyMode() {
        ShadowPac.triggerFrenzyMode();
        currentSpeed = PLAYER_SPEED + FRENZY_SPEED_BOOST;
    }

    /**
     * Method which makes player speed normal after frenzy mode
     */
    public void makeSpeedNormal() {
        currentSpeed = PLAYER_SPEED;
    }

}
