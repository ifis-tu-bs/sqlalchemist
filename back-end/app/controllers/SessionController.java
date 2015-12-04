package controllers;

import dao.UserSessionDAO;

import forms.Login;
import models.UserSession;

import play.data.Form;
import play.mvc.BodyParser;
import secured.*;

import play.Logger;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

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
    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()) {
            Logger.error("LoginForm has errors");
            Logger.error(loginForm.errorsAsJson().toString());
            return badRequest(loginForm.errorsAsJson());
        }

        Login       login   = loginForm.bindFromRequest().get();

        return forbidden("Wrong ID/Password");
    /*

        String  id          = json.path("id").textValue();
        String  password    = json.path("password").textValue();

        if (id.length() == 0 || password.length() == 0) {
            Logger.info("SessionController.create - Expecting Json data");
            Logger.info("SessionController.create - Json: " + json.toString());
            return badRequest("Expecting Json data");
        }

        id = id.trim();
        User user;

        if ((user = User.validate(id, password)) != null) {
            if(user.isActive()) {
                UserSession userSession = UserSession.create(
                        user,
                        Play.application().configuration().getInt("UserManagement.SessionDuration"),
                        request().remoteAddress()
                );

                if (userSession != null) {
                    session().put("sessionID", userSession.getSessionID());
                    return redirect(routes.ProfileController.read());
                }
                Logger.warn("SessionController.create - Can't create userSession");
                return badRequest("Can't create userSession");
            } else {
                Logger.warn("Cannot login because it is an disabled User");
                return badRequest("Cannot login because it is an disabled User");
            }

        }
        Logger.warn("SessionController.create - Wrong ID or Password: " + id);
        return forbidden("Wrong ID/Password");*/
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
