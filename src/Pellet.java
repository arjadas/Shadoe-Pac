public class Pellet extends StationaryEntity {

    private final static String IMG_LOCATION = "res/pellet.png";

    /**
     * Public constructor which takes in the position of the pellet
     */
    public Pellet(double x, double y) {
        super(IMG_LOCATION, x, y);
    }

}
