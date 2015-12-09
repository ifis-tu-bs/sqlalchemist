package controllers;


import dao.UserDAO;

import models.Settings;
import models.User;

import play.libs.Json;
import secured.UserAuthenticator;


import play.mvc.Security;

import com.fasterxml.jackson.databind.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;



/**
 * This is the controller class for the settings
 *
 * @author fabiomazzone
 */
@Security.Authenticated(UserAuthenticator.class)
public class SettingsController extends Controller {
    /**
     * GET      /profile/settings
     *
     * @return  returns the Player Settings as JSON Object
     */
    public Result index(String username) {
        User user = UserDAO.getBySession(request().username());
        if(!user.getUsername().equals(username)) {
            return forbidden();
        }
        return ok(Json.toJson(user.getSettings()));
    }

    /**
     * POST     /profile/settings
     *
     * @return  returns a http responds code if the action was successfully or not
     */
    public Result update(String username) {
        User user = UserDAO.getBySession(request().username());
        if(!user.getUsername().equals(username)) {
            return forbidden();
        }
        JsonNode json       = request().body().asJson();
        user.setSettings(Json.fromJson(json, Settings.class));
        user.update();

        return redirect(routes.SettingsController.index(username));
    }
}
