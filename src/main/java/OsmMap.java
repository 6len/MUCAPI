import java.util.HashMap;
import java.util.Map;

//Map for storing OsmObjectMaps
public class OsmMap {
    public Map<String, OsmObjectMap> map = new HashMap<String, OsmObjectMap>();

    public void addObject(String identifier, OsmObjectMap osmObject) {
        map.put(identifier, osmObject);
    }
    public OsmObjectMap getObject(String identifier) {
        return map.get(identifier);
    }
}
