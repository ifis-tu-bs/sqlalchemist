package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Profile;
import dao.UserDAO;
import dao.ProfileDAO;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;


/**
 * This is the controller class for the settings
 *
 * @author fabiomazzone
 */
@Authenticated(secured.UserSecured.class)
public class SettingsController extends Controller {
    /**
     * GET      /profile/settings
     *
     * @return  returns the Player Settings as JSON Object
     */
    public static Result index() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        return ok(profile.settings.getSettings());
    }

    /**
     * POST     /profile/settings
     *
     * @return  returns a http responds code if the action was successfully or not
     */
    public static Result edit() {
        Profile profile = ProfileDAO.getByUsername(request().username());
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
