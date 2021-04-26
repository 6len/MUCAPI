import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Filter;
import spark.utils.IOUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static spark.Spark.*;

public class RunServer {
    Properties appProperties = new Properties();

    public static void main(String[] args) throws IOException, SQLException {
        staticFileLocation("/web"); //Defines static file location for use below
        port(getHerokuAssignedPort()); // define spark port

        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
        });

        get("/mapinfo/:databaseURL", (request, response) -> {
            DatabaseQueryEngine databaseQueryEngine = new DatabaseQueryEngine(request.params(":databaseURL"));
            //Initializes all map types, storing the corresponding information within
            OsmMap osmMap = new OsmMap();
            OsmObjectMap nodes = databaseQueryEngine.getNodes();
            OsmObjectMap ways = databaseQueryEngine.getWays();
            OsmObjectMap dimensions = databaseQueryEngine.getMapDimensions();
            //Gives kes to each map, storing them within an osmMap
            osmMap.addObject("nodes", nodes);
            osmMap.addObject("ways", ways);
            osmMap.addObject("dimensions", dimensions);
            //Converts the osmMap to JSON
            OsmToJsonConverter osmToJsonConverter = new OsmToJsonConverter(osmMap);
            return osmToJsonConverter.convertToJson(); //return json information
        });

        get("/", (request, response) -> {
            response.redirect("/mucapi.html"); //Redirects to mucapi home page
            return null;
        });

        get("/getMaps", (request, response) -> { //Retrieves a list of all databases contained in the API
            DatabaseQueryEngine databaseQueryEngine = new DatabaseQueryEngine("pgsnapshot");
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> mapList = databaseQueryEngine.getDatabaseList();
            return objectMapper.writeValueAsString(mapList);
        });

        post("/uploadOSM", (request, response) -> { //Creates a new database and schema (Unstable) schema creation does not work on windows, and has many prerequisites outside of the server
            DatabaseQueryEngine databaseQueryEngine = new DatabaseQueryEngine("glen");
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("src/main/resources/web/osmFiles"));
            Part part = request.raw().getPart("osmFile");
            try (InputStream inputStream = part.getInputStream()) {
                OutputStream outputStream = new FileOutputStream("src/main/resources/web/osmFiles/" + part.getSubmittedFileName());
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
            }
            databaseQueryEngine.schemaUpdated();
            return "Database Created.";
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 1233;
    }

}

