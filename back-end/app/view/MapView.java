package view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Map;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class MapView {
    /**
     *
     * @return returns the Map-Object as a Json model
     */
    public static ObjectNode toJson(Map map) {
        ObjectNode node = Json.newObject();

        node.put("id",          map.getId());
        node.put("level",       map.getLevel());
        node.put("path",        map.getPath());
        node.put("isBossMap",   map.isBossMap());

        return node;
    }
}
