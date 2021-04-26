import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

//Converts data returned from postgres to JSON using an object mapper
public class OsmToJsonConverter {
    private OsmMap osmMap;
    private ObjectMapper objectMapper = new ObjectMapper();

    public OsmToJsonConverter(OsmMap osmMap){
        this.osmMap = osmMap;
    }

    public String convertToJson() throws IOException {
        return objectMapper.writeValueAsString(osmMap);
    }
}
