import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Getter;
import lombok.Setter;
import org.jasypt.util.text.StrongTextEncryptor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

//This class contains all database functionality, most variables within this class are self documenting name wise, however there will be explainations throughout
@Setter
@Getter
public class DatabaseQueryEngine {
    private String databaseURL;
    private String databaseUser;
    private String password;
    private Connection connection;
    private Statement databaseStatement;
    private ObjectMapper objectMapper = new ObjectMapper();
    private TypeFactory factory = TypeFactory.defaultInstance();
    private CoordinateConverter coordinateConverter;
    private double converterSetX = 0;
    private double converterSetY = 0;

    //Constructor, taking in databaseURL, the URL can be set mapinfo endpoint
    public DatabaseQueryEngine(String databaseURL) throws SQLException, IOException {
        this.databaseURL = System.getenv("DB_URL") + databaseURL;
        databaseUser = System.getenv("DB_USER");
        password = System.getenv("DB_PASS");
        connection = DriverManager.getConnection(this.databaseURL, databaseUser, password);
        databaseStatement = connection.createStatement();
        ResultSet databaseResult = databaseStatement.executeQuery("SELECT VERSION()"); //checking if connection worked
        if (databaseResult.next()) {
            System.out.println(databaseResult.getString(1));
        }
        ResultSet smallestX = databaseStatement.executeQuery("SELECT *,  ST_X(ST_TRANSFORM(geom,4326)) as xCoord FROM NODES ORDER BY xCoord ASC LIMIT 1;"); //retrieving the smallest X value
        while (smallestX.next()) {
            converterSetX = smallestX.getDouble("xCoord");
            System.out.println("Smallest X " + smallestX.getDouble("xCoord"));
        }
        ResultSet smallestY = databaseStatement.executeQuery("SELECT *, ST_Y(ST_TRANSFORM(geom,4326)) as yCoord FROM NODES ORDER BY yCoord ASC LIMIT 1;"); //retrieving the smallest Y value
        while (smallestY.next()) {
            converterSetY = smallestY.getDouble("yCoord");
            System.out.println("Smallest Y " + smallestY.getDouble("yCoord"));

        }

        coordinateConverter = new CoordinateConverter(converterSetX, converterSetY); //converting the coordinates so they can be stored in a map later
    }

    //Retrieves all nodes within the database
    public OsmObjectMap getNodes() throws SQLException {
        OsmObjectMap osmObjectMap = new OsmObjectMap();
        ResultSet nodes = databaseStatement.executeQuery("SELECT *, ST_X(ST_TRANSFORM(geom,4326)) as xCoord, ST_Y(ST_TRANSFORM(geom,4326)) as yCoord FROM NODES;");
        while (nodes.next()) { //Mapping nodes
            Map<String, String> tagMap = objectMapper.convertValue(nodes.getObject("tags"), Map.class);
            double unityXValue = coordinateConverter.convertXCoordinate(nodes.getDouble("xCoord"));
            double unityYValue = coordinateConverter.convertYCoordinate(nodes.getDouble("yCoord"));
            OsmNodeObject osmNodeObject = new OsmNodeObject(nodes.getString("id"), tagMap, nodes.getDouble("xCoord"), nodes.getDouble("yCoord"), unityXValue, unityYValue);
            osmObjectMap.addObject(nodes.getString("id"), osmNodeObject);
        }
        return osmObjectMap;
    }
    //Retrieves all ways within the database
    public OsmObjectMap getWays() throws SQLException {
        OsmObjectMap osmObjectMap = new OsmObjectMap();
        ResultSet ways = databaseStatement.executeQuery("SELECT * FROM WAYS;");
        while (ways.next()) {
            Map<String, String> tagMap = objectMapper.convertValue(ways.getObject("tags"), Map.class);
            OsmWaysObject osmWaysObject = new OsmWaysObject(ways.getString("id"), tagMap, Arrays.asList((Long[]) ways.getArray("nodes").getArray()));
            osmObjectMap.addObject(ways.getString("id"), osmWaysObject);
        }
        return osmObjectMap;
    }
    //Retrieves a list of the databases for use on the web endpoint
    public List<String> getDatabaseList() throws SQLException {
        List<String> databaseList = new ArrayList<>();
        ResultSet databases = databaseStatement.executeQuery("SELECT datname FROM pg_database;");
        while (databases.next()) {
            databaseList.add(databases.getString("datname"));
        }
        return databaseList;
    }
    //Creates and converts the map dimensions to something that we can use within unity
    public OsmObjectMap getMapDimensions() throws SQLException {
        double bigX = 0, bigY = 0;
        ResultSet largestX = databaseStatement.executeQuery("SELECT *, ST_X(geom) as xCoord FROM NODES ORDER BY xCoord DESC LIMIT 1;");
        while (largestX.next()) {
            bigX = coordinateConverter.convertXCoordinate(largestX.getDouble("xCoord"));
            System.out.println("Largest X " + largestX.getDouble("xCoord"));
        }
       ResultSet width = databaseStatement.executeQuery("SELECT ST_DISTANCE('SRID=4326;POINT("+coordinateConverter.getMinX()+" "+coordinateConverter.getMinY()+")'::geometry, 'SRID=4326;POINT("+bigX+" "+coordinateConverter.getMinY()+")'::geometry) as distance;");
        while (width.next()) {
            System.out.println(width.getDouble("distance"));
        }
        ResultSet largestY = databaseStatement.executeQuery("SELECT *, ST_Y(geom) as yCoord FROM NODES ORDER BY yCoord DESC LIMIT 1;");
        while (largestY.next()) {
            System.out.println("Largest Y " + largestY.getDouble("yCoord"));
            bigY = coordinateConverter.convertYCoordinate(largestY.getDouble("yCoord"));
        }
        ResultSet length = databaseStatement.executeQuery("SELECT ST_DISTANCE('SRID=4326;POINT("+coordinateConverter.getMinX()+" "+coordinateConverter.getMinY()+")'::geometry, 'SRID=4326;POINT("+coordinateConverter.getMinX()+" "+bigY+")'::geometry) as distance;");
        while (length.next()) {
            System.out.println(length.getDouble("distance"));
        }
        OsmObjectMap osmObjectMap = new OsmObjectMap();
        OsmMapDimensions osmMapDimensions = new OsmMapDimensions(0, 0, bigX, bigY);
        osmObjectMap.addObject("dimensions", osmMapDimensions);

        return osmObjectMap;
    }

    //Creates a new database
    public void createNewSchema() throws SQLException {
        ResultSet resultSet = databaseStatement.executeQuery("select count(datname) from pg_database");
        int uniqueID = 0;
        if(resultSet.next()) {
            uniqueID = resultSet.getInt(1);
        }
        if(uniqueID != 0) {
            uniqueID++;
            databaseStatement.executeQuery("CREATE DATABASE OSM"+uniqueID+";");
        }
    }

    //Creates a new schema and updates it with the map information (THIS IS INACTIVE FOR NOW DUE TO ISSUES WITH WINDOWS)
    public void schemaUpdated() throws SQLException, IOException {
        ResultSet resultSet = databaseStatement.executeQuery("select count(datname) from pg_database");
        int uniqueID = 0;
        if(resultSet.next()) {
            uniqueID = resultSet.getInt(1);
        }
        String newDBName = "OSM"+uniqueID;
        ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/schema/schemaUploader.sh", newDBName, password);
        Process process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null; // This is here for testing purposes
        while ((line = bufferedReader.readLine()) != null ) {
            System.out.println(line);
        }
    }
}
