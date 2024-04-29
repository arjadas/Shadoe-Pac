import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2023
 *
 * @author - Arja Das
 * student ID - 1336044
 */

public class ShadowPac extends AbstractGame  {

    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW PAC";
    private final static String INSTRUCTION_MESSAGE_LEVEL0 = "PRESS SPACE TO START\nUSE ARROW KEYS TO MOVE";
    private final static String INSTRUCTION_MESSAGE_LEVEL1 = INSTRUCTION_MESSAGE_LEVEL0 + "\nEAT THE PELLET TO ATTACK";
    private final static String LOSE_MESSAGE = "GAME OVER!";
    private final static String WIN_MESSAGE = "WELL DONE!";
    private final static String LEVEL_UP_MESSAGE = "LEVEL COMPLETE!";

    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Image HEART = new Image("res/heart.png");

    private final static int LEVEL0_WIN_SCORE = 100;
    private final static int LEVEL1_WIN_SCORE = 800;

    private final String FONT_LOCATION = "res/FSO8BITR.ttf";
    private final int FONT_SIZE_LARGE = 64;
    private final int FONT_SIZE_TERTIARY = 40;
    private final int FONT_SIZE_MEDIUM = 24;
    private final int FONT_SIZE_SMALL = 20;

    private final String CSV_LOCATION_LEVEL0 = "res/level0.csv";
    private final String CSV_LOCATION_LEVEL1 = "res/level1.csv";
    private final String DEFAULT_TEXT_SPLITTER = ",";

    private Player player;
    private static ArrayList<Wall> walls = new ArrayList<Wall>();
    private static ArrayList<Dot> dots = new ArrayList<Dot>();
    private static ArrayList<Cherry> cherries = new ArrayList<Cherry>();
    private static ArrayList<Pellet> pellets = new ArrayList<Pellet>();
    private static ArrayList<Ghost> ghosts = new ArrayList<Ghost>();

    private int playerCurrentScore = 0;
    private boolean gameStarted = false;
    private boolean gamePaused = false;
    private boolean gameLost = false;
    private boolean gameWon = false;
    private int levelUpFrameCount = 0;
    private int currentGameLevel = 0;

    private static boolean frenzyMode = false;
    private int frenzyFrameCount = 0;

    /*
     * Setters and Getters section
     */

    public static ArrayList<Wall> getWalls(){
        return walls;
    }

    public static ArrayList<Dot> getDots(){
        return dots;
    }

    public static ArrayList<Ghost> getGhosts(){
        return ghosts;
    }

    public static ArrayList<Cherry> getCherries(){
        return cherries;
    }

    public static ArrayList<Pellet> getPellets(){
        return pellets;
    }

    public static boolean getFrenzyState() {
        return frenzyMode;
    }

    public static void triggerFrenzyMode() {
        frenzyMode = true;
    }

    public ShadowPac(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV(CSV_LOCATION_LEVEL0);
    }

