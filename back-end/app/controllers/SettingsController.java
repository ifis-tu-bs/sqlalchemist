package controllers;

import dao.ProfileDAO;

import models.Profile;

import view.SettingsView;

import com.fasterxml.jackson.databind.JsonNode;

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
    public Result index() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        return ok(SettingsView.toJson(profile.settings));
    }

    /**
     * POST     /profile/settings
     *
     * @return  returns a http responds code if the action was successfully or not
     */
    public Result edit() {
        Profile profile     = ProfileDAO.getByUsername(request().username());
        JsonNode json       = request().body().asJson();
        profile.setSettings(SettingsView.fromJson(json));
        profile.update();
        return redirect(routes.SettingsController.index());
    }
}
