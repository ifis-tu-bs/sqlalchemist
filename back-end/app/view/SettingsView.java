package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Settings;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class SettingsView {
    public static ObjectNode toJson(Settings settings) {
        ObjectNode node = Json.newObject();

        node.put("music", settings.isMusic());
        node.put("sound", settings.isSound());

        return node;
    }

    public static Settings fromJson(JsonNode json) {
        boolean music = json.path("music").asBoolean();
        boolean sound = json.path("sound").asBoolean();

        return new Settings(sound, music);
    }
}
