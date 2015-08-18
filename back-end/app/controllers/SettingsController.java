package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;

import play.Logger;
import play.mvc.*;

/**
 * This is the controller class for the settings
 *
 * @author fabiomazzone
 */
@Security.Authenticated(secured.UserSecured.class)
public class SettingsController extends Controller {
    /**
     * GET      /profile/settings
     *
     * @return  returns the Player Settings as JSON Object
     */
    public static Result index() {
        Profile profile = User.getProfile(session());
        return ok(profile.settings.getSettings());
    }

    /**
     * POST     /profile/settings
     *
     * @return  returns a http responds code if the action was successfully or not
     */
    public static Result edit() {
        Profile profile = User.getProfile(session());
        JsonNode json = request().body().asJson();
        if(json == null || json.get("music") == null && json.get("sound") == null) {
            Logger.warn("SettingsController.edit - not Valid");
            return badRequest("Invalid Json");
        }
        boolean music = json.get("music").asBoolean();
        boolean sound = json.get("sound").asBoolean();

        profile.settings.setSettings(music, sound);
        profile.update();
        return ok(profile.settings.getSettings());
    }
}
