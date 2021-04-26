import org.junit.Test;

        import java.io.IOException;
        import java.sql.SQLException;
import java.util.List;

public class DatabaseQueryEngineTest {
    @Test
    public void testNodesQuery() throws SQLException, IOException {
        OsmMap osmMap = new OsmMap();
        DatabaseQueryEngine databaseQueryEngine = new DatabaseQueryEngine("pgsnapshot");
        OsmObjectMap osmObjectMap = databaseQueryEngine.getNodes();
        assert(osmObjectMap != null);
        OsmObjectMap osmObjectMap1 = databaseQueryEngine.getMapDimensions();
        assert(osmObjectMap1 != null);
    }

    @Test
    public void testDatabaseListQuery() throws  SQLException, IOException {
        DatabaseQueryEngine databaseQueryEngine = new DatabaseQueryEngine("pgsnapshot");
        List<String> databaseList = databaseQueryEngine.getDatabaseList();
        assert(databaseList != null);
    }

    //These tests were depracated to prevent too many databases from being created and to prevent errors when ran on a windows machine
    public void newDBTest() throws  SQLException, IOException {
        DatabaseQueryEngine databaseQueryEngine = new DatabaseQueryEngine("pgsnapshot");
        databaseQueryEngine.createNewSchema();
    }

    public void schemaUploaderTest() throws SQLException, IOException {
        DatabaseQueryEngine databaseQueryEngine = new DatabaseQueryEngine("pgsnapshot");
        databaseQueryEngine.schemaUpdated();
    }
}