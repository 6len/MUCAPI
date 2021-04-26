import org.junit.*;

import static org.junit.Assert.assertEquals;


public class OsmMapTest {
    public OsmObjectMap osmObjectMap = new OsmObjectMap();
    public OsmMap osmMap = new OsmMap();

    @Test
    public void testMapFilling() {
        osmObjectMap.addObject("TestName", new OsmObject("TestName", "TestDescription", null, 2.3, 4.2));
        osmMap.addObject("TestObjectIdentifier", osmObjectMap);
        assertEquals("TestName",osmMap.getObject("TestObjectIdentifier").getObject("TestName", OsmObject.class).getName());
    }
}