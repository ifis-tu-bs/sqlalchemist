package controllers;

import dao.UserSessionDAO;

import models.User;
import models.UserSession;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;


import secured.*;

/**
 * The SessionController
 *
 * Created by fabiomazzone on 25/04/15.
 * @author fabiomazzone
 */
public class SessionController extends Controller {
    /**
     * Needs a JSON Body Request with values:
     *  id: String (y-ID or EMail)
     *  password: String
     *
     * @return Returns the Global ProfileState
     */
    public Result create() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            Logger.info("SessionController.create - Could not retrieve Json from POST body");
            return badRequest("Could not retrieve Json from POST body");
        }

        String  id          = json.path("id").textValue();
        String  password    = json.path("password").textValue();
        boolean adminTool   = json.path("adminTool").asBoolean();

        if (id.length() == 0 || password.length() == 0) {
            Logger.info("SessionController.create - Expecting Json data");
            Logger.info("SessionController.create - Json: " + json.toString());
            return badRequest("Expecting Json data");
        }

        id = id.trim();
        User user;

        if ((user = User.validate(id, password, adminTool)) != null) {
            UserSession userSession = UserSession.create(
                    user,
                    Play.application().configuration().getInt("UserManagement.SessionDuration"),
                    request().remoteAddress()
            );

            if (userSession != null ) {
                session().put("sessionID", userSession.getSessionID());
                return redirect(routes.ProfileController.read());
            }
            Logger.warn("SessionController.create - Can't create userSession");
            return badRequest("Can't create userSession");
        }
        Logger.warn("SessionController.create - Wrong ID or Password: " + id);
        return forbidden("Wrong ID/Password");
    }

    @Authenticated(UserSecured.class)
    public Result delete() {
        String sessionID = session().get("sessionID");
        UserSession userSession = UserSessionDAO.getBySessionID(sessionID);

        if (userSession == null) {
            Logger.info("SessionController.delete - No session has been deleted.");
            return badRequest("Whops, your session has been deleted earlier.");
        }

        Logger.info("SessionController.delete - Deleting Session: " + userSession.getSessionID());
        userSession.delete();
        session().clear();
        return ok("you are now logged out!");
    }

}
