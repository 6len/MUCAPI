import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class OsmToJsonConverterTest {
    public OsmObjectMap osmObjectMap = new OsmObjectMap();
    public OsmMap osmMap = new OsmMap();

    public void init() {
        osmObjectMap.addObject("TestName", new OsmObject("TestName", "TestDescription", null, 2.3, 4.2));
        osmMap.addObject("TestObjectIdentifier", osmObjectMap);
    }

    //deprecated due to ordering issue, information still remains the same in relation to tags
    public void conversionTest() throws IOException {
        init();
        osmObjectMap.addObject("Node_One", new OsmNodeObject("10", null, 1.2, 1.3, 100, 100));
        osmMap.addObject("Nodes", osmObjectMap);
        String checker = "{\"map\":{\"TestObjectIdentifier\":{\"map\":{\"TestName\":{\"name\":\"TestName\",\"description\":\"TestDescription\",\"tags\":null,\"ycoordinates\":4.2,\"xcoordinates\":2.3},\"Node_One\":{\"id\":\"10\",\"tags\":null,\"unityXValue\":100.0,\"unityYValue\":100.0,\"xcoord\":1.2,\"ycoord\":1.3}}},\"Nodes\":{\"map\":{\"TestName\":{\"name\":\"TestName\",\"description\":\"TestDescription\",\"tags\":null,\"ycoordinates\":4.2,\"xcoordinates\":2.3},\"Node_One\":{\"id\":\"10\",\"tags\":null,\"unityXValue\":100.0,\"unityYValue\":100.0,\"xcoord\":1.2,\"ycoord\":1.3}}}}}";
        assertEquals(checker, new OsmToJsonConverter(osmMap).convertToJson());
    }
}


