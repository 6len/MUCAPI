import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OsmObjectTest {
    public OsmObject osmObject;

    public void init(){
        this.osmObject = new OsmObject("Name", "Desciption", null, 2.3, 2.5);
    }

    @Test
    public void nameTest() {
        init();
        osmObject.setName("NewName");
        assertEquals("NewName", osmObject.getName());
    }

    @Test
    public void descriptionTest() {
        init();
        osmObject.setDescription("newDesc");
        assertEquals("newDesc", osmObject.getDescription());
    }

    @Test
    public void XYTest() {
        init();
        osmObject.setXCoordinates(5);
        osmObject.setYCoordinates(5);
        assertEquals(5, osmObject.getXCoordinates(),.01);
        assertEquals(5, osmObject.getYCoordinates(),.01);
    }

}