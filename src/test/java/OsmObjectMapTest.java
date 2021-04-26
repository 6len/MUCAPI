import java.util.HashMap;

import org.junit.*;

import static junit.framework.TestCase.assertEquals;

public class OsmObjectMapTest {
    public OsmObjectMap osmObjectMap = new OsmObjectMap();
    public OsmObject osmObject = new OsmObject("TestName", "TestDescription", null, 2.3, 3.2);

    @Test
    public void osmObjectInsertTest() {
        osmObjectMap.addObject("TestName", osmObject);
        assertEquals(1, osmObjectMap.map.size());
    }

    @Test
    public void osmObjectGetTest() {
        osmObjectMap.addObject("TestIndex", osmObject);
        String testString = osmObjectMap.getObject("TestIndex", OsmObject.class).getName();
        assertEquals("TestName", testString);
    }

}
