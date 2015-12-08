package view;

import models.Settings;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author fabiomazzone
 */
public class SettingsView {
    public static Settings fromJson(JsonNode json) {
        boolean music = json.path("music").asBoolean();
        boolean sound = json.path("sound").asBoolean();

        return new Settings(sound, music);
    }
}
