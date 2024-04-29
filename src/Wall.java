public class Wall extends StationaryEntity {

    private final static String IMG_LOCATION = "res/wall.png";

    /**
     * Public constructor which takes in the position of the walls
     */
    public Wall(double x, double y) {
        super(IMG_LOCATION, x, y);
    }

}
