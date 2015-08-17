package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.*;
import play.mvc.*;

import secured.*;

/**
 * The SessionController
 *
 * Created by fabiomazzone on 25/04/15.
 */
public class SessionController extends Controller {
    /**
     * Needs a JSON Body Request with values:
     *  id: String (y-ID or EMail)
     *  password: String
     *
     * @return Returns the Global ProfileState
     */
    public static Result create() {

        JsonNode json = request().body().asJson();
        if (json == null) {
            Logger.info("SessionController.create - Could not retrieve Json from POST body");
            return badRequest("Could not retrieve Json from POST body");
        }
        String id = json.findPath("id").textValue();
        String password = json.findPath("password").textValue();
        boolean adminTool = json.findPath("adminTool").asBoolean();
        
        if (id == null || password == null) {
            Logger.info("SessionController.create - Expecting Json data");
            Logger.info("SessionController.create - Json: " + json.toString());
            return badRequest("Expecting Json data");
        }

        id = id.trim();
        User user;

        if ((user = User.validate(id, password, adminTool)) != null) {
            UserSession userSession = UserSession.create(
                    user,
                    Play.application().configuration().getInt("Session.duration"),
                    request().remoteAddress()
            );

            if (userSession != null ) {
                session().put("sessionID", userSession.getSessionID());
                return controllers.ProfileController.index();
            }
            Logger.warn("SessionController.create - Can't create userSession");
            return badRequest("Can't create userSession");
        }
        Logger.warn("SessionController.create - Wrong ID or Password: " + id);
        return forbidden("Wrong ID/Password");
    }

    /**
     *
     * @return
     */
    @Security.Authenticated(UserSecured.class)
    public static Result delete() {
        UserSession userSession = UserSession.getSession(session());

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
