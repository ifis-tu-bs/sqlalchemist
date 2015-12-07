package controllers;


import dao.UserDAO;

import models.User;

import secured.UserAuthenticator;
import view.SettingsView;


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
    public Result index() {
        User user = UserDAO.getBySession(request().username());

        return ok(SettingsView.toJson(user.getSettings()));
    }

    /**
     * POST     /profile/settings
     *
     * @return  returns a http responds code if the action was successfully or not
     */
    public Result edit() {
        User user = UserDAO.getBySession(request().username());

        JsonNode json       = request().body().asJson();
        user.setSettings(SettingsView.fromJson(json));
        user.update();

        return redirect(routes.SettingsController.index());
    }
}
