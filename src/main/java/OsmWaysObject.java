import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter

//Stores way objects from postgres
public class OsmWaysObject {
    private String id;
    private Map<String, String> tags = new HashMap<>();
    private List<Long> nodes;
}
