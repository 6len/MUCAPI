import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//Converts coordinates to a value which can be used in unity, done by simply scailing entire map off of min x and min y as seen below
public class CoordinateConverter {
    private double minX;
    private double minY;

    public CoordinateConverter(double minX, double minY) {
        this.minX = minX;
        this.minY = minY;
    }

    public double convertXCoordinate(double xCoordinate) {
        return Math.abs((Math.abs(xCoordinate) - Math.abs(minX))*10000);
    }
    public double convertYCoordinate(double yCoordinate) {
        return Math.abs((Math.abs(yCoordinate) - Math.abs(minY))*10000);
    }
}
