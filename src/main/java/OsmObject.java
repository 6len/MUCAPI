import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter

//This is deprecated
public class OsmObject {
   /* This class needs to be changed to be in accordance with the osmosis output.
    There will be a script to export an osmosis output to a database.
    Once stored in a database, a JAVA object will be created for each object in the database.
    This object will be able to use all types of OSMdata including vectors etc..
    This object will then be plugged into a map before either being converted to JSON via jackson, or
    unity objects will be created through the JAVA map*/
    private String name;
    private String description;
    private List<String> tags;
    private double xCoordinates;
    private double yCoordinates;


}
