package controllers;

import dao.SessionDAO;

import forms.Login;

import models.Session;
import models.User;

import play.libs.Json;
import secured.SessionAuthenticator;

import service.ServiceUser;

import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import java.util.List;

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
            return unauthorized("Wrong email or password");
        }

        if(!user.isEmailVerified()) {
          return unauthorized("you must verify your email before you can enter");
        }

        List<Session> sessionList = SessionDAO.getByOwner(user);
        sessionList.stream().filter(sessionI -> !sessionI.getId().equals(session.getId())).forEach(sessionI -> {
            sessionI.disable();
            sessionI.update();
        });

        session.setOwner(user);
        session.update();

        return redirect(routes.UserController.show(user.getUsername()));
    }

    @Authenticated(SessionAuthenticator.class)
    public Result logout() {
        Session session = SessionDAO.getById(session("session"));
        session.disable();
        session.update();
        
        session().remove("session");
        session().clear();

        return ok();
    }

    public Result index() {
        Session session = SessionDAO.getById(session("session"));
        if(session != null && session.isActive()) {
            if(session.getOwner() != null) {
                List<Session> sessionList = SessionDAO.getByOwner(session.getOwner());
                for (Session sessionI : sessionList) {
                    sessionI.disable();
                    sessionI.update();
                }
            }

            Session newSession = SessionDAO.create();
            newSession.setOwner(session.getOwner());
            newSession.update();

            session("session", newSession.getId());
            return ok(Json.toJson(newSession));
        } else {

            session = SessionDAO.create();
            session("session", session.getId());

            return ok(Json.toJson(session));
        }
    }

}