    /**
     * Method used to read the csv file and create objects
     */
    private void readCSV(String fileLocation) {

        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String text = "";
            while ((text = reader.readLine()) != null) {
                String[] data = text.split(DEFAULT_TEXT_SPLITTER);

                /*
                 * in data, column at index 0 - type of the icon
                 *          column at index 1 - xCoordinate of the icon
                 *          column at index 2 - yCoordinate of the icon
                 */

                switch (data[0]) {

                    case "Player":
                        player = new Player(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
                        break;

                    case "GhostRed":
                        ghosts.add(new GhostRed(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
                        break;

                    case "GhostBlue":
                        ghosts.add(new GhostBlue(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
                        break;

                    case "GhostGreen":
                        ghosts.add(new GhostGreen(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
                        break;

                    case "GhostPink":
                        ghosts.add(new GhostPink(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
                        break;

                    case "Cherry":
                        cherries.add(new Cherry(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
                        break;

                    case "Pellet":
                        pellets.add(new Pellet(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
                        break;

                    case "Dot":
                        dots.add(new Dot(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
                        break;

                    case "Wall":
                        walls.add(new Wall(Double.parseDouble(data[1]), Double.parseDouble(data[2])));
                        break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to display level win message
     */
    public void renderLevelUpMessage() {

        levelUpFrameCount++;

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        new Font(FONT_LOCATION, FONT_SIZE_LARGE).drawString(LEVEL_UP_MESSAGE,  160, Window.getHeight()/2.0);

        if ((levelUpFrameCount == 300) && (gamePaused)) {
            levelUpFrameCount = 0;
            gamePaused = false;
        }

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPac game = new ShadowPac();
        game.run();
    }

    /**
     * Method used to check the current game level and whether the game is won or lost
     */
    private void checkGameStatus() {

        playerCurrentScore = player.getCurrentScore();

        if (player.getHeartCount() == 0) {
            gameLost = true;
        }

        if ((currentGameLevel == 0) && (playerCurrentScore == LEVEL0_WIN_SCORE)) {
            currentGameLevel++;
            gamePaused = true;
            gameStarted = false;
            player.notifyLeveledUp();
            updateArrayLists();
        } else if ((currentGameLevel == 1) && (playerCurrentScore == LEVEL1_WIN_SCORE)) {
            gameWon = true;
        }

    }

    /**
     * Method used to reset all the arraylists for storing the information from CSV for the next level
     */
    private void updateArrayLists(){
        walls.removeAll(walls);
        dots.removeAll(dots);
        ghosts.removeAll(ghosts);
        readCSV(CSV_LOCATION_LEVEL1);
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        checkGameStatus();

        if (gameStarted && !gamePaused) {

            // game is started

            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

            new Font(FONT_LOCATION, FONT_SIZE_SMALL).drawString("SCORE " + playerCurrentScore, 25, 25);

            // keeps track of the frame for pacman mouth update
            player.checkPacmanMouth();
            player.draw();
            player.move(input);

            displayHeart();

            // performs the desired actions depending on the game level
            if (currentGameLevel == 0) {
                performLevel0();
            } else if (currentGameLevel == 1) {
                checkFrenzyMode();
                performLevel1();
            }

        } else if (gamePaused) {

            // level up message
            renderLevelUpMessage();

        } else {

            // Game hasn't started. It's the Welcome Screen

            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            // displays the instruction message depending on the game level
            if (currentGameLevel == 0) {
                new Font(FONT_LOCATION, FONT_SIZE_LARGE).drawString(GAME_TITLE, 260, 250);
                new Font(FONT_LOCATION, FONT_SIZE_MEDIUM).drawString(INSTRUCTION_MESSAGE_LEVEL0, 320, 440);
            } else if (currentGameLevel == 1) {
                new Font(FONT_LOCATION, FONT_SIZE_TERTIARY).drawString(INSTRUCTION_MESSAGE_LEVEL1, 200, 350);
            }

        }

        if (input.wasPressed(Keys.SPACE)){
            gameStarted = true;
        }

        // Game Lost!
        if (gameLost && !gameWon) {
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            new Font(FONT_LOCATION, FONT_SIZE_LARGE).drawString(LOSE_MESSAGE, 260, Window.getHeight()/2.0);
        }

        // Game Won!
        if (gameWon && !gameLost) {
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            new Font(FONT_LOCATION, FONT_SIZE_LARGE).drawString(WIN_MESSAGE, 260, Window.getHeight()/2.0);
        }

    }

    /**
     * Method used to keep track of the time during frenzy mode
     */
    private void checkFrenzyMode() {
        if (frenzyMode) {
            if (frenzyFrameCount <= 1000) {
                frenzyFrameCount++;
            } else {
                frenzyFrameCount = 0;
                frenzyMode = false;
                player.makeSpeedNormal();
            }
        }
    }

    /**
     * Method used to display the heart images
     */
    private void displayHeart() {
        for (int i = 0; i < player.getHeartCount(); i++) {
            // x coordinate gets incremented by i*(30) pixels for the consecutive hearts
            HEART.drawFromTopLeft(900 + i*(30),10);
        }

    }

    /**
     * Method which performs level 0 instructions
     */
    private void performLevel0() {
        drawEntities();
    }

    /**
     * Method which performs level 1 instructions
     */
    private void performLevel1() {
        drawEntities();
        moveEntities();
    }

    /**
     * Method which draws all the entities like cherry, pellet, dots and walls
     */
    private void drawEntities() {
        for (Wall w: walls) {
            w.draw();
        }
        for (Dot d: dots) {
            d.draw();
        }
        for (Ghost g: ghosts) {
            g.draw();
        }

        for (Cherry c: cherries) {
            c.draw();
        }

        for (Pellet p: pellets) {
            p.draw();
        }
    }

    /**
     * Method which moves the movable entities: ghosts
     */
    private void moveEntities() {
        for (Ghost g: ghosts) {
            g.move();
        }
    }

}
