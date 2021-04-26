import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

//Class for storing the dimensions of the OSM map
public class OsmMapDimensions {
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;
}
