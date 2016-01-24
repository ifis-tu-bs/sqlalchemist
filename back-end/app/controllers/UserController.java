package controllers;

import dao.ActionDAO;
import dao.SessionDAO;
import dao.UserDAO;

import forms.SignUp;

import models.Action;
import models.Session;
import models.User;

import play.Logger;
import secured.user.CanReadUsers;
import secured.SessionAuthenticator;
import secured.UserAuthenticator;

import com.fasterxml.jackson.databind.ObjectMapper;

import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import service.ServiceUser;

import java.io.IOException;
import java.util.List;

/**
 * This Class handle all User Actions
 *
 * @author Fabio Mazzone
 */
@Authenticated(SessionAuthenticator.class)
public class UserController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        Session session = SessionDAO.getById(request().username());
        if(session == null) {
            return forbidden("Your Client is not registered");
        }

        Form<SignUp>signUpForm  = Form.form(SignUp.class).bindFromRequest();

        if(signUpForm.hasErrors()) {
            return badRequest(signUpForm.errorsAsJson());
        }

        SignUp      signUp      = signUpForm.bindFromRequest().get();

        User user = UserDAO.create(signUp);
        if(user == null) {
            return unauthorized("Wrong  email or password");
        }

        session.setOwner(user);
        session.addAction(ActionDAO.create(Action.LOGIN));
        session.update();

        return redirect(routes.UserController.show(user.getUsername()));
    }


    @Authenticated(CanReadUsers.class)
    public Result index() {
        List<User> users = UserDAO.getAll();
        return ok(Json.toJson(users));
    }

    @Authenticated(UserAuthenticator.class)
    public Result show(String username) {
        User user = UserDAO.getBySession(request().username());
        if(!user.getUsername().equals(username) && !user.getRole().getUserPermissions().canRead()) {
            return forbidden(Json.parse("{\"message\":\"you have not the permissions to read informations from other users\"}"));
        }
        User userShow = UserDAO.getByUsername(username);
        if(userShow == null) {
            return notFound();
        }

        return ok(Json.toJson(userShow));
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(UserAuthenticator.class)
    public Result edit(String username) {
        User userEdit = UserDAO.getByUsername(username);
        if(userEdit == null)
            return notFound();
        User user = UserDAO.getBySession(request().username());
        if(userEdit.getId() != user.getId() && !user.getRole().getUserPermissions().canUpdate()) {
            return forbidden(Json.parse("{\"message\": \"you have not the permissions to edit other user data\"}"));
        }

        Logger.info(request().body().asJson().toString());
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readerForUpdating(userEdit).readValue(request().body().asJson());
        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError(Json.parse("{\"message\": \"unexpected exception!\"}"));
        }

        userEdit.update();
        return redirect(routes.UserController.show(username));
    }

    /**
     * This method delete the user
     * DELETE /users
     *
     * @return ok
     */
    @Authenticated(UserAuthenticator.class)
    public Result destroy(String username) {
        User user = UserDAO.getBySession(request().username());
        if(user.getUsername().equals(username)
                || user.getRole().getUserPermissions().canDelete()) {

            ServiceUser.disable(user);
            user.update();

            session().clear();
            return ok();
        }
        return forbidden();
    }
}
