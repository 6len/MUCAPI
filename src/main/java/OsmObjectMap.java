import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter

//Stored OsmObjects using templating
public class OsmObjectMap {
    public Map<String, Object> map = new HashMap<String, Object>();

    public OsmObjectMap() {}

    public <T> void addObject(String identifier, T value ) {
        map.put(identifier, value);
    }

    public <T> T getObject(String identifier, Class <T> valueType) {
        System.out.println(map.get(identifier));
        return (T)map.get(identifier);
    }
}
