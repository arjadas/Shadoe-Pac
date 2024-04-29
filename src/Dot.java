public class Dot extends StationaryEntity {

    private final static String IMG_LOCATION = "res/dot.png";
    public static final int POINTS = 10;

    /**
     * Public constructor which takes in the position of dot
     */
    public Dot(double x, double y) {
        super(IMG_LOCATION, x, y);
    }

}
