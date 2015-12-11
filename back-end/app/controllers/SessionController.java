package controllers;

import dao.ActionDAO;
import dao.SessionDAO;

import forms.Login;

import models.Action;
import models.Session;
import models.User;

import secured.SessionAuthenticator;

import service.ServiceUser;

import view.SessionView;

import play.data.Form;
import play.mvc.BodyParser;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import java.util.Calendar;

/**
 * The SessionController
 *
 * Created by fabiomazzone on 25/04/15.
 * @author fabiomazzone
 */
public class SessionController extends Controller {
    /**
     * Needs a JSON Body Request with values:
     *  email:      String
     *  password:   String
     *
     * @return Returns the Global ProfileState
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(SessionAuthenticator.class)
    public Result login() {
        Session session = SessionDAO.getById(request().username());
        if(session == null) {
            return forbidden("Your Client is not registered");
        }

        Form<Login> loginForm   = Form.form(Login.class).bindFromRequest();

        if(loginForm.hasErrors()) {
            return badRequest(loginForm.errorsAsJson());
        }

        Login       login       = loginForm.bindFromRequest().get();

        User user = ServiceUser.authenticate(login);
        if(user == null) {
            return unauthorized("Wrong email or password, mist");
        }

        session.setOwner(user);
        session.addAction(ActionDAO.create(Action.LOGIN));
        session.update();

        return redirect(routes.UserController.show(user.getUsername()));
    }

    public Result logout() {
        session().clear();
        return redirect(routes.SessionController.index());
    }

    public Result index() {
        Session session = SessionDAO.getById(session("session"));
        if(session != null) {
            Logger.info("Contains an valid Session");

            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);

            if(yesterday.after(session.getCreatedAt())) {
                Logger.info("Create a new one & assign the user to session");
                Session newSession = SessionDAO.create();
                newSession.setOwner(session.getOwner());
                newSession.update();
                session("session", newSession.getId());


                return ok(SessionView.toJson(newSession));
            }

            return ok(SessionView.toJson(session));
        } else {
            Logger.info("Create a new Session");

            session = SessionDAO.create();
            session("session", session.getId());

            return ok(SessionView.toJson(session));
        }
    }

}
