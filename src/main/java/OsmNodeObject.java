import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter

//Map for storing node objects
public class OsmNodeObject {
    private String id;
    private Map<String, String> tags = new HashMap<>();
    private double xCoord;
    private double yCoord;
    private double unityXValue;
    private double unityYValue;
}
