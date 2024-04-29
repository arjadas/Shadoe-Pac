public class Cherry extends StationaryEntity {

    private final static String IMG_LOCATION = "res/cherry.png";
    public static final int POINTS = 20;

    /**
     * Public constructor which takes in the position of the cherry
     */
    public Cherry(double x, double y) {
        super(IMG_LOCATION, x, y);
    }

}
